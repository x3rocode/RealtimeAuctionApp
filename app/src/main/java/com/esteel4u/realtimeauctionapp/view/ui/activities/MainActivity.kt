package com.esteel4u.realtimeauctionapp.view.ui.activities

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.esteel4u.realtimeauctionapp.R
import com.esteel4u.realtimeauctionapp.data.model.UserData
import com.esteel4u.realtimeauctionapp.data.repository.UserRepository
import com.esteel4u.realtimeauctionapp.databinding.ActivityMainBinding
import com.esteel4u.realtimeauctionapp.utils.AuthUtil
import com.esteel4u.realtimeauctionapp.view.adapter.MainViewPagerAdapter
import com.esteel4u.realtimeauctionapp.view.adapter.OnboardingViewPagerAdapter
import com.esteel4u.realtimeauctionapp.view.utils.Animatoo
import com.gauravk.bubblenavigation.BubbleNavigationConstraintView
import com.gauravk.bubblenavigation.BubbleNavigationLinearView
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.ptrbrynt.firestorelivedata.FirestoreResource
import com.ptrbrynt.firestorelivedata.asLiveData
import com.ptrbrynt.firestorelivedata.currentUserLiveData
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_onboarding.*

class MainActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        view_pager.adapter =
            MainViewPagerAdapter(
                supportFragmentManager,
                lifecycle
            )
        bottom_bar.setupWithViewPager2(view_pager)


        //var bubbleNavigationConstraintView: BubbleNavigationConstraintView = findViewById(R.id.equal_navigation_bar)
        //bubbleNavigationConstraintView.setTypeface(Typeface.createFromAsset(assets, "lexend_deca.ttf"))
//        val db = FirebaseFirestore.getInstance()
//        val name = db.collection("users").document(AuthUtil.getAuthId()).asLiveData<UserData>()
//
//        name.observe(this, Observer{
//            resources: FirestoreResource<UserData> ->
//            idtxt.setText(resources.data?.userName)
//        })
    }
}