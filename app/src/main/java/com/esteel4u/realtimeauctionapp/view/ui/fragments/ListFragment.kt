package com.esteel4u.realtimeauctionapp.view.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.esteel4u.realtimeauctionapp.databinding.FragmentHomeBinding
import com.esteel4u.realtimeauctionapp.databinding.FragmentLikeBinding
import com.esteel4u.realtimeauctionapp.databinding.FragmentListBinding

class ListFragment : Fragment() {
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

    private lateinit var binding: FragmentListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }
}