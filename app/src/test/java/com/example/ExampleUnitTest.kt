package com.example

import com.example.data.model.ProductCategory
import com.example.data.model.ProductSource
import com.example.data.sample.SampleData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun verifySampleProductsCatalog() {
        val products = SampleData.initialProducts
        assertTrue("Catalog should have products", products.isNotEmpty())

        // Verify Mama Organic products exist
        val mamaOrganicProducts = products.filter { it.brand == "Mama Organic" }
        assertTrue("Mama Organic products should be in catalog", mamaOrganicProducts.isNotEmpty())

        // Verify source channels are set
        val cjProducts = products.filter { it.source == ProductSource.CJ_DROPSHIPPING.name }
        val ebayProducts = products.filter { it.source == ProductSource.EBAY_DEALS.name }
        val aliProducts = products.filter { it.source == ProductSource.ALIEXPRESS.name }

        assertTrue("CJ dropshipping products present", cjProducts.isNotEmpty())
        assertTrue("eBay deals products present", ebayProducts.isNotEmpty())
        assertTrue("AliExpress deals products present", aliProducts.isNotEmpty())
    }

    @Test
    fun verifyPromoCodesAndDiscounts() {
        val promos = SampleData.promoCodes
        assertTrue("SAVE10 promo exists", promos.containsKey("SAVE10"))
        assertTrue("FREESHIP promo exists", promos.containsKey("FREESHIP"))
        assertTrue("GREENDEAL promo exists", promos.containsKey("GREENDEAL"))
    }

    @Test
    fun verifyShippingZonesConfiguration() {
        val zones = SampleData.shippingZones
        val usZone = zones.find { it.country == "United States" }
        val pkZone = zones.find { it.country == "Pakistan" }

        assertNotNull(usZone)
        assertNotNull(pkZone)
        assertEquals("CJ Packet Express", usZone?.carrier)
        assertEquals("TCS Express / Leopards", pkZone?.carrier)
    }
}
