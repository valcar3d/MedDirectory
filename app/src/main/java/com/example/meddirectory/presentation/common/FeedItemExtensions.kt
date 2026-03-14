package com.example.meddirectory.presentation.common

import androidx.compose.ui.graphics.Color
import com.example.meddirectory.data.constants.ApiConstants
import com.example.meddirectory.domain.model.FeedItem

fun FeedItem.formattedName(): String =
    if (suffix != null) "$firstName $lastName, $suffix" else "$firstName $lastName"

fun FeedItem.getAvatarUrl(): String {
    val seed = "${firstName}_${lastName}"
        .replace(" ", "_")
        .replace(",", "")
        .replace(".", "")
    return "${ApiConstants.DICEBEAR_BASE_URL}${ApiConstants.DICEBEAR_STYLE}/${ApiConstants.DICEBEAR_FORMAT}?seed=$seed&backgroundColor=e0e0e0"
}

fun String.parseSalaryRange(): Pair<Int, Int>? {
    return try {
        val cleaned = this.replace("$", "").replace(",", "")
        val parts = cleaned.split(" - ")
        if (parts.size == 2) {
            val min = parts[0].trim().toInt()
            val max = parts[1].trim().toInt()
            Pair(min, max)
        } else {
            null
        }
    } catch (e: Exception) {
        null
    }
}

data class SalaryStats(
    val min: Int,
    val max: Int,
    val range: Int
) {
    companion object {
        fun fromItems(items: List<FeedItem>): SalaryStats? {
            val ranges = items.mapNotNull { it.salaryRange?.parseSalaryRange() }
            if (ranges.isEmpty()) return null
            
            val allValues = ranges.flatMap { listOf(it.first, it.second) }
            val min = allValues.minOrNull() ?: 0
            val max = allValues.maxOrNull() ?: 0
            
            return SalaryStats(min, max, max - min)
        }
    }
}

fun FeedItem.getSalaryPercentage(stats: SalaryStats): Int {
    val range = salaryRange?.parseSalaryRange() ?: return 0
    val maxSalary = range.second
    
    return if (stats.range > 0) {
        ((maxSalary - stats.min).toFloat() / stats.range * 100).toInt().coerceIn(0, 100)
    } else {
        0
    }
}

fun Int.getSalaryColor() = when {
    this >= 75 -> Color.Green
    this >= 25 -> Color.Yellow
    else -> Color.Red
}
