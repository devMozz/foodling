package devmozz.foodling.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodlingRecipesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodlingRecipes(foodlingRecipesEntity: FoodlingRecipesEntity)

    @Query("SELECT * FROM foodling_recipes_table ORDER BY id ASC")
    fun readFoodlingRecipes(): Flow<List<FoodlingRecipesEntity>>

}