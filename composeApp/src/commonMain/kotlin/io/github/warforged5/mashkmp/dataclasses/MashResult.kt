package io.github.warforged5.mashkmp.dataclasses

import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.serialization.Serializable

@Serializable
data class MashResult @OptIn(ExperimentalUuidApi::class, ExperimentalTime::class) constructor(
    val id: String = Uuid.random().toString(),
    val template: MashTemplate,
    val allChoices: Map<String, List<String>>, // All user inputs
    val selections: Map<String, String>, // Final selections
    val story: String? = null,
    val spiralCount: Int? = null, // Number of counts for spiral elimination
    val timestamp: Long = Clock.System.now().toEpochMilliseconds()
)