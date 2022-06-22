package com.esteel4u.realtimeauctionapp.view.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.esteel4u.realtimeauctionapp.R
import com.esteel4u.realtimeauctionapp.databinding.ActivityLoginBinding
import com.esteel4u.realtimeauctionapp.databinding.ActivityWelcomeBinding

class LoginActivity : AppCompatActivity() {
    private var activityLoginBinding: ActivityLoginBinding ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)


    }
}