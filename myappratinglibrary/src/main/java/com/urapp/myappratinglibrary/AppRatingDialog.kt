package com.urapp.myappratinglibrary

import android.content.Context
import android.text.TextUtils
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.urapp.myappratinglibrary.Constants.DEFAULT_AFTER_INSTALL_DAY
import com.urapp.myappratinglibrary.Constants.DEFAULT_NUMBER_OF_LAUNCH
import com.urapp.myappratinglibrary.Constants.DEFAULT_RATING
import com.urapp.myappratinglibrary.Constants.DEFAULT_REMIND_INTERVAL
import com.urapp.myappratinglibrary.Constants.MAX_RATING
import com.urapp.myappratinglibrary.PreferenceHelper.getInstallDate
import com.urapp.myappratinglibrary.PreferenceHelper.getIsAgreeShowDialog
import com.urapp.myappratinglibrary.PreferenceHelper.getLaunchTimes
import com.urapp.myappratinglibrary.PreferenceHelper.getRemindInterval
import com.urapp.myappratinglibrary.PreferenceHelper.isFirstLaunch
import com.urapp.myappratinglibrary.PreferenceHelper.setInstallDate
import com.urapp.myappratinglibrary.PreferenceHelper.setLaunchTimes
import com.urapp.myappratinglibrary.common.Preconditions
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList


class AppRatingDialog private constructor(
        private val fragmentActivity: FragmentActivity,
        private val data: Builder.Data,
        private val context: Context
) {

    private var fragment: Fragment? = null
    private var requestCode: Int = 0

    fun setTargetFragment(fragment: Fragment, requestCode: Int): AppRatingDialog {
        this.fragment = fragment
        this.requestCode = requestCode
        return this
    }


    fun show() {
        AppRatingDialogFragment.newInstance(data).apply {
            fragment?.let {
                setTargetFragment(it, requestCode)
            }
            show(fragmentActivity.supportFragmentManager, "")
        }
    }

    fun showRateDialogIfMeetsConditions(): Boolean {
        val isMeetsConditions = shouldShowRateDialog()
        if (isMeetsConditions) {
            show()
        }
        return isMeetsConditions
    }

    fun shouldShowRateDialog(): Boolean {
        return getIsAgreeShowDialog(context,true) &&
                isOverLaunchTimes() &&
                isOverInstallDate() &&
                isOverRemindDate()
    }

    private fun isOverLaunchTimes(): Boolean {
        return getLaunchTimes(context,0) >= data.numberOfLaunches
    }

    private fun isOverInstallDate(): Boolean {
        return isOverDate(getInstallDate(context,0), data.afterInstallDay)
    }

    private fun isOverRemindDate(): Boolean {
        return isOverDate(getRemindInterval(context,0), data.remindInterval)
    }

    private fun isOverDate(targetDate: Long, interval: Int): Boolean {
        return Date().time - targetDate >= interval * 24 * 60 * 60 * 1000
    }

    fun monitor() {
        if (isFirstLaunch(context)) {
            setInstallDate(context)
        }
        setLaunchTimes(context, getLaunchTimes(context,0) + 1)
    }

    fun clearAgreeShowDialog(): AppRatingDialog {
        PreferenceHelper.setAgreeShowDialog(context, true)
        return this
    }

    fun clearSettingsParam(): AppRatingDialog {
        PreferenceHelper.setAgreeShowDialog(context, true)
        PreferenceHelper.clearSharedPreferences(context)
        return this
    }

    fun setAgreeShowDialog(clear: Boolean): AppRatingDialog {
        PreferenceHelper.setAgreeShowDialog(context, clear)
        return this
    }


    class Builder : Serializable {

        data class Data(
                var numberOfStars: Int = MAX_RATING,
                var defaultRating: Int = DEFAULT_RATING,
                var afterInstallDay: Int = DEFAULT_AFTER_INSTALL_DAY,   // number of days after Installation day
                var numberOfLaunches: Int = DEFAULT_NUMBER_OF_LAUNCH,   // number of launch after last show rate dialog
                var remindInterval: Int = DEFAULT_REMIND_INTERVAL,      // number of days after click on remind me later
                val positiveButtonText: StringValue = StringValue(),
                val negativeButtonText: StringValue = StringValue(),
                val neutralButtonText: StringValue = StringValue(),
                val title: StringValue = StringValue(),
                val description: StringValue = StringValue(),
                var starColorResId: Int = 0,
                var titleTextColorResId: Int = 0,
                var descriptionTextColorResId: Int = 0,
                var dialogBackgroundColorResId: Int = 0,
                var windowAnimationResId: Int = 0,
                var cancelable: Boolean? = null,
                var canceledOnTouchOutside: Boolean? = null
        ) : Serializable

        val data = Data()


        fun create(activity: FragmentActivity): AppRatingDialog {
            Preconditions.checkNotNull(activity, "FragmentActivity cannot be null")
            return AppRatingDialog(activity, data, activity.applicationContext)
        }


        fun setNumberOfStars(maxRating: Int): Builder {
            Preconditions.checkArgument(
                    maxRating in 1..MAX_RATING,
                    "max rating value should be between 1 and $MAX_RATING"
            )
            data.numberOfStars = maxRating
            return this
        }




        fun setDefaultRating(defaultRating: Int): Builder {
            Preconditions.checkArgument(
                    defaultRating >= 0 && defaultRating <= data.numberOfStars,
                    "default rating value should be between 0 and " + data.numberOfStars
            )
            data.defaultRating = defaultRating
            return this
        }


        fun setAfterInstallDay(afterInstallDay: Int): Builder {
            Preconditions.checkArgument(
                    afterInstallDay >= 0,
                    "AfterInstallDay value should be more than 0"
            )
            data.afterInstallDay = afterInstallDay
            return this
        }


        fun setNumberOfLaunches(numberOfLaunches: Int): Builder {
            Preconditions.checkArgument(
                    numberOfLaunches >= 0,
                    "NumberOfLaunches value should be more than 0"
            )
            data.numberOfLaunches = numberOfLaunches
            return this
        }


        fun setRemindIntervalDay(remindInterval: Int): Builder {
            Preconditions.checkArgument(
                    remindInterval >= 0,
                    "RemindInterval value should be more than 0"
            )
            data.remindInterval = remindInterval
            return this
        }


        fun setTitle(title: String): Builder {
            Preconditions.checkArgument(!TextUtils.isEmpty(title), "title cannot be empty")
            data.title.text = title
            return this
        }


        fun setTitle(@StringRes resId: Int): Builder {
            data.title.textResId = resId
            return this
        }


        fun setDescription(content: String): Builder {
            Preconditions.checkArgument(!TextUtils.isEmpty(content), "description cannot be empty")
            data.description.text = content
            return this
        }


        fun setDescription(@StringRes resId: Int): Builder {
            data.description.textResId = resId
            return this
        }

        fun setPositiveButtonText(positiveButtonText: String): Builder {
            Preconditions.checkArgument(
                    !TextUtils.isEmpty(positiveButtonText),
                    "text cannot be empty"
            )
            data.positiveButtonText.text = positiveButtonText
            return this
        }


        fun setPositiveButtonText(@StringRes resId: Int): Builder {
            data.positiveButtonText.textResId = resId
            return this
        }


        fun setNegativeButtonText(negativeButtonText: String): Builder {
            Preconditions.checkArgument(
                    !TextUtils.isEmpty(negativeButtonText),
                    "text cannot be empty"
            )
            data.negativeButtonText.text = negativeButtonText
            return this
        }


        fun setNeutralButtonText(neutralButtonText: String): Builder {
            Preconditions.checkArgument(
                    !TextUtils.isEmpty(neutralButtonText),
                    "text cannot be empty"
            )
            data.neutralButtonText.text = neutralButtonText
            return this
        }


        fun setNegativeButtonText(@StringRes resId: Int): Builder {
            data.negativeButtonText.textResId = resId
            return this
        }


        fun setNeutralButtonText(@StringRes resId: Int): Builder {
            data.neutralButtonText.textResId = resId
            return this
        }


        fun setStarColor(@ColorRes colorResId: Int): Builder {
            data.starColorResId = colorResId
            return this
        }

        fun setTitleTextColor(@ColorRes colorResId: Int): Builder {
            data.titleTextColorResId = colorResId
            return this
        }


        fun setDescriptionTextColor(@ColorRes colorResId: Int): Builder {
            data.descriptionTextColorResId = colorResId
            return this
        }

        fun setDialogBackgroundColor(@ColorRes colorResId: Int): Builder {
            data.dialogBackgroundColorResId = colorResId
            return this
        }


        fun setWindowAnimation(@StyleRes animationResId: Int): Builder {
            data.windowAnimationResId = animationResId
            return this
        }


        fun setCancelable(cancelable: Boolean): Builder {
            data.cancelable = cancelable
            return this
        }


        fun setCanceledOnTouchOutside(cancel: Boolean): Builder {
            data.canceledOnTouchOutside = cancel
            return this
        }
    }


}
