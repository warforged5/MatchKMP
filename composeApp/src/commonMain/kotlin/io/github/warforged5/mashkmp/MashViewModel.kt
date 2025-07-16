package io.github.warforged5.mash

import androidx.compose.runtime.*
import io.github.warforged5.mashkmp.dataclasses.MashResult
import io.github.warforged5.mashkmp.dataclasses.MashTemplate
import android.content.Context
import android.content.SharedPreferences
import com.google.ai.client.generativeai.type.content
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dev.shreyaspatil.ai.client.generativeai.GenerativeModel
import dev.shreyaspatil.ai.client.generativeai.type.content
import io.github.warforged5.mash.BuildConfig.GEMINI_API_KEY
import io.github.warforged5.mashkmp.dataclasses.CategoryData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MashViewModel(private val context: Context) : androidx.lifecycle.ViewModel() {
    private val prefs: SharedPreferences = context.getSharedPreferences("mash_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    var tempTemplate: MashTemplate? = null

    // Gemini API
    private val geminiApiKey = GEMINI_API_KEY
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
        val json = gson.toJson(templates)
        prefs.edit().putString("templates", json).apply()
    }

    private fun loadTemplates() {
        val json = prefs.getString("templates", null) ?: return
        try {
            val type = object : TypeToken<List<MashTemplate>>() {}.type
            val loaded = gson.fromJson<List<MashTemplate>>(json, type)

            // Only load templates that have the new CategoryData structure
            val validTemplates = loaded.filter { template ->
                template.categories.isNotEmpty() &&
                        template.categories.all { it.realName.isNotEmpty() }
            }

            templates.clear()
            templates.addAll(validTemplates)
        } catch (e: Exception) {
            // If there's any parsing error, just clear the templates
            templates.clear()
        }
    }

    private fun saveHistory() {
        val json = gson.toJson(history.take(50))
        prefs.edit().putString("history", json).apply()
    }

    private fun loadHistory() {
        val json = prefs.getString("history", null) ?: return
        try {
            val type = object : TypeToken<List<MashResult>>() {}.type
            val loaded = gson.fromJson<List<MashResult>>(json, type)
            history.clear()
            history.addAll(loaded)
        } catch (e: Exception) {
            // If there's any parsing error, just clear the history
            history.clear()
        }
    }
}