package devmozz.foodling.data

import devmozz.foodling.data.remote.FoodlingRecipesApi
import devmozz.foodling.models.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val foodlingRecipesApi: FoodlingRecipesApi
) {

    suspend fun getRecipes(queries: Map<String, String>): Response<FoodRecipe> =
        foodlingRecipesApi.getRecipes(queries)

}