package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.model.ProductEntity
import com.example.data.model.ProductSource
import com.example.ui.components.ProductCard
import com.example.ui.components.SourceBadge
import com.example.ui.theme.BrandDarkCanvas
import com.example.ui.theme.BrandDarkCard
import com.example.ui.theme.BrandGold
import com.example.ui.theme.BrandGrayLight
import com.example.ui.theme.BrandGrayMedium
import com.example.ui.theme.BrandGreen
import com.example.ui.theme.BrandGreenDark
import com.example.ui.theme.BrandGreenLight
import com.example.ui.theme.BrandSaleOrange
import com.example.ui.theme.BrandTextDark
import com.example.ui.theme.BrandTextMuted
import com.example.ui.theme.BrandWhite
import com.example.ui.theme.Typography
import com.example.ui.viewmodel.StoreViewModel

// 1. Wishlist Screen
@Composable
fun WishlistScreen(
    viewModel: StoreViewModel,
    onNavigateToShop: () -> Unit,
    onNavigateToProduct: (String) -> Unit,
    onExternalAffiliateClick: (ProductEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val wishlistItems by viewModel.wishlistItems.collectAsState()
    val allProducts by viewModel.allProducts.collectAsState()

    val wishlistedProducts = remember(wishlistItems, allProducts) {
        val ids = wishlistItems.map { it.productId }.toSet()
        allProducts.filter { ids.contains(it.id) }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandGrayLight)
            .testTag("wishlist_screen")
    ) {
        Surface(color = BrandWhite, modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Favorite, contentDescription = null, tint = Color.Red)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "YOUR WISHLIST (${wishlistedProducts.size})",
                    style = Typography.titleLarge.copy(fontWeight = FontWeight.Black)
                )
            }
        }

        if (wishlistedProducts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null,
                        tint = BrandTextMuted,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Your Wishlist is Empty",
                        style = Typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Save items you love and buy them whenever you're ready.",
                        style = Typography.bodyMedium.copy(color = BrandTextMuted)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = onNavigateToShop,
                        colors = ButtonDefaults.buttonColors(containerColor = BrandGreen, contentColor = Color.Black),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("EXPLORE PRODUCTS", fontWeight = FontWeight.Black)
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                val chunked = wishlistedProducts.chunked(2)
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
                                isInWishlist = true,
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
}

// 2. Cross-Marketplace Comparison Screen
@Composable
fun CompareDealsScreen(
    viewModel: StoreViewModel,
    onBackClick: () -> Unit,
    onNavigateToProduct: (String) -> Unit,
    onExternalAffiliateClick: (ProductEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val allProducts by viewModel.allProducts.collectAsState()
    val compareIds by viewModel.comparisonProductIds.collectAsState()

    val compareProducts = remember(compareIds, allProducts) {
        allProducts.filter { compareIds.contains(it.id) }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BrandGrayLight)
            .testTag("compare_deals_screen")
    ) {
        item {
            Surface(color = BrandDarkCanvas, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = BrandWhite)
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "CROSS-MARKETPLACE COMPARISON",
                            style = Typography.titleLarge.copy(fontWeight = FontWeight.Black, color = BrandWhite)
                        )
                    }
                    Text(
                        text = "Side-by-side comparison across CJ Dropshipping, eBay, AliExpress, and Direct channels.",
                        style = Typography.bodySmall.copy(color = Color(0xFF94A3B8)),
                        modifier = Modifier.padding(start = 48.dp)
                    )
                }
            }
        }

        // Horizontal comparison scroll container
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = BrandWhite),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, BrandGrayMedium.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        compareProducts.forEach { product ->
                            Column(
                                modifier = Modifier
                                    .width(200.dp)
                                    .background(BrandGrayLight, RoundedCornerShape(8.dp))
                                    .padding(10.dp)
                            ) {
                                // Image
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(110.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(BrandWhite)
                                ) {
                                    if (product.imageDrawableRes != 0) {
                                        Image(
                                            painter = painterResource(id = product.imageDrawableRes),
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.matchParentSize()
                                        )
                                    } else {
                                        AsyncImage(
                                            model = product.affiliateUrl,
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.matchParentSize()
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))
                                SourceBadge(sourceName = product.source)

                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = product.title,
                                    style = Typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = viewModel.formatPrice(product.price),
                                    style = Typography.titleMedium.copy(fontWeight = FontWeight.Black, color = BrandGreenDark)
                                )

                                HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))

                                // Supplier
                                Text("Supplier:", style = Typography.labelSmall.copy(color = BrandTextMuted))
                                Text(product.supplier, style = Typography.bodySmall.copy(fontWeight = FontWeight.Bold))

                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Shipping Info:", style = Typography.labelSmall.copy(color = BrandTextMuted))
                                Text(product.shippingInfo, style = Typography.bodySmall.copy(fontSize = 11.sp))

                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Buyer Guarantee:", style = Typography.labelSmall.copy(color = BrandTextMuted))
                                Text(product.returnPolicy, style = Typography.bodySmall.copy(fontSize = 11.sp, color = BrandGreenDark))

                                Spacer(modifier = Modifier.height(10.dp))

                                if (product.source == ProductSource.DIRECT.name || product.source == ProductSource.CJ_DROPSHIPPING.name) {
                                    Button(
                                        onClick = { viewModel.addToCart(product.id, 1) },
                                        colors = ButtonDefaults.buttonColors(containerColor = BrandGreen, contentColor = Color.Black),
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.fillMaxWidth().height(34.dp)
                                    ) {
                                        Text("ADD TO CART", fontSize = 10.sp, fontWeight = FontWeight.Black)
                                    }
                                } else {
                                    OutlinedButton(
                                        onClick = { onExternalAffiliateClick(product) },
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = BrandGreenDark),
                                        border = BorderStroke(1.dp, BrandGreenDark),
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.fillMaxWidth().height(34.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text("VIEW DEAL", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Icon(Icons.Default.OpenInNew, contentDescription = null, modifier = Modifier.size(10.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
