package io.github.warforged5.mashkmp

import androidx.compose.runtime.*
import io.github.warforged5.mashkmp.dataclasses.MashResult
import io.github.warforged5.mashkmp.dataclasses.MashTemplate
import io.github.warforged5.mashkmp.platform.Settings
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.builtins.ListSerializer
import dev.shreyaspatil.ai.client.generativeai.GenerativeModel
import dev.shreyaspatil.ai.client.generativeai.type.content
import io.github.warforged5.mashkmp.dataclasses.CategoryData
import io.github.warforged5.mashkmp.dataclasses.PremadeTemplates
import io.github.warforged5.mashkmp.enumclasses.MashType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class MashViewModel(private val settings: Settings) : androidx.lifecycle.ViewModel() {
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    var tempTemplate: MashTemplate? = null

    // Gemini API
    private val geminiApiKey = ""

    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.5-flash",
        apiKey = geminiApiKey
    )

    // Updated classic categories with nicknames
    private val classicCategories = listOf(
        CategoryData("MASH (Housing)", "MASH", "🏠", true),
        CategoryData("Spouse", "People", "💏", true),
        CategoryData("Number of Kids", "Number", "👨‍👩‍👧‍👦", true),
        CategoryData("Car", "Vehicle", "🚗", true),
        CategoryData("Place to Live", "Places", "🏙️", true),
        CategoryData("Job", "Occupations", "🥼", true),
        CategoryData("Color", "Color", "🔴", true),
        CategoryData("Flavor", "Flavor", "🍦", true)
    )

    private val additionalCategories = listOf(
        CategoryData("Pet", "Pet", "🐕"),
        CategoryData("Vacation Spot", "Vacation Spot", "⛱️"),
        CategoryData("Hobby", "Hobby", "🪓"),
        CategoryData("Best Friend", "Best Friend", "🤗"),
        CategoryData("College/School", "College/School", "🏫"),
        CategoryData("Wedding Theme", "Wedding Theme", "👰"),
        CategoryData("First Date", "First Date", "🍵"),
        CategoryData("Honeymoon", "Honeymoon", "💘"),
        CategoryData("Salary", "Salary", "💸"),
        CategoryData("House Color", "House Color", "🔵"),
        CategoryData("Age When Married", "Age When Married", "🧓"),
        CategoryData("Anniversary Gift", "Anniversary Gift", "🎁"),
        CategoryData("Lucky Number", "Lucky Number", "🍀"),
        CategoryData("Dream Destination", "Dream Destination", "🏖️"),
        CategoryData("Favorite Food", "Favorite Food", "🌮"),
        CategoryData("Music Genre", "Music Genre", "🎸"),
        CategoryData("Weather", "Weather", "☁️"),
        CategoryData("Season", "Season", "🍂")
    )

    var templates = mutableStateListOf<MashTemplate>()
    var history = mutableStateListOf<MashResult>()

    init {
        loadTemplates()
        loadHistory()
        // Load premade templates if not already loaded
        loadPremadeTemplates()
    }

    fun getClassicCategories() = classicCategories

    fun getRandomCategories(count: Int, excludeClassic: Boolean = false): List<CategoryData> {
        val available = if (excludeClassic) {
            additionalCategories
        } else {
            additionalCategories + classicCategories
        }
        return available.shuffled().take(count)
    }

    fun getAllAvailableCategories(): List<CategoryData> {
        return classicCategories + additionalCategories
    }

    fun saveTemplate(template: MashTemplate) {
        templates.add(0, template)
        saveTemplates()
    }

    // NEW: Edit template function
    fun updateTemplate(updatedTemplate: MashTemplate) {
        val index = templates.indexOfFirst { it.id == updatedTemplate.id }
        if (index != -1) {
            templates[index] = updatedTemplate
            saveTemplates()
        }
    }

    // NEW: Get template by ID for editing
    fun getTemplateById(id: String): MashTemplate? {
        return templates.find { it.id == id }
    }

    fun deleteTemplate(template: MashTemplate) {
        templates.remove(template)
        saveTemplates()
    }

    fun saveResult(result: MashResult) {
        history.add(0, result)
        saveHistory()
    }

    fun deleteResult(result: MashResult) {
        history.remove(result)
        saveHistory()
    }

    suspend fun generateAIStory(selections: Map<String, String>): String = withContext(Dispatchers.IO) {
        try {
            val prompt = buildString {
                appendLine("Create a short, fun, and engaging story about someone's future based on these MASH game results:")
                appendLine()
                selections.forEach { (category, selection) ->
                    appendLine("$category: $selection")
                }
                appendLine()
                appendLine("Make the story creative, future tense, 2nd person, and about 2-3 paragraphs long. Include all the elements naturally in the narrative, don't make it overly positive.")
                appendLine("Do not mention the MASH game in your response")
            }

            val response = generativeModel.generateContent(
                content { text(prompt) }
            )

            response.text ?: generateFallbackStory(selections)
        } catch (e: Exception) {
            generateFallbackStory(selections)
        }
    }

    private fun generateFallbackStory(selections: Map<String, String>): String {
        val housing = selections["MASH (Housing)"] ?: "house"
        val spouse = selections["Spouse"] ?: "someone special"
        val kids = selections["Number of Kids"] ?: "some"
        val car = selections["Car"] ?: "car"
        val place = selections["Place to Live"] ?: "somewhere wonderful"
        val job = selections["Job"] ?: "dream job"

        return """
            🌟 Your Amazing Future Awaits! 🌟
            
            Picture this: You wake up each morning in your beautiful $housing, where sunlight streams through the windows and the aroma of fresh coffee fills the air. $spouse is already up, preparing breakfast and humming your favorite song.
            
            Your $kids kids burst into the kitchen, their laughter echoing through the halls. After a cheerful family breakfast, you hop into your $car and cruise through the scenic streets of $place, waving to neighbors who've become lifelong friends.
            
            At your job as a $job, you're living your passion and making a real difference. Every day brings new adventures and opportunities to grow. Evenings are spent with your loved ones, creating memories that will last a lifetime.
            
            This is your future - full of love, laughter, and endless possibilities! ✨
        """.trimIndent()
    }

    private fun saveTemplates() {
        try {
            val jsonString = json.encodeToString(ListSerializer(MashTemplate.serializer()), templates.toList())
            settings.putString("templates", jsonString)
        } catch (e: Exception) {
            // Handle serialization error
            println("Error saving templates: ${e.message}")
        }
    }

    private fun loadTemplates() {
        try {
            val jsonString = settings.getString("templates", null)
            if (jsonString != null) {
                val loaded = json.decodeFromString(ListSerializer(MashTemplate.serializer()), jsonString)

                // Only load templates that have the new CategoryData structure
                val validTemplates = loaded.filter { template ->
                    template.categories.isNotEmpty() &&
                            template.categories.all { it.realName.isNotEmpty() }
                }

                templates.clear()
                templates.addAll(validTemplates)
            }
        } catch (e: Exception) {
            // If there's any parsing error, just clear the templates
            templates.clear()
            println("Error loading templates: ${e.message}")
        }
    }

    private fun saveHistory() {
        try {
            val jsonString = json.encodeToString(ListSerializer(MashResult.serializer()), history.take(50))
            settings.putString("history", jsonString)
        } catch (e: Exception) {
            println("Error saving history: ${e.message}")
        }
    }

    private fun loadHistory() {
        try {
            val jsonString = settings.getString("history", null)
            if (jsonString != null) {
                val loaded = json.decodeFromString(ListSerializer(MashResult.serializer()), jsonString)
                history.clear()
                history.addAll(loaded)
            }
        } catch (e: Exception) {
            // If there's any parsing error, just clear the history
            history.clear()
            println("Error loading history: ${e.message}")
        }
    }

    @OptIn(ExperimentalTime::class)
    fun loadPremadeTemplates() {
        // Check if premade templates have been loaded before
        val hasLoadedPremades = settings.getString("premade_templates_loaded", null) != null

        if (!hasLoadedPremades) {
            // Load all premade templates
            PremadeTemplates.allPremadeTemplates.forEach { premadeTemplate ->
                // Create a copy with a unique ID for saving
                val templateToSave = premadeTemplate.template.copy(
                    id = "premade_${premadeTemplate.template.name.replace(" ", "_").lowercase()}_${Clock.System.now().toEpochMilliseconds()}"
                )
                templates.add(0, templateToSave)
            }

            // Save that we've loaded premade templates
            settings.putString("premade_templates_loaded", "true")
            saveTemplates()
        }
    }

    fun getPremadeTemplateByName(name: String): MashTemplate? {
        return templates.find { it.name == name }
    }

    fun getTemplatesByType(type: MashType): List<MashTemplate> {
        return templates.filter { it.type == type }
    }

    fun searchTemplates(query: String): List<MashTemplate> {
        val lowercaseQuery = query.lowercase()
        return templates.filter { template ->
            template.name.lowercase().contains(lowercaseQuery) ||
                    template.categories.any { category ->
                        category.realName.lowercase().contains(lowercaseQuery) ||
                                category.nickname.lowercase().contains(lowercaseQuery)
                    }
        }
    }

    // Add a method to get featured templates
    fun getFeaturedTemplates(): List<MashTemplate> {
        val featuredNames = listOf(
            "First Date Fortune",
            "Career Destiny",
            "Dream Vacation",
            "College Experience",
            "Superhero Destiny"
        )

        return templates.filter { template ->
            featuredNames.contains(template.name)
        }.take(5)
    }

    // Add method to track template usage
    fun trackTemplateUsage(templateId: String) {
        val usageKey = "template_usage_$templateId"
        val currentUsage = settings.getString(usageKey, "0")?.toIntOrNull() ?: 0
        settings.putString(usageKey, (currentUsage + 1).toString())
    }

    // Get most used templates
    fun getMostUsedTemplates(limit: Int = 5): List<MashTemplate> {
        return templates.map { template ->
            val usageKey = "template_usage_${template.id}"
            val usage = settings.getString(usageKey, "0")?.toIntOrNull() ?: 0
            template to usage
        }
            .sortedByDescending { it.second }
            .take(limit)
            .map { it.first }
    }
}