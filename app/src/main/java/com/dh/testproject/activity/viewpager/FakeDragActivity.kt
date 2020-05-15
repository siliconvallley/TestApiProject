package com.dh.testproject.activity.viewpager

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.TextUtilsCompat
import androidx.core.view.ViewCompat
import androidx.viewpager2.widget.ViewPager2
import com.dh.testproject.R
import kotlinx.android.synthetic.main.activity_fake_drag.*
import java.util.*

private const val TAG: String = "FakeDragActivity"

class FakeDragActivity : AppCompatActivity() {

    // 判断是否是横屏
    private var landscape = false;
    private var lastValue: Float = 0f

    // 判断是否是从右到左的显示方式
    private val isRtl = TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) ==
            ViewCompat.LAYOUT_DIRECTION_RTL
    // 获取当前ViewPager2的方向
    private val ViewPager2.isHorizontal: Boolean
        get() {
            return orientation == ViewPager2.ORIENTATION_HORIZONTAL
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fake_drag)


        init()
    }

    private fun init() {
        landscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        viewPager.adapter = CardViewAdapter()

        UserInputController(viewPager, disable_user_input_checkbox).setUp()
        OrientationController(viewPager, orientation_spinner).setUp()

        touchpad.setOnTouchListener { v, event ->
            handleOnTouchEvent(event)
        }
    }

    private fun mirrorInRtl(f: Float): Float {
        return if (isRtl) -f else f
    }

    private fun getValue(event: MotionEvent): Float {
        // 处理横竖屏
        return if (landscape) event.y else mirrorInRtl(event.x)
    }

    private fun handleOnTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastValue = getValue(event)
                Log.e(TAG, "down: $lastValue")
                viewPager.beginFakeDrag()
            }
            MotionEvent.ACTION_MOVE -> {
                val value = getValue(event)
                val delta = value - lastValue
                Log.e(TAG, "move: $value ==> $lastValue ===> $delta")
                viewPager.fakeDragBy(if (viewPager.isHorizontal) mirrorInRtl(delta) else delta)
                lastValue = value
            }
            MotionEvent.ACTION_UP -> {
                viewPager.endFakeDrag()
            }
        }
        return true
    }
}
