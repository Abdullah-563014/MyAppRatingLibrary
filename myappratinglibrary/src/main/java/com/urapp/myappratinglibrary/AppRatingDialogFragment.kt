package com.urapp.myappratinglibrary


import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.urapp.myappratinglibrary.Constants.CURRENT_RATE_NUMBER
import com.urapp.myappratinglibrary.Constants.DIALOG_DATA
import com.urapp.myappratinglibrary.PreferenceHelper.setAgreeShowDialog
import com.urapp.myappratinglibrary.PreferenceHelper.setLaunchTimes
import com.urapp.myappratinglibrary.PreferenceHelper.setRemindInterval
import com.urapp.myappratinglibrary.extensions.applyIfNotZero
import com.urapp.myappratinglibrary.listener.RatingDialogListener


class AppRatingDialogFragment : DialogFragment() {

    private var listener: RatingDialogListener? = null
        get() {
            if (host is RatingDialogListener) {
                return host as RatingDialogListener
            }
            return targetFragment as RatingDialogListener?
        }

    private lateinit var data: AppRatingDialog.Builder.Data
    private lateinit var alertDialog: AlertDialog
    private lateinit var dialogView: AppRatingDialogView

    private val title by lazy { data.title.resolveText(resources) }
    private val description by lazy { data.description.resolveText(resources) }
    private val positiveButtonText by lazy { data.positiveButtonText.resolveText(resources) }
    private val neutralButtonText by lazy { data.neutralButtonText.resolveText(resources) }
    private val negativeButtonText by lazy { data.negativeButtonText.resolveText(resources) }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return setupAlertDialog(activity!!)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putFloat(CURRENT_RATE_NUMBER, dialogView.rateNumber)
        super.onSaveInstanceState(outState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val rateNumber: Float? = savedInstanceState?.getFloat(CURRENT_RATE_NUMBER)
        if (rateNumber != null) {
            dialogView.setDefaultRating(rateNumber.toInt())
        }
    }

    private fun setupAlertDialog(context: Context): AlertDialog {
        dialogView = AppRatingDialogView(context)
        val builder = AlertDialog.Builder(activity!!)
        data = arguments?.getSerializable(DIALOG_DATA) as AppRatingDialog.Builder.Data

        setupPositiveButton(dialogView, builder)
        setupNegativeButton(builder)
        setupNeutralButton(builder)
        setupTitleAndContentMessages(dialogView)
        setupColors(dialogView)
        setupRatingBar()

        builder.setView(dialogView)
        alertDialog = builder.create()
        if (data.dialogBackgroundColorResId != 0) {
            alertDialog.window?.setBackgroundDrawableResource(data.dialogBackgroundColorResId)
        }
        setupAnimation()
        setupCancelable()
        return alertDialog
    }

    private fun setupRatingBar() {
        dialogView.setNumberOfStars(data.numberOfStars)
        dialogView.setDefaultRating(data.defaultRating)
    }

    private fun setupCancelable() {
        data.cancelable?.let { isCancelable = it }
        data.canceledOnTouchOutside?.let { alertDialog.setCanceledOnTouchOutside(it) }
    }

    private fun setupAnimation() {
        if (data.windowAnimationResId != 0) {
            alertDialog.window?.attributes?.windowAnimations = data.windowAnimationResId
        }
    }

    private fun setupColors(dialogView: AppRatingDialogView) {
        data.titleTextColorResId.applyIfNotZero {
            dialogView.setTitleTextColor(this)
        }
        data.descriptionTextColorResId.applyIfNotZero {
            dialogView.setDescriptionTextColor(this)
        }
        data.starColorResId.applyIfNotZero {
            dialogView.setStarColor(this)
        }
    }

    private fun setupTitleAndContentMessages(dialogView: AppRatingDialogView) {
        if (!title.isNullOrEmpty()) {
            dialogView.setTitleText(title!!)
        }
        if (!description.isNullOrEmpty()) {
            dialogView.setDescriptionText(description!!)
        }
    }

    private fun setupNegativeButton(builder: AlertDialog.Builder) {
        if (!TextUtils.isEmpty(negativeButtonText)) {
            builder.setNegativeButton(negativeButtonText) { _, _ ->
                setAgreeShowDialog(context, false)
                listener?.onNegativeButtonClicked()
            }
        }
    }

    private fun setupPositiveButton(dialogView: AppRatingDialogView, builder: AlertDialog.Builder) {
        if (!TextUtils.isEmpty(positiveButtonText)) {
            builder.setPositiveButton(positiveButtonText) { _, _ ->
                val rateNumber = dialogView.rateNumber.toInt()
                setAgreeShowDialog(context, false)
                listener?.onPositiveButtonClicked(rateNumber)
            }
        }
    }

    private fun setupNeutralButton(builder: AlertDialog.Builder) {
        if (!TextUtils.isEmpty(neutralButtonText)) {
            builder.setNeutralButton(neutralButtonText) { _, _ ->
                setRemindInterval(context)
                setLaunchTimes(context!!,0)
                listener?.onNeutralButtonClicked()
            }
        }
    }

    companion object {

        fun newInstance(data: AppRatingDialog.Builder.Data): AppRatingDialogFragment {
            val fragment = AppRatingDialogFragment()
            val bundle = Bundle()
            bundle.putSerializable(DIALOG_DATA, data)
            fragment.arguments = bundle
            return fragment
        }
    }


}