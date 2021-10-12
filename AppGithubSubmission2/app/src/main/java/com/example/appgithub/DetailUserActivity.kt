package com.example.appgithub

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.appgithub.databinding.ActivityDetailUserBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailUserBinding

    // data
    private var username : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //component binding
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(EXTRA_USERNAME)){
            username = intent.getStringExtra(EXTRA_USERNAME)
            setBottomNavigation(username!!)
        }

        if (savedInstanceState == null){
            binding.navView.selectedItemId = R.id.bmenu_detail_user_about
        }
    }

    companion object{
        const val EXTRA_USERNAME = "username"
    }

    fun makeToast(pesan: String){
        Toast.makeText(applicationContext, pesan, Toast.LENGTH_SHORT).show()
    }

    fun setBottomNavigation(username : String){
        binding.navView.setOnNavigationItemSelectedListener(
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                val fragment: Fragment
                when (item.itemId) {
                    R.id.bmenu_detail_user_about-> {
                        fragment = DetailUserDataFragment.newInstance(username)
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainerDetailUser, fragment)
                            .commit()

                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.bmenu_detail_user_followers-> {
                        fragment = DetailUserFollowsFragment.newInstance(username, true)
                        supportFragmentManager.beginTransaction()
                                .replace(R.id.fragmentContainerDetailUser, fragment)
                                .commit()

                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.bmenu_detail_user_followings-> {
                        fragment = DetailUserFollowsFragment.newInstance(username, false)
                        supportFragmentManager.beginTransaction()
                                .replace(R.id.fragmentContainerDetailUser, fragment)
                                .commit()

                        return@OnNavigationItemSelectedListener true
                    }
                }
                false
            })
    }


}