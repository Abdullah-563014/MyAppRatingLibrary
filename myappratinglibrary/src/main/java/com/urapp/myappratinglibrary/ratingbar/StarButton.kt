package com.urapp.myappratinglibrary.ratingbar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import com.urapp.myappratinglibrary.Constants.ALPHA_INVISIBLE
import com.urapp.myappratinglibrary.Constants.ALPHA_VISIBLE
import com.urapp.myappratinglibrary.Constants.CHECK_STAR_DURATION
import com.urapp.myappratinglibrary.R
import com.urapp.myappratinglibrary.databinding.StarButtonLayoutBinding


class StarButton : FrameLayout {

    private lateinit var binding: StarButtonLayoutBinding

    constructor(context: Context) : super(context) {
        initialize()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private fun initialize() {
//        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        inflater.inflate(R.layout.star_button_layout, this, true)
        binding= StarButtonLayoutBinding.inflate(LayoutInflater.from(context),this)
    }

    fun setChecked(checked: Boolean): StarButton {
        binding.fullStarImage
                .animate()
                .alpha(if (checked) ALPHA_VISIBLE else ALPHA_INVISIBLE)
                .setDuration(CHECK_STAR_DURATION)
                .start()

        return this
    }

    fun setCheckedWithoutAnimation(checked: Boolean): StarButton {
        binding.fullStarImage.alpha = if (checked) ALPHA_VISIBLE else ALPHA_INVISIBLE
        return this
    }

    fun setColor(@ColorInt color: Int): StarButton {
        binding.emptyStarImage.setColorFilter(color)
        binding.fullStarImage.setColorFilter(color)
        return this
    }
}