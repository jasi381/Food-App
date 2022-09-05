package com.jasmeet.food_app.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jasmeet.food_app.pojo.Meal



@Dao
interface MealDao {

    @Insert(onConflict =  OnConflictStrategy.REPLACE)
    suspend fun upsert(meal: Meal)

    @Delete
    suspend fun delete(meal: Meal)

    @Query("SELECT * FROM mealInformation")

    fun getAllMeals():LiveData<List<Meal>>


}