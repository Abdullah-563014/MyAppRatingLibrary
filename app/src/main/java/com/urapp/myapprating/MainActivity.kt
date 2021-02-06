package com.urapp.myapprating


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.urapp.myapprating.databinding.ActivityMainBinding
import com.urapp.myappratinglibrary.AppRatingDialog
import com.urapp.myappratinglibrary.listener.RatingDialogListener
import java.util.*


class MainActivity : AppCompatActivity(), RatingDialogListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        buildRatingDialog().monitor()
        buildRatingDialog().showRateDialogIfMeetsConditions()


    }


    private fun buildRatingDialog(): AppRatingDialog {
        return AppRatingDialog.Builder()
            .setCancelable(false)
            .setPositiveButtonText("Submit")
            .setNegativeButtonText("Never")
            .setNeutralButtonText("Later")
            .setTitle("sample title")
            .setDescription("Please select some stars and give your feedback")
            .setStarColor(R.color.red)
            .setAfterInstallDay(0)
                .setDefaultRating(3)
            .setNumberOfLaunches(3)
            .setRemindIntervalDay(0)
            .setCanceledOnTouchOutside(false)
            .create(this)
    }



    override fun onPositiveButtonClicked(rate: Int) {
        Log.d(Constants.TAG,"onPositiveButtonClicked $rate")
    }

    override fun onNegativeButtonClicked() {
        Log.d(Constants.TAG,"onNegativeButtonClicked")
    }

    override fun onNeutralButtonClicked() {
        Log.d(Constants.TAG,"onNeutralButtonClicked")
    }


}