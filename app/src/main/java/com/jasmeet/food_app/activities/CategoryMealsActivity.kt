package com.jasmeet.food_app.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.jasmeet.food_app.adapter.CategoryMealsAdapter
import com.jasmeet.food_app.databinding.ActivityCategoryMealsBinding
import com.jasmeet.food_app.fragments.HomeFragment
import com.jasmeet.food_app.viewModel.CategoryMealViewModel

class CategoryMealsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryMealsBinding
    private lateinit var categoryMealsViewModel: CategoryMealViewModel
    lateinit var categoryMealsAdapter :CategoryMealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareRecView()

        categoryMealsViewModel = ViewModelProviders.of(this)[CategoryMealViewModel::class.java]

        categoryMealsViewModel.getMealsByCategory(intent.getStringExtra(HomeFragment.CATEGORY_NAME)!!)

        categoryMealsViewModel.observeMealsLiveData().observe(this, Observer {mealsList ->
            binding.tvCategoryCount.text = mealsList.size.toString()
           categoryMealsAdapter.setMealsList(mealsList)
        })
    }

    private fun prepareRecView() {
        categoryMealsAdapter = CategoryMealsAdapter()
        binding.rvMeals.apply {
            layoutManager = GridLayoutManager(context,2,GridLayoutManager.VERTICAL,false)
            adapter = categoryMealsAdapter
        }

    }
}