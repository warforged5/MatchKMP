package io.github.warforged5.mashkmp.dataclasses

import io.github.warforged5.mashkmp.enumclasses.MashType

// Template categories for organization
enum class TemplateCategory {
    ROMANCE,
    CAREER,
    LIFESTYLE,
    ADVENTURE,
    FAMILY,
    EDUCATION,
    SEASONAL,
    FANTASY,
    CELEBRITY,
    RETIREMENT
}

data class PremadeTemplate(
    val template: MashTemplate,
    val category: TemplateCategory,
    val description: String,
    val tags: List<String>,
    val difficulty: Int // 1-3 (easy, medium, hard to fill out)
)

object PremadeTemplates {

    // Romance & Dating Templates
    val firstDateTemplate = MashTemplate(
        name = "First Date Fortune",
        categories = listOf(
            CategoryData("Dating Location", "Where", "📍", false),
            CategoryData("Type of Food", "Cuisine", "🍽️", false),
            CategoryData("Activity After Dinner", "Activity", "🎯", false),
            CategoryData("Conversation Topic", "Talk About", "💬", false),
            CategoryData("Who Pays", "Bill", "💳", false),
            CategoryData("Transportation", "Getting There", "🚗", false),
            CategoryData("Outfit Style", "Wearing", "👔", false),
            CategoryData("Date Outcome", "Ending", "💕", false)
        ),
        type = MashType.CUSTOM
    )

    val weddingPlannerTemplate = MashTemplate(
        name = "Dream Wedding",
        categories = listOf(
            CategoryData("Wedding Venue", "Venue", "💒", false),
            CategoryData("Season", "When", "📅", false),
            CategoryData("Color Theme", "Colors", "🎨", false),
            CategoryData("Number of Guests", "Guests", "👥", false),
            CategoryData("Honeymoon Destination", "Honeymoon", "✈️", false),
            CategoryData("Wedding Cake Flavor", "Cake", "🎂", false),
            CategoryData("First Dance Song Genre", "Music", "🎵", false),
            CategoryData("Wedding Budget", "Budget", "💰", false)
        ),
        type = MashType.CUSTOM
    )

    val valentinesTemplate = MashTemplate(
        name = "Valentine's Day Magic",
        categories = listOf(
            CategoryData("Valentine Gift", "Gift", "🎁", false),
            CategoryData("Romantic Dinner Location", "Dinner", "🕯️", false),
            CategoryData("Type of Flowers", "Flowers", "🌹", false),
            CategoryData("Love Song", "Song", "🎶", false),
            CategoryData("Romantic Activity", "Activity", "💏", false),
            CategoryData("Dessert", "Sweet Treat", "🍫", false)
        ),
        type = MashType.CUSTOM
    )

    // Career & Professional Templates
    val careerPathTemplate = MashTemplate(
        name = "Career Destiny",
        categories = listOf(
            CategoryData("Industry", "Field", "🏢", false),
            CategoryData("Job Title", "Position", "💼", false),
            CategoryData("Company Type", "Company", "🏛️", false),
            CategoryData("Work Location", "Office", "📍", false),
            CategoryData("Salary Range", "Salary", "💵", false),
            CategoryData("Work Schedule", "Hours", "⏰", false),
            CategoryData("Team Size", "Team", "👥", false),
            CategoryData("Career Achievement", "Success", "🏆", false)
        ),
        type = MashType.CUSTOM
    )

    val startupFounderTemplate = MashTemplate(
        name = "Startup Dreams",
        categories = listOf(
            CategoryData("Startup Industry", "Industry", "🚀", false),
            CategoryData("Product Type", "Product", "📱", false),
            CategoryData("Funding Amount", "Funding", "💸", false),
            CategoryData("Team Size", "Team", "👥", false),
            CategoryData("Office Location", "HQ", "🏙️", false),
            CategoryData("Exit Strategy", "Exit", "🎯", false),
            CategoryData("Company Culture", "Culture", "🎨", false),
            CategoryData("IPO Year", "IPO", "📈", false)
        ),
        type = MashType.CUSTOM
    )

    // Adventure & Travel Templates
    val vacationTemplate = MashTemplate(
        name = "Dream Vacation",
        categories = listOf(
            CategoryData("Destination Country", "Where", "🌍", false),
            CategoryData("Accommodation Type", "Stay", "🏨", false),
            CategoryData("Travel Companion", "With", "👫", false),
            CategoryData("Vacation Duration", "Days", "📅", false),
            CategoryData("Main Activity", "Do", "🏄", false),
            CategoryData("Transportation", "Travel By", "✈️", false),
            CategoryData("Souvenir", "Bring Back", "🎁", false),
            CategoryData("Budget", "Spend", "💳", false)
        ),
        type = MashType.CUSTOM
    )

    val gapYearTemplate = MashTemplate(
        name = "Gap Year Adventure",
        categories = listOf(
            CategoryData("First Country", "Start", "🗺️", false),
            CategoryData("Volunteer Work", "Help With", "🤝", false),
            CategoryData("Skill to Learn", "Learn", "📚", false),
            CategoryData("Adventure Sport", "Try", "🏔️", false),
            CategoryData("Cultural Experience", "Experience", "🎭", false),
            CategoryData("Travel Budget", "Budget", "💰", false),
            CategoryData("Travel Style", "Style", "🎒", false),
            CategoryData("Life-Changing Moment", "Moment", "✨", false)
        ),
        type = MashType.CUSTOM
    )

    // Lifestyle Templates
    val retirementTemplate = MashTemplate(
        name = "Golden Years",
        categories = listOf(
            CategoryData("Retirement Location", "Live", "🏖️", false),
            CategoryData("Retirement Age", "Retire At", "🎯", false),
            CategoryData("Main Hobby", "Hobby", "🎨", false),
            CategoryData("Retirement Home Style", "Home", "🏡", false),
            CategoryData("Travel Frequency", "Travel", "✈️", false),
            CategoryData("Volunteer Activity", "Give Back", "🤲", false),
            CategoryData("Retirement Income", "Income", "💰", false),
            CategoryData("Legacy Project", "Legacy", "🌟", false)
        ),
        type = MashType.CUSTOM
    )

    val healthWellnessTemplate = MashTemplate(
        name = "Wellness Journey",
        categories = listOf(
            CategoryData("Fitness Activity", "Exercise", "💪", false),
            CategoryData("Diet Style", "Eat", "🥗", false),
            CategoryData("Meditation Practice", "Mindfulness", "🧘", false),
            CategoryData("Sleep Hours", "Sleep", "😴", false),
            CategoryData("Wellness Goal", "Goal", "🎯", false),
            CategoryData("Stress Relief", "Relax", "🛀", false),
            CategoryData("Health Milestone", "Achieve", "🏃", false),
            CategoryData("Wellness Mentor", "Guide", "👨‍⚕️", false)
        ),
        type = MashType.CUSTOM
    )

    // Family & Kids Templates
    val familyLifeTemplate = MashTemplate(
        name = "Family Future",
        categories = listOf(
            CategoryData("Number of Children", "Kids", "👶", false),
            CategoryData("Family Pet", "Pet", "🐕", false),
            CategoryData("Family Vacation Spot", "Vacation", "🏖️", false),
            CategoryData("Family Tradition", "Tradition", "🎄", false),
            CategoryData("Kids' Activities", "Activities", "⚽", false),
            CategoryData("Family Car", "Vehicle", "🚗", false),
            CategoryData("Neighborhood Type", "Live", "🏘️", false),
            CategoryData("Family Dinner Night", "Dinner", "🍝", false)
        ),
        type = MashType.CUSTOM
    )

    val babyNamingTemplate = MashTemplate(
        name = "Baby's Destiny",
        categories = listOf(
            CategoryData("Baby's First Name", "Name", "👶", false),
            CategoryData("Birth Month", "Born", "📅", false),
            CategoryData("Baby's Personality", "Personality", "😊", false),
            CategoryData("First Word", "First Word", "💬", false),
            CategoryData("Favorite Toy", "Toy", "🧸", false),
            CategoryData("Future Talent", "Talent", "⭐", false),
            CategoryData("Childhood Nickname", "Nickname", "💕", false),
            CategoryData("Dream Career", "Grows Up", "🎯", false)
        ),
        type = MashType.CUSTOM
    )

    // Education Templates
    val collegeLifeTemplate = MashTemplate(
        name = "College Experience",
        categories = listOf(
            CategoryData("College/University", "School", "🎓", false),
            CategoryData("Major", "Study", "📚", false),
            CategoryData("Dorm or Housing", "Live", "🏫", false),
            CategoryData("Extracurricular", "Activity", "🏃", false),
            CategoryData("Study Abroad Location", "Abroad", "🌍", false),
            CategoryData("College Job", "Work", "💼", false),
            CategoryData("GPA Range", "GPA", "📊", false),
            CategoryData("Best College Memory", "Memory", "🎉", false)
        ),
        type = MashType.CUSTOM
    )

    val gradSchoolTemplate = MashTemplate(
        name = "Grad School Path",
        categories = listOf(
            CategoryData("Degree Type", "Degree", "🎓", false),
            CategoryData("Field of Study", "Field", "🔬", false),
            CategoryData("University", "School", "🏛️", false),
            CategoryData("Thesis Topic", "Research", "📝", false),
            CategoryData("Advisor Personality", "Advisor", "👨‍🏫", false),
            CategoryData("Funding Source", "Funding", "💰", false),
            CategoryData("Graduation Timeline", "Years", "⏰", false),
            CategoryData("Post-Grad Plan", "After", "🚀", false)
        ),
        type = MashType.CUSTOM
    )

    // Seasonal & Holiday Templates
    val summerTemplate = MashTemplate(
        name = "Summer Vibes",
        categories = listOf(
            CategoryData("Beach Destination", "Beach", "🏖️", false),
            CategoryData("Summer Job", "Work", "💼", false),
            CategoryData("BBQ Food", "Grill", "🍔", false),
            CategoryData("Summer Romance", "Meet", "💕", false),
            CategoryData("Outdoor Activity", "Activity", "🏃", false),
            CategoryData("Summer Drink", "Drink", "🍹", false),
            CategoryData("Music Festival", "Festival", "🎵", false),
            CategoryData("Tan Level", "Tan", "☀️", false)
        ),
        type = MashType.CUSTOM
    )

    val holidaySeasonTemplate = MashTemplate(
        name = "Holiday Magic",
        categories = listOf(
            CategoryData("Holiday Destination", "Travel To", "✈️", false),
            CategoryData("Gift to Receive", "Get", "🎁", false),
            CategoryData("Holiday Meal", "Feast", "🦃", false),
            CategoryData("Party Theme", "Party", "🎉", false),
            CategoryData("Holiday Movie", "Watch", "🎬", false),
            CategoryData("Decoration Style", "Decorate", "🎄", false),
            CategoryData("Holiday Tradition", "Tradition", "⭐", false),
            CategoryData("New Year Resolution", "Resolution", "🎯", false)
        ),
        type = MashType.CUSTOM
    )

    // Fantasy & Fun Templates
    val superpowerTemplate = MashTemplate(
        name = "Superhero Destiny",
        categories = listOf(
            CategoryData("Superpower", "Power", "⚡", false),
            CategoryData("Superhero Name", "Name", "🦸", false),
            CategoryData("Sidekick", "Partner", "👥", false),
            CategoryData("Nemesis Type", "Enemy", "👹", false),
            CategoryData("Secret Hideout", "Base", "🏰", false),
            CategoryData("Costume Color", "Suit", "🦸", false),
            CategoryData("Weakness", "Weakness", "💔", false),
            CategoryData("First Save", "Save", "🌟", false)
        ),
        type = MashType.CUSTOM
    )

    val fantasyAdventureTemplate = MashTemplate(
        name = "Fantasy Quest",
        categories = listOf(
            CategoryData("Fantasy Race", "You Are", "🧙", false),
            CategoryData("Quest Type", "Quest", "⚔️", false),
            CategoryData("Magical Item", "Weapon", "🗡️", false),
            CategoryData("Companion Creature", "Pet", "🐉", false),
            CategoryData("Kingdom to Save", "Save", "🏰", false),
            CategoryData("Magic School", "Learn At", "📚", false),
            CategoryData("Final Boss", "Defeat", "👾", false),
            CategoryData("Reward", "Win", "👑", false)
        ),
        type = MashType.CUSTOM
    )

    val zombieApocalypseTemplate = MashTemplate(
        name = "Zombie Survival",
        categories = listOf(
            CategoryData("Survival Location", "Hide", "🏚️", false),
            CategoryData("Weapon of Choice", "Weapon", "🔫", false),
            CategoryData("Survival Partner", "Team Up", "👥", false),
            CategoryData("Food Supply", "Eat", "🥫", false),
            CategoryData("Transportation", "Escape In", "🚗", false),
            CategoryData("Special Skill", "Skill", "💪", false),
            CategoryData("Zombie Type", "Fight", "🧟", false),
            CategoryData("Survival Duration", "Survive", "⏰", false)
        ),
        type = MashType.CUSTOM
    )

    // Celebrity & Fame Templates
    val famousLifeTemplate = MashTemplate(
        name = "Celebrity Life",
        categories = listOf(
            CategoryData("Famous For", "Known For", "⭐", false),
            CategoryData("Hollywood Home", "Live In", "🏡", false),
            CategoryData("Celebrity BFF", "Best Friend", "👯", false),
            CategoryData("Award to Win", "Win", "🏆", false),
            CategoryData("Scandal Type", "Drama", "📰", false),
            CategoryData("Red Carpet Look", "Wear", "👗", false),
            CategoryData("Net Worth", "Worth", "💰", false),
            CategoryData("Charity Cause", "Support", "❤️", false)
        ),
        type = MashType.CUSTOM
    )

    val musicStarTemplate = MashTemplate(
        name = "Music Star Journey",
        categories = listOf(
            CategoryData("Music Genre", "Genre", "🎵", false),
            CategoryData("Stage Name", "Name", "🎤", false),
            CategoryData("Record Label", "Label", "💿", false),
            CategoryData("Hit Song Title", "Hit Song", "🎶", false),
            CategoryData("Tour Locations", "Tour", "🌍", false),
            CategoryData("Collaboration Artist", "Collab", "🎸", false),
            CategoryData("Grammy Categories", "Grammy", "🏆", false),
            CategoryData("Music Video Theme", "Video", "🎬", false)
        ),
        type = MashType.CUSTOM
    )

    // Sports & Competition Templates
    val athleteTemplate = MashTemplate(
        name = "Athletic Glory",
        categories = listOf(
            CategoryData("Sport", "Play", "⚽", false),
            CategoryData("Team Name", "Team", "🏆", false),
            CategoryData("Jersey Number", "Number", "🔢", false),
            CategoryData("Training Location", "Train", "🏃", false),
            CategoryData("Coach Personality", "Coach", "👨‍🏫", false),
            CategoryData("Championship Won", "Win", "🥇", false),
            CategoryData("Sponsorship Deal", "Sponsor", "💰", false),
            CategoryData("Career Highlight", "Highlight", "⭐", false)
        ),
        type = MashType.CUSTOM
    )

    // Foodie Templates
    val foodieAdventureTemplate = MashTemplate(
        name = "Foodie Dreams",
        categories = listOf(
            CategoryData("Restaurant to Open", "Restaurant", "🍽️", false),
            CategoryData("Signature Dish", "Specialty", "👨‍🍳", false),
            CategoryData("Cuisine Type", "Cuisine", "🌍", false),
            CategoryData("Food Show to Host", "TV Show", "📺", false),
            CategoryData("Cookbook Theme", "Cookbook", "📚", false),
            CategoryData("Food Festival", "Festival", "🎪", false),
            CategoryData("Secret Ingredient", "Secret", "🌟", false),
            CategoryData("Michelin Stars", "Stars", "⭐", false)
        ),
        type = MashType.CUSTOM
    )

    // Pet Owner Template
    val petParentTemplate = MashTemplate(
        name = "Perfect Pet Life",
        categories = listOf(
            CategoryData("Pet Type", "Pet", "🐾", false),
            CategoryData("Pet Name", "Name", "💕", false),
            CategoryData("Pet Personality", "Acts Like", "😊", false),
            CategoryData("Favorite Toy", "Plays With", "🎾", false),
            CategoryData("Pet Trick", "Can Do", "🎪", false),
            CategoryData("Vet Visit Frequency", "Vet", "🏥", false),
            CategoryData("Pet Instagram Followers", "Famous", "📸", false),
            CategoryData("Pet's Favorite Treat", "Loves", "🦴", false)
        ),
        type = MashType.CUSTOM
    )

    // Birthday Party Template
    val birthdayBashTemplate = MashTemplate(
        name = "Birthday Spectacular",
        categories = listOf(
            CategoryData("Party Theme", "Theme", "🎉", false),
            CategoryData("Party Location", "Where", "📍", false),
            CategoryData("Number of Guests", "Guests", "👥", false),
            CategoryData("Birthday Cake Flavor", "Cake", "🎂", false),
            CategoryData("Surprise Element", "Surprise", "🎁", false),
            CategoryData("Entertainment", "Fun", "🎪", false),
            CategoryData("Party Favor", "Give Away", "🎀", false),
            CategoryData("Birthday Wish", "Wish For", "🌟", false)
        ),
        type = MashType.CUSTOM
    )

    // Tech Life Template
    val techLifeTemplate = MashTemplate(
        name = "Digital Destiny",
        categories = listOf(
            CategoryData("Tech Company", "Work At", "💻", false),
            CategoryData("Programming Language", "Code In", "⌨️", false),
            CategoryData("Side Project", "Build", "🚀", false),
            CategoryData("Tech Conference", "Speak At", "🎤", false),
            CategoryData("Open Source Project", "Contribute", "🌐", false),
            CategoryData("Tech Stack", "Use", "🔧", false),
            CategoryData("AI Assistant Name", "AI Friend", "🤖", false),
            CategoryData("Tech Achievement", "Achieve", "🏆", false)
        ),
        type = MashType.CUSTOM
    )

    val allPremadeTemplates = listOf(
        // Romance
        PremadeTemplate(
            firstDateTemplate,
            TemplateCategory.ROMANCE,
            "Discover how your perfect first date will unfold",
            listOf("dating", "romance", "love", "relationship"),
            1
        ),
        PremadeTemplate(
            weddingPlannerTemplate,
            TemplateCategory.ROMANCE,
            "Plan your dream wedding with cosmic guidance",
            listOf("wedding", "marriage", "romance", "celebration"),
            2
        ),
        PremadeTemplate(
            valentinesTemplate,
            TemplateCategory.ROMANCE,
            "Your Valentine's Day fortune awaits",
            listOf("valentine", "romance", "love", "holiday"),
            1
        ),

        // Career
        PremadeTemplate(
            careerPathTemplate,
            TemplateCategory.CAREER,
            "Unveil your professional destiny",
            listOf("career", "job", "work", "professional"),
            2
        ),
        PremadeTemplate(
            startupFounderTemplate,
            TemplateCategory.CAREER,
            "Your entrepreneurial journey revealed",
            listOf("startup", "business", "entrepreneur", "tech"),
            3
        ),

        // Adventure
        PremadeTemplate(
            vacationTemplate,
            TemplateCategory.ADVENTURE,
            "Discover your dream vacation destination",
            listOf("travel", "vacation", "adventure", "holiday"),
            1
        ),
        PremadeTemplate(
            gapYearTemplate,
            TemplateCategory.ADVENTURE,
            "Your gap year adventure awaits",
            listOf("travel", "gap year", "adventure", "explore"),
            2
        ),

        // Lifestyle
        PremadeTemplate(
            retirementTemplate,
            TemplateCategory.LIFESTYLE,
            "Preview your golden years",
            listOf("retirement", "future", "lifestyle", "senior"),
            2
        ),
        PremadeTemplate(
            healthWellnessTemplate,
            TemplateCategory.LIFESTYLE,
            "Your wellness journey mapped out",
            listOf("health", "wellness", "fitness", "lifestyle"),
            1
        ),

        // Family
        PremadeTemplate(
            familyLifeTemplate,
            TemplateCategory.FAMILY,
            "Your future family life revealed",
            listOf("family", "kids", "parenting", "home"),
            2
        ),
        PremadeTemplate(
            babyNamingTemplate,
            TemplateCategory.FAMILY,
            "Discover your baby's destiny",
            listOf("baby", "parenting", "family", "names"),
            1
        ),
        PremadeTemplate(
            petParentTemplate,
            TemplateCategory.FAMILY,
            "Your perfect pet companion awaits",
            listOf("pet", "animal", "family", "companion"),
            1
        ),

        // Education
        PremadeTemplate(
            collegeLifeTemplate,
            TemplateCategory.EDUCATION,
            "Your college experience predicted",
            listOf("college", "university", "education", "student"),
            2
        ),
        PremadeTemplate(
            gradSchoolTemplate,
            TemplateCategory.EDUCATION,
            "Your graduate school journey",
            listOf("grad school", "masters", "phd", "education"),
            3
        ),

        // Seasonal
        PremadeTemplate(
            summerTemplate,
            TemplateCategory.SEASONAL,
            "Your perfect summer awaits",
            listOf("summer", "vacation", "seasonal", "beach"),
            1
        ),
        PremadeTemplate(
            holidaySeasonTemplate,
            TemplateCategory.SEASONAL,
            "Holiday magic and festivities",
            listOf("holiday", "christmas", "seasonal", "celebration"),
            1
        ),
        PremadeTemplate(
            birthdayBashTemplate,
            TemplateCategory.SEASONAL,
            "Your ultimate birthday celebration",
            listOf("birthday", "party", "celebration", "fun"),
            1
        ),

        // Fantasy
        PremadeTemplate(
            superpowerTemplate,
            TemplateCategory.FANTASY,
            "Discover your superhero destiny",
            listOf("superhero", "fantasy", "power", "fun"),
            1
        ),
        PremadeTemplate(
            fantasyAdventureTemplate,
            TemplateCategory.FANTASY,
            "Your epic fantasy quest awaits",
            listOf("fantasy", "adventure", "magic", "quest"),
            2
        ),
        PremadeTemplate(
            zombieApocalypseTemplate,
            TemplateCategory.FANTASY,
            "Survive the zombie apocalypse",
            listOf("zombie", "survival", "apocalypse", "horror"),
            2
        ),

        // Celebrity
        PremadeTemplate(
            famousLifeTemplate,
            TemplateCategory.CELEBRITY,
            "Live your celebrity dreams",
            listOf("celebrity", "fame", "hollywood", "star"),
            2
        ),
        PremadeTemplate(
            musicStarTemplate,
            TemplateCategory.CELEBRITY,
            "Your music stardom journey",
            listOf("music", "star", "celebrity", "artist"),
            2
        ),
        PremadeTemplate(
            athleteTemplate,
            TemplateCategory.CELEBRITY,
            "Your athletic glory awaits",
            listOf("sports", "athlete", "champion", "fitness"),
            2
        ),
        PremadeTemplate(
            foodieAdventureTemplate,
            TemplateCategory.CELEBRITY,
            "Your culinary empire begins",
            listOf("food", "chef", "restaurant", "cooking"),
            2
        ),
        PremadeTemplate(
            techLifeTemplate,
            TemplateCategory.CAREER,
            "Your tech future revealed",
            listOf("tech", "programming", "startup", "digital"),
            2
        )
    )

    fun getTemplatesByCategory(category: TemplateCategory): List<PremadeTemplate> {
        return allPremadeTemplates.filter { it.category == category }
    }

    fun searchTemplates(query: String): List<PremadeTemplate> {
        val lowercaseQuery = query.lowercase()
        return allPremadeTemplates.filter { template ->
            template.template.name.lowercase().contains(lowercaseQuery) ||
                    template.description.lowercase().contains(lowercaseQuery) ||
                    template.tags.any { it.lowercase().contains(lowercaseQuery) }
        }
    }

    fun getRecommendedTemplates(currentMonth: Int): List<PremadeTemplate> {
        return when (currentMonth) {
            1 -> listOf("Career Destiny", "Health Wellness", "Tech Life") // New Year
            2 -> listOf("Valentine's Day Magic", "First Date Fortune") // Valentine's
            6, 7, 8 -> listOf("Summer Vibes", "Dream Vacation", "Gap Year Adventure") // Summer
            10 -> listOf("Zombie Survival", "Superhero Destiny") // Halloween
            11, 12 -> listOf("Holiday Magic", "Family Future") // Holiday season
            else -> listOf("Career Destiny", "Dream Vacation", "Family Future")
        }.mapNotNull { name ->
            allPremadeTemplates.find { it.template.name == name }
        }
    }
}