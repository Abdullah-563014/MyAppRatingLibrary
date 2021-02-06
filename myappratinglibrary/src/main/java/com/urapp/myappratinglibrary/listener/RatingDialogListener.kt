package com.urapp.myappratinglibrary.listener


interface RatingDialogListener {

    fun onPositiveButtonClicked(rate: Int)

    fun onNegativeButtonClicked()

    fun onNeutralButtonClicked()
}