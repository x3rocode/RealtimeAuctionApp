package com.esteel4u.realtimeauctionapp.view.ui.activities

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.esteel4u.realtimeauctionapp.R
import com.esteel4u.realtimeauctionapp.Utils.AuthUtil
import com.esteel4u.realtimeauctionapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {
    private lateinit var activityLoginBinding: ActivityLoginBinding
    //private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)


        SkipLoginIfUserExist()

        activityLoginBinding.loginBtn.setOnClickListener{
            login(activityLoginBinding.inputId.text.toString(), activityLoginBinding.inputPw.text.toString())
        }
    }

    override fun onStart() {
        super.onStart()

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

    }

    fun SkipLoginIfUserExist(){
        if (AuthUtil.firebaseAuthInstance.currentUser != null) {
            //TODO: goto home activity
        }
    }

}