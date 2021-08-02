package devmozz.foodling.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import devmozz.foodling.data.local.FoodlingRecipesDatabase
import devmozz.foodling.util.Constants.Companion.DATABASE_NAME
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideFoodlingDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        FoodlingRecipesDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideFoodlingRecipesDao(database: FoodlingRecipesDatabase) = database.foodlingRecipesDao()

}