package com.esteel4u.realtimeauctionapp.view.ui.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import com.balysv.materialmenu.MaterialMenuDrawable
import com.bitvale.switcher.SwitcherX
import com.esteel4u.realtimeauctionapp.R
import com.esteel4u.realtimeauctionapp.databinding.ActivityFullAnimBinding
import com.esteel4u.realtimeauctionapp.databinding.ActivityMainBinding
import com.esteel4u.realtimeauctionapp.model.data.UserData
import com.esteel4u.realtimeauctionapp.model.datastore.DataStoreModule
import com.esteel4u.realtimeauctionapp.view.adapter.MainViewPagerAdapter
import com.esteel4u.realtimeauctionapp.viewmodel.LoginViewModel
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.nineoldandroids.view.ViewHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_header.*
import kotlinx.android.synthetic.main.drawer_switch.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@SuppressLint("MissingPermission")
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var isCheckedAlarm = true
    private var auth = Firebase.auth
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var sd: SweetAlertDialog
    private lateinit var barDrawerToggle: ActionBarDrawerToggle
    private lateinit var materialMenu: MaterialMenuDrawable
    private var isDrawerOpened = false
    private lateinit var dataStore: DataStoreModule
    private lateinit var switch: SwitcherX
    private var user: UserData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this, LoginViewModel.Factory(this, this)).get(LoginViewModel::class.java)

        Log.d(ContentValues.TAG, "*********mainactivity create***********")
        setContentView(binding.root)


        setViewPager()
        setNavigation()
        getUserInfo()
        initializeDefaultFragment(savedInstanceState,0);


        checkIsIntented()
        setAlarmOnOffSwitch()
    }

    private fun getUserInfo(){
        // get userinfo from data store
        dataStore = DataStoreModule(applicationContext)
        CoroutineScope(Dispatchers.Main).launch {
            dataStore.user.collect{
                user = it

                drawer_name.text = "안녕하세요, " + it.userName + "님!"
                drawer_sys.text = it.gcsCompCode
                alarm_switcher.setChecked(it.setAlarm!!.toBoolean())
            }
        }
        val pref =  getSharedPreferences("SET",0)
        val editor = pref.edit()
        editor.putString("alarm", user?.setAlarm)
        editor.commit()

    }

    private fun checkIsIntented(){

        val extras = intent.extras
        if (extras != null) {
            val tag = extras.getString("tag")
            val id = extras.getString("buyuserid")
            reciveInput(tag!!, id!!)
        }
    }


    @SuppressLint("ResourceAsColor")
    private fun setNavigation(){
        setSupportActionBar(toolbar)
        materialMenu = MaterialMenuDrawable(this, Color.BLACK, MaterialMenuDrawable.Stroke.THIN)
        toolbar.navigationIcon = materialMenu
        supportActionBar?.setTitle("")

        barDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawer_layout, toolbar,
            nl.joery.animatedbottombar.R.string.nav_app_bar_open_drawer_description,
            nl.joery.animatedbottombar.R.string.mtrl_chip_close_icon_content_description
        ) {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)
                materialMenu.setTransformationOffset(
                    MaterialMenuDrawable.AnimationState.BURGER_ARROW,
                    if (isDrawerOpened) 2 - slideOffset else slideOffset
                )
                if (slideOffset > 0.0f) {
                    setBlurAlpha(slideOffset)
                } else {
                    clearBlurImage()
                }
                invalidateOptionsMenu()
            }
            override fun onDrawerOpened(drawerView: View) {
                isDrawerOpened = true
            }

            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                isDrawerOpened = false
                clearBlurImage()
                invalidateOptionsMenu()

            }

            fun setBlurAlpha(slideOffset: Float) {
                ViewHelper.setAlpha(blur_view, slideOffset)
                if (blur_view.getVisibility() !== View.VISIBLE) {
                    setBlurImage()
                }

            }

            fun setBlurImage() {
                blur_view.visibility = View.VISIBLE
            }

            fun clearBlurImage() {
                blur_view.visibility = View.GONE;
            }
        }
        drawer_layout.addDrawerListener(barDrawerToggle)
        drawer_layout.setScrimColor(android.R.color.transparent)

        nav_view.setNavigationItemSelectedListener(this)
         var a = nav_view.menu.findItem(R.id.alarm_set).actionView as LinearLayout
        switch = a.getChildAt(0) as SwitcherX
    }
    private fun initializeDefaultFragment(savedInstanceState: Bundle?, itemIndex: Int) {
        if (savedInstanceState == null) {
            val menuItem: MenuItem = nav_view.getMenu().getItem(itemIndex).setChecked(true)
            onNavigationItemSelected(menuItem)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.frg_home -> {
                view_pager.currentItem = 0
                view_pager.setCurrentItem(0, true)
                bottom_bar.selectTabAt(0, true)
            }
            R.id.frg_list -> {
                view_pager.currentItem = 1
                view_pager.setCurrentItem(1, true)
                bottom_bar.selectTabAt(1, true)
            }
            R.id.frg_cart -> {
                view_pager.currentItem = 2
                view_pager.setCurrentItem(2, true)
                bottom_bar.selectTabAt(2, true)
            }
            R.id.frg_like -> {
                view_pager.currentItem = 3
                view_pager.setCurrentItem(3, true)
                bottom_bar.selectTabAt(3, true)
            }

            R.id.alarm_set -> {

            }
            R.id.logout_set -> {
                CoroutineScope(Dispatchers.Main).launch {
                    dataStore.clearData()
                }
                viewModel.logout()
                finish()
            }
            R.id.about_set -> {
                reciveInput("about", "")
            }
        }
        return true
    }

    private fun setViewPager(){
        view_pager.adapter =
            MainViewPagerAdapter(
                supportFragmentManager,
                lifecycle
            )
        view_pager.offscreenPageLimit = 3
        bottom_bar.setupWithViewPager2(view_pager)
        view_pager.setOnScrollChangeListener { view, i, i2, i3, i4 ->

            val menuItem: MenuItem = nav_view.getMenu().getItem(view_pager.currentItem).setChecked(true)
            onNavigationItemSelected(menuItem)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        val tag = intent!!.getStringExtra("tag")
        val id = intent!!.getStringExtra("buyuserid")
        Log.d("idiiiiiiiiiiiiiiiiii", "22")
        Log.d("idiiiiiiiiiiiiiiiiii", tag.toString())
        if(tag != "start")
            reciveInput(tag!!, id!!)
        super.onNewIntent(intent)
    }


    @SuppressLint("ResourceType")
    private fun reciveInput(tag: String, id: String) {
        when (tag) {
            "start" -> {}
            "end" -> {
                val dialog = Dialog(this, R.layout.activity_full_anim)
                val dialogBinding = ActivityFullAnimBinding.inflate(dialog.layoutInflater)
                dialog.setContentView(dialogBinding.root)
                dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)

                dialogBinding.lottieView.setOnTouchListener { p0, p1 ->
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
                    false;
                }

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
            "about" -> {
                val dialog = Dialog(this, R.layout.activity_full_anim)
                val dialogBinding = ActivityFullAnimBinding.inflate(dialog.layoutInflater)
                dialog.setContentView(dialogBinding.root)
                dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)

                dialogBinding.lottieView.setOnTouchListener { p0, p1 ->
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
                    false;
                }

                sd = SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                sd.setTitleText("developed by heylin")
                sd.setContentText("github : https://github.com/fascinate98/RealtimeAuctionApp")
                sd.setCancelable(true)
                sd.setConfirmText("OK")
                sd.setCanceledOnTouchOutside(true)

                dialogBinding.lottieView.setAnimation(R.raw.lottie_cat)

                sd.show()
                dialog.show()
            }
        }
    }


    private fun setAlarmOnOffSwitch() {
        getUserInfo()
        switch.setOnCheckedChangeListener { checked ->
            viewModel.setAlarmOnOff(checked.toString())

            val pref =  getSharedPreferences("SET",0)
            val editor = pref.edit()
            editor.putString("alarm", checked.toString())
            editor.commit()
            Log.d("ddddddddddddddd", pref.getString("alarm", "").toString())
        }


    }


}



