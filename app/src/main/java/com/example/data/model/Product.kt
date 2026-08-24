package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.annotation.DrawableRes
import com.example.R

enum class ProductSource(
    val displayName: String,
    val badgeLabel: String,
    val isAffiliate: Boolean,
    val ctaLabel: String,
    val partnerDisclaimer: String? = null
) {
    DIRECT(
        displayName = "Direct Store",
        badgeLabel = "DIRECT STORE",
        isAffiliate = false,
        ctaLabel = "SHOP NOW",
        partnerDisclaimer = null
    ),
    CJ_DROPSHIPPING(
        displayName = "CJ Dropshipping",
        badgeLabel = "CJ DROPSHIPPING",
        isAffiliate = false,
        ctaLabel = "SHOP NOW",
        partnerDisclaimer = "Direct supplier fulfillment via CJ Dropshipping Global Logistics."
    ),
    CJ_AFFILIATE(
        displayName = "CJ Affiliate Network",
        badgeLabel = "CJ AFFILIATE",
        isAffiliate = true,
        ctaLabel = "VIEW DEAL",
        partnerDisclaimer = "This offer is provided by an external merchant partner via CJ Affiliate. We may earn a qualifying commission."
    ),
    EBAY(
        displayName = "eBay Deals",
        badgeLabel = "EBAY FIND",
        isAffiliate = true,
        ctaLabel = "VIEW ON EBAY",
        partnerDisclaimer = "This product is listed on eBay. Checkout and order fulfillment occur securely on eBay."
    ),
    ALIEXPRESS(
        displayName = "AliExpress Partner",
        badgeLabel = "ALIEXPRESS FIND",
        isAffiliate = true,
        ctaLabel = "VIEW ON ALIEXPRESS",
        partnerDisclaimer = "Curated from AliExpress authorized partner feeds. Checkout is completed on AliExpress."
    ),
    ETSY(
        displayName = "Etsy Curated",
        badgeLabel = "ETSY FIND",
        isAffiliate = true,
        ctaLabel = "VIEW ON ETSY",
        partnerDisclaimer = "Handmade & unique artisan find on Etsy. Order is placed with the independent artisan on Etsy."
    )
}

enum class ProductCategory(val displayName: String, val iconName: String) {
    ALL("All Categories", "grid_view"),
    BEAUTY_SKINCARE("Beauty & Skincare", "spa"),
    SMART_GADGETS("Smart Gadgets", "devices"),
    HOME_LIVING("Home & Living", "home"),
    FASHION("Fashion & Bags", "checkroom"),
    FITNESS_WELLNESS("Fitness & Wellness", "fitness_center"),
    PETS_MORE("Pets & Family", "pets")
}

data class ProductReview(
    val reviewerName: String,
    val rating: Float,
    val date: String,
    val comment: String,
    val isVerifiedPurchase: Boolean = true,
    val location: String = "Verified Buyer"
)

data class ShippingRate(
    val country: String,
    val method: String,
    val estimatedDays: String,
    val costUsd: Double,
    val isFree: Boolean = false
)

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    val title: String,
    val brand: String,
    val category: String,
    val subcategory: String,
    val source: String,
    val price: Double,
    val compareAtPrice: Double,
    val rating: Float,
    val reviewCount: Int,
    val stockCount: Int,
    val isTrending: Boolean = false,
    val isBestSeller: Boolean = false,
    val isFlashDeal: Boolean = false,
    val flashEndsInMinutes: Int = 0,
    val shortDescription: String,
    val fullDescription: String,
    val supplier: String,
    val sku: String,
    val affiliateUrl: String = "",
    val externalId: String = "",
    val imageDrawableRes: Int = 0,
    val tags: String = "",
    val specifications: String = "", // Key: Value; Key2: Value2
    val features: String = "", // Feature1|Feature2|Feature3
    val shippingInfo: String = "Standard 5-10 business days delivery with tracking",
    val returnPolicy: String = "30-Day Money Back Guarantee"
)
