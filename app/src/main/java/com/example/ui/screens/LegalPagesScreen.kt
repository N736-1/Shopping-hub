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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

enum class LegalTab(val key: String, val title: String) {
    ABOUT("about", "About Us"),
    PRIVACY("privacy", "Privacy Policy"),
    DISCLAIMER("disclaimer", "Disclaimer"),
    AFFILIATE("disclosure", "Affiliate Disclosure"),
    TERMS("terms", "Terms of Service"),
    SHIPPING("shipping", "Shipping & Returns"),
    CONTACT("contact", "Contact Us")
}

@Composable
fun LegalPagesScreen(
    initialTabKey: String = "about",
    modifier: Modifier = Modifier
) {
    var selectedTab by remember {
        mutableStateOf(LegalTab.entries.find { it.key == initialTabKey } ?: LegalTab.ABOUT)
    }

    // Contact Form state
    var contactName by remember { mutableStateOf("") }
    var contactEmail by remember { mutableStateOf("") }
    var contactSubject by remember { mutableStateOf("") }
    var contactMessage by remember { mutableStateOf("") }
    var messageSent by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BrandGrayLight)
            .testTag("legal_pages_screen")
    ) {
        // Header
        Surface(color = BrandDarkCanvas, modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "POLICIES, INFORMATION & CONTACT",
                    style = Typography.titleLarge.copy(fontWeight = FontWeight.Black, color = BrandWhite)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Transparency, data security & store compliance standards.",
                    style = Typography.bodySmall.copy(color = Color(0xFF94A3B8))
                )
            }
        }

        // Horizontal Tabs Row
        Surface(color = BrandWhite, modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                LegalTab.entries.forEach { tab ->
                    val isSelected = selectedTab == tab
                    Surface(
                        color = if (isSelected) BrandGreenDark else BrandGrayLight,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, if (isSelected) BrandGreenDark else BrandGrayMedium),
                        modifier = Modifier.clickable { selectedTab = tab }
                    ) {
                        Text(
                            text = tab.title,
                            color = if (isSelected) BrandWhite else BrandTextDark,
                            style = Typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        // Body Content
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            when (selectedTab) {
                LegalTab.ABOUT -> {
                    item {
                        PolicyCard(title = "About Dropship Hub & Mama Organic") {
                            Text(
                                text = "Dropship Hub is an intelligent multi-marketplace aggregation platform that connects consumers directly with verified dropshipping manufacturers, CJ Logistics warehouses, and curated affiliate merchants (eBay, AliExpress, Etsy).",
                                style = Typography.bodyMedium.copy(lineHeight = 22.sp, color = BrandTextDark)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Our Mission: To eliminate middleman markups while guaranteeing origin transparency, fast delivery times, and complete customer satisfaction.",
                                style = Typography.bodyMedium.copy(lineHeight = 22.sp, color = BrandTextDark)
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Text("Core Principles:", style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                            Spacer(modifier = Modifier.height(6.dp))
                            Bullet("100% Verified Quality & Organic Ingredients")
                            Bullet("Clear Source Identification on Every Product Card")
                            Bullet("Direct Trackable Courier Dispatch")
                            Bullet("Global Customer Protection & Dispute Resolution")
                        }
                    }
                }

                LegalTab.PRIVACY -> {
                    item {
                        PolicyCard(title = "Privacy Policy (GDPR & CCPA Compliant)") {
                            Text(
                                text = "Effective Date: January 1, 2026\nLast Updated: August 2026",
                                style = Typography.bodySmall.copy(color = BrandTextMuted)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "1. Information We Collect:\nWhen you browse or purchase from Dropship Hub, we collect details necessary to process shipments: your name, delivery address, email, phone number, and encrypted transaction tokens via our payment processor partners.",
                                style = Typography.bodyMedium.copy(lineHeight = 20.sp, color = BrandTextDark)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "2. How We Use Information:\nWe use personal data strictly for fulfilling supplier dispatches, providing live tracking updates, preventing fraud, and delivering opted-in deal notifications.",
                                style = Typography.bodyMedium.copy(lineHeight = 20.sp, color = BrandTextDark)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "3. Third-Party Sharing:\nWe transmit shipping information only to verified supplier logistics partners (e.g. CJ Dropshipping, TCS Express). We never sell your personal data to advertisers.",
                                style = Typography.bodyMedium.copy(lineHeight = 20.sp, color = BrandTextDark)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "4. Your Rights:\nYou have the right to access, rectify, or request deletion of your stored profile at any time by writing to privacy@dropshiphub.com.",
                                style = Typography.bodyMedium.copy(lineHeight = 20.sp, color = BrandTextDark)
                            )
                        }
                    }
                }

                LegalTab.DISCLAIMER -> {
                    item {
                        PolicyCard(title = "Store Disclaimer & Product Information") {
                            Text(
                                text = "1. Product Listings & Authenticity:\nWhile we rigorously vet all suppliers and authorized affiliate merchants, specifications, colors, and dimensions are provided by the respective manufacturers. Always verify individual manufacturer labels prior to use.",
                                style = Typography.bodyMedium.copy(lineHeight = 20.sp, color = BrandTextDark)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "2. Skincare & Cosmetic Products (Mama Organic):\nSkincare and beauty items (e.g., Tea Tree Face Wash, Vitamin C Serums) are intended for cosmetic use. Conduct a small patch test before full application. Consult a dermatologist if you have specific skin allergies.",
                                style = Typography.bodyMedium.copy(lineHeight = 20.sp, color = BrandTextDark)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "3. Pricing & Availability:\nPrices, promotions, and stock levels may fluctuate dynamically based on supplier inventory and currency exchange rates.",
                                style = Typography.bodyMedium.copy(lineHeight = 20.sp, color = BrandTextDark)
                            )
                        }
                    }
                }

                LegalTab.AFFILIATE -> {
                    item {
                        PolicyCard(title = "FTC Affiliate Disclosure") {
                            Text(
                                text = "In compliance with the Federal Trade Commission (FTC) guidelines, please note:",
                                style = Typography.bodyMedium.copy(lineHeight = 20.sp, color = BrandTextDark)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Some products featured on Dropship Hub are curated from partner networks including CJ Affiliate (Commission Junction), eBay Partner Network, AliExpress Affiliate Program, and Etsy Affiliates.",
                                style = Typography.bodyMedium.copy(lineHeight = 20.sp, color = BrandTextDark)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "If you click on an affiliate product link and complete a purchase on the external merchant site, we may receive a small referral commission. This occurs at zero additional cost to you and helps maintain our platform services.",
                                style = Typography.bodyMedium.copy(lineHeight = 20.sp, color = BrandTextDark)
                            )
                        }
                    }
                }

                LegalTab.TERMS -> {
                    item {
                        PolicyCard(title = "Terms & Conditions of Service") {
                            Text(
                                text = "By accessing and using Dropship Hub, you agree to comply with our Terms of Service. All orders placed are subject to supplier confirmation, availability, and valid delivery addresses.",
                                style = Typography.bodyMedium.copy(lineHeight = 20.sp, color = BrandTextDark)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Users agree not to misuse checkout mechanisms, provide false shipping data, or engage in unauthorized scraping of platform feeds.",
                                style = Typography.bodyMedium.copy(lineHeight = 20.sp, color = BrandTextDark)
                            )
                        }
                    }
                }

                LegalTab.SHIPPING -> {
                    item {
                        PolicyCard(title = "Shipping & 30-Day Return Guarantee") {
                            Text("Worldwide Shipping Coverage:", style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                            Spacer(modifier = Modifier.height(6.dp))
                            Bullet("United States & Canada: 4-7 business days via CJ Express")
                            Bullet("United Kingdom & Europe: 5-8 business days")
                            Bullet("Pakistan & South Asia: 2-4 business days via TCS / Leopard Express")
                            Bullet("Rest of World: 7-12 business days with full online tracking")
                            Spacer(modifier = Modifier.height(14.dp))
                            Text("30-Day Return Policy:", style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "If you are not 100% satisfied with your item, you may request a free return or replacement within 30 days of delivery. Items must be unopened with original packaging.",
                                style = Typography.bodyMedium.copy(lineHeight = 20.sp, color = BrandTextDark)
                            )
                        }
                    }
                }

                LegalTab.CONTACT -> {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = BrandWhite),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, BrandGrayMedium.copy(alpha = 0.5f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Contact Customer Support", style = Typography.titleLarge.copy(fontWeight = FontWeight.Black))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("We reply within 2 hours during active business hours.", style = Typography.bodySmall.copy(color = BrandTextMuted))

                                Spacer(modifier = Modifier.height(14.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Email, contentDescription = null, tint = BrandGreenDark, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("support@dropshiphub.com", style = Typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Phone, contentDescription = null, tint = BrandGreenDark, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("+1 (800) 555-DROPSHIP / +92 300 1234567", style = Typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
                                }

                                Spacer(modifier = Modifier.height(16.dp))
                                HorizontalDivider(color = BrandGrayLight)
                                Spacer(modifier = Modifier.height(16.dp))

                                if (messageSent) {
                                    Surface(
                                        color = BrandGreenLight,
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = BrandGreenDark)
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "Thank you! Your message has been received. Our support team will contact you shortly.",
                                                style = Typography.bodyMedium.copy(color = BrandGreenDark, fontWeight = FontWeight.Bold)
                                            )
                                        }
                                    }
                                } else {
                                    Text("Send a Message", style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                                    Spacer(modifier = Modifier.height(10.dp))

                                    OutlinedTextField(
                                        value = contactName,
                                        onValueChange = { contactName = it },
                                        label = { Text("Your Name") },
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))

                                    OutlinedTextField(
                                        value = contactEmail,
                                        onValueChange = { contactEmail = it },
                                        label = { Text("Your Email") },
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))

                                    OutlinedTextField(
                                        value = contactSubject,
                                        onValueChange = { contactSubject = it },
                                        label = { Text("Subject (Order inquiry, partnership, question)") },
                                        singleLine = true,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))

                                    OutlinedTextField(
                                        value = contactMessage,
                                        onValueChange = { contactMessage = it },
                                        label = { Text("Message") },
                                        minLines = 3,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Spacer(modifier = Modifier.height(14.dp))

                                    Button(
                                        onClick = {
                                            if (contactName.isNotBlank() && contactEmail.isNotBlank()) {
                                                messageSent = true
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = BrandGreen, contentColor = Color.Black),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.fillMaxWidth().height(46.dp)
                                    ) {
                                        Text("SUBMIT MESSAGE", fontWeight = FontWeight.Black)
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

@Composable
private fun PolicyCard(title: String, content: @Composable () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = BrandWhite),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, BrandGrayMedium.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(text = title, style = Typography.titleLarge.copy(fontWeight = FontWeight.Black, color = BrandTextDark))
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
private fun Bullet(text: String) {
    Row(modifier = Modifier.padding(vertical = 3.dp), verticalAlignment = Alignment.Top) {
        Text("•", color = BrandGreenDark, fontWeight = FontWeight.Black, modifier = Modifier.padding(end = 8.dp))
        Text(text, style = Typography.bodyMedium.copy(color = BrandTextDark, lineHeight = 20.sp))
    }
}
