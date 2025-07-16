package io.github.warforged5.mashkmp.dataclasses

import io.github.warforged5.mashkmp.enumclasses.MashType
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.serialization.Serializable

@Serializable
data class MashTemplate @OptIn(ExperimentalUuidApi::class, ExperimentalTime::class) constructor(
    val id: String = Uuid.random().toString(),
    val name: String,
    val categories: List<CategoryData>, // Updated to use CategoryData
    val type: MashType,
    val timestamp: Long = Clock.System.now().toEpochMilliseconds()
)