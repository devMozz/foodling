package devmozz.foodling.ui.fragments.recipes

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import devmozz.foodling.R
import devmozz.foodling.databinding.FragmentFoodlingRecipesBinding
import devmozz.foodling.ui.adapters.FoodlingRecipesAdapter
import devmozz.foodling.util.NetworkResult
import devmozz.foodling.util.observeOnce
import devmozz.foodling.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_foodling_recipes.view.*
import kotlinx.coroutines.launch


@AndroidEntryPoint
class FoodlingRecipesFragment : Fragment() {

    private var _binding: FragmentFoodlingRecipesBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainViewModel: MainViewModel
    private lateinit var foodlingRecipesViewModel: FoodlingRecipesViewModel
    private val mAdapter by lazy { FoodlingRecipesAdapter() }

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
        _binding = FragmentFoodlingRecipesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        setupRecyclerView()
        readFoodlingDatabase()

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun readFoodlingDatabase() {
        lifecycleScope.launch {
            with(mainViewModel) {
                readFoodlingRecipes.observeOnce(viewLifecycleOwner, { database ->
                    when {
                        database.isNotEmpty() -> {
                            Log.d("FoodlingRecipesFragment", "readDatabase called!")
                            mAdapter.setData(database[0].foodlingRecipes)
                            hideShimmer()
                        }
                        else -> {
                            requestData()
                        }
                    }
                })
            }
        }
    }

    @SuppressLint("ShowToast")
    private fun requestData() {
        Log.d("FoodlingRecipesFragment", "requestData called!")
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
                    loadDataFromCache()
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

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            mainViewModel.readFoodlingRecipes.observe(viewLifecycleOwner, { database ->
                when {
                    database.isNotEmpty() -> mAdapter.setData(database[0].foodlingRecipes)
                }
            })
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.adapter = mAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        showShimmer()
    }

    private fun showShimmer() {
        binding.recyclerView.showShimmer()
    }

    private fun hideShimmer() {
        binding.recyclerView.hideShimmer()
    }

}
