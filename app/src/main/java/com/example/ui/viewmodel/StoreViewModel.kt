package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.CartItemEntity
import com.example.data.model.OrderEntity
import com.example.data.model.ProductCategory
import com.example.data.model.ProductEntity
import com.example.data.model.ProductSource
import com.example.data.model.RecentSearchEntity
import com.example.data.model.WishlistItemEntity
import com.example.data.repository.StoreRepository
import com.example.data.repository.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale

enum class SortOption(val displayName: String) {
    TRENDING("Trending"),
    PRICE_LOW_TO_HIGH("Price: Low to High"),
    PRICE_HIGH_TO_LOW("Price: High to Low"),
    RATING("Highest Rated"),
    DISCOUNT("Biggest Discount")
}

data class CartItemWithProduct(
    val product: ProductEntity,
    val cartItem: CartItemEntity
)

data class FilterState(
    val query: String = "",
    val category: ProductCategory = ProductCategory.ALL,
    val source: ProductSource? = null,
    val sort: SortOption = SortOption.TRENDING,
    val maxPrice: Double = 150.0,
    val minRating: Float = 0f
)

data class CartSummary(
    val items: List<CartItemWithProduct> = emptyList(),
    val subtotal: Double = 0.0,
    val shippingCost: Double = 0.0,
    val discount: Double = 0.0,
    val total: Double = 0.0,
    val freeShippingThreshold: Double = 50.0,
    val freeShippingProgress: Float = 0f,
    val amountNeededForFreeShipping: Double = 0.0
)

class StoreViewModel(application: Application) : AndroidViewModel(application) {
    val repository = StoreRepository(application)

    val userProfile: StateFlow<UserProfile> = repository.userProfile
    val syncStatus = repository.syncStatus
    val appliedPromoCode = repository.appliedPromoCode

    // Filter states
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow(ProductCategory.ALL)
    val selectedCategory: StateFlow<ProductCategory> = _selectedCategory.asStateFlow()

    private val _selectedSource = MutableStateFlow<ProductSource?>(null)
    val selectedSource: StateFlow<ProductSource?> = _selectedSource.asStateFlow()

    private val _selectedSort = MutableStateFlow(SortOption.TRENDING)
    val selectedSort: StateFlow<SortOption> = _selectedSort.asStateFlow()

    private val _maxPriceFilter = MutableStateFlow(150.0)
    val maxPriceFilter: StateFlow<Double> = _maxPriceFilter.asStateFlow()

    private val _minRatingFilter = MutableStateFlow(0f)
    val minRatingFilter: StateFlow<Float> = _minRatingFilter.asStateFlow()

    // Base Products Flow
    val allProducts: StateFlow<List<ProductEntity>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val trendingProducts: StateFlow<List<ProductEntity>> = repository.trendingProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bestSellers: StateFlow<List<ProductEntity>> = repository.bestSellers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val flashDeals: StateFlow<List<ProductEntity>> = repository.flashDeals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val wishlistItems: StateFlow<List<WishlistItemEntity>> = repository.wishlistItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allOrders: StateFlow<List<OrderEntity>> = repository.allOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentSearches: StateFlow<List<RecentSearchEntity>> = repository.recentSearches
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Combined Filter State
    private val filterState: Flow<FilterState> = combine(
        combine(_searchQuery, _selectedCategory, _selectedSource) { query, cat, src ->
            Triple(query, cat, src)
        },
        combine(_selectedSort, _maxPriceFilter, _minRatingFilter) { sort, maxP, minR ->
            Triple(sort, maxP, minR)
        }
    ) { (query, cat, src), (sort, maxP, minR) ->
        FilterState(
            query = query,
            category = cat,
            source = src,
            sort = sort,
            maxPrice = maxP,
            minRating = minR
        )
    }

    // Filtered Products for Catalog / Search
    val filteredProducts: StateFlow<List<ProductEntity>> = combine(
        allProducts,
        filterState
    ) { products, filter ->
        var list = products.filter { product ->
            val matchesQuery = filter.query.isBlank() ||
                    product.title.contains(filter.query, ignoreCase = true) ||
                    product.tags.contains(filter.query, ignoreCase = true) ||
                    product.brand.contains(filter.query, ignoreCase = true)

            val matchesCategory = filter.category == ProductCategory.ALL || product.category == filter.category.name
            val matchesSource = filter.source == null || product.source == filter.source.name
            val matchesPrice = product.price <= filter.maxPrice
            val matchesRating = product.rating >= filter.minRating

            matchesQuery && matchesCategory && matchesSource && matchesPrice && matchesRating
        }

        list = when (filter.sort) {
            SortOption.TRENDING -> list.sortedByDescending { it.isTrending }
            SortOption.PRICE_LOW_TO_HIGH -> list.sortedBy { it.price }
            SortOption.PRICE_HIGH_TO_LOW -> list.sortedByDescending { it.price }
            SortOption.RATING -> list.sortedByDescending { it.rating }
            SortOption.DISCOUNT -> list.sortedByDescending {
                if (it.compareAtPrice > 0) (it.compareAtPrice - it.price) / it.compareAtPrice else 0.0
            }
        }
        list
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Cart Summary
    val cartSummary: StateFlow<CartSummary> = combine(
        repository.cartItems,
        allProducts,
        appliedPromoCode
    ) { cartList, products, promo ->
        val productMap = products.associateBy { it.id }
        val itemsWithProducts = cartList.mapNotNull { cartItem ->
            productMap[cartItem.productId]?.let { prod ->
                CartItemWithProduct(product = prod, cartItem = cartItem)
            }
        }

        val subtotal = itemsWithProducts.sumOf { it.product.price * it.cartItem.quantity }
        val threshold = 50.0
        val isFreeShipping = subtotal >= threshold || promo == "FREESHIP"
        val shipping = if (itemsWithProducts.isEmpty() || isFreeShipping) 0.0 else 4.99

        val discount = when (promo) {
            "SAVE10" -> (subtotal * 0.10)
            "GREENDEAL" -> 5.0
            else -> 0.0
        }

        val total = (subtotal + shipping - discount).coerceAtLeast(0.0)
        val progress = (subtotal / threshold).toFloat().coerceIn(0f, 1f)
        val needed = (threshold - subtotal).coerceAtLeast(0.0)

        CartSummary(
            items = itemsWithProducts,
            subtotal = subtotal,
            shippingCost = shipping,
            discount = discount,
            total = total,
            freeShippingThreshold = threshold,
            freeShippingProgress = progress,
            amountNeededForFreeShipping = needed
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CartSummary())

    // Product Comparison selection
    private val _comparisonProductIds = MutableStateFlow<List<String>>(
        listOf("prod_mama_teatree_facewash", "prod_ebay_smart_watch", "prod_ali_wireless_earbuds")
    )
    val comparisonProductIds: StateFlow<List<String>> = _comparisonProductIds.asStateFlow()

    // Tracking Lookup State
    private val _trackedOrder = MutableStateFlow<OrderEntity?>(null)
    val trackedOrder: StateFlow<OrderEntity?> = _trackedOrder.asStateFlow()

    private val _trackingSearchInput = MutableStateFlow("")
    val trackingSearchInput: StateFlow<String> = _trackingSearchInput.asStateFlow()

    private val _newsletterSubscribed = MutableStateFlow(false)
    val newsletterSubscribed: StateFlow<Boolean> = _newsletterSubscribed.asStateFlow()

    // Actions
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        if (query.length > 2) {
            viewModelScope.launch {
                repository.addSearchQuery(query)
            }
        }
    }

    fun clearSearchQuery() {
        _searchQuery.value = ""
    }

    fun setCategory(category: ProductCategory) {
        _selectedCategory.value = category
    }

    fun setSource(source: ProductSource?) {
        _selectedSource.value = source
    }

    fun setSort(sort: SortOption) {
        _selectedSort.value = sort
    }

    fun setMaxPrice(price: Double) {
        _maxPriceFilter.value = price
    }

    fun setMinRating(rating: Float) {
        _minRatingFilter.value = rating
    }

    fun resetFilters() {
        _selectedCategory.value = ProductCategory.ALL
        _selectedSource.value = null
        _selectedSort.value = SortOption.TRENDING
        _maxPriceFilter.value = 150.0
        _minRatingFilter.value = 0f
    }

    fun addToCart(productId: String, quantity: Int = 1) {
        viewModelScope.launch {
            repository.addToCart(productId, quantity)
        }
    }

    fun updateCartQuantity(productId: String, quantity: Int) {
        viewModelScope.launch {
            repository.updateCartQuantity(productId, quantity)
        }
    }

    fun removeFromCart(productId: String) {
        viewModelScope.launch {
            repository.removeFromCart(productId)
        }
    }

    fun toggleWishlist(productId: String) {
        viewModelScope.launch {
            repository.toggleWishlist(productId)
        }
    }

    fun applyPromoCode(code: String): Boolean {
        return repository.applyPromoCode(code)
    }

    fun removePromoCode() {
        repository.removePromoCode()
    }

    fun switchCurrency(currency: String) {
        repository.setCurrency(currency)
    }

    fun formatPrice(usdPrice: Double): String {
        val user = userProfile.value
        val converted = usdPrice * user.currencyRate
        return if (user.selectedCurrency == "PKR") {
            "${user.currencySymbol}${String.format(Locale.US, "%,.0f", converted)}"
        } else {
            "${user.currencySymbol}${String.format(Locale.US, "%.2f", converted)}"
        }
    }

    fun placeOrder(
        customerName: String,
        email: String,
        phone: String,
        address: String,
        city: String,
        country: String,
        postalCode: String,
        paymentMethod: String,
        onSuccess: (OrderEntity) -> Unit
    ) {
        viewModelScope.launch {
            val summary = cartSummary.value
            val items = summary.items.map { it.product to it.cartItem.quantity }
            if (items.isNotEmpty()) {
                val order = repository.placeOrder(
                    customerName = customerName,
                    email = email,
                    phone = phone,
                    address = address,
                    city = city,
                    country = country,
                    postalCode = postalCode,
                    paymentMethod = paymentMethod,
                    items = items,
                    subtotal = summary.subtotal,
                    shippingCost = summary.shippingCost,
                    discount = summary.discount
                )
                _trackedOrder.value = order
                _trackingSearchInput.value = order.orderNumber
                onSuccess(order)
            }
        }
    }

    fun setTrackingSearch(query: String) {
        _trackingSearchInput.value = query
    }

    fun lookupOrder(query: String) {
        viewModelScope.launch {
            val order = repository.getOrderByIdOrNumber(query.trim())
            _trackedOrder.value = order
        }
    }

    fun toggleComparisonProduct(productId: String) {
        val current = _comparisonProductIds.value.toMutableList()
        if (current.contains(productId)) {
            current.remove(productId)
        } else {
            if (current.size >= 4) current.removeAt(0)
            current.add(productId)
        }
        _comparisonProductIds.value = current
    }

    fun subscribeNewsletter(email: String) {
        if (email.contains("@")) {
            _newsletterSubscribed.value = true
        }
    }

    fun triggerMarketplaceSync() {
        viewModelScope.launch {
            repository.triggerMarketplaceSync()
        }
    }
}
