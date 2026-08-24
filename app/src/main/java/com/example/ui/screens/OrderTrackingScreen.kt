package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.example.data.model.OrderEntity
import com.example.ui.theme.BrandDarkCanvas
import com.example.ui.theme.BrandDarkCard
import com.example.ui.theme.BrandGrayLight
import com.example.ui.theme.BrandGrayMedium
import com.example.ui.theme.BrandGreen
import com.example.ui.theme.BrandGreenDark
import com.example.ui.theme.BrandGreenLight
import com.example.ui.theme.BrandTextDark
import com.example.ui.theme.BrandTextMuted
import com.example.ui.theme.BrandWhite
import com.example.ui.theme.Typography
import com.example.ui.viewmodel.StoreViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OrderTrackingScreen(
    viewModel: StoreViewModel,
    onContactSupport: () -> Unit,
    modifier: Modifier = Modifier
) {
    val trackedOrder by viewModel.trackedOrder.collectAsState()
    val searchInput by viewModel.trackingSearchInput.collectAsState()
    val allOrders by viewModel.allOrders.collectAsState()

    var input by remember { mutableStateOf(searchInput) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BrandGrayLight)
            .testTag("order_tracking_screen")
    ) {
        // Header
        item {
            Surface(color = BrandDarkCanvas, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "LIVE ORDER TRACKING",
                        style = Typography.headlineMedium.copy(fontWeight = FontWeight.Black, color = BrandWhite)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Track your parcel in real-time across CJ Logistics, TCS, and regional carriers.",
                        style = Typography.bodySmall.copy(color = Color(0xFF94A3B8))
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = input,
                            onValueChange = { input = it },
                            placeholder = { Text("Enter Order # (e.g. DH-123456) or Tracking #", fontSize = 11.sp, color = BrandTextMuted) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BrandGreen,
                                unfocusedBorderColor = Color(0xFF334155),
                                focusedTextColor = BrandWhite,
                                unfocusedTextColor = BrandWhite
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f).testTag("tracking_input_field")
                        )

                        Button(
                            onClick = { viewModel.lookupOrder(input) },
                            colors = ButtonDefaults.buttonColors(containerColor = BrandGreen, contentColor = Color.Black),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(54.dp).testTag("tracking_search_btn")
                        ) {
                            Icon(Icons.Default.Search, contentDescription = "Track")
                        }
                    }
                }
            }
        }

        // Active Order Result
        if (trackedOrder != null) {
            val order = trackedOrder!!
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = BrandWhite),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, BrandGreen.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Order: ${order.orderNumber}",
                                    style = Typography.titleLarge.copy(fontWeight = FontWeight.Black, color = BrandTextDark)
                                )
                                Text(
                                    text = "Tracking: ${order.trackingNumber}",
                                    style = Typography.bodySmall.copy(color = BrandGreenDark, fontWeight = FontWeight.Bold)
                                )
                            }

                            Surface(
                                color = BrandGreenLight,
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = order.carrier,
                                    color = BrandGreenDark,
                                    style = Typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = BrandGrayLight)
                        Spacer(modifier = Modifier.height(16.dp))

                        // Graphical 5-Step Timeline
                        Text("Shipment Progress", style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        Spacer(modifier = Modifier.height(12.dp))

                        val steps = listOf(
                            "Order Placed & Payment Confirmed" to "Order verified with dropship supplier",
                            "Supplier Processing & Packing" to "Quality checked & packaged at central hub",
                            "Handed Over to International Carrier" to "Customs documentation completed",
                            "Out for Local Delivery" to "Courier dispatched to ${order.city}",
                            "Delivered" to "Package received at destination address"
                        )

                        steps.forEachIndexed { index, (title, desc) ->
                            val isCompleted = index <= 2 // Active mockup stage
                            val isCurrent = index == 2

                            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                                Box(
                                    modifier = Modifier
                                        .size(26.dp)
                                        .background(
                                            if (isCompleted) BrandGreenDark else BrandGrayMedium,
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isCompleted) {
                                        Icon(Icons.Default.Check, contentDescription = null, tint = BrandWhite, modifier = Modifier.size(16.dp))
                                    } else {
                                        Text("${index + 1}", color = BrandWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = title,
                                        style = Typography.bodyMedium.copy(
                                            fontWeight = if (isCurrent) FontWeight.Black else FontWeight.Medium,
                                            color = if (isCompleted) BrandTextDark else BrandTextMuted
                                        )
                                    )
                                    Text(
                                        text = desc,
                                        style = Typography.bodySmall.copy(color = BrandTextMuted, fontSize = 11.sp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = BrandGrayLight)
                        Spacer(modifier = Modifier.height(12.dp))

                        // Shipping Details
                        Text("Destination & Items", style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Deliver to: ${order.customerName}", style = Typography.bodySmall.copy(color = BrandTextDark))
                        Text("Address: ${order.address}, ${order.city}, ${order.country}", style = Typography.bodySmall.copy(color = BrandTextMuted))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Items: ${order.itemsSummary}", style = Typography.bodySmall.copy(color = BrandTextDark, fontWeight = FontWeight.Medium))
                        Text("Total Paid: ${viewModel.formatPrice(order.totalAmount)} (${order.paymentMethod})", style = Typography.bodySmall.copy(color = BrandGreenDark, fontWeight = FontWeight.Bold))
                    }
                }
            }
        }

        // Recent Orders List (if user has orders)
        if (allOrders.isNotEmpty()) {
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Text(
                        text = "YOUR RECENT ORDERS (${allOrders.size})",
                        style = Typography.titleLarge.copy(fontWeight = FontWeight.Black)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    allOrders.forEach { order ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = BrandWhite),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, BrandGrayMedium.copy(alpha = 0.6f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(order.orderNumber, style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                                    Text("Status: ${order.status}", style = Typography.bodySmall.copy(color = BrandGreenDark, fontWeight = FontWeight.Bold))
                                    Text("Total: ${viewModel.formatPrice(order.totalAmount)}", style = Typography.bodySmall.copy(color = BrandTextMuted))
                                }
                                Button(
                                    onClick = {
                                        input = order.orderNumber
                                        viewModel.lookupOrder(order.orderNumber)
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = BrandDarkCanvas, contentColor = BrandGreen),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text("TRACK", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Need Help Section
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = BrandWhite),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, BrandGrayMedium.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.SupportAgent, contentDescription = null, tint = BrandGreenDark, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Have a question about your order?", style = Typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                            Text("Our 24/7 support team is here to assist.", style = Typography.bodySmall.copy(color = BrandTextMuted))
                        }
                    }
                    Button(
                        onClick = onContactSupport,
                        colors = ButtonDefaults.buttonColors(containerColor = BrandGreenDark),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text("CONTACT", fontSize = 11.sp)
                    }
                }
            }
        }
    }
}
