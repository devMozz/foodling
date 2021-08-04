package devmozz.foodling.ui.fragments.recipes.bottom

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import devmozz.foodling.R
import devmozz.foodling.ui.fragments.recipes.FoodlingRecipesViewModel
import devmozz.foodling.util.Constants.Companion.DEFAULT_DIET_TYPE
import devmozz.foodling.util.Constants.Companion.DEFAULT_MEAL_TYPE
import kotlinx.android.synthetic.main.foodling_recipes_bottom_sheet.view.*
import java.util.*

class FoodlingRecipesBottomSheet : BottomSheetDialogFragment() {

    private lateinit var foodlingRecipesViewModel: FoodlingRecipesViewModel

    private var chipMealType = DEFAULT_MEAL_TYPE
    private var chipMealTypeId = 0
    private var chipDietType = DEFAULT_DIET_TYPE
    private var chipDietTypeId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        foodlingRecipesViewModel =
            ViewModelProvider(requireActivity()).get(FoodlingRecipesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.foodling_recipes_bottom_sheet, container, false)

        foodlingRecipesViewModel.readMealTypeAndDietType.asLiveData()
            .observe(viewLifecycleOwner, { value ->
                chipMealType = value.selectedMealType
                chipDietType = value.selectedDietType
                updateChip(value.selectedMealTypeId, view.chipGroup_meal_type)
                updateChip(value.selectedDietTypeId, view.chipGroup_diet_type)
            })

        view.chipGroup_meal_type.setOnCheckedChangeListener { group, selectedChipIndex ->
            val chip = group.findViewById<Chip>(selectedChipIndex)
            val selectedMealType = chip.text.toString().toLowerCase(Locale.ROOT)
            chipMealType = selectedMealType
            chipMealTypeId = selectedChipIndex
        }

        view.chipGroup_diet_type.setOnCheckedChangeListener { group, selectedChipIndex ->
            val chip = group.findViewById<Chip>(selectedChipIndex)
            val selectedDietType = chip.text.toString().toLowerCase(Locale.ROOT)
            chipDietType = selectedDietType
            chipDietTypeId = selectedChipIndex
        }

        view.btn_apply.setOnClickListener {
            foodlingRecipesViewModel.saveMealTypeAndDietType(
                chipMealType,
                chipMealTypeId,
                chipDietType,
                chipDietTypeId
            )

            val action =
                FoodlingRecipesBottomSheetDirections.actionFoodlingRecipesBottomSheetToFoodlingRecipesFragment(
                    true)

            findNavController().navigate(action)
        }

        return view
    }

    private fun updateChip(chipIndex: Int, chipGroup: ChipGroup) {
        when {
            chipIndex != 0 ->
                try {
                    chipGroup.findViewById<Chip>(chipIndex).isChecked = true
                } catch (e: Exception) {
                    Log.d("RecipesBottomSheet", e.message.toString())
                }
        }
    }
}