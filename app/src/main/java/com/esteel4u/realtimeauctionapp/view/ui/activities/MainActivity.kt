package com.esteel4u.realtimeauctionapp.view.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.RemoteInput
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import cn.pedant.SweetAlert.SweetAlertDialog
import com.esteel4u.realtimeauctionapp.R
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.esteel4u.realtimeauctionapp.databinding.ActivityMainBinding
import com.esteel4u.realtimeauctionapp.view.adapter.MainViewPagerAdapter
import com.google.android.material.appbar.AppBarLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import com.scwang.smartrefresh.layout.listener.CoordinatorLayoutListener

import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.atomic.AtomicInteger

@SuppressLint("MissingPermission")
class MainActivity: AppCompatActivity() {
    private val KEY_REPLY = "key_reply"
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
//
//        val data = getIntent().getExtras()
//        data!!.getString("a")?.let { Log.d("a", it) }

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

        //reciveInput()
    }

    private fun reciveInput(){

        val remoteInput = RemoteInput.getResultsFromIntent(intent)
        val a = remoteInput.get("a")
        Log.d("dfdfd", a.toString() + "gggggggggggggggggggggggggggg")
        remoteInput?.let{
            Log.d("dfdfd", "인텐ㄴㄴㄴㄴㄴㄴㄴㄴㄴ")
            val sd = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
            sd.setTitleText("Congratulation!")
            sd.setContentText("구매에 성공했어요")
            sd.setCancelable(true)
            sd.setConfirmText("OK")

            sd.setCanceledOnTouchOutside(true);
            sd.show()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        var a = intent!!.getStringExtra("a")
        Log.d("dfdfd", a + "asdfffffffffffffffffffff")
        val sd = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
        sd.setTitleText("Congratulation!")
        sd.setContentText("구매에 성공했어요")
        sd.setCancelable(true)
        sd.setConfirmText("OK")

        sd.setCanceledOnTouchOutside(true);
        sd.show()
        super.onNewIntent(intent)
    }
}