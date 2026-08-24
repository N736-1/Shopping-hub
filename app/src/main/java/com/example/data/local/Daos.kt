package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.CartItemEntity
import com.example.data.model.OrderEntity
import com.example.data.model.ProductEntity
import com.example.data.model.RecentSearchEntity
import com.example.data.model.WishlistItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :productId LIMIT 1")
    suspend fun getProductById(productId: String): ProductEntity?

    @Query("SELECT * FROM products WHERE isTrending = 1")
    fun getTrendingProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE isBestSeller = 1")
    fun getBestSellers(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE isFlashDeal = 1")
    fun getFlashDeals(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE source = :source")
    fun getProductsBySource(source: String): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE category = :category")
    fun getProductsByCategory(category: String): Flow<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<ProductEntity>)

    @Query("SELECT COUNT(*) FROM products")
    suspend fun getProductCount(): Int
}

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items ORDER BY addedTimestamp DESC")
    fun getCartItems(): Flow<List<CartItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(item: CartItemEntity)

    @Query("UPDATE cart_items SET quantity = :quantity WHERE productId = :productId")
    suspend fun updateQuantity(productId: String, quantity: Int)

    @Query("DELETE FROM cart_items WHERE productId = :productId")
    suspend fun deleteItem(productId: String)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}

@Dao
interface WishlistDao {
    @Query("SELECT * FROM wishlist_items ORDER BY addedTimestamp DESC")
    fun getWishlistItems(): Flow<List<WishlistItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToWishlist(item: WishlistItemEntity)

    @Query("DELETE FROM wishlist_items WHERE productId = :productId")
    suspend fun removeFromWishlist(productId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM wishlist_items WHERE productId = :productId)")
    fun isInWishlist(productId: String): Flow<Boolean>
}

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders ORDER BY orderTimestamp DESC")
    fun getAllOrders(): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE orderId = :orderId OR orderNumber = :orderNumber LIMIT 1")
    suspend fun getOrderByIdOrNumber(orderId: String, orderNumber: String): OrderEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity)
}

@Dao
interface SearchDao {
    @Query("SELECT * FROM recent_searches ORDER BY timestamp DESC LIMIT 8")
    fun getRecentSearches(): Flow<List<RecentSearchEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearch(search: RecentSearchEntity)

    @Query("DELETE FROM recent_searches WHERE query = :query")
    suspend fun deleteSearch(query: String)
}
