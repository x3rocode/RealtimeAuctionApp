package com.esteel4u.realtimeauctionapp.view.ui.fragments

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.esteel4u.realtimeauctionapp.R
import com.esteel4u.realtimeauctionapp.data.model.ProductData
import com.esteel4u.realtimeauctionapp.data.model.UserData
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
import com.varunest.sparkbutton.SparkEventListener
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
import kotlinx.android.synthetic.main.item_product_list.view.*
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

class HomeFragment  : Fragment(),
    ProductListAdapter.Interaction {

    companion object {
        fun newInstance(position: Int): HomeFragment {
            val instance =
                HomeFragment()
            val args = Bundle()
            args.putInt("position", position)
            instance.arguments = args
            return instance
        }
    }
    private val viewModel: ProductViewModel by activityViewModels { ProductViewModel.Factory(viewLifecycleOwner) }
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var todayAdapter : ProductListAdapter
    private lateinit var dataStore: DataStoreModule
    lateinit var prdList: List<ProductData>
    private val loadingDuration: Long
        get() = (resources.getInteger(R.integer.loadingAnimDuration)  / animationPlaybackSpeed).toLong()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(ContentValues.TAG, "-------------------oncreateview home " )
        prdList = arrayListOf()
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBanner()
        initRecyclerView()

        dataStore = DataStoreModule(view.context)
        CoroutineScope(Dispatchers.Main).launch {
            dataStore.user.collect{
                hello_txt.setText("Hi, "+ it.userName)
            }
        }

        viewModel.getTodayPrdList().observe(viewLifecycleOwner, Observer{
            todayAdapter.setData(it!!)
        })
    }

    private fun setBanner() {
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
            setPageMargin(resources.getDimensionPixelOffset(R.dimen.dp_10))
            setRevealWidth(BannerUtils.dp2px(0f))
            setIndicatorMargin(0,0,0,resources.getDimensionPixelOffset(R.dimen.dp_40))
            stopLoopWhenDetachedFromWindow(true)
            create(getBannerList(4) as List<Nothing>?)
        }
    }

    private fun itemClick(position: Int) {
        if (position != banner_view.currentItem) {
            banner_view.setCurrentItem(position, true)
        }
    }

    // Method #4
    private fun initRecyclerView() {

        binding.todayRecyclerView.apply {
            todayAdapter = ProductListAdapter(
                prdList,
                this@HomeFragment,
                this.context
            )
            layoutManager = LinearLayoutManager( this@HomeFragment.context)
            adapter = todayAdapter
        }
    }

    private fun setUserNameFromDataStore(){
        dataStore = DataStoreModule(this.requireContext())
        CoroutineScope(Dispatchers.Main).launch {
            dataStore.user.collect{
                hello_txt.setText("Hi, "+ it.userName)
            }
        }
    }

    private fun setPrdListObserver(){
        viewModel.getTodayPrdList().observe(viewLifecycleOwner, Observer{
            todayAdapter.setData(it!!)
        })
    }


    private var bannerViewList: MutableList<Int> = ArrayList()
    private fun getBannerList(count: Int): MutableList<Int> {
        bannerViewList.clear()
        for (i in 0..count) {
            val drawable = resources.getIdentifier("advertise$i", "drawable", this.requireContext().packageName)
            bannerViewList.add(drawable)
        }
        return bannerViewList;
    }

    @ColorInt
    fun getColor(@ColorRes colorRes: Int): Int {
        return ContextCompat.getColor(requireContext(), colorRes)
    }


    override fun OnLikeButtonClickListener(v: View, prd: ProductData) {
        viewModel.updateUserLikePrdList(v.spark_button.isChecked, prd);
    }

//    override fun OnLikeButtonClickListener(v: View, prd: ProductData) {
//
//        viewModel.updateUserLikePrdList(prd)
//    }


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
}

