package com.jksurajpuriya.myshop.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.jksurajpuriya.myshop.Activities.AddressActivity
import com.jksurajpuriya.myshop.Adapters.CartAdapter
import com.jksurajpuriya.myshop.R
import com.jksurajpuriya.myshop.RoomDB.AppDatabase
import com.jksurajpuriya.myshop.RoomDB.ProductModel
import com.jksurajpuriya.myshop.databinding.FragmentCartBinding

class CartFragment : Fragment() {
   private lateinit var binding: FragmentCartBinding
   private lateinit var list: ArrayList<String>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentCartBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment


        val preferences = requireContext().getSharedPreferences("info", AppCompatActivity.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean("isCart",false)
        editor.apply()


        val dao = AppDatabase.getInstance(requireContext()).productDao()
        list=ArrayList()

        dao.getAllProducts().observe(requireActivity()){
            binding.cartRecycler.adapter=CartAdapter(requireContext(), it)

            list.clear()

            for (data in it){
                list.add(data.productId)
            }
            totalCost(it)
        }


        return binding.root


    }

    private fun totalCost(data: List<ProductModel>?) {
        var total = 0
        for (item in data!!){
            total += item.productSp!!.toInt()
        }
        binding.totalItem.text="Total item in Cart is ${data.size}"
        binding.totalCost.text="Total Cost ${total}"

        binding.checkOut.setOnClickListener{
            val intent = Intent(context, AddressActivity::class.java)
            val b = Bundle()
            b.putStringArrayList("productsIds", list)
            b.putString("totalCost", total.toString())
            intent.putExtras(b)
            startActivity(intent)
        }
    }
}