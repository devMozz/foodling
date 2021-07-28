package devmozz.foodling.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import devmozz.foodling.models.FoodRecipe
import devmozz.foodling.util.Constants.Companion.FOODLING_RECIPES_TABLE

@Entity(tableName = FOODLING_RECIPES_TABLE)
data class FoodlingRecipesEntity(
    var foodlingRecipes: FoodRecipe
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}