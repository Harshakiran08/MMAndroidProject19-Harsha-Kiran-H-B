package com.pashuaahar.data

import android.content.Context
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pashuaahar.model.FeedResult

data class HistoryEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val cowName: String,
    val breed: String,
    val weight: Double,
    val milkYield: Double,
    val savingsRupees: Double,
    val marketCostRupees: Double, // Added this to fix the error
    val date: Long
)

class HistoryManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("PashuAaharHistory", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val HISTORY_KEY = "history_list"

    fun saveEntry(entry: HistoryEntry) {
        val historyList = getHistory().toMutableList()
        historyList.add(0, entry) // Add to top
        val json = gson.toJson(historyList)
        sharedPreferences.edit().putString(HISTORY_KEY, json).apply()
    }

    fun getHistory(): List<HistoryEntry> {
        val json = sharedPreferences.getString(HISTORY_KEY, null) ?: return emptyList()
        val type = object : TypeToken<List<HistoryEntry>>() {}.type
        return gson.fromJson(json, type)
    }
}
