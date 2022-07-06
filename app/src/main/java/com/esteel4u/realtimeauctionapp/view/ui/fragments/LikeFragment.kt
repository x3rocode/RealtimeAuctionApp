package com.esteel4u.realtimeauctionapp.view.ui.fragments

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.esteel4u.realtimeauctionapp.R
import com.esteel4u.realtimeauctionapp.databinding.FragmentHomeBinding
import com.esteel4u.realtimeauctionapp.databinding.FragmentLikeBinding
import com.esteel4u.realtimeauctionapp.databinding.FragmentListBinding
import com.esteel4u.realtimeauctionapp.view.adapter.ProductListAdapter
import com.esteel4u.realtimeauctionapp.view.adapter.animationPlaybackSpeed
import com.esteel4u.realtimeauctionapp.viewmodel.ProductViewModel

class LikeFragment : Fragment() {
    private var _binding : FragmentLikeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductViewModel by activityViewModels { ProductViewModel.Factory(viewLifecycleOwner) }
    private lateinit var todayAdapter : ProductListAdapter

    private val loadingDuration: Long
        get() = (resources.getInteger(R.integer.loadingAnimDuration)  / animationPlaybackSpeed).toLong()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLikeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        todayAdapter = ProductListAdapter(view.context)

        binding.likeRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = todayAdapter
        }


        viewModel.getUserLikePrdList().observe(viewLifecycleOwner, Observer{
            (binding.likeRecyclerView.adapter as ProductListAdapter).setData(it!!)
        })
    }

    //Update RecyclerView Item Animation Durations
    private fun updateRecyclerViewAnimDuration() = binding.likeRecyclerView.itemAnimator?.run {
        removeDuration = loadingDuration * 60 / 100
        addDuration = loadingDuration
    }

    //Update RecyclerView Item Animation Durations
    fun getAdapterScaleDownAnimator(isScaledDown: Boolean): ValueAnimator =
        todayAdapter.getScaleDownAnimator(isScaledDown)

}