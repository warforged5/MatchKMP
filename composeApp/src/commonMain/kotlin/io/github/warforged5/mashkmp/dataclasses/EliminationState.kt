package io.github.warforged5.mashkmp.dataclasses

data class EliminationState(
    val allChoices: List<EliminationChoice>,
    val currentCountingIndex: Int = -1,
    val isCountingActive: Boolean = false,
    val currentRound: Int = 0,
    val eliminationsComplete: Boolean = false
)
