package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import com.example.data.model.OrderEntity
import com.example.ui.components.SourceBadge
import com.example.ui.theme.BrandDarkCanvas
import com.example.ui.theme.BrandDarkCard
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

// 1. Cart Screen
@Composable
fun CartScreen(
    viewModel: StoreViewModel,
    onNavigateToShop: () -> Unit,
    onNavigateToCheckout: () -> Unit,
    onNavigateToProduct: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val cartSummary by viewModel.cartSummary.collectAsState()
    val promoCode by viewModel.appliedPromoCode.collectAsState()
    var promoInput by remember { mutableStateOf("") }
    var promoError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandGrayLight)
            .testTag("cart_screen")
    ) {
        // Header
        Surface(color = BrandWhite, modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = BrandGreenDark)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "YOUR SHOPPING CART (${cartSummary.items.size})",
                    style = Typography.titleLarge.copy(fontWeight = FontWeight.Black)
                )
            }
        }

        if (cartSummary.items.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        tint = BrandTextMuted,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Your Cart is Empty",
                        style = Typography.headlineMedium.copy(fontWeight = FontWeight.Bold, color = BrandTextDark)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Explore trending deals and add items to your cart.",
                        style = Typography.bodyMedium.copy(color = BrandTextMuted)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = onNavigateToShop,
                        colors = ButtonDefaults.buttonColors(containerColor = BrandGreen, contentColor = Color.Black),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(48.dp)
                    ) {
                        Text("START SHOPPING", fontWeight = FontWeight.Black)
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // Free Shipping Progress Card
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = BrandWhite),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, BrandGreen.copy(alpha = 0.4f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.LocalShipping, contentDescription = null, tint = BrandGreenDark)
                                Spacer(modifier = Modifier.width(8.dp))
                                if (cartSummary.amountNeededForFreeShipping <= 0.0) {
                                    Text(
                                        text = "Unlocked FREE Express Shipping!",
                                        style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = BrandGreenDark)
                                    )
                                } else {
                                    Text(
                                        text = "Add ${viewModel.formatPrice(cartSummary.amountNeededForFreeShipping)} more for FREE Shipping",
                                        style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = BrandTextDark)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            LinearProgressIndicator(
                                progress = { cartSummary.freeShippingProgress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = BrandGreen,
                                trackColor = BrandGrayLight
                            )
                        }
                    }
                }

                // Cart Items List
                items(cartSummary.items) { item ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = BrandWhite),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, BrandGrayMedium.copy(alpha = 0.6f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Image
                            Box(
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(BrandGrayLight)
                                    .clickable { onNavigateToProduct(item.product.id) }
                            ) {
                                if (item.product.imageDrawableRes != 0) {
                                    Image(
                                        painter = painterResource(id = item.product.imageDrawableRes),
                                        contentDescription = item.product.title,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.matchParentSize()
                                    )
                                } else {
                                    AsyncImage(
                                        model = item.product.affiliateUrl,
                                        contentDescription = item.product.title,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.matchParentSize()
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            // Details
                            Column(modifier = Modifier.weight(1f)) {
                                SourceBadge(sourceName = item.product.source)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = item.product.title,
                                    style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 13.sp),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = viewModel.formatPrice(item.product.price),
                                    style = Typography.titleMedium.copy(fontWeight = FontWeight.Black, color = BrandGreenDark)
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                // Quantity Controls
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .background(BrandGrayLight, RoundedCornerShape(6.dp))
                                            .padding(horizontal = 4.dp)
                                    ) {
                                        IconButton(
                                            onClick = { viewModel.updateCartQuantity(item.product.id, item.cartItem.quantity - 1) },
                                            modifier = Modifier.size(26.dp)
                                        ) {
                                            Icon(Icons.Default.Remove, contentDescription = null, modifier = Modifier.size(14.dp))
                                        }
                                        Text(
                                            text = "${item.cartItem.quantity}",
                                            style = Typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                            modifier = Modifier.padding(horizontal = 8.dp)
                                        )
                                        IconButton(
                                            onClick = { viewModel.updateCartQuantity(item.product.id, item.cartItem.quantity + 1) },
                                            modifier = Modifier.size(26.dp)
                                        ) {
                                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                                        }
                                    }

                                    IconButton(
                                        onClick = { viewModel.removeFromCart(item.product.id) },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(Icons.Default.DeleteOutline, contentDescription = "Remove", tint = Color.Red)
                                    }
                                }
                            }
                        }
                    }
                }

                // Coupon / Promo Code
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = BrandWhite),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, BrandGrayMedium.copy(alpha = 0.6f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("Promotional Code", style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                            Spacer(modifier = Modifier.height(8.dp))

                            if (promoCode != null) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(BrandGreenLight, RoundedCornerShape(6.dp))
                                        .padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = BrandGreenDark)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Coupon '$promoCode' Applied!",
                                            style = Typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = BrandGreenDark)
                                        )
                                    }
                                    Text(
                                        text = "Remove",
                                        color = Color.Red,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.clickable { viewModel.removePromoCode() }
                                    )
                                }
                            } else {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedTextField(
                                        value = promoInput,
                                        onValueChange = { promoInput = it; promoError = null },
                                        placeholder = { Text("e.g. SAVE10, GREENDEAL", fontSize = 12.sp) },
                                        singleLine = true,
                                        modifier = Modifier.weight(1f),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = BrandGreenDark,
                                            unfocusedBorderColor = BrandGrayMedium
                                        )
                                    )
                                    Button(
                                        onClick = {
                                            val ok = viewModel.applyPromoCode(promoInput)
                                            if (!ok) promoError = "Invalid code. Try 'SAVE10' or 'GREENDEAL'"
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = BrandDarkCanvas, contentColor = BrandGreen),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("APPLY", fontWeight = FontWeight.Bold)
                                    }
                                }
                                if (promoError != null) {
                                    Text(
                                        text = promoError!!,
                                        color = Color.Red,
                                        style = Typography.bodySmall.copy(fontSize = 11.sp),
                                        modifier = Modifier.padding(top = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Summary Breakdown
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = BrandWhite),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, BrandGrayMedium.copy(alpha = 0.6f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("Order Summary", style = Typography.titleMedium.copy(fontWeight = FontWeight.Black))
                            Spacer(modifier = Modifier.height(10.dp))

                            SummaryRow("Subtotal", viewModel.formatPrice(cartSummary.subtotal))
                            SummaryRow(
                                "Shipping",
                                if (cartSummary.shippingCost == 0.0) "FREE" else viewModel.formatPrice(cartSummary.shippingCost)
                            )
                            if (cartSummary.discount > 0.0) {
                                SummaryRow("Discount", "-${viewModel.formatPrice(cartSummary.discount)}", isGreen = true)
                            }

                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Total", style = Typography.titleLarge.copy(fontWeight = FontWeight.Black))
                                Text(
                                    text = viewModel.formatPrice(cartSummary.total),
                                    style = Typography.titleLarge.copy(fontWeight = FontWeight.Black, color = BrandGreenDark, fontSize = 20.sp)
                                )
                            }
                        }
                    }
                }
            }

            // Bottom Sticky Checkout Button
            Surface(
                color = BrandWhite,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Total Payable", style = Typography.bodySmall.copy(color = BrandTextMuted))
                        Text(
                            text = viewModel.formatPrice(cartSummary.total),
                            style = Typography.titleLarge.copy(fontWeight = FontWeight.Black, color = BrandGreenDark)
                        )
                    }

                    Button(
                        onClick = onNavigateToCheckout,
                        colors = ButtonDefaults.buttonColors(containerColor = BrandGreen, contentColor = Color.Black),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1.5f)
                            .height(48.dp)
                            .testTag("cart_proceed_checkout_btn")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("CHECKOUT NOW", fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }
    }
}

// 2. Checkout Screen
@Composable
fun CheckoutScreen(
    viewModel: StoreViewModel,
    onBackClick: () -> Unit,
    onOrderSuccess: (OrderEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val cartSummary by viewModel.cartSummary.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()

    var fullName by remember { mutableStateOf(userProfile.displayName) }
    var email by remember { mutableStateOf(userProfile.email) }
    var phone by remember { mutableStateOf("+92 300 1234567") }
    var address by remember { mutableStateOf("House 12, Street 4, Sector F-7") }
    var city by remember { mutableStateOf("Islamabad") }
    var country by remember { mutableStateOf("Pakistan") }
    var postalCode by remember { mutableStateOf("44000") }
    var paymentMethod by remember { mutableStateOf("Cash on Delivery (COD)") }

    var countryDropdownExpanded by remember { mutableStateOf(false) }
    var isSubmitting by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BrandGrayLight)
            .testTag("checkout_screen")
    ) {
        // Header
        item {
            Surface(color = BrandWhite, modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("SECURE CHECKOUT", style = Typography.titleLarge.copy(fontWeight = FontWeight.Black))
                }
            }
        }

        // Contact & Shipping Information
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = BrandWhite),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, BrandGrayMedium.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("1. Shipping & Customer Details", style = Typography.titleMedium.copy(fontWeight = FontWeight.Black))
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Full Name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email for Order Confirmation & Tracking") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone Number") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = address,
                        onValueChange = { address = it },
                        label = { Text("Delivery Street Address") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = city,
                            onValueChange = { city = it },
                            label = { Text("City") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = postalCode,
                            onValueChange = { postalCode = it },
                            label = { Text("Postal Code") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Country Selection
                    Box {
                        OutlinedTextField(
                            value = country,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Country / Region") },
                            trailingIcon = { Text("▾", modifier = Modifier.padding(end = 12.dp)) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { countryDropdownExpanded = true }
                        )
                        DropdownMenu(
                            expanded = countryDropdownExpanded,
                            onDismissRequest = { countryDropdownExpanded = false }
                        ) {
                            listOf("Pakistan", "United States", "United Kingdom", "United Arab Emirates", "Canada", "Germany").forEach { c ->
                                DropdownMenuItem(
                                    text = { Text(c) },
                                    onClick = {
                                        country = c
                                        countryDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Payment Method Selection
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
                    Text("2. Payment Method", style = Typography.titleMedium.copy(fontWeight = FontWeight.Black))
                    Spacer(modifier = Modifier.height(10.dp))

                    val paymentOptions = listOf(
                        "Cash on Delivery (COD)" to "Pay cash when carrier delivers to your doorstep.",
                        "Credit / Debit Card (Stripe)" to "Secure encrypted Visa, MasterCard, UnionPay.",
                        "EasyPaisa / JazzCash" to "Instant mobile wallet payment for Pakistani buyers.",
                        "PayPal Express" to "Pay with your PayPal account or Buyer Protection."
                    )

                    paymentOptions.forEach { (title, desc) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { paymentMethod = title }
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = paymentMethod == title,
                                onClick = { paymentMethod = title },
                                colors = RadioButtonDefaults.colors(selectedColor = BrandGreenDark)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(title, style = Typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                                Text(desc, style = Typography.bodySmall.copy(color = BrandTextMuted, fontSize = 11.sp))
                            }
                        }
                    }
                }
            }
        }

        // Order Total & Place Order Button
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = BrandWhite),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, BrandGrayMedium.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Subtotal", style = Typography.bodyMedium)
                        Text(viewModel.formatPrice(cartSummary.subtotal), style = Typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Estimated Shipping", style = Typography.bodyMedium)
                        Text(if (cartSummary.shippingCost == 0.0) "FREE" else viewModel.formatPrice(cartSummary.shippingCost))
                    }
                    if (cartSummary.discount > 0.0) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Discount", style = Typography.bodyMedium.copy(color = BrandGreenDark))
                            Text("-${viewModel.formatPrice(cartSummary.discount)}", style = Typography.bodyMedium.copy(color = BrandGreenDark, fontWeight = FontWeight.Bold))
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total Amount", style = Typography.titleLarge.copy(fontWeight = FontWeight.Black))
                        Text(
                            text = viewModel.formatPrice(cartSummary.total),
                            style = Typography.titleLarge.copy(fontWeight = FontWeight.Black, color = BrandGreenDark)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            isSubmitting = true
                            viewModel.placeOrder(
                                customerName = fullName,
                                email = email,
                                phone = phone,
                                address = address,
                                city = city,
                                country = country,
                                postalCode = postalCode,
                                paymentMethod = paymentMethod,
                                onSuccess = { order ->
                                    isSubmitting = false
                                    onOrderSuccess(order)
                                }
                            )
                        },
                        enabled = !isSubmitting && cartSummary.items.isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandGreen, contentColor = Color.Black),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("checkout_place_order_btn")
                    ) {
                        Text("PLACE ORDER NOW", fontWeight = FontWeight.Black, fontSize = 15.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun SummaryRow(label: String, value: String, isGreen: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = Typography.bodyMedium.copy(color = if (isGreen) BrandGreenDark else BrandTextMuted))
        Text(value, style = Typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = if (isGreen) BrandGreenDark else BrandTextDark))
    }
}
