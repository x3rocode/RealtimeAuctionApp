package com.esteel4u.realtimeauctionapp.view.ui.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import cn.pedant.SweetAlert.SweetAlertDialog
import com.airbnb.lottie.LottieAnimationView
import com.esteel4u.realtimeauctionapp.R
import com.esteel4u.realtimeauctionapp.databinding.ActivityFullAnimBinding
import com.esteel4u.realtimeauctionapp.databinding.ActivityMainBinding
import com.esteel4u.realtimeauctionapp.view.adapter.MainViewPagerAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_full_anim.*
import kotlinx.android.synthetic.main.activity_main.*
import org.checkerframework.checker.units.qual.s


@SuppressLint("MissingPermission")
class MainActivity: AppCompatActivity() {
    private val KEY_REPLY = "key_reply"
    private var auth = Firebase.auth
    private lateinit var binding: ActivityMainBinding
    private lateinit var sd : SweetAlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)


        Log.d(ContentValues.TAG, "*********mainactivity create***********" )
        setContentView(binding.root)

        view_pager.adapter =
            MainViewPagerAdapter(
                supportFragmentManager,
                lifecycle
            )
        view_pager.offscreenPageLimit = 3
        bottom_bar.setupWithViewPager2(view_pager)

        toolbar.setElevationVisibility(false)




        val extras = intent.extras
        if (extras != null) {
            val tag = extras.getString("tag")

            when(tag){
                "start" -> {}
                "end" -> {
                    val id = extras.getString("buyuserid")
                    reciveInput(id!!)
                }
                "loser" -> {
                    val prdId = extras.getString("prdId")
                    val intent = Intent( this, BidActivity::class.java)
                    intent.putExtra("prddata", prdId)
                    startActivity(intent)
                    finish()
                }
            }

        }
    }


    override fun onNewIntent(intent: Intent?) {
        val tag = intent!!.getStringExtra("tag")

        when(tag){
            "start" -> {}
            "end" -> {
                val id = intent!!.getStringExtra("buyuserid")
                reciveInput(id!!)
            }
            "loser" -> {
                val prdId = intent!!.getStringExtra("prdId")
                val intent = Intent( this, BidActivity::class.java)
                intent.putExtra("prddata", prdId)
                startActivity(intent)
                finish()
            }
        }

        super.onNewIntent(intent)
    }

    @SuppressLint("ResourceType")
    private fun reciveInput(id: String){
        val dialog = Dialog(this, R.layout.activity_full_anim)
        val dialogBinding = ActivityFullAnimBinding.inflate(dialog.layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window!!.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)

//        val lott =  LottieAnimationView(this)
//        lott.setAnimation(R.raw.lottie_sad)


        dialogBinding.lottieView.setOnTouchListener(object: View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                if(id == auth.uid){
                    view_pager.currentItem = 2
                    view_pager.setCurrentItem(2, true)
                    bottom_bar.selectTabAt(2, true)
                }else{
                    view_pager.currentItem = 0
                    view_pager.setCurrentItem(0, true)
                    bottom_bar.selectTabAt(0, true)
                }
                dialog.dismiss()
                sd.dismissWithAnimation()
                return false;
            }
        })
        Log.d(ContentValues.TAG, id + "ㅏㅓㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏ" )
        if( id == auth.uid){
            sd = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
            sd.setTitleText("Congratulation!")
            sd.setContentText("구매에 성공했어요")
            sd.setCancelable(true)
            sd.setConfirmText("OK")
            sd.setCanceledOnTouchOutside(true)

            dialogBinding.lottieView.setAnimation(R.raw.lottie_congratulation)

        } else {
            sd = SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
            sd.setTitleText("Oh..")
            sd.setCustomImage(R.drawable.sad2)
            sd.setContentText("구매 실패했어요")
            sd.setCancelable(true)
            sd.setConfirmText("다른 제품 보러가기")
            sd.setCanceledOnTouchOutside(true);

            dialogBinding.lottieView.setAnimation(R.raw.lottie_falling)

        }

        sd.show()
        dialog.show()
    }
}