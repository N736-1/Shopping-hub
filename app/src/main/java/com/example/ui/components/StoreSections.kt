package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.sample.SampleData
import com.example.ui.theme.BrandDarkCanvas
import com.example.ui.theme.BrandDarkCard
import com.example.ui.theme.BrandDarkSurface
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
import kotlinx.coroutines.delay

// Main Store Header
@Composable
fun MainStoreHeader(
    currentCurrency: String,
    onCurrencyChange: (String) -> Unit,
    wishlistCount: Int,
    cartCount: Int,
    onSearchClick: () -> Unit,
    onWishlistClick: () -> Unit,
    onCartClick: () -> Unit,
    onAdminClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currencyMenuExpanded by remember { mutableStateOf(false) }

    Surface(
        color = BrandDarkSurface,
        modifier = modifier.fillMaxWidth()
    ) {
        Column {
            AnnouncementBar()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Brand Logo & Title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.testTag("app_logo")
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .background(
                                Brush.linearGradient(listOf(BrandGreen, BrandGreenDark)),
                                CircleShape
                            )
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = "Logo",
                            tint = Color.Black,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "DROPSHIP HUB",
                            style = Typography.titleLarge.copy(
                                fontWeight = FontWeight.Black,
                                color = BrandWhite,
                                letterSpacing = 0.5.sp
                            )
                        )
                        Text(
                            text = "MAMA ORGANIC & TECH DEALS",
                            style = Typography.labelSmall.copy(
                                color = BrandGreen,
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            )
                        )
                    }
                }

                // Actions: Currency, Search, Wishlist, Cart, Sync
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Currency Dropdown
                    Box {
                        Surface(
                            color = BrandDarkCard,
                            shape = RoundedCornerShape(6.dp),
                            border = BorderStroke(1.dp, BrandGreen.copy(alpha = 0.4f)),
                            modifier = Modifier.clickable { currencyMenuExpanded = true }
                        ) {
                            Text(
                                text = currentCurrency,
                                color = BrandGreen,
                                style = Typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = currencyMenuExpanded,
                            onDismissRequest = { currencyMenuExpanded = false },
                            modifier = Modifier.background(BrandDarkCard)
                        ) {
                            listOf("USD ($)", "PKR (Rs)", "EUR (€)", "GBP (£)").forEach { currOption ->
                                val code = currOption.take(3)
                                DropdownMenuItem(
                                    text = { Text(currOption, color = BrandWhite) },
                                    onClick = {
                                        onCurrencyChange(code)
                                        currencyMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Search Trigger
                    IconButton(
                        onClick = onSearchClick,
                        modifier = Modifier.testTag("header_search_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = BrandWhite
                        )
                    }

                    // Wishlist Badge
                    IconButton(
                        onClick = onWishlistClick,
                        modifier = Modifier.testTag("header_wishlist_btn")
                    ) {
                        BadgedBox(
                            badge = {
                                if (wishlistCount > 0) {
                                    Badge(containerColor = BrandGreen, contentColor = Color.Black) {
                                        Text("$wishlistCount")
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.FavoriteBorder,
                                contentDescription = "Wishlist",
                                tint = BrandWhite
                            )
                        }
                    }

                    // Cart Badge
                    IconButton(
                        onClick = onCartClick,
                        modifier = Modifier.testTag("header_cart_btn")
                    ) {
                        BadgedBox(
                            badge = {
                                if (cartCount > 0) {
                                    Badge(containerColor = BrandGreen, contentColor = Color.Black) {
                                        Text("$cartCount")
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Cart",
                                tint = BrandWhite
                            )
                        }
                    }

                    // Admin Hub Sync Icon
                    IconButton(
                        onClick = onAdminClick,
                        modifier = Modifier.testTag("header_admin_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Sync,
                            contentDescription = "Marketplace Sync",
                            tint = BrandGreen
                        )
                    }
                }
            }
            HorizontalDivider(color = Color(0xFF1E2D22), thickness = 1.dp)
        }
    }
}

// Hero Section (Dark canvas with green highlights & high impact imagery)
@Composable
fun HeroSection(
    onShopNowClick: () -> Unit,
    onExploreDealsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(BrandDarkCanvas)
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Live Status Pill
            Surface(
                color = BrandGreen.copy(alpha = 0.15f),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, BrandGreen.copy(alpha = 0.4f))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .background(BrandGreen, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "MULTI-MARKETPLACE DIRECT & AFFILIATE ENGINE",
                        style = Typography.labelSmall.copy(
                            fontWeight = FontWeight.Black,
                            color = BrandGreen,
                            fontSize = 9.sp,
                            letterSpacing = 0.5.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Main Display Headline
            Text(
                text = "DISCOVER SMART PRODUCTS AT BETTER PRICES",
                style = Typography.displayLarge.copy(
                    fontSize = 28.sp,
                    lineHeight = 32.sp,
                    fontWeight = FontWeight.Black,
                    color = BrandWhite
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Subtitle
            Text(
                text = "Shop curated trending products from CJ Dropshipping, eBay, AliExpress, Etsy, and direct organic cosmetic labs with source-aware checkout.",
                style = Typography.bodyMedium.copy(color = Color(0xFFCBD5E1), lineHeight = 20.sp)
            )

            Spacer(modifier = Modifier.height(18.dp))

            // CTA Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onShopNowClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BrandGreen,
                        contentColor = Color.Black
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .testTag("hero_shop_now_btn")
                ) {
                    Text(
                        text = "SHOP NOW",
                        style = Typography.labelLarge.copy(fontWeight = FontWeight.Black)
                    )
                }

                OutlinedButton(
                    onClick = onExploreDealsClick,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.5.dp, BrandGreen),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = BrandGreen),
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp)
                        .testTag("hero_explore_deals_btn")
                ) {
                    Text(
                        text = "EXPLORE DEALS",
                        style = Typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Hero Graphic Image Banner with floating stats
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, Color(0xFF1E2D22), RoundedCornerShape(14.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_hero_banner),
                    contentDescription = "Mama Organic & Lifestyle Products",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )

                // Overlay gradient for contrast
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, Color.Black.copy(alpha = 0.75f))
                            )
                        )
                )

                // Floating feature badge bottom left
                Surface(
                    color = BrandDarkCanvas.copy(alpha = 0.90f),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, BrandGreen.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = BrandGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = "100% Verified Sources",
                                style = Typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = BrandWhite)
                            )
                            Text(
                                text = "CJ Dropship & Partner Tracked",
                                style = Typography.bodySmall.copy(fontSize = 10.sp, color = BrandGreen)
                            )
                        }
                    }
                }
            }
        }
    }
}

// 4 Benefit Columns / Trust Pillars
@Composable
fun TrustBenefitSection(modifier: Modifier = Modifier) {
    val benefits = listOf(
        Triple(Icons.Default.TrendingUp, "TRENDING PRODUCTS", "Curated selection aggregated across global suppliers."),
        Triple(Icons.Default.Security, "TRUSTED SOURCES", "Authorized dropshipping & partner affiliate channels."),
        Triple(Icons.Default.LocalShipping, "TRACKABLE ORDERS", "Real-time carrier tracking provided on all shipments."),
        Triple(Icons.Default.SupportAgent, "CUSTOMER SUPPORT", "Centralized customer assistance & dispute resolution.")
    )

    Surface(
        color = BrandWhite,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            benefits.forEachIndexed { index, (icon, title, desc) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(BrandGreenLight, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = BrandGreenDark,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = title,
                            style = Typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp,
                                letterSpacing = 0.3.sp
                            ),
                            color = BrandTextDark
                        )
                        Text(
                            text = desc,
                            style = Typography.bodySmall.copy(color = BrandTextMuted, lineHeight = 16.sp)
                        )
                    }
                }
                if (index < benefits.size - 1) {
                    HorizontalDivider(color = BrandGrayLight, thickness = 1.dp)
                }
            }
        }
    }
}

// Flash Deals Section with Live Countdown Timer
@Composable
fun FlashDealHeader(modifier: Modifier = Modifier) {
    var secondsLeft by remember { mutableIntStateOf(14400) }

    LaunchedEffect(Unit) {
        while (secondsLeft > 0) {
            delay(1000)
            secondsLeft--
        }
    }

    val hours = secondsLeft / 3600
    val minutes = (secondsLeft % 3600) / 60
    val seconds = secondsLeft % 60

    Surface(
        color = BrandDarkCard,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, BrandSaleOrange.copy(alpha = 0.4f)),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(BrandSaleOrange, RoundedCornerShape(6.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.FlashOn,
                        contentDescription = "Flash Sale",
                        tint = BrandWhite,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "FLASH DEALS",
                        style = Typography.titleLarge.copy(
                            fontWeight = FontWeight.Black,
                            color = BrandWhite,
                            fontSize = 16.sp
                        )
                    )
                    Text(
                        text = "Up to 50% Off Supplier Direct",
                        style = Typography.bodySmall.copy(color = BrandSaleOrange, fontWeight = FontWeight.Bold)
                    )
                }
            }

            // Timer display
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                TimeBox(value = String.format("%02d", hours))
                Text(":", color = BrandWhite, fontWeight = FontWeight.Bold)
                TimeBox(value = String.format("%02d", minutes))
                Text(":", color = BrandWhite, fontWeight = FontWeight.Bold)
                TimeBox(value = String.format("%02d", seconds))
            }
        }
    }
}

@Composable
private fun TimeBox(value: String) {
    Surface(
        color = Color.Black,
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, Color(0xFF333333))
    ) {
        Text(
            text = value,
            color = BrandWhite,
            style = Typography.labelMedium.copy(fontWeight = FontWeight.Black, fontSize = 12.sp),
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
        )
    }
}

// Complete 4-Column Footer with Newsletter & Legal
@Composable
fun FooterSection(
    onNavigateLegal: (String) -> Unit,
    onSubscribe: (String) -> Unit,
    isSubscribed: Boolean,
    modifier: Modifier = Modifier
) {
    var emailInput by remember { mutableStateOf("") }

    Surface(
        color = BrandDarkCanvas,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Newsletter Container
            Card(
                colors = CardDefaults.cardColors(containerColor = BrandDarkCard),
                border = BorderStroke(1.dp, BrandGreen.copy(alpha = 0.4f)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            tint = BrandGreen,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "GET THE BEST DEALS FIRST",
                            style = Typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = BrandWhite
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Subscribe for exclusive dropshipping discounts, partner coupons & new arrivals.",
                        style = Typography.bodySmall.copy(color = Color(0xFF94A3B8))
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    if (isSubscribed) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(BrandGreenLight, RoundedCornerShape(8.dp))
                                .padding(12.dp)
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = BrandGreenDark)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Thank you! You're subscribed to deal alerts.",
                                style = Typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = BrandGreenDark)
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = emailInput,
                                onValueChange = { emailInput = it },
                                placeholder = { Text("Enter your email address", color = BrandTextMuted) },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = BrandGreen,
                                    unfocusedBorderColor = Color(0xFF334155),
                                    focusedTextColor = BrandWhite,
                                    unfocusedTextColor = BrandWhite
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            )
                            Button(
                                onClick = {
                                    if (emailInput.isNotBlank()) {
                                        onSubscribe(emailInput)
                                        emailInput = ""
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = BrandGreen,
                                    contentColor = Color.Black
                                ),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.height(54.dp)
                            ) {
                                Text("JOIN", fontWeight = FontWeight.Black)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Navigation Links Section
            Text(
                text = "STORE NAVIGATION & POLICIES",
                style = Typography.labelLarge.copy(
                    fontWeight = FontWeight.Black,
                    color = BrandGreen,
                    letterSpacing = 1.sp
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Quick Policy Links
            val links = listOf(
                "About Us" to "about",
                "Privacy Policy" to "privacy",
                "Terms & Conditions" to "terms",
                "Affiliate Disclosure" to "disclosure",
                "Disclaimer" to "disclaimer",
                "Shipping & Returns" to "shipping",
                "Contact Support" to "contact"
            )

            links.chunked(2).forEach { rowLinks ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    rowLinks.forEach { (title, key) ->
                        Text(
                            text = "• $title",
                            color = Color(0xFFCBD5E1),
                            style = Typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                            modifier = Modifier
                                .clickable { onNavigateLegal(key) }
                                .padding(vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))
            HorizontalDivider(color = Color(0xFF1E293B), thickness = 1.dp)
            Spacer(modifier = Modifier.height(14.dp))

            // Payment Methods Supported
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "SECURE PAYMENTS",
                    style = Typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = BrandTextMuted)
                )
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("Stripe", "PayPal", "Visa/MC", "COD", "EasyPaisa").forEach { method ->
                        Surface(
                            color = BrandDarkCard,
                            shape = RoundedCornerShape(4.dp),
                            border = BorderStroke(1.dp, Color(0xFF334155))
                        ) {
                            Text(
                                text = method,
                                color = BrandWhite,
                                style = Typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // FTC Affiliate Disclosure Statement
            Text(
                text = "Affiliate Disclosure: Some products listed on this platform are curated from authorized partner feeds (CJ Affiliate, eBay, AliExpress, Etsy). If you purchase through external qualifying links, we may earn a small referral commission at no additional cost to you.",
                style = Typography.bodySmall.copy(color = Color(0xFF64748B), fontSize = 10.sp, lineHeight = 14.sp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Copyright
            Text(
                text = "© 2026 Dropship Hub & Mama Organic. All rights reserved.",
                style = Typography.bodySmall.copy(color = Color(0xFF475569), fontSize = 10.sp)
            )
        }
    }
}
