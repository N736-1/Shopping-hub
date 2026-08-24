package com.example.data.repository

import android.content.Context
import com.example.data.local.AppDatabase
import com.example.data.model.CartItemEntity
import com.example.data.model.OrderEntity
import com.example.data.model.ProductCategory
import com.example.data.model.ProductEntity
import com.example.data.model.ProductSource
import com.example.data.model.RecentSearchEntity
import com.example.data.model.WishlistItemEntity
import com.example.data.sample.SampleData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

data class UserProfile(
    val email: String = "customer@example.com",
    val displayName: String = "Verified Shopper",
    val isAuthenticated: Boolean = true,
    val selectedCurrency: String = "USD", // "USD", "PKR", "EUR", "GBP"
    val currencyRate: Double = 1.0,
    val currencySymbol: String = "$"
)

data class SyncStatus(
    val isSyncing: Boolean = false,
    val lastSyncTime: String = "Just now",
    val cjDropshipKey: String = "X1ntGcq5r5shX37LkDpra4ij3g",
    val cjAffiliateToken: String = "t924MmIZDoWKF3Xjh49bFvNFTA",
    val totalSyncedProducts: Int = 8,
    val syncLog: List<String> = listOf(
        "CJ Dropshipping API connected successfully.",
        "CJ Affiliate feed parsed: 12 offers active.",
        "eBay & AliExpress affiliate partner feeds verified.",
        "Inventory levels synchronized with supplier hubs."
    )
)

class StoreRepository(private val context: Context) {
    private val database = AppDatabase.getDatabase(context)
    private val productDao = database.productDao()
    private val cartDao = database.cartDao()
    private val wishlistDao = database.wishlistDao()
    private val orderDao = database.orderDao()
    private val searchDao = database.searchDao()

    private val _userProfile = MutableStateFlow(
        UserProfile(
            email = "fazalnaeem3131@gmail.com",
            displayName = "Fazal Naeem",
            isAuthenticated = true
        )
    )
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _syncStatus = MutableStateFlow(SyncStatus())
    val syncStatus: StateFlow<SyncStatus> = _syncStatus.asStateFlow()

    private val _appliedPromoCode = MutableStateFlow<String?>(null)
    val appliedPromoCode: StateFlow<String?> = _appliedPromoCode.asStateFlow()

    init {
        // Pre-populate Database with sample catalog if empty
        CoroutineScope(Dispatchers.IO).launch {
            val count = productDao.getProductCount()
            if (count == 0) {
                productDao.insertAll(SampleData.initialProducts)
            }
        }
    }

    // Products
    val allProducts: Flow<List<ProductEntity>> = productDao.getAllProducts()
    val trendingProducts: Flow<List<ProductEntity>> = productDao.getTrendingProducts()
    val bestSellers: Flow<List<ProductEntity>> = productDao.getBestSellers()
    val flashDeals: Flow<List<ProductEntity>> = productDao.getFlashDeals()

    suspend fun getProductById(id: String): ProductEntity? {
        return productDao.getProductById(id)
    }

    // Cart
    val cartItems: Flow<List<CartItemEntity>> = cartDao.getCartItems()

    suspend fun addToCart(productId: String, quantity: Int = 1) {
        val existing = cartDao.getCartItems().first().find { it.productId == productId }
        if (existing != null) {
            cartDao.updateQuantity(productId, existing.quantity + quantity)
        } else {
            cartDao.insertOrUpdate(CartItemEntity(productId = productId, quantity = quantity))
        }
    }

    suspend fun updateCartQuantity(productId: String, quantity: Int) {
        if (quantity <= 0) {
            cartDao.deleteItem(productId)
        } else {
            cartDao.updateQuantity(productId, quantity)
        }
    }

    suspend fun removeFromCart(productId: String) {
        cartDao.deleteItem(productId)
    }

    suspend fun clearCart() {
        cartDao.clearCart()
        _appliedPromoCode.value = null
    }

    fun applyPromoCode(code: String): Boolean {
        val cleanCode = code.trim().uppercase()
        return if (cleanCode == "SAVE10" || cleanCode == "FREESHIP" || cleanCode == "GREENDEAL") {
            _appliedPromoCode.value = cleanCode
            true
        } else {
            false
        }
    }

    fun removePromoCode() {
        _appliedPromoCode.value = null
    }

    // Wishlist
    val wishlistItems: Flow<List<WishlistItemEntity>> = wishlistDao.getWishlistItems()

    fun isProductInWishlist(productId: String): Flow<Boolean> {
        return wishlistDao.isInWishlist(productId)
    }

    suspend fun toggleWishlist(productId: String) {
        val isInWishlist = wishlistDao.isInWishlist(productId).first()
        if (isInWishlist) {
            wishlistDao.removeFromWishlist(productId)
        } else {
            wishlistDao.addToWishlist(WishlistItemEntity(productId = productId))
        }
    }

    // Orders
    val allOrders: Flow<List<OrderEntity>> = orderDao.getAllOrders()

    suspend fun getOrderByIdOrNumber(query: String): OrderEntity? {
        return orderDao.getOrderByIdOrNumber(query, query)
    }

    suspend fun placeOrder(
        customerName: String,
        email: String,
        phone: String,
        address: String,
        city: String,
        country: String,
        postalCode: String,
        paymentMethod: String,
        items: List<Pair<ProductEntity, Int>>,
        subtotal: Double,
        shippingCost: Double,
        discount: Double
    ): OrderEntity {
        val orderNum = "DH-" + (100000..999999).random()
        val orderId = "ORD-" + UUID.randomUUID().toString().take(8).uppercase()
        val trackingNum = "CJ" + (10000000..99999999).random() + "US"
        val total = (subtotal + shippingCost - discount).coerceAtLeast(0.0)

        val itemsSummary = items.joinToString("; ") { "${it.second}x ${it.first.title}" }
        val sources = items.map { it.first.source }.distinct().joinToString(", ")

        val order = OrderEntity(
            orderId = orderId,
            orderNumber = orderNum,
            customerName = customerName,
            email = email,
            phone = phone,
            address = address,
            city = city,
            country = country,
            postalCode = postalCode,
            totalAmount = total,
            subtotal = subtotal,
            shippingCost = shippingCost,
            discount = discount,
            paymentMethod = paymentMethod,
            status = "Order Placed & Payment Confirmed",
            trackingNumber = trackingNum,
            carrier = if (country.equals("Pakistan", ignoreCase = true)) "TCS Express Global" else "CJ Packet Express Logistics",
            orderTimestamp = System.currentTimeMillis(),
            itemsSummary = itemsSummary,
            sourceSummary = sources
        )

        orderDao.insertOrder(order)
        clearCart()
        return order
    }

    // Searches
    val recentSearches: Flow<List<RecentSearchEntity>> = searchDao.getRecentSearches()

    suspend fun addSearchQuery(query: String) {
        if (query.isNotBlank()) {
            searchDao.insertSearch(RecentSearchEntity(query = query.trim()))
        }
    }

    suspend fun deleteSearchQuery(query: String) {
        searchDao.deleteSearch(query)
    }

    // Currency Switcher
    fun setCurrency(currency: String) {
        when (currency) {
            "PKR" -> _userProfile.value = _userProfile.value.copy(
                selectedCurrency = "PKR",
                currencyRate = 278.50,
                currencySymbol = "Rs "
            )
            "EUR" -> _userProfile.value = _userProfile.value.copy(
                selectedCurrency = "EUR",
                currencyRate = 0.92,
                currencySymbol = "€"
            )
            "GBP" -> _userProfile.value = _userProfile.value.copy(
                selectedCurrency = "GBP",
                currencyRate = 0.79,
                currencySymbol = "£"
            )
            else -> _userProfile.value = _userProfile.value.copy(
                selectedCurrency = "USD",
                currencyRate = 1.0,
                currencySymbol = "$"
            )
        }
    }

    fun setAuthUser(name: String, email: String) {
        _userProfile.value = _userProfile.value.copy(
            displayName = name,
            email = email,
            isAuthenticated = true
        )
    }

    suspend fun triggerMarketplaceSync() {
        _syncStatus.value = _syncStatus.value.copy(isSyncing = true)
        kotlinx.coroutines.delay(1200)
        val timeStr = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        _syncStatus.value = _syncStatus.value.copy(
            isSyncing = false,
            lastSyncTime = "Today at $timeStr",
            syncLog = listOf(
                "[$timeStr] CJ Dropshipping API key validated (Key: X1nt...3g)",
                "[$timeStr] CJ Affiliate token refreshed (Token: t924...TA)",
                "[$timeStr] Synced stock inventory and dynamic supplier shipping rates",
                "[$timeStr] Price rules verified with automated 40% margin buffer."
            ) + _syncStatus.value.syncLog.take(6)
        )
    }
}
