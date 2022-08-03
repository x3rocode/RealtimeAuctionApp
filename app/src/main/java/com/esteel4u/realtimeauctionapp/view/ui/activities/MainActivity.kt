package com.esteel4u.realtimeauctionapp.view.ui.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.core.view.GravityCompat
import cn.pedant.SweetAlert.SweetAlertDialog
import com.esteel4u.realtimeauctionapp.databinding.ActivityMainBinding
import com.esteel4u.realtimeauctionapp.view.adapter.MainViewPagerAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*


@SuppressLint("MissingPermission")
class MainActivity : AppCompatActivity() {
    private val KEY_REPLY = "key_reply"
    private var auth = Firebase.auth
    private lateinit var binding: ActivityMainBinding
    private lateinit var sd: SweetAlertDialog
    private lateinit var drawerArrow: DrawerArrowDrawable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        Log.d(ContentValues.TAG, "*********mainactivity create***********")
        setContentView(binding.root)

        view_pager.adapter =
            MainViewPagerAdapter(
                supportFragmentManager,
                lifecycle
            )
        view_pager.offscreenPageLimit = 3
        bottom_bar.setupWithViewPager2(view_pager)


        drawerArrow = DrawerArrowDrawable(this)
        menu_btn.setOnClickListener{
            drawer_layout.openDrawer(GravityCompat.START)
        }


        val extras = intent.extras
        if (extras != null) {
            val tag = extras.getString("tag")
            val id = extras.getString("buyuserid")
            Log.d("idiiiiiiiiiiiiiiiiii", "11")
            Log.d("idiiiiiiiiiiiiiiiiii", tag.toString())
            reciveInput(tag!!, id!!)
        }

    }

    override fun onNewIntent(intent: Intent?) {
        val tag = intent!!.getStringExtra("tag")
        val id = intent!!.getStringExtra("buyuserid")
        Log.d("idiiiiiiiiiiiiiiiiii", "22")
        Log.d("idiiiiiiiiiiiiiiiiii", tag.toString())
        reciveInput(tag!!, id!!)
        super.onNewIntent(intent)
    }


    @SuppressLint("ResourceType")
    private fun reciveInput(tag: String, id: String) {
        when (tag) {
            "start" -> {}
            "end" -> {
                val dialog = Dialog(this, com.esteel4u.realtimeauctionapp.R.layout.activity_full_anim)
                val dialogBinding = com.esteel4u.realtimeauctionapp.databinding.ActivityFullAnimBinding.inflate(dialog.layoutInflater)
                dialog.setContentView(dialogBinding.root)
                dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)

                dialogBinding.lottieView.setOnTouchListener(object : View.OnTouchListener {
                    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                        if (id == auth.uid) {
                            view_pager.currentItem = 2
                            view_pager.setCurrentItem(2, true)
                            bottom_bar.selectTabAt(2, true)
                        } else {
                            view_pager.currentItem = 0
                            view_pager.setCurrentItem(0, true)
                            bottom_bar.selectTabAt(0, true)
                        }
                        dialog.dismiss()
                        sd.dismissWithAnimation()
                        return false;
                    }
                })

                if (id == auth.uid) {
                    sd = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    sd.setTitleText("Congratulation!")
                    sd.setContentText("구매에 성공했어요")
                    sd.setCancelable(true)
                    sd.setConfirmText("OK")
                    sd.setCanceledOnTouchOutside(true)

                    dialogBinding.lottieView.setAnimation(com.esteel4u.realtimeauctionapp.R.raw.lottie_congratulation)

                } else {
                    sd = SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                    sd.setTitleText("Oh..")
                    sd.setCustomImage(com.esteel4u.realtimeauctionapp.R.drawable.sad2)
                    sd.setContentText("구매 실패했어요")
                    sd.setCancelable(true)
                    sd.setConfirmText("다른 제품 보러가기")
                    sd.setCanceledOnTouchOutside(true);

                    dialogBinding.lottieView.setAnimation(com.esteel4u.realtimeauctionapp.R.raw.lottie_falling)
                }
                sd.show()
                dialog.show()
            }
        }
    }
}