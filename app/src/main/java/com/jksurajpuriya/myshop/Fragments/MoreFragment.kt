package com.jksurajpuriya.myshop.Fragments

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.jksurajpuriya.myshop.Adapters.AllOrderAdapter
import com.jksurajpuriya.myshop.Model.AllOrderModel
import com.jksurajpuriya.myshop.R
import com.jksurajpuriya.myshop.databinding.FragmentMoreBinding

class MoreFragment : Fragment() {
    private lateinit var binding:FragmentMoreBinding
    private lateinit var list: ArrayList<AllOrderModel>
    lateinit var dialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentMoreBinding.inflate(layoutInflater)


        //ProgressBar
        dialog= Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_box)
        dialog.setCancelable(true)
        if (dialog.window!=null){
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(0))
        }

        dialog.show()
        list= ArrayList()

        val preferences=requireContext().getSharedPreferences("user",AppCompatActivity.MODE_PRIVATE)

        Firebase.firestore.collection("allOrders")
            .whereEqualTo("userId",preferences.getString("number",""))
            .get().addOnSuccessListener {
                list.clear()
                for (doc in it){
                    val data = doc.toObject(AllOrderModel::class.java)
                    list.add(data)
                    dialog.dismiss()
                }
                binding.recyclerView.adapter=AllOrderAdapter(list,requireContext())
            }.addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(requireContext(), "Something Went Wrong...", Toast.LENGTH_SHORT).show()
            }
        return binding.root
    }

}