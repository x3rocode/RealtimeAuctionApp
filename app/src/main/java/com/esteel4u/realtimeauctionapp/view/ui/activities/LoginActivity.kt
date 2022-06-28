package com.esteel4u.realtimeauctionapp.view.ui.activities

import android.content.ContentValues
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.transition.Fade
import android.transition.Slide
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.esteel4u.realtimeauctionapp.databinding.ActivityLoginBinding
import com.esteel4u.realtimeauctionapp.viewmodel.LoginViewModel


class LoginActivity : AppCompatActivity() {
    private lateinit var activityLoginBinding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityLoginBinding = DataBindingUtil.setContentView(this, com.esteel4u.realtimeauctionapp.R.layout.activity_login)
        setupWindowAnimations();
        // Setting viewmodel
        viewModel = ViewModelProvider(this, LoginViewModel.Factory(this)).get(LoginViewModel::class.java)
        activityLoginBinding.lifecycleOwner = this

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        viewModel.isSignIn.observe(this, Observer{
            if (it) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                //TODO: login fail
            }
        })
        activityLoginBinding.loginBtn.setOnClickListener{
            login(activityLoginBinding.inputId.text.toString(), activityLoginBinding.inputPw.text.toString())
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

    fun login(id: String, pass: String) {
        Log.d(ContentValues.TAG, "ddfasdfasdfasdf f fdfdf " + id)
        Log.d(ContentValues.TAG, "ddfasdfasdfasdf f fdfdf " + pass)
        viewModel.signIn(id + "@email.com", pass)
    }

    private fun setupWindowAnimations() {
        val slide = Slide()
        slide.duration = 1000
        window.enterTransition = slide
    }

}