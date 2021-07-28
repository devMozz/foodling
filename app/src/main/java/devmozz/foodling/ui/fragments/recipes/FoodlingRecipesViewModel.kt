package devmozz.foodling.ui.fragments.recipes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import devmozz.foodling.util.Constants.Companion.API_KEY
import devmozz.foodling.util.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import devmozz.foodling.util.Constants.Companion.QUERY_API_KEY
import devmozz.foodling.util.Constants.Companion.QUERY_DIET
import devmozz.foodling.util.Constants.Companion.QUERY_FILL_INGREDIENTS
import devmozz.foodling.util.Constants.Companion.QUERY_NUMBER
import devmozz.foodling.util.Constants.Companion.QUERY_TYPE

class FoodlingRecipesViewModel(application: Application) : AndroidViewModel(application) {

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = hashMapOf()

        queries[QUERY_NUMBER] = "50"
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_TYPE] = "snack"
        queries[QUERY_DIET] = "vegan"
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }

}