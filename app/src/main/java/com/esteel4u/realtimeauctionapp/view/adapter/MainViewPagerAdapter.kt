package com.esteel4u.realtimeauctionapp.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.esteel4u.realtimeauctionapp.view.ui.fragments.*


private const val NUM_TABS = 4

class MainViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when(position) {
            0 -> return HomeFragment()
            1 -> return LikeFragment()
            2 -> return CartFragment()
            3 -> return MyPageFragment()
        }

        return MyPageFragment()
    }
}
