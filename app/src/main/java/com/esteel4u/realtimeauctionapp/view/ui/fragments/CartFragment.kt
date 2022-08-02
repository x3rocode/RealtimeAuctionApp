package com.esteel4u.realtimeauctionapp.view.ui.fragments

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
import com.esteel4u.realtimeauctionapp.model.data.ProductData
import com.esteel4u.realtimeauctionapp.databinding.FragmentCartBinding
import com.esteel4u.realtimeauctionapp.model.data.AuctionData
import com.esteel4u.realtimeauctionapp.view.adapter.CartBidSuccessAdapter
import com.esteel4u.realtimeauctionapp.view.adapter.CartListAdapter
import com.esteel4u.realtimeauctionapp.view.ui.activities.BidActivity
import com.esteel4u.realtimeauctionapp.viewmodel.AuctionViewModel
import com.esteel4u.realtimeauctionapp.viewmodel.ProductViewModel
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

        viewModel.getPurchasePrdList().observe(viewLifecycleOwner, Observer {
            madapter.setData(it!!)
            if(it.isNotEmpty()) {

//                cartAdapter.setData(it!!)
//                sad_txt.visibility = View.GONE
//                lottie_img.visibility = View.GONE
//                cart_recycler_view.visibility = View.VISIBLE
//                bid_success_recview.visibility = View.VISIBLE
//                my_bid_txt.visibility = View.VISIBLE
//                bid_txt_desc.visibility = View.VISIBLE
//                my_cart_txt.visibility = View.VISIBLE
//                cart_txt_desc.visibility = View.VISIBLE

            }else{
//                sad_txt.visibility = View.VISIBLE
//                lottie_img.visibility = View.VISIBLE
//                cart_recycler_view.visibility = View.GONE
//                bid_success_recview.visibility = View.GONE
//                my_bid_txt.visibility = View.GONE
//                bid_txt_desc.visibility = View.GONE
//                my_cart_txt.visibility = View.GONE
//                cart_txt_desc.visibility = View.GONE
            }
        })

        viewModel.getUserBidPrdList().observe(viewLifecycleOwner, Observer {
            Log.d("assvsdvsdv", it.size.toString())
            //cartAdapter.productList = it.toMutableList()
            cartAdapter.setData(it)
        })
        auctionViewModel.getAllAuctionList().observe(viewLifecycleOwner, Observer{
            Log.d("assvsdvsdv22", it.toString())
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
    }

    override fun OnBidButtonClickListener(v: View, p: ProductData) {
        val intent = Intent(activity, BidActivity::class.java)
        intent.putExtra("prddata", p.prdId)
        startActivity(intent)
    }
}