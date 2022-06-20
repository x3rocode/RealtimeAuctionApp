package com.esteel4u.realtimeauctionapp.view.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.esteel4u.realtimeauctionapp.R
import com.esteel4u.realtimeauctionapp.databinding.ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity(){

    private var activityWelcomeBinding: ActivityWelcomeBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityWelcomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_welcome)

        activityWelcomeBinding?.welcomeBtn?.setOnClickListener {
            val nextIntent = Intent(this, LoginActivity::class.java)
            startActivity(nextIntent)
            finish()
        }
    }
}