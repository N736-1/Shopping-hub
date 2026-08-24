package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ProductCategory
import com.example.data.model.ProductEntity
import com.example.data.model.ProductSource
import com.example.ui.components.ProductCard
import com.example.ui.theme.BrandDarkCanvas
import com.example.ui.theme.BrandDarkCard
import com.example.ui.theme.BrandGold
import com.example.ui.theme.BrandGrayLight
import com.example.ui.theme.BrandGrayMedium
import com.example.ui.theme.BrandGreen
import com.example.ui.theme.BrandGreenDark
import com.example.ui.theme.BrandGreenLight
import com.example.ui.theme.BrandTextDark
import com.example.ui.theme.BrandTextMuted
import com.example.ui.theme.BrandWhite
import com.example.ui.theme.Typography
import com.example.ui.viewmodel.SortOption
import com.example.ui.viewmodel.StoreViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopScreen(
    viewModel: StoreViewModel,
    onNavigateToProduct: (String) -> Unit,
    onExternalAffiliateClick: (ProductEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedSource by viewModel.selectedSource.collectAsState()
    val selectedSort by viewModel.selectedSort.collectAsState()
    val maxPrice by viewModel.maxPriceFilter.collectAsState()
    val minRating by viewModel.minRatingFilter.collectAsState()

    val filteredProducts by viewModel.filteredProducts.collectAsState()
    val wishlistItems by viewModel.wishlistItems.collectAsState()
    val recentSearches by viewModel.recentSearches.collectAsState()
    val wishlistIds = remember(wishlistItems) { wishlistItems.map { it.productId }.toSet() }

    var showFilterSheet by remember { mutableStateOf(false) }
    var sortMenuExpanded by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandGrayLight)
    ) {
        // Top Search Bar
        Surface(
            color = BrandWhite,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    placeholder = { Text("Search products, brands, ingredients...", color = BrandTextMuted) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = BrandGreenDark)
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.clearSearchQuery() }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear", tint = BrandTextMuted)
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrandGreen,
                        unfocusedBorderColor = BrandGrayMedium,
                        focusedContainerColor = BrandWhite,
                        unfocusedContainerColor = BrandWhite
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("shop_search_input")
                )

                // Recent searches if query empty and searches exist
                if (searchQuery.isEmpty() && recentSearches.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Recent:",
                            style = Typography.labelSmall.copy(color = BrandTextMuted, fontSize = 10.sp)
                        )
                        recentSearches.forEach { search ->
                            Surface(
                                color = BrandGrayLight,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.clickable { viewModel.setSearchQuery(search.query) }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Icon(Icons.Default.History, contentDescription = null, tint = BrandTextMuted, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(search.query, style = Typography.bodySmall.copy(fontSize = 11.sp))
                                }
                            }
                        }
                    }
                }
            }
        }

        // Category Filter Chips
        Surface(
            color = BrandWhite,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                ProductCategory.entries.forEach { category ->
                    val isSelected = selectedCategory == category
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.setCategory(category) },
                        label = { Text(category.displayName, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BrandGreenDark,
                            selectedLabelColor = BrandWhite,
                            containerColor = BrandGrayLight,
                            labelColor = BrandTextDark
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = if (isSelected) BrandGreenDark else BrandGrayMedium
                        )
                    )
                }
            }
        }

        // Filter and Sort Bar
        Surface(
            color = Color(0xFFF1F5F0),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${filteredProducts.size} Products Found",
                    style = Typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = BrandTextDark)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Filter Dialog Trigger
                    Button(
                        onClick = { showFilterSheet = true },
                        colors = ButtonDefaults.buttonColors(containerColor = BrandWhite, contentColor = BrandTextDark),
                        border = BorderStroke(1.dp, BrandGrayMedium),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Icon(Icons.Default.FilterList, contentDescription = null, tint = BrandGreenDark, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Filters", style = Typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                    }

                    // Sort Dropdown
                    Box {
                        Button(
                            onClick = { sortMenuExpanded = true },
                            colors = ButtonDefaults.buttonColors(containerColor = BrandWhite, contentColor = BrandTextDark),
                            border = BorderStroke(1.dp, BrandGrayMedium),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text("Sort: ${selectedSort.displayName}", style = Typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                        }

                        DropdownMenu(
                            expanded = sortMenuExpanded,
                            onDismissRequest = { sortMenuExpanded = false }
                        ) {
                            SortOption.entries.forEach { sortOpt ->
                                DropdownMenuItem(
                                    text = { Text(sortOpt.displayName) },
                                    onClick = {
                                        viewModel.setSort(sortOpt)
                                        sortMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Active filter chips row if any
        if (selectedSource != null || maxPrice < 150.0 || minRating > 0f) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BrandWhite)
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Active:", style = Typography.labelSmall.copy(color = BrandTextMuted))
                if (selectedSource != null) {
                    Surface(
                        color = BrandGreenLight,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.clickable { viewModel.setSource(null) }
                    ) {
                        Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(selectedSource!!.displayName, style = Typography.labelSmall.copy(color = BrandGreenDark))
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.Clear, contentDescription = null, tint = BrandGreenDark, modifier = Modifier.size(10.dp))
                        }
                    }
                }
                if (maxPrice < 150.0) {
                    Surface(
                        color = BrandGreenLight,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.clickable { viewModel.setMaxPrice(150.0) }
                    ) {
                        Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text("Under $${maxPrice.toInt()}", style = Typography.labelSmall.copy(color = BrandGreenDark))
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.Default.Clear, contentDescription = null, tint = BrandGreenDark, modifier = Modifier.size(10.dp))
                        }
                    }
                }
                Text(
                    text = "Reset All",
                    style = Typography.labelSmall.copy(color = BrandGreenDark, fontWeight = FontWeight.Bold),
                    modifier = Modifier.clickable { viewModel.resetFilters() }
                )
            }
        }

        // Product Results Grid
        if (filteredProducts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "No products found",
                        style = Typography.headlineMedium.copy(fontWeight = FontWeight.Bold, color = BrandTextDark)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Try adjusting your search terms or clearing your filters.",
                        style = Typography.bodyMedium.copy(color = BrandTextMuted)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.resetFilters(); viewModel.clearSearchQuery() },
                        colors = ButtonDefaults.buttonColors(containerColor = BrandGreenDark)
                    ) {
                        Icon(Icons.Default.RestartAlt, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Reset All Filters")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .testTag("shop_products_grid")
            ) {
                val chunked = filteredProducts.chunked(2)
                items(chunked) { rowProducts ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowProducts.forEach { product ->
                            ProductCard(
                                product = product,
                                formattedPrice = viewModel.formatPrice(product.price),
                                formattedCompareAtPrice = viewModel.formatPrice(product.compareAtPrice),
                                isInWishlist = wishlistIds.contains(product.id),
                                onProductClick = { onNavigateToProduct(product.id) },
                                onWishlistToggle = { viewModel.toggleWishlist(product.id) },
                                onCtaClick = {
                                    if (product.source == ProductSource.DIRECT.name || product.source == ProductSource.CJ_DROPSHIPPING.name) {
                                        viewModel.addToCart(product.id, 1)
                                    } else {
                                        onExternalAffiliateClick(product)
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (rowProducts.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }

    // Filter Bottom Sheet
    if (showFilterSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFilterSheet = false },
            sheetState = sheetState,
            containerColor = BrandWhite
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Filter Products", style = Typography.headlineMedium.copy(fontWeight = FontWeight.Black))
                    Text(
                        text = "Reset",
                        color = BrandGreenDark,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { viewModel.resetFilters() }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Supplier / Source
                Text("Marketplace Source", style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val sources = listOf(null) + ProductSource.entries
                    sources.forEach { source ->
                        val isSelected = selectedSource == source
                        Surface(
                            color = if (isSelected) BrandDarkCanvas else BrandGrayLight,
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, if (isSelected) BrandGreen else BrandGrayMedium),
                            modifier = Modifier.clickable { viewModel.setSource(source) }
                        ) {
                            Text(
                                text = source?.displayName ?: "All Sources",
                                color = if (isSelected) BrandGreen else BrandTextDark,
                                style = Typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Max Price Slider
                Text("Max Price: $${maxPrice.toInt()}", style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Slider(
                    value = maxPrice.toFloat(),
                    onValueChange = { viewModel.setMaxPrice(it.toDouble()) },
                    valueRange = 10f..150f,
                    colors = SliderDefaults.colors(
                        thumbColor = BrandGreenDark,
                        activeTrackColor = BrandGreenDark,
                        inactiveTrackColor = BrandGrayMedium
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Minimum Rating
                Text("Minimum Rating", style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(0f to "Any", 4.0f to "4.0+ ★", 4.5f to "4.5+ ★", 4.8f to "4.8+ ★").forEach { (rating, label) ->
                        val isSelected = minRating == rating
                        Surface(
                            color = if (isSelected) BrandGreenDark else BrandGrayLight,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.clickable { viewModel.setMinRating(rating) }
                        ) {
                            Text(
                                text = label,
                                color = if (isSelected) BrandWhite else BrandTextDark,
                                style = Typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            showFilterSheet = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandGreen, contentColor = Color.Black),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().height(48.dp)
                ) {
                    Text("APPLY FILTERS (${filteredProducts.size} RESULTS)", fontWeight = FontWeight.Black)
                }
            }
        }
    }
}
