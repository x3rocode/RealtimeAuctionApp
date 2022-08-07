package com.esteel4u.realtimeauctionapp.view.ui.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import cn.pedant.SweetAlert.SweetAlertDialog
import com.esteel4u.realtimeauctionapp.R
import com.esteel4u.realtimeauctionapp.databinding.FragmentBidBinding
import com.esteel4u.realtimeauctionapp.model.data.ProductData
import com.esteel4u.realtimeauctionapp.viewmodel.AuctionViewModel
import com.esteel4u.realtimeauctionapp.viewmodel.ProductViewModel
import java.text.DecimalFormat
import java.util.*


class BidFragment:  Fragment() {
    private val viewModel: ProductViewModel by activityViewModels { ProductViewModel.Factory(viewLifecycleOwner) }
    private val auctionViewModel: AuctionViewModel by activityViewModels { AuctionViewModel.Factory(viewLifecycleOwner) }
    private var pid: String? = null
    lateinit var prdList: List<ProductData>

    companion object{
        fun newInstance(prddata: String): Fragment {
            val fragment = BidFragment()
            fragment.arguments = Bundle().apply {
                putString("prddata", prddata)
            }
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_bid, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentBidBinding.bind(view)
        pid = arguments?.getString("prddata")
        if(pid !== null){
            Log.d("ㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇㅇ", pid!!)
//        var extra = this.arguments
//        if (extra != null) {
//            extra = arguments
//            pid = extra!!.getString("prddata")!!
//        }

            binding.timmer.setOnCountdownEndListener {
                val sd = SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                sd.setTitleText("Oops...")
                sd.setContentText("경매가 종료되었어요")
                sd.setCancelable(false)
                sd.setConfirmText("OK")
                sd.setCanceledOnTouchOutside(false)
                sd.setConfirmClickListener {

                    viewModel!!.setBidPrice(binding.bidinfo!!.bidPrice!!, pid!!)
                    viewModel!!.setBuyUser(pid!!)

                    activity?.finish()
                }
                sd.show()
            }



            viewModel.getPrdDataByPid(pid!!).observe(viewLifecycleOwner, Observer{
                binding.timmer.start(it!!.endDate!!.toDate().time - Date().time)

                binding.prdlist = it

                when(binding.prdlist!!.worksCode){
                    "K" -> binding.works.text = "광양"
                    "P" -> binding.works.text = "포항"
                }


                val myFormatter = DecimalFormat("###,###")
                val formattedWgt: String = myFormatter.format(binding.prdlist!!.prdWgt) + "Kg"
                val formattedWth: String = myFormatter.format(binding.prdlist!!.prdWth)
                binding.prdPrdwth.text = formattedWth
                binding.prdPrdwgt.text = formattedWgt
            })

            auctionViewModel!!.getAuctionInfo(pid!!).observe(viewLifecycleOwner, Observer{
                if(it.bidPrice == null){
                    it.bidPrice = 0
                }
                binding.bidinfo = it

            })
        }



    }



}