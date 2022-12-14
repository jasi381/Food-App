package com.jasmeet.food_app.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jasmeet.food_app.db.MealDatabase
import com.jasmeet.food_app.pojo.*
import com.jasmeet.food_app.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(
    private val mealDatabase: MealDatabase
):ViewModel() {

    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularItemsLiveData = MutableLiveData<List<MealsByCategory>>()
    private var categoriesLiveData = MutableLiveData<List<Category>>()
    private var favouritesMealsLiveData = mealDatabase.mealDao().getAllMeals()
    private var bottomSheetMealLiveData = MutableLiveData<Meal>()

    fun getRandomMeal(){
        RetrofitInstance.api.getRandomMeal().enqueue(object: Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body()!= null){
                    val randomMeal : Meal = response.body()!!.meals[0]
                    randomMealLiveData.value = randomMeal
                }else{
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeFragment" , t.message.toString())
            }

        })
    }
    fun getPopularItems(){
        RetrofitInstance.api.getPopularItems("Dessert").enqueue(object :Callback<MealsByCategoryList>{
            override fun onResponse(call: Call<MealsByCategoryList>, response: Response<MealsByCategoryList>) {
                if (response.body() != null) {
                    popularItemsLiveData.value = response.body()!!.meals
                }
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.d("HomeFragment" , t.message.toString())
            }

        })
    }

    fun getCategories(){
        RetrofitInstance.api.getCategories().enqueue(object :Callback<CategoryList>{
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                response.body()?.let { categoryList ->
                    categoriesLiveData.postValue(categoryList.categories)
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.e("HomeMVVM",t.message.toString())

            }

        })
    }

    fun getMealById(id:String){
        RetrofitInstance.api.getMealDetails(id).enqueue(object:Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {

                val meal = response.body()?.meals?.first()
                meal?.let { meal->
                    bottomSheetMealLiveData.postValue(meal)

                }

            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.e("HomeMvvme",t.message.toString())
            }

        })
    }


    fun deleteMeal(meal: Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().delete(meal)
        }
    }
    fun insertMeal(meal: Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().upsert(meal)
        }
    }

    fun observeRandomMealLivedata():LiveData<Meal>{
        return randomMealLiveData
    }

    fun observerPopularMeals():LiveData<List<MealsByCategory>>{
        return popularItemsLiveData
    }

    fun observeCategoriesLiveData():LiveData<List<Category>>{
        return  categoriesLiveData
    }

    fun observeFavouritesMealsLiveData():LiveData<List<Meal>>{
        return favouritesMealsLiveData
    }

    fun observeBottomSheetMeal():LiveData<Meal>{
        return bottomSheetMealLiveData
    }
}