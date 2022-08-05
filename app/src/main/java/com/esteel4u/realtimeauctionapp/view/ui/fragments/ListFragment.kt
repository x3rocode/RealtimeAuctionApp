package com.esteel4u.realtimeauctionapp.view.ui.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.esteel4u.realtimeauctionapp.R
import com.esteel4u.realtimeauctionapp.model.data.ProductData
import com.esteel4u.realtimeauctionapp.databinding.FragmentListBinding
import com.esteel4u.realtimeauctionapp.view.adapter.ProductListAdapter
import com.esteel4u.realtimeauctionapp.view.ui.activities.BidActivity
import com.esteel4u.realtimeauctionapp.viewmodel.ProductViewModel
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener
import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_product_list.view.*
import org.joda.time.DateTime
import java.util.*

class ListFragment : Fragment() , DatePickerListener
    , ProductListAdapter.Interaction{
    companion object {
        fun newInstance(position: Int): ListFragment {
            val instance =
                ListFragment()
            val args = Bundle()
            args.putInt("position", position)
            instance.arguments = args
            return instance
        }
    }

    private val viewModel: ProductViewModel by activityViewModels { ProductViewModel.Factory(viewLifecycleOwner) }
    private var _binding : FragmentListBinding? = null
    private val binding get() = _binding!!


    private lateinit var alladapter : ProductListAdapter
    private lateinit var prdList: List<ProductData>
    private val calendar = Calendar.getInstance()
    private var currentMonth = 0
    private lateinit var picker: HorizontalPicker

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prdList = arrayListOf()
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()


        picker =  binding.calendarView
        picker
            .setListener(this)
            .setDateSelectedColor(getColor(R.color.lblue60))
            .setDateSelectedTextColor(Color.WHITE)
            .setMonthAndYearTextColor(Color.DKGRAY)
            .setTodayButtonTextColor(getColor(R.color.egray70))
            .setTodayDateTextColor(getColor(R.color.white))
            .setTodayDateBackgroundColor(getColor(R.color.egray40))
            .setUnselectedDayTextColor(getColor(R.color.egray70))
            .setDayOfWeekTextColor(getColor(R.color.egray70))
            .showTodayButton(true)
            .init()

        picker.setDate(DateTime().plusDays(0))

        viewModel.getPrdListByDate(DateTime.now()).observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            prdList = it
            alladapter.setData(prdList!!)
        })
    }


    // Method #4
    private fun initRecyclerView() {

        binding.likeRecyclerView.apply {
            alladapter = ProductListAdapter(
                prdList,
                this@ListFragment,
                this.context
            )
            layoutManager = LinearLayoutManager( this@ListFragment.context)
            adapter = alladapter
        }
    }

    private fun getDatesOfNextMonth(): List<Date> {
        currentMonth++ // + because we want next month
        if (currentMonth == 12) {
            // we will switch to january of next year, when we reach last month of year
            calendar.set(Calendar.YEAR, calendar[Calendar.YEAR] + 1)
            currentMonth = 0 // 0 == january
        }
        return getDates(mutableListOf())
    }

    private fun getDatesOfPreviousMonth(): List<Date> {
        currentMonth-- // - because we want previous month
        if (currentMonth == -1) {
            // we will switch to december of previous year, when we reach first month of year
            calendar.set(Calendar.YEAR, calendar[Calendar.YEAR] - 1)
            currentMonth = 11 // 11 == december
        }
        return getDates(mutableListOf())
    }

    private fun getDatesOfNextWeek(): List<Date> {
        currentMonth++ // + because we want next month
        if (currentMonth == 12) {
            // we will switch to january of next year, when we reach last month of year
            calendar.set(Calendar.YEAR, calendar[Calendar.YEAR] + 1)
            currentMonth = 0 // 0 == january
        }
        return getDates(mutableListOf())
    }

    private fun getFutureDatesOfCurrentMonth(): List<Date> {
        // get all next dates of current month
        currentMonth = calendar[Calendar.MONTH]
        return getDates(mutableListOf())
    }


    private fun getDates(list: MutableList<Date>): List<Date> {
        // load dates of whole month
        calendar.set(Calendar.MONTH, currentMonth)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        list.add(calendar.time)
        while (currentMonth == calendar[Calendar.MONTH]) {
            calendar.add(Calendar.DATE, +1)
            if (calendar[Calendar.MONTH] == currentMonth)
                list.add(calendar.time)
        }
        calendar.add(Calendar.DATE, -1)
        return list
    }

    override fun onDateSelected(dateSelected: DateTime?) {
       // picker.setBackgroundResource(R.drawable.selected_calendar_item_background)
        viewModel.getPrdListByDate(dateSelected!!).observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            prdList = it
            alladapter.setData(prdList!!)
        })
    }

    @ColorInt
    fun getColor(@ColorRes colorRes: Int): Int {
        return ContextCompat.getColor(requireContext(), colorRes)
    }

    override fun OnLikeButtonClickListener(v: View, prd: ProductData) {
        viewModel.updateUserLikePrdList(v.spark_button.isChecked, prd);
    }

    override fun OnBidButtonClickListener(v: View, p: ProductData) {
        val intent = Intent(activity, BidActivity::class.java)
        startActivity(intent)
    }
}