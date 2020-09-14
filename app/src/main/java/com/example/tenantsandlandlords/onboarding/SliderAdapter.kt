package com.example.tenantsandlandlords.onboarding

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.example.tenantsandlandlords.R
import kotlinx.android.synthetic.main.slide_layout.view.*


class SliderAdapter(val context: Context) : PagerAdapter() {

    lateinit var layoutInflater: LayoutInflater

    //array of slider images, each slide will have two different images
    val slider_images: Array<IntArray> = arrayOf(
        intArrayOf(R.drawable.profile1, R.drawable.profile2),
        intArrayOf(R.drawable.profile3, R.drawable.profile4),
        intArrayOf(R.drawable.profile5, R.drawable.profile6)
    )

    val slider_headings = arrayOf(
        "Registrations",
        "House Payment",
        "House Management"
    )

    val slider_descriptions = arrayOf(
        arrayOf(
            "Log in to application. Search and Register to a home.\n" +
                    "When approved receive and agree to terms of contracts and make down payments from you smartphone",
            "Be notified about interested \n" +
                    "customers.\n" +
                    "Sort through customers and \n" +
                    "approve those interested in.\n" +
                    "Send terms of contracts to \n" +
                    "customer for approval, deliver \n" +
                    "and receive down payments for \n" +
                    "house"
        ),
        arrayOf(
            "Receive, view and pay house payments online using the app on your smartphone",
            "Automatically send invoice at the end of the month to each registered tenants\n" +
                    "Manually send payments request to tenant and receive payments online"
        ),
        arrayOf(
            "Report household related problems directly from you phone and receive support\n" +
                    "Manage and view you house payment transactions from app",
            "Be notified about reports by tenants and send assistance from app\n" +
                    "Manage, sort and issue receipts from phone for you various houses"
        )
    )

    override fun getCount() = 3

    override fun isViewFromObject(view: View, `object`: Any): Boolean  = view == `object` as ConstraintLayout

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        //create layout item
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.slide_layout, container, false)
        container.addView(view)

        //set values to the various views on the current screen number
        view.headingItem.text = slider_headings[position]
        view.tenantDescription.text = slider_descriptions[position][0]
        view.landlordDescription.text = slider_descriptions[position][1]
        view.tenantImageView.setImageResource(slider_images[position][0])
        view.landlordImageView.setImageResource(slider_images[position][1])

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }
}