package com.esteel4u.realtimeauctionapp.view.ui.activities

import android.content.Intent
import android.os.Bundle
import android.transition.Slide
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import cn.pedant.SweetAlert.SweetAlertDialog
import com.esteel4u.realtimeauctionapp.view.adapter.OnboardingViewPagerAdapter
import com.esteel4u.realtimeauctionapp.view.utils.Animatoo
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_onboarding.*


class OnboardActivity: AppCompatActivity() {

    private lateinit var mViewPager: ViewPager2
    private lateinit var textSkip: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.esteel4u.realtimeauctionapp.R.layout.activity_onboarding)
        setupWindowAnimations();

        mViewPager = viewPager
        mViewPager.adapter = OnboardingViewPagerAdapter(this, this)
        TabLayoutMediator(pageIndicator, mViewPager) { _, _ -> }.attach()
        textSkip = findViewById(com.esteel4u.realtimeauctionapp.R.id.text_skip)
        textSkip.setOnClickListener {
            finish()
            val intent =
                Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            Animatoo.animateSlideLeft(this)
        }


        next_btn.setOnClickListener {
            if (getItem() > mViewPager.childCount) {
                finish()
                val intent =
                    Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
                Animatoo.animateSlideLeft(this)
            } else {
                mViewPager.setCurrentItem(getItem() + 1, true)
            }
        }

    }



    private fun getItem(): Int {
        return mViewPager.currentItem
    }
    private fun setupWindowAnimations() {
        val slide = Slide()
        slide.setDuration(1000)
        window.exitTransition = slide
    }
}
