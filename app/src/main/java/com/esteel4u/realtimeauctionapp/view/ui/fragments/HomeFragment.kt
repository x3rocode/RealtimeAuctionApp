package com.esteel4u.realtimeauctionapp.view.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.esteel4u.realtimeauctionapp.R
import com.esteel4u.realtimeauctionapp.databinding.FragmentHomeBinding
import com.esteel4u.realtimeauctionapp.view.adapter.HomeBannerAdapter
import com.zhpan.bannerview.BannerViewPager
import com.zhpan.bannerview.constants.IndicatorGravity
import com.zhpan.bannerview.utils.BannerUtils
import com.zhpan.indicator.enums.IndicatorSlideMode
import com.zhpan.indicator.enums.IndicatorStyle
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment: Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var mViewPager : BannerViewPager<Any>
    var mSlideMode = IndicatorSlideMode.SMOOTH

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        var adatper = HomeBannerAdapter(resources.getDimensionPixelOffset(R.dimen.dp_8))
        mViewPager = binding.bannerView
        banner_view.setIndicatorSliderGap(BannerUtils.dp2px(6f))
            .setScrollDuration(800)
            .setLifecycleRegistry(lifecycle)
            .setIndicatorGravity(IndicatorGravity.CENTER)
            //.setAdapter(adatper)
            .create()
        setupRoundRectIndicator()
        return binding.root
    }


    @SuppressLint("ResourceAsColor")
    private fun setupRoundRectIndicator() {
        val checkedWidth = resources.getDimensionPixelOffset(R.dimen.dp_10)
        val normalWidth = getNormalWidth()
        mViewPager.setIndicatorStyle(IndicatorStyle.ROUND_RECT)
            .setIndicatorSliderGap(BannerUtils.dp2px(4f))
            .setIndicatorSlideMode(mSlideMode)
            .setIndicatorHeight(resources.getDimensionPixelOffset(R.dimen.dp_4))
            .setIndicatorSliderColor(
                R.color.egray40,
                R.color.lblue40)
            .setIndicatorSliderWidth(normalWidth, checkedWidth)
    }

    @SuppressLint("ResourceAsColor")
    private fun setupCircleIndicator() {
        val checkedWidth: Int
        val normalWidth: Int
        if (mSlideMode == IndicatorSlideMode.SCALE) {
            checkedWidth = resources.getDimensionPixelOffset(R.dimen.dp_5)
            normalWidth = resources.getDimensionPixelOffset(R.dimen.dp_4)
        } else {
            checkedWidth = resources.getDimensionPixelOffset(R.dimen.dp_4)
            normalWidth = checkedWidth
        }
        mViewPager.setIndicatorStyle(IndicatorStyle.CIRCLE)
            .setIndicatorSlideMode(mSlideMode)
            .setIndicatorSliderGap(resources.getDimensionPixelOffset(R.dimen.dp_6))
            .setIndicatorSliderRadius(normalWidth, checkedWidth)
            .setIndicatorSliderColor(
                R.color.egray40,
                R.color.lblue40)
    }

    @SuppressLint("ResourceAsColor")
    private fun setupDashIndicator() {
        val checkedWidth = resources.getDimensionPixelOffset(R.dimen.dp_10)
        val normalWidth = getNormalWidth()
        mViewPager.setIndicatorStyle(IndicatorStyle.DASH)
            .setIndicatorHeight(resources.getDimensionPixelOffset(R.dimen.dp_3))
            .setIndicatorSlideMode(mSlideMode)
            .setIndicatorSliderGap(resources.getDimensionPixelOffset(R.dimen.dp_3))
            .setIndicatorSliderWidth(normalWidth, checkedWidth)
            .setIndicatorSliderColor(
                R.color.egray40,
                R.color.lblue40)
    }

    private fun getNormalWidth(): Int {
        return if (mSlideMode == IndicatorSlideMode.SMOOTH || mSlideMode == IndicatorSlideMode.WORM || mSlideMode == IndicatorSlideMode.COLOR) {
            resources.getDimensionPixelOffset(R.dimen.dp_10)
        } else {
            resources.getDimensionPixelOffset(R.dimen.dp_4)
        }
    }
}


