package com.esteel4u.realtimeauctionapp.view.ui.activities

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.transition.Slide
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import com.esteel4u.realtimeauctionapp.databinding.ActivityLoginBinding
import com.esteel4u.realtimeauctionapp.model.data.UserData
import com.esteel4u.realtimeauctionapp.viewmodel.LoginViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.esteel4u.realtimeauctionapp.model.datastore.DataStoreModule
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var activityLoginBinding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var datastore: DataStoreModule
    private var user: UserData = UserData()
    private var auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityLoginBinding = DataBindingUtil.setContentView(this, com.esteel4u.realtimeauctionapp.R.layout.activity_login)
        setupWindowAnimations()
        datastore = DataStoreModule(applicationContext)

        // Setting viewmodel
        viewModel = ViewModelProvider(this, LoginViewModel.Factory(applicationContext, this)).get(LoginViewModel::class.java)
        activityLoginBinding.lifecycleOwner = this

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

//        if(user.uid == null){
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
        viewModel.isSignIn.observe(this, Observer{
            if (it) {
                viewModel.getLoggedInUserInfo().observe(this, Observer{ userInfo ->
                    GlobalScope.launch {
                        datastore.setUserData(userInfo)
                    }
                })
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val sd = SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                sd.setTitleText("Oops...")
                sd.setContentText("Something went wrong!")
                sd.setCancelable(true)
                sd.setConfirmText("Retry")

                sd.setCanceledOnTouchOutside(true);
                sd.show()
            }
        })


        activityLoginBinding.loginBtn.setOnClickListener{
            login(activityLoginBinding.inputId.text.toString(), activityLoginBinding.inputPw.text.toString())
        }

    }

    public override fun onStart() {
        super.onStart()
        moveMainPage(auth?.currentUser)
    }

    // 유저정보 넘겨주고 메인 액티비티 호출
    fun moveMainPage(user: FirebaseUser?){
        if( user!= null){

            viewModel.getLoggedInUserInfo().observe(this, Observer{ userInfo ->
                GlobalScope.launch {
                    datastore.setUserData(userInfo)
                }
            })


            startActivity(Intent(this, MainActivity::class.java))
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

    fun login(id: String, pass: String) {
        viewModel.signIn(id + "@email.com", pass)
    }

    private fun setupWindowAnimations() {
        val slide = Slide()
        slide.duration = 1000
        window.enterTransition = slide
    }
}