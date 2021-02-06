package com.urapp.myappratinglibrary

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import java.util.*


object PreferenceHelper {


    private fun getPreference(context: Context): SharedPreferences{
        return context.getSharedPreferences(Constants.PREF_FILE_NAME, MODE_PRIVATE)
    }

    private fun getPreferencesEditor(context: Context): SharedPreferences.Editor{
        return getPreference(context).edit()
    }

    public fun clearSharedPreferences(context: Context) {
        val editor: SharedPreferences.Editor= getPreferencesEditor(context)
        editor.remove(Constants.PREF_KEY_INSTALL_DATE)
        editor.remove(Constants.PREF_KEY_LAUNCH_TIMES)
        editor.apply()
    }

    public fun setAgreeShowDialog(context: Context?, isAgree: Boolean) {
        context?.let { getPreferencesEditor(it).putBoolean(Constants.PREF_KEY_IS_AGREE_SHOW_DIALOG, isAgree).apply() }
    }

    public fun getIsAgreeShowDialog(context: Context, defaultValue: Boolean): Boolean {
        return getPreference(context).getBoolean(Constants.PREF_KEY_IS_AGREE_SHOW_DIALOG, defaultValue)
    }

    public fun setRemindInterval(context: Context?) {
        val editor = context?.let { getPreferencesEditor(it) }
        if (editor != null) {
            editor.remove(Constants.PREF_KEY_REMIND_INTERVAL)
            editor.putLong(Constants.PREF_KEY_REMIND_INTERVAL, Date().time)
            editor.apply()
        }
    }

    public fun getRemindInterval(context: Context, defaultValue: Long): Long {
        return getPreference(context).getLong(Constants.PREF_KEY_REMIND_INTERVAL, defaultValue)
    }

    public fun setInstallDate(context: Context) {
        getPreferencesEditor(context).putLong(Constants.PREF_KEY_INSTALL_DATE, Date().time).apply()
    }

    public fun getInstallDate(context: Context, defaultValue: Long): Long {
        return getPreference(context).getLong(Constants.PREF_KEY_INSTALL_DATE, defaultValue)
    }

    public fun setLaunchTimes(context: Context, launchTimes: Int) {
        getPreferencesEditor(context).putInt(Constants.PREF_KEY_LAUNCH_TIMES, launchTimes).apply()
    }

    public fun getLaunchTimes(context: Context, defaultValue: Int): Int {
        return getPreference(context).getInt(Constants.PREF_KEY_LAUNCH_TIMES, defaultValue)
    }

    public fun isFirstLaunch(context: Context): Boolean {
        return getPreference(context).getLong(Constants.PREF_KEY_INSTALL_DATE, 0L) == 0L
    }







}