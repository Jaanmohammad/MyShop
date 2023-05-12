package com.jksurajpuriya.myshop.Activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jksurajpuriya.myshop.MainActivity
import com.jksurajpuriya.myshop.R
import com.jksurajpuriya.myshop.RoomDB.AppDatabase
import com.jksurajpuriya.myshop.RoomDB.ProductModel
import com.jksurajpuriya.myshop.databinding.ActivityCheckoutBinding
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class CheckoutActivity : AppCompatActivity(), PaymentResultListener {
    private lateinit var binding: ActivityCheckoutBinding
    private lateinit var preferences : SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = getSharedPreferences("user", MODE_PRIVATE)


        //UPI ID
        //success@razorpay

        //card
        //5267318187975449
        val price = intent.getStringExtra("totalCost")
        val co = Checkout()
        co.setKeyID("rzp_test_OfyquO6kYyWdG4")


        try {
            val options = JSONObject()
            options.put("name","My Shop")
            options.put("description","bet jkh jhb nj")
            options.put("image","https://firebasestorage.googleapis.com/v0/b/my-shop-d4c62.appspot.com/o/icon%2Fmy_shop_icon.png?alt=media&token=5f9107ac-161f-4ee8-976b-69aa8de6751b")
            options.put("theme.color", "#3399cc");
            options.put("currency","INR")
            options.put("amount",price!!.toInt()*100)//pass amount in currency subunits
            options.put("prefill.email","jabidkhan111@gmail.com")
            options.put("prefill.contact", preferences.getString("number",""));
            co.open(this,options)
        }catch (e: Exception){
            Toast.makeText(this,"Error in payment: "+ e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }

    }

    override fun onPaymentSuccess(p0: String?) {
        Toast.makeText(this,"Payment success", Toast.LENGTH_LONG).show()
        uploadData()
    }
    override fun onPaymentError(p0: Int, p1: String?) {
        Toast.makeText(this,"Payment Failed", Toast.LENGTH_LONG).show()
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }
    private fun uploadData() {
        val id = intent.getStringArrayListExtra("productsIds")
        for (currentId in id!!){
            fetchData(currentId)
        }
    }

    private fun fetchData(productId: String?) {
        val dao = AppDatabase.getInstance(this).productDao()

        Firebase.firestore.collection("products")
            .document(productId!!).get().addOnSuccessListener {

                //cart item delete
                lifecycleScope.launch(Dispatchers.IO){
                    dao.deleteProduct(ProductModel(productId))
                }



                saveData(it.getString("productName"),
                    it.getString("productSp"),
                    productId)
            }
    }

    private fun saveData(name: String?, price: String?, productId: String) {
        val preference = this.getSharedPreferences("user", MODE_PRIVATE)
        val data = hashMapOf<String, Any>()
        data["name"] = name!!
        data["price"] = price!!
        data["productId"] = productId
        data["status"] = "Ordered"
        data["userId"] = preference.getString("number","")!!

        val firestore = Firebase.firestore.collection("allOrders")
        val key = firestore.document().id
        data["orderId"]=key

        firestore.document(key).set(data).addOnSuccessListener {
            Toast.makeText(this,"Order Placed",Toast.LENGTH_LONG).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }.addOnFailureListener {
            Toast.makeText(this,"Order Placed",Toast.LENGTH_LONG).show()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }

}