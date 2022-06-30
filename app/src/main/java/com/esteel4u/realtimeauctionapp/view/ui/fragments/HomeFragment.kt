package com.esteel4u.realtimeauctionapp.view.ui.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.esteel4u.realtimeauctionapp.R
import com.esteel4u.realtimeauctionapp.view.adapter.ViewBindingSampleAdapter
import com.esteel4u.realtimeauctionapp.view.utils.FigureIndicatorView
import com.zhpan.bannerview.BannerViewPager
import com.zhpan.bannerview.annotation.APageStyle
import com.zhpan.bannerview.constants.IndicatorGravity
import com.zhpan.bannerview.constants.PageStyle
import com.zhpan.bannerview.indicator.DrawableIndicator
import com.zhpan.bannerview.utils.BannerUtils
import com.zhpan.indicator.IndicatorView
import com.zhpan.indicator.base.IIndicator
import com.zhpan.indicator.enums.IndicatorSlideMode
import java.util.*

class HomeFragment  : BaseFragment() {
    private lateinit var mViewPager: BannerViewPager<Int>
    private var mIndicatorView: IndicatorView? = null
    override val layout: Int
        get() = R.layout.fragment_home

    override fun initTitle() {}
    override fun initView(savedInstanceState: Bundle?, view: View) {
        mViewPager = view.findViewById(R.id.banner_view)
       // mIndicatorView = view.findViewById(R.id.indicator_view)
        initBVP()
//        setupBanner(
//            PageStyle.MULTI_PAGE_OVERLAP,
//            resources.getDimensionPixelOffset(R.dimen.dp_100)
//        )
       // setDrawableIndicator(getDrawableIndicator()!!)
        setQQMusicStyle()

    }

    private fun initBVP() {
        mViewPager.apply {
            setLifecycleRegistry(lifecycle)
            adapter = ViewBindingSampleAdapter(resources.getDimensionPixelOffset(R.dimen.dp_8))
            setIndicatorSliderColor(
                getColor(R.color.egray30),
                getColor(R.color.lblue30)
            )
//            setIndicatorSliderRadius(
//                resources.getDimensionPixelOffset(R.dimen.dp_4),
//                resources.getDimensionPixelOffset(R.dimen.dp_5)
//            )
            setOnPageClickListener { _: View, position: Int -> itemClick(position) }
            setInterval(5000)
        }
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

    private fun setDrawableIndicator(indicator: IIndicator) {
        mIndicatorView?.setVisibility(View.INVISIBLE)
        mViewPager
            .setIndicatorView(indicator)
            .setIndicatorSlideMode(IndicatorSlideMode.NORMAL)
            .setIndicatorVisibility(View.VISIBLE)
            .setIndicatorGravity(IndicatorGravity.CENTER)
    }
    private fun getDrawableIndicator(): IIndicator? {
        val dp10 = resources.getDimensionPixelOffset(R.dimen.dp_10)
        return DrawableIndicator(context)
            .setIndicatorGap(resources.getDimensionPixelOffset(R.dimen.dp_2_5))
            .setIndicatorDrawable(R.drawable.onboarding_selected_dot_blue, R.drawable.onboarding_unselected_dot_blue)

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

    /**
     * 这里可以是自定义的Indicator，需要继承BaseIndicatorView或者实现IIndicator接口;
     */
//    private fun setupIndicatorView(): IIndicator? {
//        val indicatorView = FigureIndicatorView(getMContext())
//        indicatorView.setRadius(resources.getDimensionPixelOffset(R.dimen.dp_18))
//        indicatorView.setTextSize(resources.getDimensionPixelSize(R.dimen.sp_13))
//        indicatorView.setBackgroundColor(Color.parseColor("#aa118EEA"))
//        return indicatorView
//    }

    private fun updateData() {
        mViewPager.refreshData(getPicList(Random().nextInt(5) - 1))
    }

    private fun itemClick(position: Int) {
        if (position != mViewPager.currentItem) {
            mViewPager.setCurrentItem(position, true)
        }
    }

    companion object {
        val instance: HomeFragment
            get() = HomeFragment()
    }
}