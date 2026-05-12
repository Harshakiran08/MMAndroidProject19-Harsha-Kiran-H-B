package com.pashuaahar.data.db

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val passwordHash: String,
    val googleLinked: Boolean = false,
    val syncEnabled: Boolean = false
)

@Entity(
    tableName = "feed_history",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId"), Index("animalProfileId")]
)
data class FeedHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val animalProfileId: Int? = null,
    val cowName: String,
    val breed: String,
    val animalType: String = "COW",
    val goalMode: String = "REDUCE_COST",
    val location: String = "Local Market",
    val weight: Double,
    val milkYield: Double,
    val date: Long = System.currentTimeMillis(),
    val maizeKg: Double,
    val cottonseedKg: Double,
    val branKg: Double,
    val proteinPercent: Double = 0.0,
    val energyValueMcal: Double = 0.0,
    val fiberPercent: Double = 0.0,
    val savingsRupees: Double,
    val totalCostRupees: Double,
    val marketCostRupees: Double = 0.0,
    val dailySavings: Double = 0.0,
    val monthlySavings: Double = 0.0,
    val yearlySavings: Double = 0.0,
    val favorite: Boolean = false
)

@Entity(
    tableName = "animal_profiles",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId")]
)
data class AnimalProfile(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val name: String,
    val animalType: String,
    val breed: String,
    val weight: Double,
    val currentMilkProduction: Double,
    val notes: String = "",
    val lastUpdated: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "inventory_items",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId")]
)
data class InventoryItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val ingredient: String,
    val quantityKg: Double,
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "favorite_recipes",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId")]
)
data class FavoriteRecipe(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val title: String,
    val summary: String,
    val animalType: String,
    val goalMode: String,
    val savedAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "market_alerts",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId")]
)
data class MarketAlert(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val ingredient: String,
    val thresholdPrice: Float,
    val enabled: Boolean = true
)

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: User): Long

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: Int): User?

    @Query("UPDATE users SET passwordHash = :passwordHash WHERE username = :username")
    suspend fun updatePassword(username: String, passwordHash: String): Int

    @Query("UPDATE users SET googleLinked = :linked, syncEnabled = :syncEnabled WHERE id = :userId")
    suspend fun updateAuthState(userId: Int, linked: Boolean, syncEnabled: Boolean)
}

@Dao
interface HistoryDao {
    @Query("SELECT * FROM feed_history WHERE userId = :userId ORDER BY date DESC")
    fun getHistoryForUser(userId: Int): Flow<List<FeedHistory>>

    @Insert
    suspend fun insertHistory(history: FeedHistory)

    @Delete
    suspend fun deleteHistory(history: FeedHistory)
}

@Dao
interface AnimalProfileDao {
    @Query("SELECT * FROM animal_profiles WHERE userId = :userId ORDER BY lastUpdated DESC")
    fun getProfilesForUser(userId: Int): Flow<List<AnimalProfile>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProfile(profile: AnimalProfile): Long
}

@Dao
interface InventoryDao {
    @Query("SELECT * FROM inventory_items WHERE userId = :userId ORDER BY ingredient ASC")
    fun getInventoryForUser(userId: Int): Flow<List<InventoryItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertInventoryItem(item: InventoryItem): Long
}

@Dao
interface FavoriteRecipeDao {
    @Query("SELECT * FROM favorite_recipes WHERE userId = :userId ORDER BY savedAt DESC")
    fun getFavoritesForUser(userId: Int): Flow<List<FavoriteRecipe>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(recipe: FavoriteRecipe): Long
}

@Dao
interface MarketAlertDao {
    @Query("SELECT * FROM market_alerts WHERE userId = :userId ORDER BY ingredient ASC")
    fun getAlertsForUser(userId: Int): Flow<List<MarketAlert>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAlert(alert: MarketAlert): Long
}

@Database(
    entities = [User::class, FeedHistory::class, AnimalProfile::class, InventoryItem::class, FavoriteRecipe::class, MarketAlert::class],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun historyDao(): HistoryDao
    abstract fun animalProfileDao(): AnimalProfileDao
    abstract fun inventoryDao(): InventoryDao
    abstract fun favoriteRecipeDao(): FavoriteRecipeDao
    abstract fun marketAlertDao(): MarketAlertDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pashuaahar_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
