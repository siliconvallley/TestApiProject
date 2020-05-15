package com.dh.testproject.activity.viewpager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dh.testproject.R
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_card_view_tab_layout.*

class CardViewTabLayoutActivity : CardViewActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_card_view_tab_layout)

        TabLayoutMediator(tabs, viewPager2) { tab, position ->
            tab.text = Card.DECK[position].toString()
        }.attach()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_card_view_tab_layout
    }
}
