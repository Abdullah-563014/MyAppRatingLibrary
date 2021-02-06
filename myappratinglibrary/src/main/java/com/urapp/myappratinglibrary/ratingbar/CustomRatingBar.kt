package com.urapp.myappratinglibrary.ratingbar

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat
import com.urapp.myappratinglibrary.R
import com.urapp.myappratinglibrary.common.Preconditions
import com.urapp.myappratinglibrary.databinding.ComponentCustomRatingBarBinding
import com.urapp.myappratinglibrary.listener.OnRatingBarChangedListener


class CustomRatingBar(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {

    private var binding: ComponentCustomRatingBarBinding = ComponentCustomRatingBarBinding.inflate(LayoutInflater.from(context),this,true)
    private val starList = ArrayList<StarButton>()

    private var numStars: Int = 0

    private var starColorResId: Int = 0

    var rating: Float = 0.0f
        private set

    private var isIndicator: Boolean = false

    private var onRatingBarChangedListener: OnRatingBarChangedListener? = null

    init {
//        LayoutInflater.from(context).inflate(R.layout.component_custom_rating_bar, this)
    }

    private fun addStars(numberOfAll: Int, numberOfChecked: Int) {
        Preconditions.checkArgument(numberOfChecked <= numberOfAll, "wrong argument")

        starList.clear()
        binding.ratingBarContainer.removeAllViews()

        for (index in 0 until numberOfAll) {
            addStar()
                    .setCheckedWithoutAnimation(index < numberOfChecked)
                    .setColor(getStarColor(context))
                    .setOnClickListener(OnStarClickedHandler(index + 1))
        }
    }

    private fun addStar(): StarButton {
        val starButton = StarButton(context)
        starButton.layoutParams = LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        starList.add(starButton)
        binding.ratingBarContainer.addView(starButton)
        return starButton
    }

    fun setStarColor(@ColorRes colorResId: Int) {
        this.starColorResId = colorResId
    }

    fun setNumStars(numStars: Int) {
        this.numStars = numStars

        addStars(numStars, 0)
    }

    fun setRating(rating: Int, withAnimation: Boolean = false) {
        this.rating = rating.toFloat()

        if (rating <= starList.size) {
            for (index in starList.indices) {
                if (withAnimation) {
                    starList[index].setChecked(index < rating)
                } else {
                    starList[index].setCheckedWithoutAnimation(index < rating)
                }
            }
        }

        onRatingBarChangedListener!!.onRatingChanged(rating)
    }

    fun setIsIndicator(isIndicator: Boolean) {
        this.isIndicator = isIndicator
    }

    fun setOnRatingBarChangeListener(onRatingBarChangedListener: OnRatingBarChangedListener) {
        this.onRatingBarChangedListener = onRatingBarChangedListener
    }

    private fun getStarColor(context: Context): Int {
        if (starColorResId != 0) {
            return ResourcesCompat.getColor(context.resources, starColorResId, context.theme)
        }
        return getThemeAccentColor(context)
    }

    private fun getThemeAccentColor(context: Context): Int {
        val colorAttr: Int = context.resources.getIdentifier("colorAccent", "attr", context.packageName)
        val outValue = TypedValue()
        context.theme.resolveAttribute(colorAttr, outValue, true)
        return outValue.data
    }

    private inner class OnStarClickedHandler(private val number: Int) : OnClickListener {

        override fun onClick(v: View) {
            setRating(number, true)
        }
    }
}