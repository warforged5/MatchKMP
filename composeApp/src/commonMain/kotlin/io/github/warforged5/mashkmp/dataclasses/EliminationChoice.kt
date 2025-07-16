package io.github.warforged5.mashkmp.dataclasses

data class EliminationChoice(
    val categoryName: String,
    val choiceIndex: Int,
    val choiceText: String,
    val isEliminated: Boolean = false,
    val isFinal: Boolean = false
)