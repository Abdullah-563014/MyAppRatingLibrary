package com.urapp.myappratinglibrary

import android.content.res.Resources
import android.text.TextUtils
import androidx.annotation.StringRes
import java.io.Serializable

class StringValue : Serializable {

    var text: String? = null
        set(value) {
            field = value
            value?.let {
                textResId = 0
            }
        }

    @StringRes
    var textResId: Int = 0
        set(value) {
            field = value
            if (value != 0) {
                text = null
            }
        }

    fun resolveText(resources: Resources): String? {
        return if (TextUtils.isEmpty(text)) {
            if (textResId == 0) {
                return null
            }
            resources.getString(textResId)
        } else text
    }
}