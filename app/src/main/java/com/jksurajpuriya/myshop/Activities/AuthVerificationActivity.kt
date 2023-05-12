package com.jksurajpuriya.myshop.Activities

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jksurajpuriya.myshop.MainActivity
import com.jksurajpuriya.myshop.Model.UserModel
import com.jksurajpuriya.myshop.R
import com.jksurajpuriya.myshop.databinding.ActivityAuthVerificationBinding
import java.util.concurrent.TimeUnit

class AuthVerificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthVerificationBinding
    lateinit var dialog:Dialog
    var auth:FirebaseAuth?=null
    var aOtp: String?=null
    var number: String? = null
    var otpId:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAuthVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.setTitle("OTP Verification")

        //ProgressBar
        dialog= Dialog(this)
        dialog.setContentView(R.layout.dialog_box)
        dialog.setCancelable(true)
        if (dialog.window!=null){
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(0))
        }


        auth= FirebaseAuth.getInstance()
        number=intent.getStringExtra("number");
        binding.phoneNumber.setText(number)
        SendVerificationCode()

        binding.submit.setOnClickListener {
            aOtp = binding.otp.text.toString()
            if (aOtp!!.isEmpty()){
                binding.otp.setError("Please Enter OTP")
            }else if (aOtp!!.length<=5){
                binding.otp.setError("Enter Correct OOT")
                return@setOnClickListener
            }else{
                dialog.show()
                val credential = PhoneAuthProvider.getCredential(
                    otpId!!,
                    binding.otp.text.toString().replace(" ", "")
                )
                signInWithPhoneAuthCredential(credential)
            }
        }

        binding.wrongNumber.setOnClickListener {
            startActivity(Intent(this,AuthenticationActivity::class.java))
            finish()
        }

        binding.resend.setOnClickListener {
            binding.otp.setText("")
            SendVerificationCode()
        }

    }
    private fun SendVerificationCode() {
        object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.resend.text = "" + millisUntilFinished / 1000
                binding.resend.isEnabled = false
            }

            override fun onFinish() {
                binding.resend.text = "Resend"
                binding.resend.isEnabled = true
            }
        }.start()
        val options = PhoneAuthOptions.newBuilder(auth!!)
            .setPhoneNumber(number!!) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(object : OnVerificationStateChangedCallbacks() {
                override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
                    otpId = s
                }

                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                    signInWithPhoneAuthCredential(phoneAuthCredential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Toast.makeText(this@AuthVerificationActivity, "Failed...", Toast.LENGTH_SHORT)
                        .show()
                }
            }) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth!!.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val preferences = this.getSharedPreferences("user", MODE_PRIVATE)
                    val editor = preferences.edit()
                    editor.putString("number",binding.phoneNumber.text.toString())
                    editor.apply()

                    val data= UserModel(userPhoneNumber = binding.phoneNumber.text.toString())



                    Firebase.firestore.collection("users").document(binding.phoneNumber.text.toString())
                        .set(data).addOnSuccessListener {

                        }.addOnFailureListener {
                            Toast.makeText(this, "Something Went Wrong Create User", Toast.LENGTH_SHORT).show()
                        }


                    dialog.dismiss()
                    startActivity(Intent(this@AuthVerificationActivity, MainActivity::class.java))
                    finish()
                } else {
                    dialog.dismiss()
                }
            }
    }
}


