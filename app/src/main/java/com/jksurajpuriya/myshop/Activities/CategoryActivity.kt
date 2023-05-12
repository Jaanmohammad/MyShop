package com.jksurajpuriya.myshop.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.jksurajpuriya.myshop.Adapters.CategoryProductAdapter
import com.jksurajpuriya.myshop.Model.AddProductModel
import com.jksurajpuriya.myshop.databinding.ActivityCategoryBinding

class CategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getCategory(intent.getStringExtra("cat"))

    }

    private fun getCategory(category: String?) {
        val list = ArrayList<AddProductModel>()
        Firebase.firestore.collection("products").whereEqualTo("productCategory", category)
            .get().addOnSuccessListener {
                for (doc in it.documents){
                    val data=doc.toObject(AddProductModel::class.java)
                    list.add(data!!)
                }
                binding.recyclerView.adapter= CategoryProductAdapter(this,list)
            }

    }
}