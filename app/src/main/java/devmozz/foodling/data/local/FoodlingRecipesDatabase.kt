package devmozz.foodling.data.local

import androidx.room.Database
import androidx.room.TypeConverters

@Database(
    entities = [FoodlingRecipesEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(FoodlingRecipesTypeConverter::class)
abstract class FoodlingRecipesDatabase {

    abstract fun foodlingRecipesDao(): FoodlingRecipesDao

}