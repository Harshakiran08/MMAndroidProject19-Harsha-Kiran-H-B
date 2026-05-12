package com.pashuaahar.model

data class VeterinaryTip(
    val id: Int,
    val title: String,
    val description: String,
    val category: TipCategory,
    val videoLabel: String,
    val videoUrl: String
)

enum class TipCategory(val label: String) {
    NUTRITION("Nutrition"),
    HYGIENE("Hygiene"),
    STORAGE("Storage")
}
