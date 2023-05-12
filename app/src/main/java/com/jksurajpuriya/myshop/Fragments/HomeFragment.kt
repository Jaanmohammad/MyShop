package com.jksurajpuriya.myshop.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.jksurajpuriya.myshop.Activities.SliderDetailActivity
import com.jksurajpuriya.myshop.Adapters.CategoryAdapter
import com.jksurajpuriya.myshop.Adapters.ProductAdapter
import com.jksurajpuriya.myshop.Model.AddProductModel
import com.jksurajpuriya.myshop.Model.CategoryModel
import com.jksurajpuriya.myshop.R
import com.jksurajpuriya.myshop.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentHomeBinding.inflate(layoutInflater)


        val preference = requireContext().getSharedPreferences("info", AppCompatActivity.MODE_PRIVATE)
        if (preference.getBoolean("isCart",false))
            findNavController().navigate(R.id.action_homeFragment_to_cartFragment)


        getSlider()
        getCategories()
        getProducts()

        binding.sliderImage.setOnClickListener {
            startActivity(Intent(requireContext(),SliderDetailActivity::class.java))
        }

        return binding.root
    }

    private fun getProducts() {
        var list =ArrayList<AddProductModel>()
        Firebase.firestore.collection("products")
            .get().addOnSuccessListener {
                for (doc in it.documents){
                    val data = doc.toObject(AddProductModel::class.java)
                    list.add(data!!)
                }
                binding.productRecycler.adapter=ProductAdapter(requireContext(),list)
            }.addOnFailureListener{
            }

    }

    private fun getCategories() {
        var list=ArrayList<CategoryModel>()
        Firebase.firestore.collection("categories")
            .get().addOnSuccessListener {
                for (doc in it.documents){
                    val data = doc.toObject(CategoryModel::class.java)
                    list.add(data!!)
                }
                binding.categoryRecycler.adapter= CategoryAdapter(requireContext(),list)
            }.addOnFailureListener{
            }

    }

    private fun getSlider() {
        Firebase.firestore.collection("slider").document("item")
            .get().addOnSuccessListener {
                Glide.with(requireActivity()).load(it.get("img")).into(binding.sliderImage)
            }.addOnFailureListener{
            }
    }

}