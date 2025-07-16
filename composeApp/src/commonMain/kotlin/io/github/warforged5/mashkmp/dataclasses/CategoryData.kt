package io.github.warforged5.mashkmp.dataclasses

data class CategoryData(
    val realName: String,
    val nickname: String,
    val icon: String, // Emoji or other identifier
    val isClassic: Boolean = false
)
