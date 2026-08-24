package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ProductCategory
import com.example.data.model.ProductEntity
import com.example.data.model.ProductSource
import com.example.data.sample.SampleData
import com.example.ui.components.FlashDealHeader
import com.example.ui.components.FooterSection
import com.example.ui.components.HeroSection
import com.example.ui.components.ProductCard
import com.example.ui.components.TrustBenefitSection
import com.example.ui.theme.BrandDarkCanvas
import com.example.ui.theme.BrandDarkCard
import com.example.ui.theme.BrandGold
import com.example.ui.theme.BrandGrayLight
import com.example.ui.theme.BrandGrayMedium
import com.example.ui.theme.BrandGreen
import com.example.ui.theme.BrandGreenContainer
import com.example.ui.theme.BrandGreenDark
import com.example.ui.theme.BrandGreenLight
import com.example.ui.theme.BrandTextDark
import com.example.ui.theme.BrandTextMuted
import com.example.ui.theme.BrandWhite
import com.example.ui.theme.Typography
import com.example.ui.viewmodel.StoreViewModel

@Composable
fun HomeScreen(
    viewModel: StoreViewModel,
    onNavigateToShop: () -> Unit,
    onNavigateToProduct: (String) -> Unit,
    onNavigateToCompare: () -> Unit,
    onNavigateToLegal: (String) -> Unit,
    onExternalAffiliateClick: (ProductEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val allProducts by viewModel.allProducts.collectAsState()
    val trendingProducts by viewModel.trendingProducts.collectAsState()
    val flashDeals by viewModel.flashDeals.collectAsState()
    val wishlistItems by viewModel.wishlistItems.collectAsState()
    val newsletterSubscribed by viewModel.newsletterSubscribed.collectAsState()

    val wishlistIds = remember(wishlistItems) { wishlistItems.map { it.productId }.toSet() }

    var selectedSourceFilter by remember { mutableStateOf<ProductSource?>(null) }
    var selectedCategoryFilter by remember { mutableStateOf(ProductCategory.ALL) }

    val displayedProducts = remember(allProducts, selectedSourceFilter, selectedCategoryFilter) {
        allProducts.filter { prod ->
            val sourceMatch = selectedSourceFilter == null || prod.source == selectedSourceFilter?.name
            val catMatch = selectedCategoryFilter == ProductCategory.ALL || prod.category == selectedCategoryFilter.name
            sourceMatch && catMatch
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BrandGrayLight)
            .testTag("home_screen_scroll")
    ) {
        // 1. Dark Hero Section
        item {
            HeroSection(
                onShopNowClick = onNavigateToShop,
                onExploreDealsClick = {
                    selectedSourceFilter = ProductSource.CJ_AFFILIATE
                    onNavigateToShop()
                }
            )
        }

        // 2. 4 Trust Benefit Columns
        item {
            TrustBenefitSection()
            Spacer(modifier = Modifier.height(14.dp))
        }

        // 3. Flash Sale Header & Carousel
        item {
            FlashDealHeader()
            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(flashDeals) { product ->
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
                        modifier = Modifier.width(180.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // 4. Featured Category Chips Row
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "EXPLORE CATEGORIES",
                        style = Typography.headlineMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = BrandTextDark,
                            fontSize = 17.sp
                        )
                    )
                    Text(
                        text = "View All",
                        style = Typography.labelMedium.copy(color = BrandGreenDark, fontWeight = FontWeight.Bold),
                        modifier = Modifier.clickable { onNavigateToShop() }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ProductCategory.entries.forEach { category ->
                        val isSelected = selectedCategoryFilter == category
                        Surface(
                            color = if (isSelected) BrandGreenDark else BrandWhite,
                            shape = RoundedCornerShape(20.dp),
                            border = BorderStroke(1.dp, if (isSelected) BrandGreenDark else BrandGrayMedium),
                            modifier = Modifier.clickable { selectedCategoryFilter = category }
                        ) {
                            Text(
                                text = category.displayName,
                                color = if (isSelected) BrandWhite else BrandTextDark,
                                style = Typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // 5. Trending Products Carousel
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(BrandGreenLight, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = BrandGreenDark,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "WHAT'S TRENDING NOW",
                            style = Typography.headlineMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = BrandTextDark,
                                fontSize = 17.sp
                            )
                        )
                    }
                    Text(
                        text = "See All",
                        style = Typography.labelMedium.copy(color = BrandGreenDark, fontWeight = FontWeight.Bold),
                        modifier = Modifier.clickable { onNavigateToShop() }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(trendingProducts) { product ->
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
                            modifier = Modifier.width(180.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // 6. Marketplace Source Filter Tabs
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "MULTI-SOURCE MARKETPLACE PICKS",
                    style = Typography.headlineMedium.copy(
                        fontWeight = FontWeight.Black,
                        color = BrandTextDark,
                        fontSize = 17.sp
                    )
                )
                Text(
                    text = "Browse curated listings by marketplace supplier",
                    style = Typography.bodySmall.copy(color = BrandTextMuted)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val sources = listOf(null) + ProductSource.entries
                    sources.forEach { source ->
                        val isSelected = selectedSourceFilter == source
                        val label = source?.displayName ?: "All Sources"
                        Surface(
                            color = if (isSelected) BrandDarkCanvas else BrandWhite,
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, if (isSelected) BrandGreen else BrandGrayMedium),
                            modifier = Modifier.clickable { selectedSourceFilter = source }
                        ) {
                            Text(
                                text = label,
                                color = if (isSelected) BrandGreen else BrandTextDark,
                                style = Typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        // 7. Dynamic Product Grid (2 columns)
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                displayedProducts.chunked(2).forEach { rowProducts ->
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
            Spacer(modifier = Modifier.height(20.dp))
        }

        // 8. Cross-Marketplace Deal Comparison Banner
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = BrandDarkCanvas),
                border = BorderStroke(1.dp, BrandGreen.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable { onNavigateToCompare() }
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CompareArrows,
                                contentDescription = null,
                                tint = BrandGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "CROSS-MARKETPLACE COMPARISON",
                                style = Typography.labelMedium.copy(fontWeight = FontWeight.Black, color = BrandGreen)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Compare CJ Dropshipping vs eBay vs AliExpress vs Etsy side-by-side on price, delivery times, and buyer protection.",
                            style = Typography.bodySmall.copy(color = Color(0xFFE2E8F0), lineHeight = 16.sp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = onNavigateToCompare,
                        colors = ButtonDefaults.buttonColors(containerColor = BrandGreen, contentColor = Color.Black),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("COMPARE", style = Typography.labelSmall.copy(fontWeight = FontWeight.Black))
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // 9. Verified Customer Reviews
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "CUSTOMER EXPERIENCES & REVIEWS",
                    style = Typography.headlineMedium.copy(
                        fontWeight = FontWeight.Black,
                        color = BrandTextDark,
                        fontSize = 17.sp
                    )
                )
                Text(
                    text = "Real feedback from verified buyers across our supplier network",
                    style = Typography.bodySmall.copy(color = BrandTextMuted)
                )

                Spacer(modifier = Modifier.height(12.dp))

                SampleData.sampleReviews.take(3).forEach { review ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = BrandWhite),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, BrandGrayMedium.copy(alpha = 0.5f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = review.reviewerName,
                                        style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = BrandTextDark)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = null,
                                        tint = BrandGreenDark,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                                Text(
                                    text = review.date,
                                    style = Typography.bodySmall.copy(color = BrandTextMuted, fontSize = 10.sp)
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                repeat(5) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = BrandGold,
                                        modifier = Modifier.size(13.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = review.location,
                                    style = Typography.bodySmall.copy(fontSize = 10.sp, color = BrandGreenDark, fontWeight = FontWeight.Bold)
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "\"${review.comment}\"",
                                style = Typography.bodyMedium.copy(color = BrandTextDark, lineHeight = 18.sp)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // 10. Full Store Footer & Policies
        item {
            FooterSection(
                onNavigateLegal = onNavigateToLegal,
                onSubscribe = { email -> viewModel.subscribeNewsletter(email) },
                isSubscribed = newsletterSubscribed
            )
        }
    }
}
