package com.jksurajpuriya.myshop.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView.ScaleType
import androidx.lifecycle.lifecycleScope
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jksurajpuriya.myshop.MainActivity
import com.jksurajpuriya.myshop.RoomDB.AppDatabase
import com.jksurajpuriya.myshop.RoomDB.ProductDao
import com.jksurajpuriya.myshop.RoomDB.ProductModel
import com.jksurajpuriya.myshop.databinding.ActivityProductDetailBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductDetailActivity : AppCompatActivity() {
    private lateinit var binding:ActivityProductDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        getProductDetails(intent.getStringExtra("id"))
    }

    private fun getProductDetails(proId: String?) {


    Firebase.firestore.collection("products")
        .document(proId!!).get().addOnSuccessListener {
            val list = it.get("productImages") as ArrayList<String>
            val name=it.getString("productName")
            val productSp=it.getString("productSp")
            val productDesc=it.getString("productDescription")

            binding.textView7.text= name
            binding.textView8.text= productSp
            binding.textView9.text= productDesc

            val sliderList=ArrayList<SlideModel>()
            for (data in list){
                sliderList.add(SlideModel(data, ScaleTypes.FIT))
            }
            cartAction(proId, name, productSp, it.getString("productCoverImg"))
            binding.imageSlider.setImageList(sliderList)

        }

    }

    private fun cartAction(proId: String, name: String?, productSp: String?, coverImg: String?) {

        val productDao = AppDatabase.getInstance(this).productDao()

        if (productDao.isExit(proId)!=null){
            binding.addToCartBtn.text="Go to Cart"
        }else{
            binding.addToCartBtn.text="Add to Cart"
        }
        binding.addToCartBtn.setOnClickListener {
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
        binding.addToCartBtn.text="Go to Cart"

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