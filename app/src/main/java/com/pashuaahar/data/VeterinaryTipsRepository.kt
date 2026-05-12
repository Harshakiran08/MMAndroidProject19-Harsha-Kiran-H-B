package com.pashuaahar.data

import com.pashuaahar.model.TipCategory
import com.pashuaahar.model.VeterinaryTip

object VeterinaryTipsRepository {
    fun getTips(): List<VeterinaryTip> = listOf(
        VeterinaryTip(
            id = 1,
            title = "Balance dry matter with milk output",
            description = "Increase the concentrate share gradually as milk yield rises, and always keep roughage available to protect rumen health.",
            category = TipCategory.NUTRITION,
            videoLabel = "2 min nutrition guide",
            videoUrl = "https://www.youtube.com/watch?v=89EGJnYiFmI"
        ),
        VeterinaryTip(
            id = 2,
            title = "Offer clean water before feed",
            description = "Cows usually eat better and maintain milk production when fresh water is available before each feeding round.",
            category = TipCategory.NUTRITION,
            videoLabel = "Water routine clip",
            videoUrl = "https://www.youtube.com/watch?v=DeB0etrBCd8"
        ),
        VeterinaryTip(
            id = 3,
            title = "Clean feed troughs daily",
            description = "Remove leftover wet feed and wash troughs to reduce fungal growth, insects, and appetite loss.",
            category = TipCategory.HYGIENE,
            videoLabel = "Daily hygiene checklist",
            videoUrl = "https://www.youtube.com/watch?v=9Nfs34kmEds"
        ),
        VeterinaryTip(
            id = 4,
            title = "Keep the udder and shed dry",
            description = "A dry resting area lowers infection risk and helps cows stay comfortable during milking and feeding.",
            category = TipCategory.HYGIENE,
            videoLabel = "Shed care basics",
            videoUrl = "https://www.youtube.com/watch?v=w3fIUyA4FaE"
        ),
        VeterinaryTip(
            id = 5,
            title = "Store maize away from moisture",
            description = "Use raised platforms, keep sacks ventilated, and avoid direct wall contact to reduce spoilage losses.",
            category = TipCategory.STORAGE,
            videoLabel = "Storage setup demo",
            videoUrl = "https://www.youtube.com/watch?v=LQPczWondWc"
        ),
        VeterinaryTip(
            id = 6,
            title = "Rotate bran and cake stock",
            description = "Use older stock first and label purchase dates so nutrient-rich ingredients stay fresh and safe.",
            category = TipCategory.STORAGE,
            videoLabel = "Feed stock rotation",
            videoUrl = "https://www.youtube.com/watch?v=EjHxIioVukU"
        )
    )
}
