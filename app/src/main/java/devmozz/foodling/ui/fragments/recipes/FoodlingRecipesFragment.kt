package devmozz.foodling.ui.fragments.recipes

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import devmozz.foodling.R
import devmozz.foodling.ui.adapters.FoodlingRecipesAdapter
import devmozz.foodling.util.Constants.Companion.API_KEY
import devmozz.foodling.util.NetworkResult
import devmozz.foodling.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_foodling_recipes.view.*


@AndroidEntryPoint
class FoodlingRecipesFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var foodlingRecipesViewModel: FoodlingRecipesViewModel
    private val mAdapter by lazy { FoodlingRecipesAdapter() }
    private lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        foodlingRecipesViewModel =
            ViewModelProvider(requireActivity()).get(FoodlingRecipesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mView = inflater.inflate(R.layout.fragment_foodling_recipes, container, false)

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        setupRecyclerView()
        requestData()

        return mView
    }

    @SuppressLint("ShowToast")
    private fun requestData() {
        mainViewModel.getRecipes(foodlingRecipesViewModel.applyQueries())
        mainViewModel.foodlingRecipesResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmer()
                    response.data?.let {
                        mAdapter.setData(it)
                    }
                }
                is NetworkResult.Error -> {
                    hideShimmer()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    )
                }
                is NetworkResult.Loading -> {
                    hideShimmer()
                }
            }
        })
    }

    private fun setupRecyclerView() {
        mView.shimmerRecyclerView.adapter = mAdapter
        mView.shimmerRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        showShimmer()
    }

    private fun showShimmer() {
        mView.shimmerRecyclerView.showShimmer()
    }

    private fun hideShimmer() {
        mView.shimmerRecyclerView.hideShimmer()
    }

}