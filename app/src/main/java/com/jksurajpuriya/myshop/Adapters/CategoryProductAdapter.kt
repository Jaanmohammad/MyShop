package com.jksurajpuriya.myshop.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jksurajpuriya.myshop.Activities.ProductDetailActivity
import com.jksurajpuriya.myshop.Model.AddProductModel
import com.jksurajpuriya.myshop.databinding.ItemCategoryProductLayoutBinding

class CategoryProductAdapter(val context: Context, val list : ArrayList<AddProductModel>)
    : RecyclerView.Adapter<CategoryProductAdapter.CategoryProductViewHolder>() {

    inner class CategoryProductViewHolder(val binding: ItemCategoryProductLayoutBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryProductViewHolder {
        return CategoryProductViewHolder(
            ItemCategoryProductLayoutBinding.
        inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: CategoryProductViewHolder, position: Int) {
        Glide.with(context).load(list[position].productCoverImg).into(holder.binding.imageView3)

        holder.binding.textView1.text=list[position].productName
        holder.binding.textView2.text=list[position].productSp

        holder.itemView.setOnClickListener {
            val intent= Intent(context, ProductDetailActivity::class.java)
            intent.putExtra("id",list[position].productId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}