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
import com.esteel4u.realtimeauctionapp.databinding.FragmentHomeBinding
import com.esteel4u.realtimeauctionapp.view.adapter.CartListAdapter
import com.esteel4u.realtimeauctionapp.view.adapter.ProductListAdapter
import com.esteel4u.realtimeauctionapp.viewmodel.ProductViewModel
import com.returnz3ro.messystem.service.model.datastore.DataStoreModule

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

    private lateinit var madapter : CartListAdapter
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
            madapter.setData(it!!)
        })
    }

    private fun initRecyclerView() {

        binding.todayRecyclerView.apply {
            madapter = CartListAdapter(
                prdList,
                this.context
            )
            layoutManager = LinearLayoutManager( this@CartFragment.context)
            adapter = madapter
        }
    }


}