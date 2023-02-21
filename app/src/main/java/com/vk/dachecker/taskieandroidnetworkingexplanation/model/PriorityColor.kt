package com.vk.dachecker.taskieandroidnetworkingexplanation.model

import com.vk.dachecker.taskieandroidnetworkingexplanation.R

enum class PriorityColor {

    LOW, MEDIUM, HIGH;

    fun getColor() = when (this) {
        LOW -> R.color.priorityLow
        MEDIUM -> R.color.priorityMedium
        HIGH -> R.color.priorityHigh
    }
}