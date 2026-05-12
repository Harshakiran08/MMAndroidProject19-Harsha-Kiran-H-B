package com.pashuaahar.ui

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pashuaahar.data.SettingsManager
import com.pashuaahar.data.VeterinaryTipsRepository
import com.pashuaahar.data.db.AnimalProfile
import com.pashuaahar.data.db.AppDatabase
import com.pashuaahar.data.db.FavoriteRecipe
import com.pashuaahar.data.db.InventoryItem
import com.pashuaahar.data.db.MarketAlert
import com.pashuaahar.model.AnimalType
import com.pashuaahar.model.AppLanguage
import com.pashuaahar.model.FarmerGoal
import com.pashuaahar.model.FeedIngredientPlan
import com.pashuaahar.model.FeedRecommendation
import com.pashuaahar.model.FeedResult
import com.pashuaahar.model.MarketPriceInfo
import com.pashuaahar.model.PricePrediction
import com.pashuaahar.model.VeterinaryTip
import com.pashuaahar.utils.FeedCalculator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class SharedViewModel(application: Application) : AndroidViewModel(application) {
    private val settingsManager = SettingsManager(application)
    private val database = AppDatabase.getDatabase(application)
    private val userDao = database.userDao()
    private val historyDao = database.historyDao()
    private val animalProfileDao = database.animalProfileDao()
    private val inventoryDao = database.inventoryDao()
    private val favoriteRecipeDao = database.favoriteRecipeDao()
    private val marketAlertDao = database.marketAlertDao()

    var loggedInUserId by mutableIntStateOf(-1)
        private set
    var currentUsername by mutableStateOf("")
        private set
    var pendingLoginUsername by mutableStateOf("")
        private set
    var pendingLoginPassword by mutableStateOf("")
        private set
    var googleSignedIn by mutableStateOf(false)
        private set
    var cloudSyncEnabled by mutableStateOf(false)
        private set

    var isDarkMode by mutableStateOf(false)
        private set
    var isKannada by mutableStateOf(false)
        private set
    var languageCode by mutableStateOf(AppLanguage.ENGLISH.code)
        private set
    var marketLocation by mutableStateOf("Mandya Market")
        private set

    var priceMaize by mutableFloatStateOf(25.0f)
    var priceCottonseed by mutableFloatStateOf(40.0f)
    var priceBran by mutableFloatStateOf(20.0f)
    var priceMarket by mutableFloatStateOf(35.0f)
    var marketLastUpdatedAt by mutableStateOf(System.currentTimeMillis())
        private set

    var selectedAnimalType by mutableStateOf(AnimalType.COW)
    var selectedGoal by mutableStateOf(FarmerGoal.REDUCE_COST)
    var selectedBreed by mutableStateOf("")
    var cowName by mutableStateOf("")
    var weight by mutableStateOf(0.0)
    var ageYears by mutableStateOf(4.0)
    var milkYield by mutableStateOf(0.0)
    var calculatorStep by mutableIntStateOf(0)
    var useInventoryOnly by mutableStateOf(false)
    var maizeInventoryInput by mutableStateOf("")
    var cottonseedInventoryInput by mutableStateOf("")
    var branInventoryInput by mutableStateOf("")
    var feedResult: FeedResult? by mutableStateOf(null)
    var reportPreview by mutableStateOf("")
        private set
    var shareText by mutableStateOf("")
        private set
    var lastSettingsMessage by mutableStateOf("")
        private set

    val guidedRecommendations = mutableStateListOf<String>()
    val marketPrices = mutableStateListOf<MarketPriceInfo>()
    val pricePredictions = mutableStateListOf<PricePrediction>()
    val activePriceAlerts = mutableStateListOf<String>()
    val veterinaryTips: List<VeterinaryTip> = VeterinaryTipsRepository.getTips()

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val allHistory: Flow<List<com.pashuaahar.data.db.FeedHistory>> =
        settingsManager.loggedInUserIdFlow.flatMapLatest { userId ->
            if (userId != -1) historyDao.getHistoryForUser(userId) else flowOf(emptyList())
        }

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val animalProfiles: Flow<List<AnimalProfile>> =
        settingsManager.loggedInUserIdFlow.flatMapLatest { userId ->
            if (userId != -1) animalProfileDao.getProfilesForUser(userId) else flowOf(emptyList())
        }

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val inventoryItems: Flow<List<InventoryItem>> =
        settingsManager.loggedInUserIdFlow.flatMapLatest { userId ->
            if (userId != -1) inventoryDao.getInventoryForUser(userId) else flowOf(emptyList())
        }

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val favoriteRecipes: Flow<List<FavoriteRecipe>> =
        settingsManager.loggedInUserIdFlow.flatMapLatest { userId ->
            if (userId != -1) favoriteRecipeDao.getFavoritesForUser(userId) else flowOf(emptyList())
        }

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val marketAlerts: Flow<List<MarketAlert>> =
        settingsManager.loggedInUserIdFlow.flatMapLatest { userId ->
            if (userId != -1) marketAlertDao.getAlertsForUser(userId) else flowOf(emptyList())
        }

    init {
        viewModelScope.launch {
            launch {
                settingsManager.loggedInUserIdFlow.collectLatest { userId ->
                    loggedInUserId = userId
                    if (userId == -1) {
                        currentUsername = ""
                        googleSignedIn = false
                        cloudSyncEnabled = false
                    } else {
                        userDao.getUserById(userId)?.let { user ->
                            currentUsername = user.username
                            googleSignedIn = user.googleLinked
                            cloudSyncEnabled = user.syncEnabled
                        }
                    }
                }
            }
            launch {
                settingsManager.saveThemeMode(false)
                settingsManager.isDarkModeFlow.collectLatest { isDarkMode = false }
            }
            launch { settingsManager.isKannadaFlow.collectLatest { isKannada = it } }
            launch { settingsManager.languageCodeFlow.collectLatest { languageCode = it; isKannada = it == "kn" } }
            launch { settingsManager.locationFlow.collectLatest { marketLocation = it; refreshMarketAnalysis() } }
            launch { settingsManager.maizePriceFlow.collectLatest { priceMaize = it; refreshMarketAnalysis() } }
            launch { settingsManager.cottonseedPriceFlow.collectLatest { priceCottonseed = it; refreshMarketAnalysis() } }
            launch { settingsManager.branPriceFlow.collectLatest { priceBran = it; refreshMarketAnalysis() } }
            launch { settingsManager.marketPriceFlow.collectLatest { priceMarket = it; refreshMarketAnalysis() } }
            launch {
                marketAlerts.collectLatest { alerts ->
                    activePriceAlerts.clear()
                    alerts.filter { it.enabled }.forEach { alert ->
                        val livePrice = when (alert.ingredient.lowercase()) {
                            "maize" -> priceMaize
                            "cottonseed cake" -> priceCottonseed
                            "wheat bran" -> priceBran
                            else -> priceMarket
                        }
                        if (livePrice <= alert.thresholdPrice) {
                            activePriceAlerts += "${alert.ingredient} dropped to Rs ${livePrice.toInt()} in $marketLocation"
                        }
                    }
                }
            }
        }
    }

    suspend fun register(username: String, passwordHash: String): Boolean {
        val existing = userDao.getUserByUsername(username)
        if (existing != null) return false
        userDao.insertUser(com.pashuaahar.data.db.User(username = username, passwordHash = passwordHash))
        pendingLoginUsername = username
        pendingLoginPassword = passwordHash
        return true
    }

    suspend fun login(username: String, passwordHash: String): Boolean {
        val user = userDao.getUserByUsername(username)
        if (user != null && user.passwordHash == passwordHash) {
            currentUsername = user.username
            clearPendingLogin()
            settingsManager.saveLoggedInUserId(user.id)
            return true
        }
        return false
    }

    suspend fun resetPassword(username: String, newPassword: String): Boolean {
        val normalizedUsername = username.trim()
        val updatedRows = userDao.updatePassword(normalizedUsername, newPassword)
        if (updatedRows > 0) {
            pendingLoginUsername = normalizedUsername
            pendingLoginPassword = newPassword
            return true
        }
        return false
    }

    fun simulateGoogleSignIn() {
        if (loggedInUserId == -1) return
        googleSignedIn = true
        cloudSyncEnabled = true
        viewModelScope.launch {
            userDao.updateAuthState(loggedInUserId, linked = true, syncEnabled = true)
        }
    }

    fun logout() {
        currentUsername = ""
        googleSignedIn = false
        cloudSyncEnabled = false
        viewModelScope.launch {
            settingsManager.saveLoggedInUserId(-1)
        }
    }

    fun toggleDarkMode() {
        viewModelScope.launch { settingsManager.saveThemeMode(!isDarkMode) }
    }

    fun cycleLanguage() {
        val next = when (languageCode) {
            "en" -> "kn"
            "kn" -> "hi"
            else -> "en"
        }
        setLanguage(next)
    }

    fun toggleLanguage() {
        cycleLanguage()
    }

    fun setLanguage(code: String) {
        viewModelScope.launch { settingsManager.saveLanguageCode(code) }
    }

    fun updateMarketLocation(location: String) {
        viewModelScope.launch { settingsManager.saveLocation(location) }
    }

    fun savePrices(maize: Float, cottonseed: Float, bran: Float, market: Float) {
        marketLastUpdatedAt = System.currentTimeMillis()
        lastSettingsMessage = "Market prices updated for $marketLocation"
        viewModelScope.launch {
            settingsManager.savePrices(maize, cottonseed, bran, market)
        }
    }

    fun clearPendingLogin() {
        pendingLoginUsername = ""
        pendingLoginPassword = ""
    }

    fun resetCalculatorFlow() {
        selectedBreed = ""
        cowName = ""
        weight = 0.0
        ageYears = 4.0
        milkYield = 0.0
        calculatorStep = 0
        selectedAnimalType = AnimalType.COW
        selectedGoal = FarmerGoal.REDUCE_COST
        useInventoryOnly = false
        maizeInventoryInput = ""
        cottonseedInventoryInput = ""
        branInventoryInput = ""
        feedResult = null
        guidedRecommendations.clear()
    }

    fun addAnimalProfile(name: String, breed: String, animalType: AnimalType, weight: Double, milkProduction: Double, notes: String = "") {
        if (loggedInUserId == -1) return
        viewModelScope.launch {
            animalProfileDao.upsertProfile(
                AnimalProfile(
                    userId = loggedInUserId,
                    name = name,
                    animalType = animalType.name,
                    breed = breed,
                    weight = weight,
                    currentMilkProduction = milkProduction,
                    notes = notes
                )
            )
        }
    }

    fun saveInventory(ingredient: String, quantityKg: Double) {
        if (loggedInUserId == -1) return
        viewModelScope.launch {
            inventoryDao.upsertInventoryItem(
                InventoryItem(
                    userId = loggedInUserId,
                    ingredient = ingredient,
                    quantityKg = quantityKg
                )
            )
        }
    }

    fun saveMarketAlert(ingredient: String, thresholdPrice: Float) {
        if (loggedInUserId == -1) return
        viewModelScope.launch {
            marketAlertDao.upsertAlert(
                MarketAlert(
                    userId = loggedInUserId,
                    ingredient = ingredient,
                    thresholdPrice = thresholdPrice
                )
            )
        }
    }

    fun generateGuidedRecommendations() {
        guidedRecommendations.clear()
        guidedRecommendations += when (selectedGoal) {
            FarmerGoal.REDUCE_COST -> "1. Compare homemade cost per kg with market feed."
            FarmerGoal.INCREASE_MILK -> "1. Increase protein-rich ingredients for better production."
            FarmerGoal.USE_AVAILABLE_INGREDIENTS -> "1. Start with ingredients already in inventory."
        }
        guidedRecommendations += "2. Check the nutrition panel before finalizing."
        guidedRecommendations += "3. Save the recipe and monitor milk production in herd history."
    }

    fun syncCalculatorInputs(
        breed: String,
        weight: Double,
        ageYears: Double,
        milkYield: Double,
        maizePrice: Float,
        cottonseedPrice: Float,
        branPrice: Float
    ) {
        selectedBreed = breed
        this.weight = weight
        this.ageYears = ageYears
        this.milkYield = milkYield
        priceMaize = maizePrice
        priceCottonseed = cottonseedPrice
        priceBran = branPrice
    }

    fun goToCalculatorStep(step: Int) {
        calculatorStep = step.coerceIn(0, 3)
    }

    fun buildSmartFeedPlan(addToHistory: Boolean = true) {
        generateGuidedRecommendations()
        val result = FeedCalculator.generateSmartFeed(
            breed = selectedBreed.ifBlank { "Jersey" },
            animalType = selectedAnimalType,
            weight = weight,
            milkYield = milkYield,
            ageYears = ageYears,
            goal = selectedGoal,
            priceMaize = priceMaize,
            priceCottonseed = priceCottonseed,
            priceBran = priceBran,
            priceMarketFeed = priceMarket,
            availableMaizeKg = maizeInventoryInput.toDoubleOrNull(),
            availableCottonseedKg = cottonseedInventoryInput.toDoubleOrNull(),
            availableBranKg = branInventoryInput.toDoubleOrNull()
        )
        feedResult = result
        shareText = buildString {
            appendLine("Pashu-Aahar Smart Feed Plan")
            appendLine("Animal: ${selectedAnimalType.name.lowercase().replaceFirstChar { it.uppercase() }}")
            appendLine("Homemade cost/kg: Rs ${result.homemadeCostPerKg}")
            appendLine("Market cost/kg: Rs ${result.marketCostPerKg}")
            appendLine(if (result.dailySavings >= 0) "Saved Rs ${result.dailySavings} daily" else "Extra Rs ${-result.dailySavings} daily")
        }
        reportPreview = buildString {
            appendLine("Smart Cattle Feed Report")
            appendLine("Farmer: $currentUsername")
            appendLine("Location: $marketLocation")
            appendLine("Goal: ${selectedGoal.name}")
            appendLine("Protein: ${result.proteinPercent}%")
            appendLine("Energy: ${result.energyValueMcal} Mcal")
            appendLine("Fiber: ${result.fiberPercent}%")
            appendLine("Daily savings: Rs ${result.dailySavings}")
            appendLine("Weekly savings: Rs ${result.weeklySavings}")
            appendLine("Monthly savings: Rs ${result.monthlySavings}")
            appendLine("Yearly savings: Rs ${result.yearlySavings}")
        }
        if (addToHistory && loggedInUserId != -1) {
            saveFeedHistory(
                com.pashuaahar.data.db.FeedHistory(
                    userId = loggedInUserId,
                    cowName = cowName.ifBlank { "${selectedAnimalType.name.lowercase().replaceFirstChar { it.uppercase() }} profile" },
                    breed = selectedBreed.ifBlank { selectedAnimalType.name.lowercase().replaceFirstChar { it.uppercase() } },
                    animalType = selectedAnimalType.name,
                    goalMode = selectedGoal.name,
                    location = marketLocation,
                    weight = weight,
                    milkYield = milkYield,
                    maizeKg = result.maizeKg,
                    cottonseedKg = result.cottonseedCakeKg,
                    branKg = result.wheatBranKg,
                    proteinPercent = result.proteinPercent,
                    energyValueMcal = result.energyValueMcal,
                    fiberPercent = result.fiberPercent,
                    savingsRupees = result.savingsRupees,
                    totalCostRupees = result.homemadeCostRupees,
                    marketCostRupees = result.marketCostRupees,
                    dailySavings = result.dailySavings,
                    monthlySavings = result.monthlySavings,
                    yearlySavings = result.yearlySavings
                )
            )
        }
    }

    fun toggleFavoriteCurrentRecipe() {
        val current = feedResult ?: return
        if (loggedInUserId == -1) return
        viewModelScope.launch {
            favoriteRecipeDao.insertFavorite(
                FavoriteRecipe(
                    userId = loggedInUserId,
                    title = "${selectedAnimalType.name.lowercase().replaceFirstChar { it.uppercase() }} ${selectedGoal.name.lowercase()} mix",
                    summary = "Protein ${current.proteinPercent}% | Saved Rs ${current.dailySavings}/day",
                    animalType = selectedAnimalType.name,
                    goalMode = selectedGoal.name
                )
            )
        }
    }

    fun saveFeedHistory(history: com.pashuaahar.data.db.FeedHistory) {
        viewModelScope.launch {
            historyDao.insertHistory(history)
        }
    }

    fun refreshMarketAnalysis() {
        marketLastUpdatedAt = System.currentTimeMillis()
        marketPrices.clear()
        val items = listOf(
            MarketPriceInfo("Maize", marketLocation, priceMaize, priceMaize - 1.5f, marketLastUpdatedAt, if (priceMaize <= 26f) "down" else "up"),
            MarketPriceInfo("Cottonseed Cake", marketLocation, priceCottonseed, priceCottonseed - 0.8f, marketLastUpdatedAt, if (priceCottonseed <= 39f) "down" else "up"),
            MarketPriceInfo("Wheat Bran", marketLocation, priceBran, priceBran + 1.1f, marketLastUpdatedAt, if (priceBran <= 20f) "down" else "up"),
            MarketPriceInfo("Commercial Feed", marketLocation, priceMarket, priceMarket + 1.8f, marketLastUpdatedAt, if (priceMarket <= 35f) "down" else "up")
        )
        marketPrices.addAll(items)
        pricePredictions.clear()
        pricePredictions.addAll(
            listOf(
                PricePrediction("Maize", if (priceMaize < 26) "Likely stable to slightly higher next week." else "Likely cool off if supply improves."),
                PricePrediction("Cottonseed Cake", if (priceCottonseed > 42) "Expect dealers to hold firm on price." else "May dip if arrivals increase."),
                PricePrediction("Wheat Bran", if (priceBran < 19) "Good time to stock inventory." else "Mild upward pressure expected.")
            )
        )
    }

    fun savedAmountText(value: Double): String {
        return if (value >= 0) "Saved Rs ${"%.2f".format(value)}" else "Extra Rs ${"%.2f".format(-value)}"
    }

    fun getLanguage(): AppLanguage = when (languageCode) {
        "kn" -> AppLanguage.KANNADA
        "hi" -> AppLanguage.HINDI
        else -> AppLanguage.ENGLISH
    }

    fun generateWhatsAppRecipeText(): String = shareText.ifBlank { "No recipe generated yet." }

    fun generatePdfLikeReport(): String = reportPreview.ifBlank { "No report available yet." }
}
