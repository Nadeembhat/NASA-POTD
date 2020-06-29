package `in`.ernb.nasaapod.util

import android.content.SharedPreferences

/**
 * Author Nadeem Bhat ,
 * Created by Nadeem Bhat on Sunday, Jun, 2020.
 * Copy Right (c) 5:09 PM.
 * Srinagar,Kashmir
 * ennennbee@gmail.com
 * NASA APOD
 */


class AppPreference(val sharedPreferences: SharedPreferences) {

    fun setPermissionStatus(status:Boolean){
        sharedPreferences.edit().putBoolean("permission", status).apply();
    }
    fun getpermissionStatus() : Boolean {
        return sharedPreferences.getBoolean("permission", false)
    }
    fun setDirStatus(status:Boolean){
        sharedPreferences.edit().putBoolean("Dir", status).apply();
    }
    fun getDirStatus() : Boolean {
        return sharedPreferences.getBoolean("Dir", false)
    }





}