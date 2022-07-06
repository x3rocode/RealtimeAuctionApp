package com.esteel4u.realtimeauctionapp.view.ui.fragments

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.ContentValues
import android.graphics.Color
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.esteel4u.realtimeauctionapp.R
import com.esteel4u.realtimeauctionapp.data.model.ProductData
import com.esteel4u.realtimeauctionapp.data.model.TestData
import com.esteel4u.realtimeauctionapp.databinding.FragmentHomeBinding
import com.esteel4u.realtimeauctionapp.databinding.FragmentLikeBinding
import com.esteel4u.realtimeauctionapp.view.adapter.ProductListAdapter
import com.esteel4u.realtimeauctionapp.view.adapter.ViewBindingSampleAdapter
import com.esteel4u.realtimeauctionapp.view.adapter.animationPlaybackSpeed
import com.esteel4u.realtimeauctionapp.viewmodel.ProductViewModel

import com.google.firebase.Timestamp
import com.google.firebase.Timestamp.now
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.type.DateTime
import com.ptrbrynt.firestorelivedata.FirestoreResource
import com.ptrbrynt.firestorelivedata.asLiveData
import com.ptrbrynt.firestorelivedata.observe
import com.returnz3ro.messystem.service.model.datastore.DataStoreModule
import com.returnz3ro.messystem.service.model.datastore.DataStoreModule.Companion.userName
import com.zhpan.bannerview.BannerViewPager
import com.zhpan.bannerview.annotation.APageStyle
import com.zhpan.bannerview.constants.IndicatorGravity
import com.zhpan.bannerview.constants.PageStyle
import com.zhpan.bannerview.indicator.DrawableIndicator
import com.zhpan.bannerview.utils.BannerUtils
import com.zhpan.indicator.IndicatorView
import com.zhpan.indicator.base.IIndicator
import com.zhpan.indicator.enums.IndicatorSlideMode
import com.zhpan.indicator.enums.IndicatorStyle
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.checkerframework.common.value.qual.IntVal
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDate.now
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.chrono.ChronoLocalDateTime

import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

class HomeFragment  : Fragment(){
    private val viewModel: ProductViewModel by activityViewModels { ProductViewModel.Factory(viewLifecycleOwner) }
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var todayAdapter : ProductListAdapter
    private lateinit var dataStore: DataStoreModule

    private val loadingDuration: Long
        get() = (resources.getInteger(R.integer.loadingAnimDuration)  / animationPlaybackSpeed).toLong()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(ContentValues.TAG, "-------------------oncreateview home " )
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(ContentValues.TAG, "-------------------onviewcreated home " )
        initBVP()
        setQQMusicStyle()
        todayAdapter = ProductListAdapter(view.context)
        // get userinfo from data store

        dataStore = DataStoreModule(view.context)
        CoroutineScope(Dispatchers.Main).launch {
            dataStore.user.collect{
                hello_txt.setText("Hi, "+ it.userName)
            }
        }

        binding.todayRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = todayAdapter
        }


        viewModel.getTodayPrdList().observe(viewLifecycleOwner, Observer{
            (binding.todayRecyclerView.adapter as ProductListAdapter).setData(it!!)
        })

    }


    //Update RecyclerView Item Animation Durations
    private fun updateRecyclerViewAnimDuration() = binding.todayRecyclerView.itemAnimator?.run {
        removeDuration = loadingDuration * 60 / 100
        addDuration = loadingDuration
    }

    //Update RecyclerView Item Animation Durations
    fun getAdapterScaleDownAnimator(isScaledDown: Boolean): ValueAnimator =
        todayAdapter.getScaleDownAnimator(isScaledDown)

//    override fun initTitle() {}
//    override fun initView(savedInstanceState: Bundle?, view: View) {
//        mbannerViewPager = view.findViewById(R.id.banner_view)
//       // mtypeSliderViewPager = view.findViewById(R.id.types_card_view)
//
//        initBVP()
//        //initSPN()
//        setQQMusicStyle()
//        //setupRightPageReveal()
//
//    }

    private fun initBVP() {
        banner_view.apply {
            setLifecycleRegistry(lifecycle)
            adapter = ViewBindingSampleAdapter(R.layout.item_home_banner_model)
            setIndicatorSliderColor(getColor(R.color.egray30),getColor(R.color.lblue60))
            setIndicatorSliderRadius(
                resources.getDimensionPixelOffset(R.dimen.dp_4),
                resources.getDimensionPixelOffset(R.dimen.dp_4))
            setIndicatorStyle(IndicatorStyle.ROUND_RECT)
            setOnPageClickListener { _: View, position: Int -> itemClick(position) }
            setInterval(5000)
            setOffScreenPageLimit(4)

        }


    }

//    private fun initSPN() {
//        mtypeSliderViewPager.apply {
//            setLifecycleRegistry(lifecycle)
//            adapter = ViewBindingSampleAdapter(R.layout.item_home_type_slider_model)
//            setIndicatorVisibility(GONE)
//            setOnPageClickListener { _: View, position: Int -> itemClick(position) }
//            setInterval(5000)
//            setScrollDuration(0)
//        }
//    }


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
        banner_view
            .setPageMargin(resources.getDimensionPixelOffset(R.dimen.dp_15))
            .setScrollDuration(800)
            .setRevealWidth(leftRevealWidth, rightRevealWidth)
            .setPageStyle(pageStyle)
    }

//    private fun setupTypeSlider(@APageStyle pageStyle: Int, leftRevealWidth: Int, rightRevealWidth: Int){
//        mtypeSliderViewPager
//            .setPageMargin(resources.getDimensionPixelOffset(R.dimen.dp_15))
//            .setScrollDuration(800)
//            .setPageStyle(pageStyle)
//            .setRevealWidth(leftRevealWidth, rightRevealWidth)
//            .create(getPicList(4))
//    }



    private fun setDrawableIndicator(indicator: IIndicator) {
        banner_view
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
        banner_view
            .setPageMargin(resources.getDimensionPixelOffset(R.dimen.dp_10))
            .setRevealWidth(resources.getDimensionPixelOffset(R.dimen.dp_10))
        banner_view.removeDefaultPageTransformer()
    }

    /**
     * Multi Page Style 2
     */
//    private fun setupRightPageReveal() {
//        mtypeSliderViewPager
//            .setPageMargin(resources.getDimensionPixelOffset(R.dimen.dp_3 )   )
//            .setRevealWidth(resources.getDimensionPixelOffset(R.dimen.dp_7), resources.getDimensionPixelOffset(R.dimen.dp_210) )
//            .setInterval(5000)
//            .setAutoPlay(false)
//            .setOffScreenPageLimit(1)
//
//
//            .create(getPicList(4))
//        mtypeSliderViewPager.removeDefaultPageTransformer()
//    }

    /**
     * QQ Music Banner Style
     */
    @SuppressLint("ResourceAsColor")
    private fun setNetEaseMusicStyle() {
        banner_view
            .setPageMargin(resources.getDimensionPixelOffset(R.dimen.dp_20))
            .setIndicatorMargin(0,0,0,60)
            .setRevealWidth(resources.getDimensionPixelOffset(R.dimen.dp_m_10))
            .setIndicatorSliderColor(
                R.color.egray30,
                R.color.lblue50)

        banner_view.removeDefaultPageTransformer()
    }

    /**
     * NetEase Music Banner Style
     */

    private fun setQQMusicStyle() {
        banner_view
            .setPageMargin(resources.getDimensionPixelOffset(R.dimen.dp_10))
            .setRevealWidth(BannerUtils.dp2px(0f))
            .setIndicatorMargin(0,0,0,resources.getDimensionPixelOffset(R.dimen.dp_40))
            .setInterval(5000)
            .stopLoopWhenDetachedFromWindow(true)
            .setIndicatorSliderColor(getColor(R.color.egray30),getColor(R.color.lblue60))
            .create(getPicList(4) as List<Nothing>?)
        banner_view.removeDefaultPageTransformer()
    }

    private var mPictureList: MutableList<Int> = ArrayList()
    fun getPicList(count: Int): MutableList<Int> {
        mPictureList.clear()
        for (i in 0..count) {
            val drawable = resources.getIdentifier("advertise$i", "drawable", this.requireContext().packageName)
            mPictureList.add(drawable)
        }
        return mPictureList;
    }
    @ColorInt
    protected fun getColor(@ColorRes colorRes: Int): Int {
        return ContextCompat.getColor(requireContext(), colorRes)
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



    private fun itemClick(position: Int) {
        if (position != banner_view.currentItem) {
            banner_view.setCurrentItem(position, true)
        }
    }

}

