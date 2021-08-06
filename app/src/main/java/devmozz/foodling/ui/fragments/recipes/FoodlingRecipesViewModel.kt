package devmozz.foodling.ui.fragments.recipes

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import devmozz.foodling.data.DataStoreRepository
import devmozz.foodling.util.Constants.Companion.API_KEY
import devmozz.foodling.util.Constants.Companion.DEFAULT_DIET_TYPE
import devmozz.foodling.util.Constants.Companion.DEFAULT_FOODLING_RECIPES_NUMBER
import devmozz.foodling.util.Constants.Companion.DEFAULT_MEAL_TYPE
import devmozz.foodling.util.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import devmozz.foodling.util.Constants.Companion.QUERY_API_KEY
import devmozz.foodling.util.Constants.Companion.QUERY_DIET
import devmozz.foodling.util.Constants.Companion.QUERY_FILL_INGREDIENTS
import devmozz.foodling.util.Constants.Companion.QUERY_NUMBER
import devmozz.foodling.util.Constants.Companion.QUERY_TYPE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class FoodlingRecipesViewModel @Inject constructor(
    application: Application, private val dataStoreRepository: DataStoreRepository,
) : AndroidViewModel(application) {

    var networkStatus = false
    var comeBackOnline = false

    private var mealType = DEFAULT_MEAL_TYPE
    private var dietType = DEFAULT_DIET_TYPE

    var readComeBackOnline = dataStoreRepository.readComaBackOnline.asLiveData()
    val readMealTypeAndDietType = dataStoreRepository.readMealTypeAndDietType

    fun saveMealTypeAndDietType(
        mealType: String,
        mealTypeId: Int,
        dietType: String,
        dietTypeId: Int,
    ) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveMealTypeAndDietType(mealType, mealTypeId, dietType, dietTypeId)
        }

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = hashMapOf()

        viewModelScope.launch {
            readMealTypeAndDietType.collect {
                mealType = it.selectedMealType
                dietType = it.selectedDietType
            }
        }

        queries[QUERY_NUMBER] = DEFAULT_FOODLING_RECIPES_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_TYPE] = mealType
        queries[QUERY_DIET] = dietType
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }

    fun showNetworkConnectionStatus() {
        when {
            !networkStatus -> {
                Toast.makeText(getApplication(), "No Internet Connection.", Toast.LENGTH_SHORT).show()
                saveBackOnline(true)
            }
            networkStatus -> if (comeBackOnline) {
                Toast.makeText(getApplication(), "Come back online!", Toast.LENGTH_SHORT).show()
                saveBackOnline(false)
            }
        }
    }

    private fun saveBackOnline(comeBackOnline: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveComeBackOnline(comeBackOnline)
        }

}