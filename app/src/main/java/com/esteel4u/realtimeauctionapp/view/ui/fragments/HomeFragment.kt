package com.esteel4u.realtimeauctionapp.view.ui.fragments

import android.animation.ValueAnimator
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.esteel4u.realtimeauctionapp.R
import com.esteel4u.realtimeauctionapp.model.data.ProductData
import com.esteel4u.realtimeauctionapp.databinding.FragmentHomeBinding
import com.esteel4u.realtimeauctionapp.service.AlarmService
import com.esteel4u.realtimeauctionapp.service.ScheduledWorker
import com.esteel4u.realtimeauctionapp.view.adapter.ProductListAdapter
import com.esteel4u.realtimeauctionapp.view.adapter.ViewBindingSampleAdapter
import com.esteel4u.realtimeauctionapp.view.adapter.animationPlaybackSpeed
import com.esteel4u.realtimeauctionapp.view.ui.activities.BidActivity
import com.esteel4u.realtimeauctionapp.viewmodel.ProductViewModel
import com.esteel4u.realtimeauctionapp.model.datastore.DataStoreModule
import com.zhpan.bannerview.constants.PageStyle
import com.zhpan.bannerview.utils.BannerUtils
import com.zhpan.indicator.enums.IndicatorStyle
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_product_list.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nl.bryanderidder.themedtogglebuttongroup.ThemedButton
import java.util.*

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


    private var bannerViewList: MutableList<Int> = ArrayList()
    private var mDrawableList: MutableList<Int> = ArrayList()

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

        viewModel.getTodayPrdList().observe(viewLifecycleOwner, Observer{
            prdList = it
            todayAdapter.setData(prdList!!)
            prdList.forEach{
                addAlarm(it.startDate!!.toDate(), it.prdId!!, "s")
                addAlarm(it.endDate!!.toDate(), it.prdId!!, "e")
            }
        })


        binding.buttongroup.setOnSelectListener { button: ThemedButton ->
            if(button.isSelected){
                when(button.id){
                    primium_btn.id -> todayAdapter.setData(prdList.filter {
                        it.auctionType == 1
                    })
                    auction_btn.id -> todayAdapter.setData(prdList.filter {
                        it.auctionType == 2
                    })
                    outlet_btn.id -> todayAdapter.setData(prdList.filter {
                        it.auctionType == 3
                    })
                    package_btn.id -> todayAdapter.setData(prdList.filter {
                        it.auctionType == 4
                    })
                }
            }
            else{
                todayAdapter.setData(prdList)
            }

        }

    }

    fun addAlarm(date: Date, id: String, code: String){
        var alarmManager = this.requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        var intent = Intent(this.requireContext(), AlarmService::class.java)
        intent.putExtra(ScheduledWorker.NOTIFICATION_MESSAGE, id)
        intent.putExtra(ScheduledWorker.NOTIFICATION_TITLE, code)
        var pIntent = PendingIntent.getBroadcast(this.requireContext(), if(code === "s") id.hashCode() + 1 else id.hashCode() + 2 , intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)

        val cal = Calendar.getInstance()
        var nowCalendar = Calendar.getInstance()
        cal.time = date
        if (cal.before(nowCalendar) || nowCalendar.time == cal.time) {
            //!이미 지난 시간 일 경우

        }else{
            Log.d(ContentValues.TAG, " bbbbbbbbbbbbbbbbbbbbbb " )
            when {

                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pIntent)
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pIntent)
                else -> alarmManager.set(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pIntent)
            }
        }

    }

    private fun setBanner() {
        banner_view.apply {
            setLifecycleRegistry(lifecycle)
            adapter = ViewBindingSampleAdapter(R.layout.item_home_banner_model)
            setIndicatorSliderColor(getColor(R.color.egray20),getColor(R.color.lblue30))
            setIndicatorSliderRadius(
                resources.getDimensionPixelOffset(R.dimen.dp_3),
                resources.getDimensionPixelOffset(R.dimen.dp_3))
            setIndicatorStyle(IndicatorStyle.CIRCLE)
            setPageStyle(PageStyle.MULTI_PAGE)
            setOnPageClickListener { _: View, position: Int -> itemClick(position) }
            setInterval(5000)
            setOffScreenPageLimit(4)
            setPageMargin(resources.getDimensionPixelOffset(R.dimen.dp_30))
            setRevealWidth(BannerUtils.dp2px(0f))
            setIndicatorMargin(0,0,0,resources.getDimensionPixelOffset(R.dimen.dp_10))
            stopLoopWhenDetachedFromWindow(true)
            create(getBannerList(3) as List<Nothing>?)
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



    private fun getBannerList(count: Int): MutableList<Int> {
        bannerViewList.clear()
        bannerViewList.add( resources.getIdentifier("premiumbg_n", "drawable", this.requireContext().packageName))
        bannerViewList.add( resources.getIdentifier("auctionbg_n", "drawable", this.requireContext().packageName))
        bannerViewList.add( resources.getIdentifier("outletbg_n", "drawable", this.requireContext().packageName))
        bannerViewList.add( resources.getIdentifier("packagebg_n", "drawable", this.requireContext().packageName))
//        for (i in 0..count) {
//            val drawable = resources.getIdentifier("advertise$i", "drawable", this.requireContext().packageName)
//            bannerViewList.add(drawable)
//        }
        return bannerViewList;
    }


    private fun initData(j: Int) {
        mDrawableList.clear()
        for (i in 0..j) {
            val drawable = resources.getIdentifier("bg_card$i", "drawable", this.requireContext().packageName)
            mDrawableList.add(drawable)
        }
    }

    @ColorInt
    fun getColor(@ColorRes colorRes: Int): Int {
        return ContextCompat.getColor(requireContext(), colorRes)
    }


    override fun OnLikeButtonClickListener(v: View, prd: ProductData) {
        viewModel.updateUserLikePrdList(v.spark_button.isChecked, prd)
    }

    override fun OnBidButtonClickListener(v: View, p: ProductData) {
        val intent = Intent(activity, BidActivity::class.java)
        intent.putExtra("prddata", p.prdId)
        startActivity(intent)
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

