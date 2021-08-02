package devmozz.foodling.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import devmozz.foodling.data.Repository
import devmozz.foodling.data.local.FoodlingRecipesEntity
import devmozz.foodling.models.FoodRecipe
import devmozz.foodling.util.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    /** Local **/
    val readFoodlingRecipes: LiveData<List<FoodlingRecipesEntity>> =
        repository.local.readFoodlingDatabase().asLiveData()

    private fun insertFoodlingRecipes(foodlingRecipesEntity: FoodlingRecipesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFoodlingRecipes(foodlingRecipesEntity)
        }

    /** Remote **/
    var foodlingRecipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> =
        MutableLiveData()

    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesCall(queries)
    }

    private suspend fun getRecipesCall(queries: Map<String, String>) {
        foodlingRecipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getRecipes(queries)
                foodlingRecipesResponse.value = handleFoodlingRecipesResponse(response)

                val foodlingRecipe = foodlingRecipesResponse.value!!.data
                foodlingRecipe?.let { offlineCacheFoodlingRecipes(it) }

            } catch (e: Exception) {
                foodlingRecipesResponse.value = NetworkResult.Error("Foodling Recipes not found.")
            }
        } else {
            foodlingRecipesResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private fun offlineCacheFoodlingRecipes(foodlingRecipe: FoodRecipe) {
        val recipesEntity = FoodlingRecipesEntity(foodlingRecipe)
        insertFoodlingRecipes(recipesEntity)
    }

    private fun handleFoodlingRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe> {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited. Check API Key")
            }
            response.body()!!.results.isNullOrEmpty() -> {
                return NetworkResult.Error("Foodling Recipes not found.")
            }
            response.isSuccessful -> {
                val foodRecipes = response.body()
                return NetworkResult.Success(foodRecipes!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

}