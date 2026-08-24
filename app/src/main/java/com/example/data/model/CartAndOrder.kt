package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey val productId: String,
    val quantity: Int,
    val selectedVariant: String = "Default",
    val addedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "wishlist_items")
data class WishlistItemEntity(
    @PrimaryKey val productId: String,
    val addedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey val orderId: String,
    val orderNumber: String,
    val customerName: String,
    val email: String,
    val phone: String,
    val address: String,
    val city: String,
    val country: String,
    val postalCode: String,
    val totalAmount: Double,
    val subtotal: Double,
    val shippingCost: Double,
    val discount: Double,
    val paymentMethod: String,
    val status: String, // "Order Placed", "Supplier Confirmed", "In Transit", "Out for Delivery", "Delivered"
    val trackingNumber: String,
    val carrier: String,
    val orderTimestamp: Long = System.currentTimeMillis(),
    val itemsSummary: String,
    val sourceSummary: String
)

@Entity(tableName = "recent_searches")
data class RecentSearchEntity(
    @PrimaryKey val query: String,
    val timestamp: Long = System.currentTimeMillis()
)
