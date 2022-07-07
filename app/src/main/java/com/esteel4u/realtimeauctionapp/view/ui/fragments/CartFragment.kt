package com.esteel4u.realtimeauctionapp.view.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.esteel4u.realtimeauctionapp.databinding.FragmentCartBinding
import com.esteel4u.realtimeauctionapp.databinding.FragmentHomeBinding

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
    private lateinit var binding: FragmentCartBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }
}