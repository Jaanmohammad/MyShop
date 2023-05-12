package com.jksurajpuriya.myshop.Activities

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jksurajpuriya.myshop.R
import com.jksurajpuriya.myshop.databinding.ActivityAddressBinding


class AddressActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAddressBinding
    private lateinit var preferences : SharedPreferences
    lateinit var dialog:Dialog
    private lateinit var totalCost: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //ProgressBar
        dialog= Dialog(this)
        dialog.setContentView(R.layout.dialog_box)
        dialog.setCancelable(true)
        if (dialog.window!=null){
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(0))
        }

        preferences = getSharedPreferences("user", MODE_PRIVATE)

        totalCost=intent.getStringExtra("totalCost")!!
        loadUserInfo()

        ClickListenerOnEditText()

        binding.proceedToCheckout.setOnClickListener{
            validateData(
                binding.name.text.toString(),
                binding.number.text.toString(),
                binding.village.text.toString(),
                binding.city.text.toString(),
                binding.pinCode.text.toString(),
                binding.state.text.toString()
            )
        }

    }

    private fun validateData(name: String, number: String, village: String, city: String, pinCode: String, state: String) {

        if (name.isEmpty()){
            binding.name.setError("Please Enter Full Name")
            return
        }
        if (number.isEmpty()){
            binding.number.setError("Please Enter Full Name")
            return
        }
        if (village.isEmpty()){
            binding.village.setError("Please Enter Full Name")
            return
        }
        if (city.isEmpty()){
            binding.city.setError("Please Enter Full Name")
            return
        }
        if (pinCode.isEmpty()){
            binding.pinCode.setError("Please Enter Full Name")
            return
        }
        if (state.isEmpty()){
            binding.state.setError("Please Enter Full Name")
            return
        }else{
            dialog.show()
            storeData(name,pinCode, city, state, village)
        }

    }

    private fun storeData(name: String, pinCode: String, city: String, state: String, village: String) {
        val map = mutableMapOf<String, Any>()
        map["name"]=name
        map["pinCode"]=pinCode
        map["city"]=city
        map["state"]=state
        map["village"]=village

        Firebase.firestore.collection("users")
            .document(preferences.getString("number","")!!)
            .update(map).addOnSuccessListener {

                val b= Bundle()
                b.putStringArrayList("productsIds",intent.getStringArrayListExtra("productsIds"))
                b.putString("totalCost",totalCost)

                dialog.dismiss()

                val intent = Intent(this, CheckoutActivity::class.java)
                intent.putExtras(b)
                startActivity(intent)
            }.addOnFailureListener{
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }

    private fun loadUserInfo() {
        dialog.show()
        Firebase.firestore.collection("users")
            .document(preferences.getString("number","")!!)
            .get().addOnSuccessListener {
                binding.name.setText(it.getString("name"))
                binding.number.setText(it.getString("userPhoneNumber"))
                binding.village.setText(it.getString("village"))
                binding.city.setText(it.getString("state"))
                binding.state.setText(it.getString("city"))
                binding.pinCode.setText(it.getString("pinCode"))
                dialog.dismiss()
            }.addOnFailureListener {
                dialog.dismiss()
            }
    }

    private fun ClickListenerOnEditText() {

        binding.name.setOnClickListener {
            binding.name.setFocusableInTouchMode(true);
        }
        binding.village.setOnClickListener {
            binding.village.setFocusableInTouchMode(true);
        }
        binding.city.setOnClickListener {
            binding.city.setFocusableInTouchMode(true);
        }
        binding.state.setOnClickListener {
            binding.state.setFocusableInTouchMode(true);
        }
        binding.pinCode.setOnClickListener {
            binding.pinCode.setFocusableInTouchMode(true);
        }

    }
}

