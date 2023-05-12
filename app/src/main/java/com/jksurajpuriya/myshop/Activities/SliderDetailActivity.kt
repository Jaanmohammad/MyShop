package com.jksurajpuriya.myshop.Activities

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jksurajpuriya.myshop.MainActivity
import com.jksurajpuriya.myshop.R
import com.jksurajpuriya.myshop.RoomDB.AppDatabase
import com.jksurajpuriya.myshop.RoomDB.ProductDao
import com.jksurajpuriya.myshop.RoomDB.ProductModel
import com.jksurajpuriya.myshop.databinding.ActivitySliderDetailBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SliderDetailActivity : AppCompatActivity() {
     private lateinit var binding:ActivitySliderDetailBinding
     lateinit var dialog:Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySliderDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //ProgressBar
        dialog= Dialog(this)
        dialog.setContentView(R.layout.dialog_box)
        dialog.setCancelable(true)
        if (dialog.window!=null){
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(0))
        }

        dialog.show()
        gatSliderDetail()

    }

    private fun gatSliderDetail() {
        Firebase.firestore.collection("slider").document("item")
            .get().addOnSuccessListener {

                Glide.with(this).load(it.get("img")).into(binding.sliderImage)

                val image=it.getString("img")
                val name=it.getString("name")
                val productSp=it.getString("price")
                val productDesc=it.getString("description")

                binding.sliderProductName.text=name
                binding.sliderPrice.text=productSp
//                binding.sliderProductName.text=name
                binding.sliderDescription.text=productDesc

                dialog.dismiss()

                cartAction("jk",name, productSp, it.getString("img"))


            }.addOnFailureListener{
                dialog.dismiss()
            }
    }
    private fun cartAction(proId: String, name: String?, productSp: String?, coverImg: String?) {

        val productDao = AppDatabase.getInstance(this).productDao()

        if (productDao.isExit(proId)!=null){
            binding.sliderAddToCartBtn.text="Go to Cart"
        }else{
            binding.sliderAddToCartBtn.text="Add to Cart"
        }
        binding.sliderAddToCartBtn.setOnClickListener {
            if (productDao.isExit(proId)!=null){
                openCart()
            }else{
                addToCart(productDao, proId, name, productSp, coverImg)
            }
        }

    }

    private fun addToCart(
        productDao: ProductDao,
        proId: String,
        name: String?,
        productSp: String?,
        coverImg: String?
    ){
        val  data = ProductModel(proId,name,coverImg,productSp)
        lifecycleScope.launch(Dispatchers.IO) {
            productDao.insertProduct(data) }
        binding.sliderAddToCartBtn.text="Go to Cart"

    }

    private fun openCart() {
        val preference = this.getSharedPreferences("info", MODE_PRIVATE)
        val editor = preference.edit()
        editor.putBoolean("isCart",true)
        editor.apply()

        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}