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
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import devmozz.foodling.R
import devmozz.foodling.databinding.FragmentFoodlingRecipesBinding
import devmozz.foodling.ui.adapters.FoodlingRecipesAdapter
import devmozz.foodling.util.NetworkListener
import devmozz.foodling.util.NetworkResult
import devmozz.foodling.util.observeOnce
import devmozz.foodling.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_foodling_recipes.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@AndroidEntryPoint
class FoodlingRecipesFragment : Fragment() {

    private var _binding: FragmentFoodlingRecipesBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainViewModel: MainViewModel
    private lateinit var foodlingRecipesViewModel: FoodlingRecipesViewModel
    private val mAdapter by lazy { FoodlingRecipesAdapter() }

    private lateinit var networkListener: NetworkListener

    private val args by navArgs<FoodlingRecipesFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        foodlingRecipesViewModel =
            ViewModelProvider(requireActivity()).get(FoodlingRecipesViewModel::class.java)
    }

    @InternalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoodlingRecipesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel

        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        setupRecyclerView()

        foodlingRecipesViewModel.readComeBackOnline.observe(viewLifecycleOwner, {
            foodlingRecipesViewModel.comeBackOnline = it
        })

        lifecycleScope.launchWhenStarted {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext()).collect {
                Log.d("FoodlingRecipesFragment(NetworkListener)", it.toString())
                foodlingRecipesViewModel.networkStatus = it
                foodlingRecipesViewModel.showNetworkConnectionStatus()
                readFoodlingDatabase()
            }
        }

        binding.fabFoodlingRecipes.setOnClickListener {
            when {
                foodlingRecipesViewModel.networkStatus -> findNavController().navigate(R.id.action_foodlingRecipesFragment_to_foodlingRecipesBottomSheet)
                else -> foodlingRecipesViewModel.showNetworkConnectionStatus()
            }
        }

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
                        database.isNotEmpty() && !args.backFromBottomSheetDialog -> {
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

