package devmozz.foodling.util

class Constants {

    companion object {
        const val BASE_URL = "https://api.spoonacular.com"
        const val API_KEY = "1e2e23f50fd3413a92bab2b7825d52a7"

        // API Query Default Key
        const val QUERY_NUMBER = "number"
        const val QUERY_API_KEY = "apiKey"
        const val QUERY_TYPE = "type"
        const val QUERY_DIET = "diet"
        const val QUERY_ADD_RECIPE_INFORMATION = "addRecipeInformation"
        const val QUERY_FILL_INGREDIENTS = "fillIngredients"

        // ROOM Database
        const val DATABASE_NAME = "foodling_recipes_database"
        const val FOODLING_RECIPES_TABLE = "foodling_recipes_table"

        // Bottom Sheet and Preferences
        const val DEFAULT_FOODLING_RECIPES_NUMBER = "50"
        const val DEFAULT_MEAL_TYPE = "main course"
        const val DEFAULT_DIET_TYPE = "gluten free"

        const val PREFERENCES_NAME = "foodling_preferences"
        const val PREFERENCES_MEAL_TYPE = "mealType"
        const val PREFERENCES_MEAL_TYPE_ID = "mealTypeId"
        const val PREFERENCES_DIET_TYPE = "dietType"
        const val PREFERENCES_DIET_TYPE_ID = "dietTypeId"
    }

}