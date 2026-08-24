package com.example

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.Policy
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.data.model.OrderEntity
import com.example.data.model.ProductEntity
import com.example.data.model.ProductSource
import com.example.ui.components.MainStoreHeader
import com.example.ui.screens.AdminHubScreen
import com.example.ui.screens.CartScreen
import com.example.ui.screens.CheckoutScreen
import com.example.ui.screens.CompareDealsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LegalPagesScreen
import com.example.ui.screens.OrderTrackingScreen
import com.example.ui.screens.ProductDetailScreen
import com.example.ui.screens.ShopScreen
import com.example.ui.screens.WishlistScreen
import com.example.ui.theme.BrandDarkCanvas
import com.example.ui.theme.BrandDarkCard
import com.example.ui.theme.BrandDarkSurface
import com.example.ui.theme.BrandGrayMedium
import com.example.ui.theme.BrandGreen
import com.example.ui.theme.BrandGreenDark
import com.example.ui.theme.BrandGreenLight
import com.example.ui.theme.BrandTextDark
import com.example.ui.theme.BrandTextMuted
import com.example.ui.theme.BrandWhite
import com.example.ui.theme.DropshipHubTheme
import com.example.ui.theme.Typography
import com.example.ui.viewmodel.StoreViewModel

class MainActivity : ComponentActivity() {
    private val storeViewModel: StoreViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DropshipHubTheme {
                MainApp(viewModel = storeViewModel)
            }
        }
    }
}

@Composable
fun MainApp(viewModel: StoreViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val userProfile by viewModel.userProfile.collectAsState()
    val wishlistItems by viewModel.wishlistItems.collectAsState()
    val cartSummary by viewModel.cartSummary.collectAsState()

    var affiliateDialogProduct by remember { mutableStateOf<ProductEntity?>(null) }
    var orderSuccessEntity by remember { mutableStateOf<OrderEntity?>(null) }

    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            // Show main header on primary screens
            if (currentRoute in listOf("home", "shop", "wishlist", "orders")) {
                MainStoreHeader(
                    currentCurrency = userProfile.selectedCurrency,
                    onCurrencyChange = { viewModel.switchCurrency(it) },
                    wishlistCount = wishlistItems.size,
                    cartCount = cartSummary.items.size,
                    onSearchClick = { navController.navigate("shop") },
                    onWishlistClick = { navController.navigate("wishlist") },
                    onCartClick = { navController.navigate("cart") },
                    onAdminClick = { navController.navigate("admin") }
                )
            }
        },
        bottomBar = {
            if (currentRoute in listOf("home", "shop", "orders", "wishlist", "legal/{tabKey}")) {
                NavigationBar(
                    containerColor = BrandDarkSurface,
                    contentColor = BrandWhite,
                    tonalElevation = 8.dp
                ) {
                    val items = listOf(
                        Triple("home", "Home", Icons.Filled.Home to Icons.Outlined.Home),
                        Triple("shop", "Shop", Icons.Filled.Search to Icons.Outlined.Search),
                        Triple("orders", "Orders", Icons.Filled.LocalShipping to Icons.Outlined.LocalShipping),
                        Triple("wishlist", "Wishlist", Icons.Filled.Favorite to Icons.Outlined.FavoriteBorder),
                        Triple("legal/about", "About/Legal", Icons.Filled.Policy to Icons.Outlined.Policy)
                    )

                    items.forEach { (route, label, icons) ->
                        val isSelected = currentRoute == route || (route.startsWith("legal") && currentRoute?.startsWith("legal") == true)
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {
                                if (!isSelected) {
                                    navController.navigate(route) {
                                        popUpTo("home") { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = {
                                if (route == "wishlist" && wishlistItems.isNotEmpty()) {
                                    BadgedBox(
                                        badge = {
                                            Badge(containerColor = BrandGreen, contentColor = Color.Black) {
                                                Text("${wishlistItems.size}")
                                            }
                                        }
                                    ) {
                                        Icon(if (isSelected) icons.first else icons.second, contentDescription = label)
                                    }
                                } else {
                                    Icon(if (isSelected) icons.first else icons.second, contentDescription = label)
                                }
                            },
                            label = { Text(label, fontSize = 10.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color.Black,
                                selectedTextColor = BrandGreen,
                                unselectedIconColor = Color(0xFF94A3B8),
                                unselectedTextColor = Color(0xFF94A3B8),
                                indicatorColor = BrandGreen
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            // Home Screen
            composable("home") {
                HomeScreen(
                    viewModel = viewModel,
                    onNavigateToShop = { navController.navigate("shop") },
                    onNavigateToProduct = { productId -> navController.navigate("product_detail/$productId") },
                    onNavigateToCompare = { navController.navigate("compare") },
                    onNavigateToLegal = { tabKey -> navController.navigate("legal/$tabKey") },
                    onExternalAffiliateClick = { product -> affiliateDialogProduct = product }
                )
            }

            // Shop / Catalog Screen
            composable("shop") {
                ShopScreen(
                    viewModel = viewModel,
                    onNavigateToProduct = { productId -> navController.navigate("product_detail/$productId") },
                    onExternalAffiliateClick = { product -> affiliateDialogProduct = product }
                )
            }

            // Product Detail Screen
            composable(
                route = "product_detail/{productId}",
                arguments = listOf(navArgument("productId") { type = NavType.StringType })
            ) { backStackEntry ->
                val prodId = backStackEntry.arguments?.getString("productId") ?: ""
                ProductDetailScreen(
                    productId = prodId,
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() },
                    onNavigateToProduct = { newId -> navController.navigate("product_detail/$newId") },
                    onNavigateToCart = { navController.navigate("cart") },
                    onExternalAffiliateClick = { product -> affiliateDialogProduct = product }
                )
            }

            // Cart Screen
            composable("cart") {
                CartScreen(
                    viewModel = viewModel,
                    onNavigateToShop = { navController.navigate("shop") },
                    onNavigateToCheckout = { navController.navigate("checkout") },
                    onNavigateToProduct = { id -> navController.navigate("product_detail/$id") }
                )
            }

            // Checkout Screen
            composable("checkout") {
                CheckoutScreen(
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() },
                    onOrderSuccess = { order ->
                        orderSuccessEntity = order
                    }
                )
            }

            // Orders / Tracking Screen
            composable("orders") {
                OrderTrackingScreen(
                    viewModel = viewModel,
                    onContactSupport = { navController.navigate("legal/contact") }
                )
            }

            // Wishlist Screen
            composable("wishlist") {
                WishlistScreen(
                    viewModel = viewModel,
                    onNavigateToShop = { navController.navigate("shop") },
                    onNavigateToProduct = { id -> navController.navigate("product_detail/$id") },
                    onExternalAffiliateClick = { product -> affiliateDialogProduct = product }
                )
            }

            // Cross-Marketplace Comparison Screen
            composable("compare") {
                CompareDealsScreen(
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() },
                    onNavigateToProduct = { id -> navController.navigate("product_detail/$id") },
                    onExternalAffiliateClick = { product -> affiliateDialogProduct = product }
                )
            }

            // Legal & Information Screens (About, Privacy, Disclaimer, Contact Us, etc.)
            composable(
                route = "legal/{tabKey}",
                arguments = listOf(navArgument("tabKey") { type = NavType.StringType; defaultValue = "about" })
            ) { backStackEntry ->
                val tabKey = backStackEntry.arguments?.getString("tabKey") ?: "about"
                LegalPagesScreen(initialTabKey = tabKey)
            }

            // Admin & Supplier Hub Screen
            composable("admin") {
                AdminHubScreen(
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }

    // External Affiliate Redirect Dialog
    if (affiliateDialogProduct != null) {
        val prod = affiliateDialogProduct!!
        AlertDialog(
            onDismissRequest = { affiliateDialogProduct = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.OpenInNew, contentDescription = null, tint = BrandGreenDark)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Partner Merchant Deal", style = Typography.titleMedium.copy(fontWeight = FontWeight.Black))
                }
            },
            text = {
                Column {
                    Text(
                        text = "You are navigating to an authorized external partner:",
                        style = Typography.bodyMedium.copy(color = BrandTextDark)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "• Product: ${prod.title}",
                        style = Typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = BrandTextDark)
                    )
                    Text(
                        text = "• Partner: ${prod.supplier}",
                        style = Typography.bodySmall.copy(color = BrandGreenDark, fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Disclaimer: Order checkout, payment, and fulfillment occur securely on the partner's platform. We may earn a qualifying referral commission at no extra cost to you.",
                        style = Typography.bodySmall.copy(color = BrandTextMuted, fontSize = 11.sp, lineHeight = 15.sp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val url = if (prod.affiliateUrl.isNotBlank()) prod.affiliateUrl else "https://cjdropshipping.com"
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        try {
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            // Fallback if browser not configured
                        }
                        affiliateDialogProduct = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandGreen, contentColor = Color.Black)
                ) {
                    Text("CONTINUE TO PARTNER", fontWeight = FontWeight.Black)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { affiliateDialogProduct = null }) {
                    Text("STAY ON STORE")
                }
            }
        )
    }

    // Order Success Dialog
    if (orderSuccessEntity != null) {
        val order = orderSuccessEntity!!
        AlertDialog(
            onDismissRequest = { orderSuccessEntity = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = BrandGreenDark)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Order Placed Successfully!", style = Typography.titleMedium.copy(fontWeight = FontWeight.Black))
                }
            },
            text = {
                Column {
                    Text(
                        text = "Thank you for your order, ${order.customerName}!",
                        style = Typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Order Number: ${order.orderNumber}", style = Typography.bodySmall.copy(fontWeight = FontWeight.Bold, color = BrandGreenDark))
                    Text("Tracking ID: ${order.trackingNumber}", style = Typography.bodySmall.copy(color = BrandTextMuted))
                    Text("Carrier: ${order.carrier}", style = Typography.bodySmall.copy(color = BrandTextMuted))
                    Text("Total: ${viewModel.formatPrice(order.totalAmount)}", style = Typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "A confirmation email with real-time tracking details has been sent to ${order.email}.",
                        style = Typography.bodySmall.copy(color = BrandTextMuted, fontSize = 11.sp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        orderSuccessEntity = null
                        navController.navigate("orders")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BrandGreen, contentColor = Color.Black)
                ) {
                    Text("TRACK ORDER NOW", fontWeight = FontWeight.Black)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        orderSuccessEntity = null
                        navController.navigate("home")
                    }
                ) {
                    Text("BACK TO HOME")
                }
            }
        )
    }
}
