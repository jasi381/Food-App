package com.jasmeet.food_app.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.jasmeet.food_app.databinding.ActivityMealBinding
import com.jasmeet.food_app.db.MealDatabase
import com.jasmeet.food_app.fragments.HomeFragment
import com.jasmeet.food_app.pojo.Meal
import com.jasmeet.food_app.viewModel.MealViewModel
import com.jasmeet.food_app.viewModel.MealViewModelFactory

class MealActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMealBinding
    private lateinit var mealName :String
    private lateinit var mealId :String
    private lateinit var mealThumb :String
    private lateinit var mealMvvm:MealViewModel
    private lateinit var ytLink :String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val  mealDatabase = MealDatabase.getInstance(this)
        val viewModelFactory = MealViewModelFactory(mealDatabase)
        mealMvvm = ViewModelProvider(this,viewModelFactory)[MealViewModel::class.java]

        getMealInformationFromIntent()

        setInfoInViews()

        loadingCase()
        mealMvvm.getMealDetails(mealId)
        observeMealDetailsLiveData()

        onYoutube()

        onFavBtn()
    }

    private fun onFavBtn() {
        binding.btnFav.setOnClickListener {
           mealToSave?.let {
               mealMvvm.insertMeal(it)

               Snackbar.make(binding.root,"Meal Saved Successfully !!",Snackbar.LENGTH_SHORT).show()
           }
        }
    }

    private fun onYoutube() {
        binding.btnYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(ytLink))
            startActivity(intent)
        }
    }
    private var mealToSave: Meal?= null
    private fun observeMealDetailsLiveData() {
        mealMvvm.observeMealDetailLiveData().observe(this,object :Observer<Meal>{
            override fun onChanged(t: Meal?) {
                responseCase()
                val meal = t
                mealToSave = meal

                binding.tvCategory.text = "Category : ${t!!.strCategory}"
                    binding.tvArea.text = "Area : ${t.strArea}"
                    binding.tvInst.text = t.strInstructions

                    ytLink = t.strYoutube

            }

        })
    }

    private fun setInfoInViews() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMeal)

        binding.mealName.text = mealName

    }

    private fun getMealInformationFromIntent() {
        val intent = intent

        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!

        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!

        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!

    }

    private fun loadingCase(){
        binding.progressBar.visibility = View.VISIBLE
        binding.btnFav.visibility = View.INVISIBLE
        binding.tvInst.visibility = View.INVISIBLE
        binding.tvArea.visibility = View.INVISIBLE
        binding.tvCategory.visibility = View.INVISIBLE
        binding.btnYoutube.visibility = View.INVISIBLE
    }

    private fun responseCase(){
        binding.progressBar.visibility = View.INVISIBLE
        binding.btnFav.visibility = View.VISIBLE
        binding.tvInst.visibility = View.VISIBLE
        binding.tvArea.visibility = View.VISIBLE
        binding.tvCategory.visibility = View.VISIBLE
        binding.btnYoutube.visibility = View.VISIBLE
    }


}