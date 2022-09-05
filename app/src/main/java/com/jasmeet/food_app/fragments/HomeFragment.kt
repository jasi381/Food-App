package com.jasmeet.food_app.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.jasmeet.food_app.activities.CategoryMealsActivity
import com.jasmeet.food_app.activities.MainActivity
import com.jasmeet.food_app.activities.MealActivity
import com.jasmeet.food_app.adapter.CategoriesAdapter
import com.jasmeet.food_app.adapter.PopularAdapter
import com.jasmeet.food_app.bottomsheet.MealBottomSheetFragment
import com.jasmeet.food_app.databinding.FragmentHomeBinding
import com.jasmeet.food_app.pojo.Meal
import com.jasmeet.food_app.pojo.MealsByCategory
import com.jasmeet.food_app.viewModel.HomeViewModel


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel :HomeViewModel
    private lateinit var randomMeal:Meal
    private lateinit var popularItemsAdapter :PopularAdapter
    private lateinit var categoriesAdapter:CategoriesAdapter

    companion object{
        const val MEAL_ID = "package com.jasmeet.food_app.fragments.idMeal"
        const val MEAL_NAME = "package com.jasmeet.food_app.fragments.nameMeal"
        const val MEAL_THUMB= "package com.jasmeet.food_app.fragments.thumbMeal"
        const val CATEGORY_NAME = "package com.jasmeet.food_app.fragments.categoryName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        popularItemsAdapter = PopularAdapter()

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preparePopularItemsRecyclerView()

        viewModel.getRandomMeal()
        observerRandomMeal()
        onRandomMealClick()

        viewModel.getPopularItems()
        observerPopularItems()
        popularItemClick()

        prepareCategoriesRecyclerView()
        viewModel.getCategories()
        observeCategoriesLiveData()
        onCategoryClick()

        onPopularItemLongClick()


    }

    private fun onPopularItemLongClick() {
        popularItemsAdapter.onLongItemClick={meal->
            val mealBottomSheetFragment = MealBottomSheetFragment.newInstance(meal.idMeal)
            mealBottomSheetFragment.show(childFragmentManager,"Meal Info")
        }
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick = {category->
            val intent = Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(CATEGORY_NAME, category.strCategory)
            startActivity(intent)
        }
    }

    private fun prepareCategoriesRecyclerView() {
        categoriesAdapter =CategoriesAdapter()
        binding.recViewCategories.apply {
            layoutManager = GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false)
            adapter = categoriesAdapter
        }
    }

    private fun observeCategoriesLiveData() {
       viewModel.observeCategoriesLiveData()
           .observe(viewLifecycleOwner, Observer { categories ->
               categoriesAdapter.setCategoryList(categories)
           })
    }

    private fun popularItemClick() {
        popularItemsAdapter.onItemClick = {
            meal->
            val intent = Intent(activity,MealActivity::class.java)
            intent.putExtra(MEAL_ID,meal.idMeal)
            intent.putExtra(MEAL_NAME,meal.strMeal)
            intent.putExtra(MEAL_THUMB,meal.strMealThumb)
            startActivity(intent)

        }
    }

    private fun preparePopularItemsRecyclerView() {
        binding.recViewMealsPopular.apply {
            layoutManager = LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false)
            adapter = popularItemsAdapter
        }
    }

    private fun observerPopularItems() {
      viewModel.observerPopularMeals().observe(viewLifecycleOwner
      ) { mealList ->
          popularItemsAdapter.setMeals(mealsList = mealList as ArrayList<MealsByCategory>)
      }
    }

    private fun onRandomMealClick() {
        binding.randomMealCard.setOnClickListener {
            val intent = Intent(activity,MealActivity::class.java)
            intent.putExtra(MEAL_ID,randomMeal.idMeal)
            intent.putExtra(MEAL_NAME, randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB,randomMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observerRandomMeal() {
        viewModel.observeRandomMealLivedata().observe(viewLifecycleOwner
        ) { meal->
            Glide.with(this@HomeFragment)
                .load(meal!!.strMealThumb)
                .into(binding.imgRandomMeal)
            this.randomMeal = meal
        }
    }
}