package com.pashuaahar.model

enum class AnimalType { COW, BUFFALO, GOAT }

enum class FarmerGoal { REDUCE_COST, INCREASE_MILK, USE_AVAILABLE_INGREDIENTS }

enum class AppLanguage(val code: String) { ENGLISH("en"), KANNADA("kn"), HINDI("hi") }

data class NutritionBreakdown(
    val proteinPercent: Double,
    val energyValueMcal: Double,
    val fiberPercent: Double
)

data class SavingsInsight(
    val homemadeCostPerKg: Double,
    val marketCostPerKg: Double,
    val dailySavings: Double,
    val monthlySavings: Double,
    val yearlySavings: Double
)

data class FeedIngredientPlan(
    val name: String,
    val quantityKg: Double,
    val costRupees: Double,
    val iconHint: String
)

data class FeedRecommendation(
    val title: String,
    val body: String
)

data class MarketPriceInfo(
    val ingredient: String,
    val location: String,
    val price: Float,
    val previousPrice: Float,
    val updatedAt: Long,
    val trendLabel: String,
    val alertThreshold: Float? = null
)

data class PricePrediction(
    val ingredient: String,
    val prediction: String
)
