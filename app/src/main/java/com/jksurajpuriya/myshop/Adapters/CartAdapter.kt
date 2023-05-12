package com.jksurajpuriya.myshop.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jksurajpuriya.myshop.Activities.ProductDetailActivity
import com.jksurajpuriya.myshop.Activities.SliderDetailActivity
import com.jksurajpuriya.myshop.RoomDB.AppDatabase
import com.jksurajpuriya.myshop.RoomDB.ProductModel
import com.jksurajpuriya.myshop.databinding.LayoutCartItemBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CartAdapter (val context: Context, val list: List<ProductModel>) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {



    inner class CartViewHolder(val binding: LayoutCartItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding= LayoutCartItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        Glide.with(context).load(list[position].productImage).into(holder.binding.imageView4)

        holder.binding.textView.text=list[position].productName
        holder.binding.textView2.text=list[position].productSp

        //for CartItem Detail
        holder.itemView.setOnClickListener {
            if (list[position].productId=="jk"){
                context.startActivity(Intent(context,SliderDetailActivity::class.java))
            }else{
            val intent= Intent(context, ProductDetailActivity::class.java)
            intent.putExtra("id",list[position].productId)
            context.startActivity(intent)
            }
        }

        //cartItem delete
        val dao= AppDatabase.getInstance(context).productDao()
        holder.binding.delete.setOnClickListener {

            GlobalScope.launch( Dispatchers.IO) {

                dao.deleteProduct(
                    ProductModel(
                        list[position].productId,
                        list[position].productName,
                        list[position].productImage,
                        list[position].productSp
                    )
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}