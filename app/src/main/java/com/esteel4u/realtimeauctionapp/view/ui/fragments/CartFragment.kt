package com.esteel4u.realtimeauctionapp.view.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.esteel4u.realtimeauctionapp.data.model.ProductData
import com.esteel4u.realtimeauctionapp.databinding.FragmentCartBinding
import com.esteel4u.realtimeauctionapp.view.adapter.CartBidSuccessAdapter
import com.esteel4u.realtimeauctionapp.view.adapter.CartListAdapter
import com.esteel4u.realtimeauctionapp.view.adapter.ProductListAdapter
import com.esteel4u.realtimeauctionapp.viewmodel.ProductViewModel
import com.returnz3ro.messystem.service.model.datastore.DataStoreModule
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_like.*

class CartFragment: Fragment() {
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
    private var _binding : FragmentCartBinding? = null
    private val binding get() = _binding!!

    private lateinit var madapter : CartBidSuccessAdapter
    private lateinit var cartAdapter: CartListAdapter
    lateinit var prdList: List<ProductData>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prdList = arrayListOf()
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()

        viewModel.getPurchasePrdList().observe(viewLifecycleOwner, Observer {
            if(it.isNotEmpty()) {
                madapter.setData(it!!)
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
            cartAdapter.setData(it)
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
                prdList,
                this.context
            )
            layoutManager = LinearLayoutManager( this@CartFragment.context)
            adapter = cartAdapter
        }
    }


}