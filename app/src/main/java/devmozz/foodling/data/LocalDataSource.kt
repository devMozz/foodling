package devmozz.foodling.data

import devmozz.foodling.data.local.FoodlingRecipesDao
import devmozz.foodling.data.local.FoodlingRecipesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipesDao: FoodlingRecipesDao
) {

    fun readFoodlingDatabase(): Flow<List<FoodlingRecipesEntity>> =
        recipesDao.readFoodlingRecipes()

    suspend fun insertFoodlingRecipes(recipesEntity: FoodlingRecipesEntity) =
        recipesDao.insertFoodlingRecipes(recipesEntity)

}
