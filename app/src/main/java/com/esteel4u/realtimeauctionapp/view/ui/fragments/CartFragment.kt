package com.esteel4u.realtimeauctionapp.view.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.esteel4u.realtimeauctionapp.databinding.FragmentCartBinding
import com.esteel4u.realtimeauctionapp.model.data.ProductData
import com.esteel4u.realtimeauctionapp.model.data.AuctionData
import com.esteel4u.realtimeauctionapp.view.adapter.CartBidSuccessAdapter
import com.esteel4u.realtimeauctionapp.view.adapter.CartListAdapter
import com.esteel4u.realtimeauctionapp.view.ui.activities.BidActivity
import com.esteel4u.realtimeauctionapp.viewmodel.AuctionViewModel
import com.esteel4u.realtimeauctionapp.viewmodel.ProductViewModel
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import com.maxpilotto.creditcardview.animations.RotationAnimation
import com.maxpilotto.creditcardview.models.CardArea
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_like.*

class CartFragment: Fragment(),
CartListAdapter.Interaction{
    companion object {
        fun newInstance(position: Int): CartFragment {
            val instance =
                CartFragment()
            val args = Bundle()
            args.putInt("position", position)
            instance.arguments = args
            return instance
        }
    }
    private val viewModel: ProductViewModel by activityViewModels { ProductViewModel.Factory(viewLifecycleOwner) }
    private val auctionViewModel: AuctionViewModel by activityViewModels { AuctionViewModel.Factory(viewLifecycleOwner) }
    private var _binding : FragmentCartBinding? = null
    private val binding get() = _binding!!

    private lateinit var madapter : CartBidSuccessAdapter
    private lateinit var cartAdapter: CartListAdapter
    lateinit var prdList: List<ProductData>
    lateinit var prdList1: List<ProductData>
    lateinit var aucList: List<AuctionData>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prdList = arrayListOf()
        prdList1 = arrayListOf()
        aucList = arrayListOf()
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        card.flipOnEdit = true
        card.flipOnEditAnimation = RotationAnimation()
        card.setAreaClickListener { card, area ->
            if (area == CardArea.LEFT) {
                card.flip(
                    RotationAnimation(RotationAnimation.LEFT)
                )
            } else if (area == CardArea.RIGHT) {
                card.flip(
                    RotationAnimation(RotationAnimation.RIGHT)
                )
            }
        }

        viewModel.getPurchasePrdList().observe(viewLifecycleOwner, Observer {
            Log.d("???????????????", it.toString())
            prdList = it
            if(it.isNotEmpty()) {
                madapter.setData(it!!)

                no_success_bid_lottie.visibility = View.INVISIBLE
                no_success_bid_txt.visibility = View.INVISIBLE
                bid_success_recview.visibility = View.VISIBLE
                binding.sadTxt.visibility = View.GONE
                binding.lottieImg.visibility = View.GONE
                binding.card.visibility = View.VISIBLE
                cart_recycler_view.visibility = View.VISIBLE
                my_bid_txt.visibility = View.VISIBLE
                bid_txt_desc.visibility = View.VISIBLE
                my_buy_txt.visibility = View.VISIBLE
                buy_txt_desc.visibility = View.VISIBLE

            }else{
                no_success_bid_lottie.visibility = View.VISIBLE
                no_success_bid_txt.visibility = View.INVISIBLE
                bid_success_recview.visibility = View.GONE
                binding.sadTxt.visibility = View.VISIBLE
                binding.lottieImg.visibility = View.VISIBLE
                binding.card.visibility = View.VISIBLE
                cart_recycler_view.visibility = View.VISIBLE
                my_bid_txt.visibility = View.VISIBLE
                bid_txt_desc.visibility = View.VISIBLE
                my_buy_txt.visibility = View.VISIBLE
                buy_txt_desc.visibility = View.VISIBLE
            }
        })

        viewModel.getUserBidPrdList().observe(viewLifecycleOwner, Observer {
            if(prdList.isEmpty()){
                binding.noSuccessBidLottie.visibility = View.VISIBLE
                binding.noSuccessBidTxt.visibility = View.VISIBLE
                bid_success_recview.visibility = View.GONE
            }else{
                binding.noSuccessBidLottie.visibility = View.INVISIBLE
                binding.noSuccessBidTxt.visibility = View.INVISIBLE
                bid_success_recview.visibility = View.VISIBLE
            }

            if(it.isNotEmpty()) {
                cartAdapter.setData(it)
                Log.d("왜ㅐㅐㅐㅐㅐㅐㅐㅐㅐㅐㅐㅐㅐㅐ", prdList.toString())

                binding.sadTxt.visibility = View.GONE
                binding.lottieImg.visibility = View.GONE
                binding.card.visibility = View.VISIBLE
                cart_recycler_view.visibility = View.VISIBLE
                //bid_success_recview.visibility = View.VISIBLE
                my_bid_txt.visibility = View.VISIBLE
                bid_txt_desc.visibility = View.VISIBLE
                my_buy_txt.visibility = View.VISIBLE
                buy_txt_desc.visibility = View.VISIBLE

            }else{
                binding.noSuccessBidLottie.visibility = View.INVISIBLE
                binding.noSuccessBidTxt.visibility = View.INVISIBLE
                binding.sadTxt.visibility = View.VISIBLE
                binding.lottieImg.visibility = View.VISIBLE
                binding.card.visibility = View.GONE
                cart_recycler_view.visibility = View.GONE
                //bid_success_recview.visibility = View.GONE
                my_bid_txt.visibility = View.GONE
                bid_txt_desc.visibility = View.GONE
                my_buy_txt.visibility = View.GONE
                buy_txt_desc.visibility = View.GONE
            }


        })
        auctionViewModel.getAllAuctionList().observe(viewLifecycleOwner, Observer{

            cartAdapter.setAuctionData(it)
        })
    }

    private fun initRecyclerView() {

        binding.bidSuccessRecview.apply {
            madapter = CartBidSuccessAdapter(
                prdList,
                this.context
            )
            layoutManager = LinearLayoutManager( this@CartFragment.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = madapter
        }

        binding.cartRecyclerView.apply {
            cartAdapter = CartListAdapter(
                prdList1,
                aucList,
                this@CartFragment,
                this.context
            )
            layoutManager = LinearLayoutManager( this@CartFragment.context)
            adapter = cartAdapter
        }

        val snapHelper = GravitySnapHelper(Gravity.START)
        snapHelper.attachToRecyclerView(binding.bidSuccessRecview)
    }

    override fun OnBidButtonClickListener(v: View, p: ProductData) {
        val intent = Intent(activity, BidActivity::class.java)
        intent.putExtra("prddata", p.prdId)
        startActivity(intent)
    }
}