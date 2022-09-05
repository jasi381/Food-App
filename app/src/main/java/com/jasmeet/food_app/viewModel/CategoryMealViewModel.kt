package com.jasmeet.food_app.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jasmeet.food_app.pojo.MealsByCategory
import com.jasmeet.food_app.pojo.MealsByCategoryList
import com.jasmeet.food_app.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryMealViewModel : ViewModel() {

    val mealsLiveData = MutableLiveData<List<MealsByCategory>>()

    fun getMealsByCategory(categoryName: String) {
        RetrofitInstance.api.getMealsByCategory(categoryName).enqueue(object:Callback<MealsByCategoryList>{
            override fun onResponse(
                call: Call<MealsByCategoryList>,
                response: Response<MealsByCategoryList>
            ) {
                response.body()?.let {
                    mealsLiveData.postValue(it.meals)

                }

            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.e("Category MVVM",t.message.toString())
            }
        })
    }

    fun observeMealsLiveData() :LiveData<List<MealsByCategory>>{
        return mealsLiveData

    }

}