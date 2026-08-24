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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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

@Composable
fun AdminHubScreen(
    viewModel: StoreViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val syncStatus by viewModel.syncStatus.collectAsState()
    var cjKey by remember { mutableStateOf(syncStatus.cjDropshipKey) }
    var cjToken by remember { mutableStateOf(syncStatus.cjAffiliateToken) }
    var priceMarkupPercent by remember { mutableStateOf("40") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BrandGrayLight)
            .testTag("admin_hub_screen")
    ) {
        // Header
        item {
            Surface(color = BrandDarkCanvas, modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = BrandWhite)
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "MARKETPLACE & SUPPLIER HUB",
                            style = Typography.titleLarge.copy(fontWeight = FontWeight.Black, color = BrandWhite)
                        )
                    }
                    Text(
                        text = "Manage CJ Dropshipping API, Affiliate Tokens & Live Inventory Sync.",
                        style = Typography.bodySmall.copy(color = Color(0xFF94A3B8)),
                        modifier = Modifier.padding(start = 48.dp)
                    )
                }
            }
        }

        // Live Sync Status Card
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .background(BrandGreen, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("API Connections Active", style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        }
                        Text(
                            text = "Last sync: ${syncStatus.lastSyncTime}",
                            style = Typography.bodySmall.copy(color = BrandTextMuted)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { viewModel.triggerMarketplaceSync() },
                        enabled = !syncStatus.isSyncing,
                        colors = ButtonDefaults.buttonColors(containerColor = BrandGreen, contentColor = Color.Black),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("admin_sync_now_btn")
                    ) {
                        if (syncStatus.isSyncing) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.Black, strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("SYNCHRONIZING CATALOG...", fontWeight = FontWeight.Bold)
                        } else {
                            Icon(Icons.Default.Refresh, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("SYNC ALL MARKETPLACES NOW", fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }

        // Supplier API Credentials
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = BrandWhite),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, BrandGrayMedium.copy(alpha = 0.5f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Key, contentDescription = null, tint = BrandGreenDark)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Configured API Keys & Tokens", style = Typography.titleMedium.copy(fontWeight = FontWeight.Black))
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = cjKey,
                        onValueChange = { cjKey = it },
                        label = { Text("CJ Dropshipping API Key (PRD Sec 14)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = cjToken,
                        onValueChange = { cjToken = it },
                        label = { Text("CJ Affiliate Token (PRD Sec 15)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = priceMarkupPercent,
                        onValueChange = { priceMarkupPercent = it },
                        label = { Text("Automated Pricing Markup Buffer (%)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Live Supplier Event Logs
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = BrandDarkCanvas),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, Color(0xFF334155)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "INTEGRATION LOGS & FEED EVENTS",
                        style = Typography.titleMedium.copy(fontWeight = FontWeight.Black, color = BrandGreen)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    syncStatus.syncLog.forEach { logLine ->
                        Text(
                            text = "• $logLine",
                            style = Typography.bodySmall.copy(color = Color(0xFFE2E8F0), fontSize = 11.sp),
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}
