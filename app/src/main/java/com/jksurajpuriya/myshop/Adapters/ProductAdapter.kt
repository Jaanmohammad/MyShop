package com.jksurajpuriya.myshop.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jksurajpuriya.myshop.Activities.ProductDetailActivity
import com.jksurajpuriya.myshop.Model.AddProductModel
import com.jksurajpuriya.myshop.databinding.LayoutProductItemBinding

class ProductAdapter (val context: Context, val list: ArrayList<AddProductModel>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(val binding: LayoutProductItemBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(LayoutProductItemBinding
            .inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        Glide.with(context).load(list[position].productCoverImg).into(holder.binding.imageView2)
        holder.binding.textView2.text=list[position].productName
        holder.binding.textView4.text=list[position].productCategory
        holder.binding.textView5.text=list[position].productMrp

        holder.binding.button.text=list[position].productSp

        holder.itemView.setOnClickListener {
            val  intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra("id",list[position].productId)
            context.startActivity(intent)
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }
}