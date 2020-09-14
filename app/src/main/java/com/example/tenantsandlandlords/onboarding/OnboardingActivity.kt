package com.example.tenantsandlandlords.onboarding

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.tenantsandlandlords.R
import com.example.tenantsandlandlords.registration.LoginActivity
import kotlinx.android.synthetic.main.activity_onboarding.*


class OnboardingActivity : AppCompatActivity() {

    private var mDots = arrayOfNulls<TextView>(3)
    private var mCurrentPage: Int = 0
    private lateinit var mSliderAdapter: SliderAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        //set the adapter of the slider
        mSliderAdapter = SliderAdapter(this)
        slideViewPager.adapter = mSliderAdapter

        //add on page change listener to slider
        slideViewPager.addOnPageChangeListener(viewPagerPageChangeListener)

        //create dots on screen
        addDotIndicator(mCurrentPage)

        //set next button on click listener
        nextBtn.setOnClickListener {
            if (mCurrentPage == mDots.size - 1) {
                //navigate to sign up activity
                startActivity(Intent(this, LoginActivity::class.java))
                //set sharedpreferences value for first time started the app to false
                val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
                val editor = prefs.edit()
                editor.putBoolean("firstStart", false)
                editor.apply()
                finish()
            } else
                slideViewPager.currentItem = mCurrentPage + 1
        }

        //set back button on click listener
        backBtn.setOnClickListener {
            slideViewPager.currentItem = mCurrentPage - 1
        }
    }

    // add dot indicator
    private fun addDotIndicator(position: Int) {

        //remove all views to create new ones
        dotsLayout.removeAllViews()

        for (i in 0 until 3) {
            mDots[i] = TextView(this)
            mDots[i]?.text = Html.fromHtml("&#8226;")
            mDots[i]?.textSize = 50F
            mDots[i]?.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.text_secondary
                )
            )  //inactive color
            dotsLayout.addView(mDots[i])
        }

        if (mDots.size > 0) {
            mDots[position]?.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.glamour
                )
            )  //active color
        }

    }


    // viewPagerPage ChangeListener according to Dots-Points
    var viewPagerPageChangeListener: OnPageChangeListener = object : OnPageChangeListener {

        override fun onPageSelected(position: Int) {
            addDotIndicator(position)
            mCurrentPage = position

            if (mCurrentPage == 0) {

                nextBtn.isEnabled = true
                backBtn.isEnabled = false
                backBtn.visibility = View.INVISIBLE

                nextBtn.setTextColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.text_primary
                    )
                )

            } else if (mCurrentPage == mDots.size - 1) {
                nextBtn.isEnabled = true
                backBtn.isEnabled = true
                backBtn.visibility = View.VISIBLE

                nextBtn.text = "Sign In"
                nextBtn.setTextColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.electron_blue
                    )
                )
                backBtn.text = "Back"
            } else {
                nextBtn.isEnabled = true
                backBtn.isEnabled = true
                backBtn.visibility = View.VISIBLE

                nextBtn.text = "Next"
                nextBtn.setTextColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.text_primary
                    )
                )
                backBtn.text = "Back"
            }
        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
        override fun onPageScrollStateChanged(arg0: Int) {}
    }


}