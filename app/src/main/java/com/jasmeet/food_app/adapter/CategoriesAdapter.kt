package com.jasmeet.food_app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jasmeet.food_app.databinding.CategoryItemBinding
import com.jasmeet.food_app.pojo.Category

class CategoriesAdapter():RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>(){

    private var categoriesList = ArrayList<Category>()
    var onItemClick : ((Category) -> Unit)? = null

    fun setCategoryList(categoriesList:List<Category>){
        this.categoriesList= categoriesList as ArrayList<Category> /* = java.util.ArrayList<com.jasmeet.food_app.pojo.Category> */
        notifyDataSetChanged()
    }

    inner class CategoryViewHolder(val binding: CategoryItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            CategoryItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )

    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

        Glide.with(holder.itemView).load(categoriesList[position].strCategoryThumb).into(holder.binding.imgCat)
        holder.binding.tvCategoryName.text = categoriesList[position].strCategory

        holder.itemView.setOnClickListener {
            onItemClick!!.invoke(categoriesList[position])
        }
    }

    override fun getItemCount(): Int {
        return  categoriesList.size
    }

}