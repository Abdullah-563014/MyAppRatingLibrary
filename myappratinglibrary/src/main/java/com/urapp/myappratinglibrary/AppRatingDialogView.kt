package com.urapp.myappratinglibrary

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.TextViewCompat
import com.urapp.myappratinglibrary.databinding.ComponentAppRateDialogBinding
import com.urapp.myappratinglibrary.listener.OnRatingBarChangedListener


class AppRatingDialogView(context: Context) : LinearLayout(context), OnRatingBarChangedListener {

    private lateinit var binding: ComponentAppRateDialogBinding

    init {
        setup(context)
    }


    val rateNumber: Float
        get() = binding.ratingBar.rating



    fun setNumberOfStars(numberOfStars: Int) {
        binding.ratingBar.setNumStars(numberOfStars)
    }


    fun setStarColor(@ColorRes colorResId: Int) {
        binding.ratingBar.setStarColor(colorResId)
    }

    fun setDefaultRating(defaultRating: Int) {
        binding.ratingBar.setRating(defaultRating)
    }

    fun setTitleText(title: String) {
        binding.titleText.text = title
        binding.titleText.visibility = View.VISIBLE
    }


    fun setDescriptionText(content: String) {
        binding.descriptionText.text = content
        binding.descriptionText.visibility = View.VISIBLE
    }


    fun setTitleTextColor(@ColorRes color: Int) {
        binding.titleText.setTextColor(getColorFromRes(color))
    }


    fun setDescriptionTextColor(@ColorRes color: Int) {
        binding.descriptionText.setTextColor(getColorFromRes(color))
    }


    @SuppressLint("ResourceType")
    private fun setup(context: Context) {
//        LayoutInflater.from(context).inflate(R.layout.component_app_rate_dialog, this, true)
        binding=ComponentAppRateDialogBinding.inflate(LayoutInflater.from(context),this,true)
        binding.ratingBar.setIsIndicator(false)
        binding.ratingBar.setOnRatingBarChangeListener(this)

        TextViewCompat.setTextAppearance(
            binding.titleText,
                fetchAttributeValue(R.attr.appRatingDialogTitleStyle)
        )
        TextViewCompat.setTextAppearance(
            binding.descriptionText,
                fetchAttributeValue(R.attr.appRatingDialogDescriptionStyle)
        )
    }

    private val theme: Resources.Theme
        get() = context.theme

    private fun getColorFromRes(@ColorRes colorResId: Int): Int {
        return ResourcesCompat.getColor(context.resources, colorResId, theme)
    }

    private fun fetchAttributeValue(attr: Int): Int {
        val outValue = TypedValue()
        context.theme.resolveAttribute(attr, outValue, true)
        return outValue.data
    }

    override fun onRatingChanged(rating: Int) {

    }
}