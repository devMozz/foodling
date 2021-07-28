package devmozz.foodling.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import devmozz.foodling.databinding.RecipesRowLayoutBinding
import devmozz.foodling.models.FoodRecipe
import devmozz.foodling.models.Result
import devmozz.foodling.util.FoodlingRecipesDiffUtil

class FoodlingRecipesAdapter : RecyclerView.Adapter<FoodlingRecipesAdapter.MyViewHolder>() {

    private var foodlingRecipes = emptyList<Result>()

    class MyViewHolder(
        private val binding: RecipesRowLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(result: Result) {
            binding.result = result
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecipesRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentFoodlingRecipe = foodlingRecipes[position]
        holder.bind(currentFoodlingRecipe)
    }

    override fun getItemCount(): Int {
        return foodlingRecipes.size
    }

    fun setData(newData: FoodRecipe) {
        val foodlingRecipesDiffUtil = FoodlingRecipesDiffUtil(foodlingRecipes, newData.results)
        val diffUtilResult = DiffUtil.calculateDiff(foodlingRecipesDiffUtil)
        foodlingRecipes = newData.results
        diffUtilResult.dispatchUpdatesTo(this)
    }

}