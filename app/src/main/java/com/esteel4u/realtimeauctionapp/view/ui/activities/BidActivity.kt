package com.esteel4u.realtimeauctionapp.view.ui.activities

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import com.esteel4u.realtimeauctionapp.R
import com.esteel4u.realtimeauctionapp.databinding.ActivityBidBinding
import com.esteel4u.realtimeauctionapp.view.ui.fragments.BidFragment
import com.esteel4u.realtimeauctionapp.viewmodel.AuctionViewModel
import com.esteel4u.realtimeauctionapp.viewmodel.ProductViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_bid.*
import java.text.DecimalFormat
import java.util.*


class BidActivity: AppCompatActivity() {

    private lateinit var binding: ActivityBidBinding
    private var viewModel: ProductViewModel? = null
    private var auctionViewModel: AuctionViewModel? = null
    private lateinit var fm : FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBidBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, ProductViewModel.Factory(this)).get(ProductViewModel::class.java)
        auctionViewModel = ViewModelProvider(this, AuctionViewModel.Factory(this)).get(AuctionViewModel::class.java)


        var pid = intent.getStringExtra("prddata")
        val newFragment = BidFragment.newInstance(pid!!)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.bidfragment, newFragment, "myTag")
        transaction.addToBackStack(null)
        transaction.commit()

        input_bid.transformationMethod = null

        viewModel!!.getPrdDataByPid(pid!!).observe(this, Observer{

            binding.prdlist = it

        })

        auctionViewModel!!.getAuctionInfo(pid!!).observe(this, Observer{
            if(it.bidPrice == null){
                it.bidPrice = 0
            }
            binding.bidinfo = it

        })

        input_bid.setOnKeyListener { view, i, keyEvent ->
            when(i){
                KeyEvent.KEYCODE_ENTER -> {
                    if(keyEvent.action == KeyEvent.ACTION_DOWN){
                        bid(pid)

                    }


                }
            }
            false
        }

        binding.bidButton.setOnClickListener {
            bid(pid)
        }

        binding.prevBtn.setOnClickListener{
            finish()
        }

    }

    private fun bid(pid: String){
        //이전에 입찰햇던 살람이 나야
        if(binding.bidinfo!!.highestBuyUserId!! == FirebaseAuth.getInstance().uid){
            val sd = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            sd.setTitleText("Oops...")
            sd.setContentText("내가 현재 최고가 입찰자에요!")
            sd.setCancelable(true)
            sd.setConfirmText("Retry")

            sd.setCanceledOnTouchOutside(true);
            sd.show()
        } else {
            //이전에 입찰햇던 살람이 나가 아니야
            //입력한 값이 더 클경우
            if(input_bid.text.toString().toInt() >= binding.bidinfo!!.bidPrice!!){
                auctionViewModel!!.setBid(binding.inputBid.text.toString().toInt(), pid!!, binding.bidinfo!!.buyUserToken!!)
                //viewModel!!.setBidPrice(binding.bidinfo!!.bidPrice!!, pid!!)
                //viewModel!!.setBuyUser(pid!!)
            } else {
                val sd = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                sd.setTitleText("Oops...")
                sd.setContentText("입찰 금액은 현재 금액보다 커야합니다.")
                sd.setCancelable(true)
                sd.setConfirmText("Retry")
                sd.setCanceledOnTouchOutside(true);
                sd.show()
            }
        }

//        if(binding.bidinfo!!.highestBuyUserId?.isEmpty() == true){
//            //내가 첫 입찰자
//            auctionViewModel!!.setBidFirst(binding.inputBid.text.toString().toInt(), pid!!)
//        }else{
//
//
//        }
        binding.inputBid.text!!.clear()
    }


}