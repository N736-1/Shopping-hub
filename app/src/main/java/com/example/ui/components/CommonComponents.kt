package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.model.ProductEntity
import com.example.data.model.ProductSource
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
import com.example.ui.theme.ColorAliExpress
import com.example.ui.theme.ColorCjAffiliate
import com.example.ui.theme.ColorCjDropship
import com.example.ui.theme.ColorDirectStore
import com.example.ui.theme.ColorEbay
import com.example.ui.theme.ColorEtsy
import com.example.ui.theme.Typography

// Top Announcement Bar
@Composable
fun AnnouncementBar(modifier: Modifier = Modifier) {
    Surface(
        color = BrandDarkCanvas,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(BrandGreen, CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "FREE SHIPPING ON SELECTED PRODUCTS • NEW DEALS ADDED DAILY",
                style = Typography.labelSmall.copy(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandWhite,
                    letterSpacing = 0.8.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// Source Badge
@Composable
fun SourceBadge(sourceName: String, modifier: Modifier = Modifier) {
    val (bgColor, textColor, label) = when (sourceName) {
        ProductSource.CJ_DROPSHIPPING.name -> Triple(ColorCjDropship.copy(alpha = 0.12f), ColorCjDropship, "CJ DROPSHIPPING")
        ProductSource.CJ_AFFILIATE.name -> Triple(ColorCjAffiliate.copy(alpha = 0.12f), ColorCjAffiliate, "CJ AFFILIATE")
        ProductSource.EBAY.name -> Triple(ColorEbay.copy(alpha = 0.12f), ColorEbay, "EBAY DEAL")
        ProductSource.ALIEXPRESS.name -> Triple(ColorAliExpress.copy(alpha = 0.12f), ColorAliExpress, "ALIEXPRESS FIND")
        ProductSource.ETSY.name -> Triple(ColorEtsy.copy(alpha = 0.12f), ColorEtsy, "ETSY ARTISAN")
        else -> Triple(ColorDirectStore.copy(alpha = 0.12f), ColorDirectStore, "DIRECT STORE")
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(4.dp),
        modifier = modifier
    ) {
        Text(
            text = label,
            color = textColor,
            style = Typography.labelSmall.copy(fontWeight = FontWeight.Black, fontSize = 9.sp),
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

// Reusable Product Card
@Composable
fun ProductCard(
    product: ProductEntity,
    formattedPrice: String,
    formattedCompareAtPrice: String,
    isInWishlist: Boolean,
    onProductClick: () -> Unit,
    onWishlistToggle: () -> Unit,
    onCtaClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isAffiliate = product.source != ProductSource.DIRECT.name && product.source != ProductSource.CJ_DROPSHIPPING.name

    Card(
        colors = CardDefaults.cardColors(containerColor = BrandWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp, hoveredElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, BrandGrayMedium.copy(alpha = 0.6f)),
        modifier = modifier
            .fillMaxWidth()
            .clickable { onProductClick() }
            .testTag("product_card_${product.id}")
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            // Product Image & Badges Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(BrandGrayLight)
            ) {
                // Product Image
                if (product.imageDrawableRes != 0) {
                    androidx.compose.foundation.Image(
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
                        error = painterResource(id = R.drawable.img_smart_gadget)
                    )
                }

                // Top Left: Source Badge
                Box(modifier = Modifier.padding(6.dp).align(Alignment.TopStart)) {
                    SourceBadge(sourceName = product.source)
                }

                // Top Right: Wishlist Button
                IconButton(
                    onClick = onWishlistToggle,
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .background(BrandWhite.copy(alpha = 0.88f), CircleShape)
                        .testTag("wishlist_btn_${product.id}")
                ) {
                    Icon(
                        imageVector = if (isInWishlist) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Wishlist",
                        tint = if (isInWishlist) Color.Red else BrandTextMuted,
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Bottom Left: Discount Badge if applicable
                if (product.compareAtPrice > product.price) {
                    val savingsPercent = (((product.compareAtPrice - product.price) / product.compareAtPrice) * 100).toInt()
                    Surface(
                        color = BrandSaleOrange,
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(6.dp)
                    ) {
                        Text(
                            text = "SAVE $savingsPercent%",
                            color = BrandWhite,
                            style = Typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 9.sp),
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Rating & Review count
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = BrandGold,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = String.format("%.1f", product.rating),
                    style = Typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = BrandTextDark)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = "(${product.reviewCount})",
                    style = Typography.bodySmall.copy(color = BrandTextMuted)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Product Title
            Text(
                text = product.title,
                style = Typography.titleMedium.copy(fontSize = 13.sp, fontWeight = FontWeight.Bold),
                color = BrandTextDark,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.height(36.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Price Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = formattedPrice,
                    style = Typography.titleLarge.copy(
                        fontWeight = FontWeight.Black,
                        color = BrandGreenDark,
                        fontSize = 16.sp
                    )
                )
                if (product.compareAtPrice > product.price) {
                    Text(
                        text = formattedCompareAtPrice,
                        style = Typography.bodySmall.copy(
                            textDecoration = TextDecoration.LineThrough,
                            color = BrandTextMuted
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Source-Aware CTA Button
            if (isAffiliate) {
                OutlinedButton(
                    onClick = onCtaClick,
                    border = BorderStroke(1.dp, BrandGreenDark),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = BrandGreenDark),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(34.dp)
                        .testTag("cta_btn_${product.id}")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = when (product.source) {
                                ProductSource.EBAY.name -> "VIEW ON EBAY"
                                ProductSource.ALIEXPRESS.name -> "VIEW ON ALI"
                                ProductSource.ETSY.name -> "VIEW ON ETSY"
                                else -> "VIEW DEAL"
                            },
                            style = Typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.OpenInNew,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            } else {
                Button(
                    onClick = onCtaClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BrandGreen,
                        contentColor = Color.Black
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(34.dp)
                        .testTag("cta_btn_${product.id}")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "SHOP NOW",
                            style = Typography.labelSmall.copy(fontWeight = FontWeight.Black)
                        )
                    }
                }
            }
        }
    }
}
