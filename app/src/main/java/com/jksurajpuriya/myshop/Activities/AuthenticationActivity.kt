package com.jksurajpuriya.myshop.Activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jksurajpuriya.myshop.databinding.ActivityAuthenticationBinding

class AuthenticationActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAuthenticationBinding
    var num: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.setTitle("Mobile Authentication")


        //  ccp is country code picker
        binding.ccp.registerCarrierNumberEditText(binding.number)

        binding.next.setOnClickListener {
            num=binding.number.getText().toString()
            if (num!!.isEmpty()){
                binding.number.setError("Please Enter Mobile Number")
                return@setOnClickListener
            }

//            else if (num!!.length<=9){
//                binding.number.setError("Please Enter Correct")
//                return@setOnClickListener
//            }
            else{
                val intent = Intent(this, AuthVerificationActivity::class.java)
                intent.putExtra("number", binding.ccp.fullNumberWithPlus.replace(" ", ""))
                startActivity(intent)
                finish()
            }
        }
    }
}