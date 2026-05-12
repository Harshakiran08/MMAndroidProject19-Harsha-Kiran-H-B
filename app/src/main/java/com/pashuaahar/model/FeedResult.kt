package com.pashuaahar.model

import java.io.Serializable

data class FeedResult(
    val animalType: AnimalType = AnimalType.COW,
    val goal: FarmerGoal = FarmerGoal.REDUCE_COST,
    val breed: String = "Jersey",
    val ageYears: Double = 0.0,
    val weightKg: Double = 0.0,
    val milkYieldLitres: Double = 0.0,
    val feedIngredients: List<FeedIngredientPlan> = emptyList(),
    val recommendations: List<FeedRecommendation> = emptyList(),
    val maizeKg: Double,
    val cottonseedCakeKg: Double,
    val wheatBranKg: Double,
    val proteinPercent: Double,
    val energyValueMcal: Double,
    val fiberPercent: Double = 0.0,
    val maizeCostRupees: Double,
    val cottonseedCostRupees: Double,
    val wheatBranCostRupees: Double,
    val homemadeCostRupees: Double,
    val marketCostRupees: Double,
    val savingsRupees: Double,
    val homemadeCostPerKg: Double = 0.0,
    val marketCostPerKg: Double = 0.0,
    val dailySavings: Double = 0.0,
    val weeklySavings: Double = 0.0,
    val monthlySavings: Double = 0.0,
    val yearlySavings: Double = 0.0
) : Serializable
