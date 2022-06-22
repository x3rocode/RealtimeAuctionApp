package com.esteel4u.realtimeauctionapp.view.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.esteel4u.realtimeauctionapp.R
import com.esteel4u.realtimeauctionapp.view.adapter.OnboardingViewPagerAdapter
import com.esteel4u.realtimeauctionapp.view.utils.Animatoo
import kotlinx.android.synthetic.main.activity_onboarding.*


class OnboardActivity: AppCompatActivity() {

    private lateinit var mViewPager: ViewPager2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        mViewPager = viewPager
        mViewPager.adapter = OnboardingViewPagerAdapter(this, this)
//        TabLayoutMediator(pageIndicator, mViewPager) { _, _ -> }.attach()
//        textSkip = findViewById(R.id.skip_btn)
//        textSkip.setOnClickListener {
//            finish()
//            val intent =
//                Intent(applicationContext, LoginActivity::class.java)
//            startActivity(intent)
//            Animatoo.animateSlideLeft(this)
//        }

        val btnNextStep: Button = findViewById(R.id.next_btn)

        btnNextStep.setOnClickListener {
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

}
