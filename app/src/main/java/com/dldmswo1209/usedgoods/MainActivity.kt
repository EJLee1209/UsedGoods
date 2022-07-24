package com.dldmswo1209.usedgoods

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.dldmswo1209.usedgoods.chatlist.ChatListFragment
import com.dldmswo1209.usedgoods.databinding.ActivityMainBinding
import com.dldmswo1209.usedgoods.home.HomeFragment
import com.dldmswo1209.usedgoods.mypage.MyPageFragment

class MainActivity : AppCompatActivity() {
    var mBinding: ActivityMainBinding? = null
    val binding get() = mBinding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val homeFragment = HomeFragment()
        val chatListFragment = ChatListFragment()
        val myPageFragment = MyPageFragment()

        replaceFragment(homeFragment) // 처음에 보여줄 화면
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> replaceFragment(homeFragment)
                R.id.chatList -> replaceFragment(chatListFragment)
                R.id.myPage -> replaceFragment(myPageFragment)
            }
            true
        }

    }
    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .apply {
                replace(R.id.fragmentContainer, fragment)
                commit()
            }
    }
}