package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.model.ProductEntity
import com.example.data.model.ProductSource
import com.example.data.sample.SampleData
import com.example.ui.components.ProductCard
import com.example.ui.components.SourceBadge
import com.example.ui.theme.BrandDarkCanvas
import com.example.ui.theme.BrandDarkCard
import com.example.ui.theme.BrandGold
import com.example.ui.theme.BrandGrayLight
import com.example.ui.theme.BrandGrayMedium
import com.example.ui.theme.BrandGreen
import com.example.ui.theme.BrandGreenContainer
import com.example.ui.theme.BrandGreenDark
import com.example.ui.theme.BrandGreenLight
import com.example.ui.theme.BrandSaleOrange
import com.example.ui.theme.BrandTextDark
import com.example.ui.theme.BrandTextMuted
import com.example.ui.theme.BrandWhite
import com.example.ui.theme.Typography
import com.example.ui.viewmodel.StoreViewModel

@Composable
fun ProductDetailScreen(
    productId: String,
    viewModel: StoreViewModel,
    onBackClick: () -> Unit,
    onNavigateToProduct: (String) -> Unit,
    onNavigateToCart: () -> Unit,
    onExternalAffiliateClick: (ProductEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val allProducts by viewModel.allProducts.collectAsState()
    val product = allProducts.find { it.id == productId } ?: SampleData.initialProducts.first()

    val wishlistItems by viewModel.wishlistItems.collectAsState()
    val isInWishlist = wishlistItems.any { it.productId == product.id }

    var quantity by remember { mutableIntStateOf(1) }
    var selectedCountry by remember { mutableStateOf("United States") }
    var countryMenuExpanded by remember { mutableStateOf(false) }
    var addedToCartSnackbar by remember { mutableStateOf(false) }

    val isAffiliate = product.source != ProductSource.DIRECT.name && product.source != ProductSource.CJ_DROPSHIPPING.name

    val relatedProducts = remember(allProducts, product) {
        allProducts.filter { it.id != product.id && (it.category == product.category || it.source == product.source) }.take(4)
    }

    val shippingRate = SampleData.shippingZones.find { it.country == selectedCountry } ?: SampleData.shippingZones.first()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BrandGrayLight)
            .testTag("product_detail_screen")
    ) {
        // Top Navigation Bar
        item {
            Surface(color = BrandWhite, modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick, modifier = Modifier.testTag("detail_back_btn")) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = BrandTextDark)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { viewModel.toggleWishlist(product.id) }) {
                            Icon(
                                imageVector = if (isInWishlist) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Wishlist",
                                tint = if (isInWishlist) Color.Red else BrandTextDark
                            )
                        }
                        IconButton(onClick = onNavigateToCart) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Cart", tint = BrandTextDark)
                        }
                    }
                }
            }
        }

        // Product Gallery Image
        item {
            Surface(color = BrandWhite, modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .padding(16.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(BrandGrayLight)
                ) {
                    if (product.imageDrawableRes != 0) {
                        Image(
                            painter = painterResource(id = product.imageDrawableRes),
                            contentDescription = product.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.matchParentSize()
                        )
                    } else {
                        AsyncImage(
                            model = product.affiliateUrl,
                            contentDescription = product.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.matchParentSize(),
                            error = painterResource(id = R.drawable.img_hero_banner)
                        )
                    }

                    // Floating Badges
                    Box(modifier = Modifier.padding(10.dp).align(Alignment.TopStart)) {
                        SourceBadge(sourceName = product.source)
                    }

                    if (product.compareAtPrice > product.price) {
                        val savings = (((product.compareAtPrice - product.price) / product.compareAtPrice) * 100).toInt()
                        Surface(
                            color = BrandSaleOrange,
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.align(Alignment.BottomStart).padding(10.dp)
                        ) {
                            Text(
                                text = "SAVE $savings%",
                                color = BrandWhite,
                                style = Typography.labelSmall.copy(fontWeight = FontWeight.Black),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }
        }

        // Affiliate Source Notice if external merchant
        if (isAffiliate) {
            item {
                Surface(
                    color = Color(0xFFEFF6FF),
                    border = BorderStroke(1.dp, Color(0xFFBFDBFE)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFF1E40AF), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "External Marketplace Deal: Checkout for this item is handled securely on the partner merchant's platform.",
                            style = Typography.bodySmall.copy(color = Color(0xFF1E3A8A), fontSize = 11.sp, lineHeight = 15.sp)
                        )
                    }
                }
            }
        }

        // Product Info Section
        item {
            Surface(
                color = BrandWhite,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = product.brand.uppercase(),
                            style = Typography.labelMedium.copy(color = BrandGreenDark, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = BrandGreenDark, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "In Stock (${product.stockCount} left)",
                                style = Typography.bodySmall.copy(color = BrandGreenDark, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = product.title,
                        style = Typography.headlineMedium.copy(fontWeight = FontWeight.Bold, color = BrandTextDark, fontSize = 18.sp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Rating & Reviews
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        repeat(5) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = BrandGold, modifier = Modifier.size(16.dp))
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${product.rating} (${product.reviewCount} customer reviews)",
                            style = Typography.bodySmall.copy(color = BrandTextDark, fontWeight = FontWeight.Medium)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Price Block
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = viewModel.formatPrice(product.price),
                            style = Typography.displayMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = BrandGreenDark,
                                fontSize = 24.sp
                            )
                        )
                        if (product.compareAtPrice > product.price) {
                            Text(
                                text = viewModel.formatPrice(product.compareAtPrice),
                                style = Typography.titleMedium.copy(
                                    textDecoration = TextDecoration.LineThrough,
                                    color = BrandTextMuted
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = BrandGrayLight, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(16.dp))

                    // Quantity Selector (if direct/dropshipping)
                    if (!isAffiliate) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Quantity", style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .background(BrandGrayLight, RoundedCornerShape(8.dp))
                                    .padding(4.dp)
                            ) {
                                IconButton(
                                    onClick = { if (quantity > 1) quantity-- },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.Default.Remove, contentDescription = "Decrease", modifier = Modifier.size(16.dp))
                                }
                                Text(
                                    text = "$quantity",
                                    style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.padding(horizontal = 12.dp)
                                )
                                IconButton(
                                    onClick = { if (quantity < product.stockCount) quantity++ },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Increase", modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Action Buttons
                    if (isAffiliate) {
                        Button(
                            onClick = { onExternalAffiliateClick(product) },
                            colors = ButtonDefaults.buttonColors(containerColor = BrandGreen, contentColor = Color.Black),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("detail_view_deal_btn")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = when (product.source) {
                                        ProductSource.EBAY.name -> "VIEW DEAL ON EBAY"
                                        ProductSource.ALIEXPRESS.name -> "VIEW ON ALIEXPRESS"
                                        ProductSource.ETSY.name -> "VIEW ON ETSY"
                                        else -> "VIEW PARTNER DEAL"
                                    },
                                    style = Typography.labelLarge.copy(fontWeight = FontWeight.Black)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(Icons.Default.OpenInNew, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    viewModel.addToCart(product.id, quantity)
                                    addedToCartSnackbar = true
                                },
                                border = BorderStroke(1.5.dp, BrandGreenDark),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp)
                                    .testTag("detail_add_cart_btn")
                            ) {
                                Text("ADD TO CART", fontWeight = FontWeight.Bold, color = BrandGreenDark)
                            }

                            Button(
                                onClick = {
                                    viewModel.addToCart(product.id, quantity)
                                    onNavigateToCart()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = BrandGreen, contentColor = Color.Black),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp)
                                    .testTag("detail_buy_now_btn")
                            ) {
                                Text("BUY NOW", fontWeight = FontWeight.Black)
                            }
                        }

                        if (addedToCartSnackbar) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Surface(
                                color = BrandGreenLight,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = BrandGreenDark)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Item added to cart!", style = Typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = BrandGreenDark))
                                    }
                                    Text("View Cart", color = BrandGreenDark, fontWeight = FontWeight.Black, modifier = Modifier.clickable { onNavigateToCart() })
                                }
                            }
                        }
                    }
                }
            }
        }

        // Dynamic Shipping & Delivery Calculator
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = BrandWhite),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, BrandGrayMedium.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocalShipping, contentDescription = null, tint = BrandGreenDark)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Shipping & Delivery", style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        }

                        // Country Selector Dropdown
                        Box {
                            Surface(
                                color = BrandGrayLight,
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier.clickable { countryMenuExpanded = true }
                            ) {
                                Text(
                                    text = "$selectedCountry ▾",
                                    style = Typography.labelSmall.copy(color = BrandGreenDark, fontWeight = FontWeight.Bold),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                            DropdownMenu(
                                expanded = countryMenuExpanded,
                                onDismissRequest = { countryMenuExpanded = false }
                            ) {
                                SampleData.shippingZones.map { it.country }.forEach { country ->
                                    DropdownMenuItem(
                                        text = { Text(country) },
                                        onClick = {
                                            selectedCountry = country
                                            countryMenuExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Carrier: ${shippingRate.method} • Est. ${shippingRate.estimatedDays}",
                        style = Typography.bodySmall.copy(color = BrandTextDark, fontWeight = FontWeight.Medium)
                    )
                    Text(
                        text = if (shippingRate.isFree) "FREE Shipping Eligible" else "Estimated Cost: $${shippingRate.costUsd}",
                        style = Typography.bodySmall.copy(color = BrandGreenDark, fontWeight = FontWeight.Bold)
                    )
                }
            }
        }

        // Product Description & Specifications
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = BrandWhite),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, BrandGrayMedium.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Product Overview", style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = product.fullDescription,
                        style = Typography.bodyMedium.copy(color = BrandTextDark, lineHeight = 22.sp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    Text("Key Highlights", style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(6.dp))

                    product.features.split("|").forEach { feature ->
                        if (feature.isNotBlank()) {
                            Row(
                                modifier = Modifier.padding(vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = BrandGreenDark, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(feature.trim(), style = Typography.bodySmall.copy(color = BrandTextDark))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text("Technical Specifications", style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(6.dp))

                    product.specifications.split(";").forEach { spec ->
                        if (spec.contains(":")) {
                            val parts = spec.split(":")
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(parts[0].trim(), style = Typography.bodySmall.copy(color = BrandTextMuted))
                                Text(parts.getOrElse(1) { "" }.trim(), style = Typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = BrandTextDark))
                            }
                        }
                    }
                }
            }
        }

        // Customer Reviews Breakdown
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = BrandWhite),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, BrandGrayMedium.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Verified Customer Reviews", style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(10.dp))

                    SampleData.sampleReviews.take(2).forEach { review ->
                        Column(modifier = Modifier.padding(vertical = 6.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(review.reviewerName, style = Typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                                Text(review.date, style = Typography.bodySmall.copy(color = BrandTextMuted))
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                repeat(5) {
                                    Icon(Icons.Default.Star, contentDescription = null, tint = BrandGold, modifier = Modifier.size(12.dp))
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(review.location, style = Typography.bodySmall.copy(color = BrandGreenDark, fontSize = 10.sp))
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(review.comment, style = Typography.bodySmall.copy(color = BrandTextDark))
                        }
                        HorizontalDivider(color = BrandGrayLight)
                    }
                }
            }
        }

        // Related Products
        if (relatedProducts.isNotEmpty()) {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                    Text(
                        text = "YOU MIGHT ALSO LIKE",
                        style = Typography.headlineMedium.copy(fontWeight = FontWeight.Black, color = BrandTextDark, fontSize = 16.sp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        items(relatedProducts) { related ->
                            ProductCard(
                                product = related,
                                formattedPrice = viewModel.formatPrice(related.price),
                                formattedCompareAtPrice = viewModel.formatPrice(related.compareAtPrice),
                                isInWishlist = wishlistItems.any { it.productId == related.id },
                                onProductClick = { onNavigateToProduct(related.id) },
                                onWishlistToggle = { viewModel.toggleWishlist(related.id) },
                                onCtaClick = {
                                    if (related.source == ProductSource.DIRECT.name || related.source == ProductSource.CJ_DROPSHIPPING.name) {
                                        viewModel.addToCart(related.id, 1)
                                    } else {
                                        onExternalAffiliateClick(related)
                                    }
                                },
                                modifier = Modifier.width(170.dp)
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
