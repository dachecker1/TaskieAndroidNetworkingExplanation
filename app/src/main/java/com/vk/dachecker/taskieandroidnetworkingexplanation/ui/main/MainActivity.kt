package com.vk.dachecker.taskieandroidnetworkingexplanation.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.vk.dachecker.taskieandroidnetworkingexplanation.R
import com.vk.dachecker.taskieandroidnetworkingexplanation.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val pagerAdapter by lazy { MainPagerAdapter(supportFragmentManager, lifecycle) }

    companion object {
        fun getIntent(context: Context): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUi()
    }

    private fun initUi() {
        binding.fragmentPager.adapter = pagerAdapter
        TabLayoutMediator(binding.tabs, binding.fragmentPager) { tab, position ->
            val text = if (position == 0) {
                "Notes"
            } else {
                "Profile"
            }
            tab.text = text
        }.attach()
    }
}
