package com.esteel4u.realtimeauctionapp.view.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.esteel4u.realtimeauctionapp.R
import com.esteel4u.realtimeauctionapp.view.adapter.ViewBindingSampleAdapter
import com.zhpan.bannerview.BannerViewPager
import com.zhpan.bannerview.annotation.APageStyle
import com.zhpan.bannerview.constants.PageStyle
import com.zhpan.bannerview.utils.BannerUtils

class HomeBannerFragment  : BaseFragment() {
    private lateinit var mViewPager: BannerViewPager<Int>
    override val layout: Int
        get() = R.layout.fragment_home

    override fun initTitle() {}
    override fun initView(savedInstanceState: Bundle?, view: View) {
        mViewPager = view.findViewById(R.id.banner_view)
        initBVP()

    }

    private fun initBVP() {
        mViewPager.apply {
            setLifecycleRegistry(lifecycle)
            adapter = ViewBindingSampleAdapter(resources.getDimensionPixelOffset(R.dimen.dp_8))
            setIndicatorSliderRadius(
                resources.getDimensionPixelOffset(R.dimen.dp_4),
                resources.getDimensionPixelOffset(R.dimen.dp_5)
            )
            setQQMusicStyle()
            setOnPageClickListener { _: View, position: Int -> itemClick(position) }
            setInterval(5000)
        }
        setupBanner(
            PageStyle.MULTI_PAGE_SCALE,
            resources.getDimensionPixelOffset(R.dimen.dp_10)
        )
    }



    /**
     * Different page styles can be implement by use [BannerViewPager.setPageStyle] and
     * [BannerViewPager.setRevealWidth]
     *
     * @param pageStyle Optional params [PageStyle.MULTI_PAGE_SCALE] and [PageStyle.MULTI_PAGE_OVERLAP]
     * @param revealWidth In the multi-page mode, The exposed width of the items on the left and right sides
     */
    private fun setupBanner(@APageStyle pageStyle: Int, revealWidth: Int) {
        setupBanner(pageStyle, revealWidth, revealWidth)
    }

    private fun setupBanner(@APageStyle pageStyle: Int, leftRevealWidth: Int, rightRevealWidth: Int) {
        mViewPager
            .setPageMargin(resources.getDimensionPixelOffset(R.dimen.dp_15))
            .setScrollDuration(800)
            .setRevealWidth(leftRevealWidth, rightRevealWidth)
            .setPageStyle(pageStyle)
            .create(getPicList(4))
    }

    /**
     * Multi Page Style 1
     */
    private fun setupMultiPageBanner() {
        mViewPager
            .setPageMargin(resources.getDimensionPixelOffset(R.dimen.dp_10))
            .setRevealWidth(resources.getDimensionPixelOffset(R.dimen.dp_10))
            .create(getPicList(4))
        mViewPager.removeDefaultPageTransformer()
    }

    /**
     * Multi Page Style 2
     */
    private fun setupRightPageReveal() {
        mViewPager
            .setPageMargin(resources.getDimensionPixelOffset(R.dimen.dp_10))
            .setRevealWidth(0, resources.getDimensionPixelOffset(R.dimen.dp_30))
            .create(getPicList(4))
        mViewPager.removeDefaultPageTransformer()
    }

    /**
     * QQ Music Banner Style
     */
    @SuppressLint("ResourceAsColor")
    private fun setNetEaseMusicStyle() {
        mViewPager
            .setPageMargin(resources.getDimensionPixelOffset(R.dimen.dp_20))
            .setRevealWidth(resources.getDimensionPixelOffset(R.dimen.dp_m_10))
            .setIndicatorSliderColor(
                R.color.egray40,
                R.color.lblue40)
            .setInterval(5000).create(getPicList(4))
        mViewPager.removeDefaultPageTransformer()
    }

    /**
     * NetEase Music Banner Style
     */
    @SuppressLint("ResourceAsColor")
    private fun setQQMusicStyle() {
        mViewPager
            .setPageMargin(resources.getDimensionPixelOffset(R.dimen.dp_15))
            .setRevealWidth(BannerUtils.dp2px(0f))

            .setInterval(5000).create(getPicList(4))

        mViewPager.removeDefaultPageTransformer()
    }

    private fun itemClick(position: Int) {
        if (position != mViewPager.currentItem) {
            mViewPager.setCurrentItem(position, true)
        }
    }

    companion object {
        val instance: HomeBannerFragment
            get() = HomeBannerFragment()
    }
}