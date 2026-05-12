package com.pashuaahar.utils

import com.pashuaahar.model.AnimalType
import com.pashuaahar.model.FarmerGoal
import com.pashuaahar.model.FeedIngredientPlan
import com.pashuaahar.model.FeedRecommendation
import com.pashuaahar.model.FeedResult

object FeedCalculator {

    fun generateFeed(
        breed: String,
        weight: Double,
        milkYield: Double,
        ageYears: Double = 4.0,
        priceMaize: Float,
        priceCottonseed: Float,
        priceBran: Float,
        priceMarketFeed: Float
    ): FeedResult {
        return generateSmartFeed(
            breed = breed,
            animalType = AnimalType.COW,
            weight = weight,
            milkYield = milkYield,
            ageYears = ageYears,
            goal = FarmerGoal.REDUCE_COST,
            priceMaize = priceMaize,
            priceCottonseed = priceCottonseed,
            priceBran = priceBran,
            priceMarketFeed = priceMarketFeed,
            availableMaizeKg = null,
            availableCottonseedKg = null,
            availableBranKg = null
        )
    }

    fun generateSmartFeed(
        breed: String,
        animalType: AnimalType,
        weight: Double,
        milkYield: Double,
        ageYears: Double,
        goal: FarmerGoal,
        priceMaize: Float,
        priceCottonseed: Float,
        priceBran: Float,
        priceMarketFeed: Float,
        availableMaizeKg: Double?,
        availableCottonseedKg: Double?,
        availableBranKg: Double?
    ): FeedResult {
        val breedFactor = getBreedFactor(breed)
        val ageFactor = getAgeFactor(ageYears)
        val factor = breedFactor * ageFactor

        val proteinReq = ((0.4 * milkYield) + (0.02 * weight)) * factor
        val energyReq = ((5 * milkYield) + (0.1 * weight)) * factor

        var maize = (1.2 + milkYield * 0.28 + weight * 0.0025) * breedFactor
        var cottonseed = (0.7 + milkYield * 0.16 + weight * 0.0011) * factor
        var bran = (0.6 + milkYield * 0.10 + weight * 0.0010) * ageFactor

        var protein = (maize * 0.09) + (cottonseed * 0.24) + (bran * 0.15)
        var energy = (maize * 14) + (cottonseed * 10) + (bran * 12)

        while (protein < proteinReq) {
            cottonseed += 0.2
            protein = (maize * 0.09) + (cottonseed * 0.24) + (bran * 0.15)
        }

        while (energy < energyReq) {
            maize += 0.2
            energy = (maize * 14) + (cottonseed * 10) + (bran * 12)
        }

        if (goal == FarmerGoal.INCREASE_MILK) {
            cottonseed += 0.35
            maize += 0.25
            protein = (maize * 0.09) + (cottonseed * 0.24) + (bran * 0.15)
            energy = (maize * 14) + (cottonseed * 10) + (bran * 12)
        }

        if (goal == FarmerGoal.USE_AVAILABLE_INGREDIENTS) {
            maize = availableMaizeKg?.coerceAtLeast(maize * 0.75) ?: maize
            cottonseed = availableCottonseedKg?.coerceAtLeast(cottonseed * 0.75) ?: cottonseed
            bran = availableBranKg?.coerceAtLeast(bran * 0.75) ?: bran
        }

        val totalFeed = (maize + cottonseed + bran).coerceAtLeast(0.1)
        val fiberPercent = ((maize * 0.02) + (cottonseed * 0.12) + (bran * 0.11)) / totalFeed * 100

        val maizeCost = maize * priceMaize
        val cottonCost = cottonseed * priceCottonseed
        val branCost = bran * priceBran
        val homemadeCost = maizeCost + cottonCost + branCost
        val marketCost = totalFeed * priceMarketFeed
        val savings = marketCost - homemadeCost
        val homemadeCostPerKg = homemadeCost / totalFeed
        val marketCostPerKg = marketCost / totalFeed
        val weeklySavings = savings * 7
        val monthlySavings = savings * 30
        val yearlySavings = savings * 365

        val ingredients = listOf(
            FeedIngredientPlan("Maize", round(maize), round(maizeCost), "grain"),
            FeedIngredientPlan("Cottonseed Cake", round(cottonseed), round(cottonCost), "seed"),
            FeedIngredientPlan("Wheat Bran", round(bran), round(branCost), "bran")
        )

        val recommendations = listOf(
            FeedRecommendation(
                title = "Breed based formula",
                body = "This plan uses the $breed breed factor and age profile to meet protein and energy needs from weight and milk yield."
            ),
            FeedRecommendation(
                title = "Protein target",
                body = "Required protein is ${round(proteinReq)} and the current mix delivers ${round(protein)}."
            ),
            FeedRecommendation(
                title = "Energy target",
                body = "Required energy is ${round(energyReq)} and the current mix delivers ${round(energy)}."
            )
        )

        return FeedResult(
            animalType = animalType,
            goal = goal,
            breed = breed,
            ageYears = round(ageYears),
            weightKg = round(weight),
            milkYieldLitres = round(milkYield),
            feedIngredients = ingredients,
            recommendations = recommendations,
            maizeKg = round(maize),
            cottonseedCakeKg = round(cottonseed),
            wheatBranKg = round(bran),
            proteinPercent = round(protein),
            energyValueMcal = round(energy),
            fiberPercent = round(fiberPercent),
            maizeCostRupees = round(maizeCost),
            cottonseedCostRupees = round(cottonCost),
            wheatBranCostRupees = round(branCost),
            homemadeCostRupees = round(homemadeCost),
            marketCostRupees = round(marketCost),
            savingsRupees = round(savings),
            homemadeCostPerKg = round(homemadeCostPerKg),
            marketCostPerKg = round(marketCostPerKg),
            dailySavings = round(savings),
            weeklySavings = round(weeklySavings),
            monthlySavings = round(monthlySavings),
            yearlySavings = round(yearlySavings)
        )
    }

    private fun getBreedFactor(breed: String): Double {
        return when (breed.trim().lowercase()) {
            "jersey" -> 1.0
            "hf", "holstein friesian", "holstein-friesian" -> 1.15
            "desi", "local" -> 0.9
            else -> 1.0
        }
    }

    private fun getAgeFactor(ageYears: Double): Double {
        return when {
            ageYears <= 2 -> 0.92
            ageYears >= 8 -> 0.96
            else -> 1.0
        }
    }

    private fun round(value: Double): Double = String.format("%.2f", value).toDouble()
}
