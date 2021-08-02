package devmozz.foodling.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import devmozz.foodling.data.local.FoodlingRecipesEntity
import devmozz.foodling.models.FoodRecipe
import devmozz.foodling.util.NetworkResult

class FoodlingRecipesBinding {

    companion object {

        @BindingAdapter("readApiResponseImageView", "readDatabaseImageView", requireAll = true)
        @JvmStatic
        fun errorImageViewVisibility(
            imageView: ImageView,
            apiResponse: NetworkResult<FoodRecipe>?,
            database: List<FoodlingRecipesEntity>?
        ) {
            when {
                apiResponse is NetworkResult.Error && database.isNullOrEmpty() -> {
                    imageView.visibility = View.VISIBLE
                }
                apiResponse is NetworkResult.Loading -> {
                    imageView.visibility = View.INVISIBLE
                }
                apiResponse is NetworkResult.Success -> {
                    imageView.visibility = View.INVISIBLE
                }
            }
        }

        @BindingAdapter("readApiResponseTextView", "readDatabaseTextView", requireAll = true)
        @JvmStatic
        fun errorTextViewVisibility(
            textView: TextView,
            apiResponse: NetworkResult<FoodRecipe>?,
            database: List<FoodlingRecipesEntity>?
        ) {
            when {
                apiResponse is NetworkResult.Error && database.isNullOrEmpty() -> {
                    textView.visibility = View.VISIBLE
                    textView.text = apiResponse.message.toString()
                }
                apiResponse is NetworkResult.Loading -> {
                    textView.visibility = View.INVISIBLE
                }
                apiResponse is NetworkResult.Success -> {
                    textView.visibility = View.INVISIBLE
                }
            }
        }

    }
}