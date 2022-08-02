package com.esteel4u.realtimeauctionapp.view.ui.fragments

import android.animation.ValueAnimator
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.esteel4u.realtimeauctionapp.R
import com.esteel4u.realtimeauctionapp.model.data.ProductData
import com.esteel4u.realtimeauctionapp.databinding.FragmentLikeBinding
import com.esteel4u.realtimeauctionapp.view.adapter.ProductListAdapter
import com.esteel4u.realtimeauctionapp.view.adapter.animationPlaybackSpeed
import com.esteel4u.realtimeauctionapp.view.ui.activities.BidActivity
import com.esteel4u.realtimeauctionapp.viewmodel.ProductViewModel
import kotlinx.android.synthetic.main.fragment_like.*
import kotlinx.android.synthetic.main.item_product_list.view.*

class LikeFragment : Fragment(),
    ProductListAdapter.Interaction {
    companion object {
        fun newInstance(position: Int): LikeFragment {
            val instance =
                LikeFragment()
            val args = Bundle()
            args.putInt("position", position)
            instance.arguments = args
            return instance
        }
    }
    private val viewModel: ProductViewModel by activityViewModels { ProductViewModel.Factory(viewLifecycleOwner) }
    private var _binding : FragmentLikeBinding? = null
    private val binding get() = _binding!!
    private lateinit var todayAdapter : ProductListAdapter
    lateinit var prdList: List<ProductData>
    private val loadingDuration: Long
        get() = (resources.getInteger(R.integer.loadingAnimDuration)  / animationPlaybackSpeed).toLong()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prdList = arrayListOf()
        Log.d(ContentValues.TAG, "-------------------oncreateview like " )
        _binding = FragmentLikeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(ContentValues.TAG, "-------------------onviewcreated like " )
        initRecyclerView()
//
//        binding.likeRecyclerView.apply {
//            layoutManager = LinearLayoutManager(activity)
//            adapter = todayAdapter
//        }


        viewModel.getUserLikePrdList().observe(viewLifecycleOwner, Observer{
            if(it.isNotEmpty()){
                todayAdapter.setData(it!!)
                sad_txt.visibility = View.GONE
                lottie_img.visibility = View.GONE
                like_recycler_view.visibility = View.VISIBLE
            }else{
                sad_txt.visibility = View.VISIBLE
                lottie_img.visibility = View.VISIBLE
                like_recycler_view.visibility = View.GONE
            }

        })
    }
    // Method #4
    private fun initRecyclerView() {
        binding.likeRecyclerView.apply {
            todayAdapter = ProductListAdapter(
                prdList,
                this@LikeFragment,
                this.context
            )
            layoutManager = LinearLayoutManager( this@LikeFragment.context)
            adapter = todayAdapter
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    //Update RecyclerView Item Animation Durations
    private fun updateRecyclerViewAnimDuration() = binding.likeRecyclerView.itemAnimator?.run {
        removeDuration = loadingDuration * 60 / 100
        addDuration = loadingDuration
    }

    //Update RecyclerView Item Animation Durations
    fun getAdapterScaleDownAnimator(isScaledDown: Boolean): ValueAnimator =
        todayAdapter.getScaleDownAnimator(isScaledDown)


    override fun OnLikeButtonClickListener(v: View, prd: ProductData) {
        viewModel.updateUserLikePrdList(v.spark_button.isChecked, prd);
    }

    override fun OnBidButtonClickListener(v: View, p: ProductData) {
        val intent = Intent(activity, BidActivity::class.java)
        startActivity(intent)
    }

//    override fun OnLikeButtonClickListener(v: View, prd: ProductData) {
//        viewModel.updateUserLikePrdList(prd);
//    }

}