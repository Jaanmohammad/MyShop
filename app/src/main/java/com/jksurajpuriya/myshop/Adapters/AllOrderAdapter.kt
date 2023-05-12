package com.jksurajpuriya.myshop.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jksurajpuriya.myshop.Model.AllOrderModel
import com.jksurajpuriya.myshop.databinding.AllOrderItemLayoutBinding


class AllOrderAdapter (val list: ArrayList<AllOrderModel>, val context: Context)
    : RecyclerView.Adapter<AllOrderAdapter.AllOrderViewHolder>() {

    inner class AllOrderViewHolder(val  binding: AllOrderItemLayoutBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllOrderViewHolder {
        return AllOrderViewHolder(
            AllOrderItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        )
    }

    override fun onBindViewHolder(holder: AllOrderViewHolder, position: Int) {
        holder.binding.productName.text =list[position].name
        holder.binding.productPrice.text =list[position].price



        when (list[position].status){
            "Ordered" -> {
                holder.binding.productStatus.text="Ordered"
            }
            "Dispatched" -> {
                holder.binding.productStatus.text="Dispatched"
            }
            "Delivered" -> {
                holder.binding.productStatus.text="Delivered"
            }
            "Canceled" -> {
                holder.binding.productStatus.text="Canceled"
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

}