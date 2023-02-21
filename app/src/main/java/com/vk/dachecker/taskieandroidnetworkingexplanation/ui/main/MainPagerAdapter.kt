package com.vk.dachecker.taskieandroidnetworkingexplanation.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.vk.dachecker.taskieandroidnetworkingexplanation.ui.notes.NotesFragment
import com.vk.dachecker.taskieandroidnetworkingexplanation.ui.profile.ProfileFragment

class MainPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            NotesFragment()
        } else {
            ProfileFragment()
        }
    }
}