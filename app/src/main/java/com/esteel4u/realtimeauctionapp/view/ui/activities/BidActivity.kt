package com.esteel4u.realtimeauctionapp.view.ui.activities

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import cn.pedant.SweetAlert.SweetAlertDialog
import com.esteel4u.realtimeauctionapp.R
import com.esteel4u.realtimeauctionapp.data.model.ProductData
import com.esteel4u.realtimeauctionapp.data.model.UserData
import com.esteel4u.realtimeauctionapp.data.repository.UserRepository
import com.esteel4u.realtimeauctionapp.databinding.ActivityBidBinding
import com.esteel4u.realtimeauctionapp.utils.AuthUtil
import com.esteel4u.realtimeauctionapp.view.adapter.MainViewPagerAdapter
import com.esteel4u.realtimeauctionapp.view.adapter.OnboardingViewPagerAdapter
import com.esteel4u.realtimeauctionapp.view.utils.Animatoo
import com.esteel4u.realtimeauctionapp.viewmodel.AuctionViewModel
import com.esteel4u.realtimeauctionapp.viewmodel.ProductViewModel
import com.gauravk.bubblenavigation.BubbleNavigationConstraintView
import com.gauravk.bubblenavigation.BubbleNavigationLinearView
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.ptrbrynt.firestorelivedata.FirestoreResource
import com.ptrbrynt.firestorelivedata.asLiveData
import com.ptrbrynt.firestorelivedata.currentUserLiveData
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_onboarding.*
import java.util.*

class BidActivity: AppCompatActivity() {

    private lateinit var binding: ActivityBidBinding
    private var viewModel: ProductViewModel? = null
    private var auctionViewModel: AuctionViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBidBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, ProductViewModel.Factory(this)).get(ProductViewModel::class.java)
        auctionViewModel = ViewModelProvider(this, AuctionViewModel.Factory(this)).get(AuctionViewModel::class.java)

        var pid = intent.getStringExtra("prddata")
        Log.d("d", "aaaaaaaaaaaaaaaaa" + pid!!)

        binding.timmer.setOnCountdownEndListener {
            val sd = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
            sd.setTitleText("Oops...")
            sd.setContentText("경매가 종료되었어요")
            sd.setCancelable(false)
            sd.setConfirmText("OK")
            sd.setCanceledOnTouchOutside(false)
            sd.setConfirmClickListener {

                viewModel!!.setBidPrice(binding.bidinfo!!.bidPrice!!, pid!!)
                viewModel!!.setBuyUser(pid!!)

                finish()
            }
            sd.show()
        }
        viewModel!!.getPrdDataByPid(pid!!).observe(this, Observer{
            binding.timmer.start(it!!.endDate!!.toDate().time - Date().time)

            binding.prdlist = it
        })

        auctionViewModel!!.getAuctionInfo(pid!!).observe(this, Observer{
            if(it.bidPrice == null){
                it.bidPrice = 0
            }
            binding.bidinfo = it

        })



        binding.bidButton.setOnClickListener{

            var mInputMethodManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            mInputMethodManager.hideSoftInputFromWindow(binding.bidButton.getWindowToken(), 0);

            if(binding.bidinfo!!.highestBuyUserId?.isEmpty() == true){
                //내가 첫 입찰자
                auctionViewModel!!.setBidFirst(binding.inputBid.text.toString().toInt(), pid)
            }else{

                //이전에 입찰햇던 살람이 나야
                if(binding.bidinfo!!.highestBuyUserId!! == FirebaseAuth.getInstance().uid){
                    val sd = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    sd.setTitleText("Oops...")
                    sd.setContentText("현재 최고가 입찰자는 you")
                    sd.setCancelable(true)
                    sd.setConfirmText("Retry")

                    sd.setCanceledOnTouchOutside(true);
                    sd.show()
                } else {
                    //이전에 입찰햇던 살람이 나가 아니야
                    //입력한 값이 더 클경우
                    if(binding.inputBid.text.toString().toInt() >= binding.bidinfo!!.bidPrice!!){
                        auctionViewModel!!.setBid(binding.inputBid.text.toString().toInt(), pid, binding.bidinfo!!.buyUserToken!!)
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
            }

            binding.inputBid.text!!.clear()
        }
        binding.prevBtn.setOnClickListener{
            finish()
        }
    }

    //화면 터치 시 키보드 내려감
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val focusView = currentFocus
        if (focusView != null) {
            val rect = Rect()
            focusView.getGlobalVisibleRect(rect)
            val x = ev.x.toInt()
            val y = ev.y.toInt()
            if (!rect.contains(x, y)) {
                val imm: InputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                if (imm != null) imm.hideSoftInputFromWindow(focusView.windowToken, 0)
                focusView.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}