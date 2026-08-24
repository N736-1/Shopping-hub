package com.example.data.sample

import com.example.R
import com.example.data.model.ProductCategory
import com.example.data.model.ProductEntity
import com.example.data.model.ProductReview
import com.example.data.model.ProductSource
import com.example.data.model.ShippingRate

object SampleData {

    val initialProducts = listOf(
        // Product 1 - Inspired by user reference screenshot
        ProductEntity(
            id = "prod_mama_teatree_facewash",
            title = "Mama Organic Tea Tree Face Wash with Vitamin C & Neem",
            brand = "Mama Organic",
            category = ProductCategory.BEAUTY_SKINCARE.name,
            subcategory = "Facial Cleansers",
            source = ProductSource.CJ_DROPSHIPPING.name,
            price = 18.99,
            compareAtPrice = 29.99,
            rating = 4.9f,
            reviewCount = 428,
            stockCount = 142,
            isTrending = true,
            isBestSeller = true,
            isFlashDeal = true,
            flashEndsInMinutes = 185,
            shortDescription = "Purifying organic tea tree daily face wash with natural vitamin C and tea tree oil for acne-free, clear skin.",
            fullDescription = "Crafted with 100% natural botanical extracts, Mama Organic Tea Tree Face Wash deeply cleanses pores without stripping natural moisture. Infused with pure Australian tea tree oil, soothing neem extracts, and brightening vitamin C to fight acne-causing bacteria and restore your skin's natural luminous glow.",
            supplier = "CJ Dropshipping Cosmetics Hub",
            sku = "CJ-BEAU-TTFW-01",
            affiliateUrl = "https://cjdropshipping.com/product/tea-tree-face-wash-p-109283",
            externalId = "CJ9928120",
            imageDrawableRes = R.drawable.img_tea_tree_facewash,
            tags = "Skincare, Tea Tree, Organic, Face Wash, Acne, Vitamin C",
            specifications = "Volume: 150ml / 5.1 fl oz; Skin Type: All / Acne-prone; Formula: Sulfate-Free & Paraben-Free; Origin: Organic Certified Botanical Labs",
            features = "Deep Pore Purification|Reduces Blemishes & Excess Sebum|Enriched with Vitamin C Glow Complex|Dermatologist Tested & Cruelty Free",
            shippingInfo = "CJ Packet Express (5-8 days with tracking) • Free shipping on orders over $35",
            returnPolicy = "30-day money-back satisfaction guarantee"
        ),

        // Product 2 - Tea Tree Serum
        ProductEntity(
            id = "prod_mama_teatree_serum",
            title = "Mama Organic Tea Tree Clarifying Serum with Vitamin C & Zinc",
            brand = "Mama Organic",
            category = ProductCategory.BEAUTY_SKINCARE.name,
            subcategory = "Serums & Treatments",
            source = ProductSource.CJ_DROPSHIPPING.name,
            price = 24.50,
            compareAtPrice = 38.00,
            rating = 4.85f,
            reviewCount = 319,
            stockCount = 89,
            isTrending = true,
            isBestSeller = false,
            isFlashDeal = true,
            flashEndsInMinutes = 240,
            shortDescription = "Concentrated blemish defense & anti-spot facial serum with 10% Vitamin C and Australian Tea Tree oil.",
            fullDescription = "A potent yet gentle antioxidant serum that calms redness, fades post-acne dark marks, and clarifies your skin texture. Fast-absorbing lightweight glass dropper formulation suitable for morning and evening skincare routines.",
            supplier = "CJ Dropshipping Skincare Direct",
            sku = "CJ-BEAU-TTSM-02",
            affiliateUrl = "https://cjdropshipping.com/product/tea-tree-serum-p-203918",
            externalId = "CJ9928121",
            imageDrawableRes = R.drawable.img_tea_tree_serum,
            tags = "Serum, Vitamin C, Tea Tree, Anti-Aging, Glow, Organic",
            specifications = "Volume: 30ml / 1.0 fl oz; Texture: Lightweight Fast-Absorbing; Key Actives: 10% Vitamin C, 2% Niacinamide, Pure Tea Tree",
            features = "Fades Hyperpigmentation & Dark Spots|Soothes Irritation & Calms Active Redness|Non-Comedogenic & Vegan Formula|Dropper Bottle Packaging",
            shippingInfo = "Tracked Priority Airmail (4-7 business days)",
            returnPolicy = "30-day risk-free guarantee"
        ),

        // Product 3 - Smart LED Lamp Gadget
        ProductEntity(
            id = "prod_smart_desk_lamp",
            title = "AuraGlow Smart LED Desk Lamp with 15W Fast Wireless Charger",
            brand = "NovaTech",
            category = ProductCategory.SMART_GADGETS.name,
            subcategory = "Desk Accessories",
            source = ProductSource.CJ_AFFILIATE.name,
            price = 39.99,
            compareAtPrice = 64.99,
            rating = 4.78f,
            reviewCount = 612,
            stockCount = 210,
            isTrending = true,
            isBestSeller = true,
            isFlashDeal = false,
            flashEndsInMinutes = 0,
            shortDescription = "Minimalist touch-controlled desk lamp with 5 color temperatures, step-less dimming, and built-in fast charging pad.",
            fullDescription = "Upgrade your workstation with the AuraGlow Smart LED Lamp. Features flicker-free eye-caring illumination, touch slide brightness adjustment, 45-minute sleep timer, and integrated 15W Qi wireless charging for smartphones and earbuds.",
            supplier = "CJ Affiliate Merchant Partner",
            sku = "AFF-NOVA-LAMP-88",
            affiliateUrl = "https://cda.cjdropshipping.com/affiliate/click?aid=38291&pid=89102",
            externalId = "CJAFF-89102",
            imageDrawableRes = R.drawable.img_smart_gadget,
            tags = "Electronics, Wireless Charger, Desk Lamp, Workstation, Smart Home",
            specifications = "Power: 18W Adapter Included; Wireless Output: 15W Max; Color Temp: 2700K - 6500K; Material: Anodized Matte Aluminum",
            features = "Integrated 15W Qi Wireless Fast Charger|5 Color Modes & 6 Brightness Levels|Auto Sleep Timer (30/45 min)|Flexible 180° Foldable Gooseneck",
            shippingInfo = "Direct Partner Express (3-5 business days)",
            returnPolicy = "45-day partner replacement warranty"
        ),

        // Product 4 - eBay Deals
        ProductEntity(
            id = "prod_ebay_smart_watch",
            title = "ApexPulse Ultra GPS Smartwatch & Fitness Telemetry Tracker",
            brand = "ApexPulse",
            category = ProductCategory.SMART_GADGETS.name,
            subcategory = "Wearables",
            source = ProductSource.EBAY.name,
            price = 54.90,
            compareAtPrice = 99.00,
            rating = 4.7f,
            reviewCount = 840,
            stockCount = 45,
            isTrending = true,
            isBestSeller = false,
            isFlashDeal = true,
            flashEndsInMinutes = 95,
            shortDescription = "Rugged waterproof GPS multisport smartwatch with AMOLED display, 14-day battery, and 24/7 heart rate monitoring.",
            fullDescription = "Curated top deal from eBay authorized electronics vendor. Tracks 100+ workout modes, sleep staging, blood oxygen SpO2, and phone notifications with Bluetooth calling.",
            supplier = "Top-Rated Plus eBay Merchant",
            sku = "EBAY-APEX-WATCH-09",
            affiliateUrl = "https://ebay.us/affiliate/smartwatch-apexpulse-deal",
            externalId = "EBAY-3819284",
            imageDrawableRes = R.drawable.img_smart_gadget,
            tags = "Smartwatch, Fitness, GPS, Wearable, AMOLED",
            specifications = "Display: 1.43\" AMOLED 466x466; Battery: 450mAh (14 Days); Water Rating: 5ATM (50m); Compatibility: Android & iOS",
            features = "Built-in Standalone GPS Navigation|100+ Sports & Exercise Tracking Modes|Bluetooth 5.3 HD Calls & Notifications|All-Day SpO2 & Heart Rate Telemetry",
            shippingInfo = "Free eBay Fast & Secure shipping (3-4 days)",
            returnPolicy = "eBay Money Back Guarantee (30 days)"
        ),

        // Product 5 - AliExpress Find
        ProductEntity(
            id = "prod_ali_wireless_earbuds",
            title = "SoundSphere Active Noise Cancelling Wireless Earbuds (Spatial Audio)",
            brand = "SoundSphere",
            category = ProductCategory.SMART_GADGETS.name,
            subcategory = "Audio",
            source = ProductSource.ALIEXPRESS.name,
            price = 28.75,
            compareAtPrice = 52.00,
            rating = 4.65f,
            reviewCount = 1420,
            stockCount = 380,
            isTrending = false,
            isBestSeller = true,
            isFlashDeal = false,
            flashEndsInMinutes = 0,
            shortDescription = "Hybrid ANC earbuds with transparency mode, titanium drivers, and 36-hour total playback case.",
            fullDescription = "Discover high-fidelity acoustics at factory direct pricing from AliExpress. Equipped with 42dB active noise cancellation, environmental ENC microphones for crystal-clear phone calls, and low latency gaming mode.",
            supplier = "AliExpress Choice Partner Store",
            sku = "ALI-SOUND-ANC-42",
            affiliateUrl = "https://s.click.aliexpress.com/e/_dZ89XwQ",
            externalId = "ALI-1005006782",
            imageDrawableRes = R.drawable.img_smart_gadget,
            tags = "Earbuds, ANC, Audio, Bluetooth, Spatial Audio",
            specifications = "Driver: 12mm Titanium Dynamic; ANC Depth: -42dB; Codecs: AAC / SBC / LDAC; Battery: 8h Earbud / 36h Case",
            features = "Hybrid 42dB Active Noise Cancellation|Environmental ENC Quad-Mic Array|Custom Equalizer App Support|IPX5 Sweat & Rain Resistance",
            shippingInfo = "AliExpress Standard Fast Tracked Shipping (7-12 days)",
            returnPolicy = "15-day free return via AliExpress Buyer Protection"
        ),

        // Product 6 - Etsy Handcrafted Find
        ProductEntity(
            id = "prod_etsy_leather_wallet",
            title = "Personalized Full-Grain Horween Leather Minimalist Card Wallet",
            brand = "NorthWood Artisans",
            category = ProductCategory.FASHION.name,
            subcategory = "Men's Accessories",
            source = ProductSource.ETSY.name,
            price = 34.00,
            compareAtPrice = 48.00,
            rating = 4.95f,
            reviewCount = 960,
            stockCount = 28,
            isTrending = true,
            isBestSeller = true,
            isFlashDeal = false,
            flashEndsInMinutes = 0,
            shortDescription = "Hand-stitched full grain premium leather slim wallet with custom name monogramming option.",
            fullDescription = "Curated handmade artisan piece on Etsy. Handcrafted using legendary Horween vegetable-tanned leather and waxed French linen thread for lifetime durability. Develops a rich custom patina over time.",
            supplier = "Independent Etsy Craft Studio",
            sku = "ETSY-WAL-NW-11",
            affiliateUrl = "https://www.etsy.com/listing/handmade-leather-minimal-wallet",
            externalId = "ETSY-8921739",
            imageDrawableRes = R.drawable.img_smart_gadget,
            tags = "Handmade, Leather, Wallet, Etsy, Custom Gift, Patina",
            specifications = "Material: 100% Genuine Horween Leather; Capacity: 6-8 Cards + Cash; Dimensions: 3.8\" x 2.7\"; Finish: Natural Beeswax Polish",
            features = "100% Hand-cut & Hand-stitched in Artisan Workshop|Free Laser Monogram Personalization|Ultra-Slim Front Pocket Profile|RFID Blocking Protection Option",
            shippingInfo = "Handcrafted to order (Dispatched in 2 business days)",
            returnPolicy = "Craftsman lifetime guarantee on stitching"
        ),

        // Product 7 - Direct Store Eco Product
        ProductEntity(
            id = "prod_direct_bamboo_skincare_kit",
            title = "Mama Organic Pure Glow Botanical Complete 4-Piece Skincare Set",
            brand = "Mama Organic",
            category = ProductCategory.BEAUTY_SKINCARE.name,
            subcategory = "Skincare Bundles",
            source = ProductSource.DIRECT.name,
            price = 49.99,
            compareAtPrice = 79.99,
            rating = 4.92f,
            reviewCount = 512,
            stockCount = 75,
            isTrending = true,
            isBestSeller = true,
            isFlashDeal = true,
            flashEndsInMinutes = 310,
            shortDescription = "Complete holistic skincare regimen: Tea Tree Cleanser, Vitamin C Glow Serum, Rosewater Toner & Hydro Cream.",
            fullDescription = "The ultimate all-in-one daily skin radiance ritual formulated with pure botanical extracts and cold-pressed organic seed oils. Packaged in eco-friendly recyclable bamboo and glass containers.",
            supplier = "Dropship Hub Official Warehouse",
            sku = "DIR-MAMA-BUNDLE-04",
            affiliateUrl = "",
            externalId = "DIR-1002",
            imageDrawableRes = R.drawable.img_tea_tree_facewash,
            tags = "Skincare, Bundle, Gift Set, Tea Tree, Vitamin C, Organic",
            specifications = "Contains: 150ml Cleanser, 30ml Serum, 100ml Toner, 50g Face Cream; Eco Packaging: Recycled Bamboo Lids",
            features = "Complete 4-Step Radiance Routine|Save 40% Compared to Buying Individually|100% Organic & Non-Toxic Formula|Luxury Gift Box Included",
            shippingInfo = "Same-Day Dispatch • Free Worldwide Courier Delivery",
            returnPolicy = "60-day unconditional happiness guarantee"
        ),

        // Product 8 - Home & Living
        ProductEntity(
            id = "prod_home_ultrasonic_diffuser",
            title = "ZenMist Ultrasonic Ceramic Essential Oil Aromatherapy Diffuser",
            brand = "ZenLiving",
            category = ProductCategory.HOME_LIVING.name,
            subcategory = "Home Fragrance",
            source = ProductSource.CJ_DROPSHIPPING.name,
            price = 32.50,
            compareAtPrice = 55.00,
            rating = 4.81f,
            reviewCount = 288,
            stockCount = 114,
            isTrending = false,
            isBestSeller = false,
            isFlashDeal = false,
            flashEndsInMinutes = 0,
            shortDescription = "Handcrafted matte ceramic aroma diffuser with warm ambient LED glow and whisper-quiet ultrasonic atomization.",
            fullDescription = "Infuse your home sanctuary with therapeutic botanical aromas. Features 4 timer modes, waterless auto-shutoff protection, and 2 mist intensity settings.",
            supplier = "CJ Dropshipping Home Living Hub",
            sku = "CJ-HOME-DIFF-08",
            affiliateUrl = "https://cjdropshipping.com/product/ceramic-diffuser-p-3829",
            externalId = "CJ9928125",
            imageDrawableRes = R.drawable.img_smart_gadget,
            tags = "Home, Aromatherapy, Diffuser, Ceramic, Essential Oils",
            specifications = "Capacity: 280ml; Coverage: Up to 350 sq ft; Noise Level: <20dB; Material: Handcrafted Ceramic Stone",
            features = "Whisper-Quiet Ultrasonic Atomization|7 Soothing Ambient Light Colors|Automatic Waterless Safety Shutoff|Continuous & Intermittent Mist Modes",
            shippingInfo = "Standard Airmail (5-9 days with live tracking)",
            returnPolicy = "30-day replacement warranty"
        )
    )

    val sampleReviews = listOf(
        ProductReview(
            reviewerName = "Sarah Jenkins",
            rating = 5.0f,
            date = "2 days ago",
            comment = "Obsessed with this Tea Tree Face Wash! After just one week my acne breakouts cleared up completely and my skin feels so refreshed without any tightness.",
            location = "Verified Customer (USA)"
        ),
        ProductReview(
            reviewerName = "Ali Raza Khan",
            rating = 5.0f,
            date = "5 days ago",
            comment = "Great packaging and super fast delivery. The tea tree serum leaves no greasy feeling and pairs wonderfully with the face wash. Highly recommend!",
            location = "Verified Customer (Lahore, PK)"
        ),
        ProductReview(
            reviewerName = "Emma Watson",
            rating = 4.5f,
            date = "1 week ago",
            comment = "The multi-marketplace price check is a game changer. I compared prices with eBay and AliExpress directly from the app and got the fastest shipping route.",
            location = "Verified Customer (UK)"
        ),
        ProductReview(
            reviewerName = "Marcus Brody",
            rating = 5.0f,
            date = "2 weeks ago",
            comment = "The smart lamp wireless charging is super convenient on my work desk. 10/10 build quality.",
            location = "Verified Customer (Canada)"
        )
    )

    val shippingZones = listOf(
        ShippingRate("United States", "CJ Express Tracked", "4 - 7 Business Days", 4.99, isFree = true),
        ShippingRate("United Kingdom", "Royal Mail Tracked", "5 - 8 Business Days", 5.49, isFree = false),
        ShippingRate("Pakistan", "TCS / Leopard Express", "2 - 4 Business Days", 2.50, isFree = true),
        ShippingRate("European Union", "Euro Express Priority", "6 - 10 Business Days", 6.99, isFree = false),
        ShippingRate("Canada & Australia", "Global Post Airmail", "7 - 12 Business Days", 7.99, isFree = false)
    )
}
