package com.jksurajpuriya.myshop

import android.os.Bundle
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.jksurajpuriya.myshop.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var i=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)




        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        val navController = navHostFragment!!.findNavController()

        val poppupMenu = PopupMenu(this, null)
        poppupMenu.inflate(R.menu.bottom_nav)
        binding.bottomBar.setupWithNavController(poppupMenu.menu,navController)



        navController.addOnDestinationChangedListener(object :NavController.OnDestinationChangedListener{
            override fun onDestinationChanged(
                controller: NavController,
                destination: NavDestination,
                arguments: Bundle?
            ) {
                title=when(destination.id){
                    R.id.cartFragment -> "Cart"
                    R.id.moreFragment -> "My Dashboard"
                    else -> "My Shop"
                }
            }

        })

        binding.bottomBar.onItemSelected={
            when(it){
                0->{
                    i=0
                    navController.navigate(R.id.homeFragment)
                }
                1->i=1
                2->i=2
            }
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (i==0){
            finish()
        }
    }
}