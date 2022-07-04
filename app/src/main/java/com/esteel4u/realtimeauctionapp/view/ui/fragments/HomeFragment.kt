package com.esteel4u.realtimeauctionapp.view.ui.fragments

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.ContentValues
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.esteel4u.realtimeauctionapp.R
import com.esteel4u.realtimeauctionapp.data.model.ProductData
import com.esteel4u.realtimeauctionapp.data.model.TestData
import com.esteel4u.realtimeauctionapp.databinding.FragmentHomeBinding
import com.esteel4u.realtimeauctionapp.view.adapter.HomeTodayListAdapter
import com.esteel4u.realtimeauctionapp.view.adapter.ViewBindingSampleAdapter
import com.esteel4u.realtimeauctionapp.view.adapter.animationPlaybackSpeed
import com.esteel4u.realtimeauctionapp.view.utils.FigureIndicatorView
import com.esteel4u.realtimeauctionapp.viewmodel.LoginViewModel
import com.esteel4u.realtimeauctionapp.viewmodel.MainViewModel
import com.esteel4u.realtimeauctionapp.viewmodel.ProductViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.ptrbrynt.firestorelivedata.FirestoreResource
import com.ptrbrynt.firestorelivedata.asLiveData
import com.ptrbrynt.firestorelivedata.observe
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
import java.util.*

class HomeFragment  : Fragment(){
    private lateinit var mbannerViewPager: BannerViewPager<Int>
    //private lateinit var mtypeSliderViewPager: BannerViewPager<Int>
//    private val viewModel: ProductViewModel  by activityViewModels()
    private val db = FirebaseFirestore.getInstance()
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val liveProductData = MutableLiveData<List<ProductData>>()
    private lateinit var todayAdapter : HomeTodayListAdapter

    private val loadingDuration: Long
        get() = (resources.getInteger(R.integer.loadingAnimDuration)  / animationPlaybackSpeed).toLong()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mbannerViewPager = view.findViewById(R.id.banner_view)
        initBVP()
        setQQMusicStyle()
        binding.todayRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = HomeTodayListAdapter(this.context)
        }

        val myQuery = db.collection("products").asLiveData<ProductData>()
        myQuery.observe(viewLifecycleOwner, Observer { resource: FirestoreResource<List<ProductData>> ->
            val data: List<ProductData>? = resource.data // The result of your query or reference, when status is SUCCESS
            Log.d(ContentValues.TAG, "ddfasdfasdfasdf f fdfdf " + data.toString())
            if(data !== null){
                (binding.todayRecyclerView.adapter as HomeTodayListAdapter).setData(data!!)
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    @SuppressLint("ResourceAsColor")
    private fun initBVP() {
        mbannerViewPager.apply {
            setLifecycleRegistry(lifecycle)
            adapter = ViewBindingSampleAdapter(R.layout.item_home_banner_model)
            setIndicatorSliderColor(R.color.egray30,R.color.lblue60
            )
            setIndicatorSliderRadius(
                resources.getDimensionPixelOffset(R.dimen.dp_4),
                resources.getDimensionPixelOffset(R.dimen.dp_4)
            )
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
        mbannerViewPager
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
        mbannerViewPager
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
        mbannerViewPager
            .setPageMargin(resources.getDimensionPixelOffset(R.dimen.dp_10))
            .setRevealWidth(resources.getDimensionPixelOffset(R.dimen.dp_10))
        mbannerViewPager.removeDefaultPageTransformer()
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
        mbannerViewPager
            .setPageMargin(resources.getDimensionPixelOffset(R.dimen.dp_20))
            .setIndicatorMargin(0,0,0,60)
            .setRevealWidth(resources.getDimensionPixelOffset(R.dimen.dp_m_10))
            .setIndicatorSliderColor(
                R.color.egray30,
                R.color.lblue50)

        mbannerViewPager.removeDefaultPageTransformer()
    }

    /**
     * NetEase Music Banner Style
     */
    @SuppressLint("ResourceAsColor")
    private fun setQQMusicStyle() {
        mbannerViewPager
            .setPageMargin(resources.getDimensionPixelOffset(R.dimen.dp_10))
            .setRevealWidth(BannerUtils.dp2px(0f))
            .setIndicatorMargin(0,0,0,resources.getDimensionPixelOffset(R.dimen.dp_40))
            .setInterval(5000)
            .stopLoopWhenDetachedFromWindow(true)
            .create(getPicList(4))
        mbannerViewPager.removeDefaultPageTransformer()
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
        if (position != mbannerViewPager.currentItem) {
            mbannerViewPager.setCurrentItem(position, true)
        }
    }

}

