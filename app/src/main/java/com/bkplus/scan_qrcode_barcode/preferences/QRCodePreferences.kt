package com.bkplus.scan_qrcode_barcode.preferences

import android.content.Context
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.bkplus.scan_qrcode_barcode.R

class QRCodePreferences(context: Context) {
    private val prefsNewUser = "prefsNewUser"
    private val prefsLocale = "locale_pref"
    private val searchEngineName = "searchEngineName"
    private val searchEnginePosition = "searchEnginePosition"
    private val prefsViewedOnboard = "prefsViewedOnboard"
    private val switch_beep = "switch_beep"
    private val switch_vibration = "switch_vibration"
    private val studentToken = "student_token"
    private val is_Admin = "is_admin"

    private val mPrefs = PreferenceManager.getDefaultSharedPreferences(context)

    var newUser
        get() = mPrefs.getBoolean(prefsNewUser, true)
        set(value) = mPrefs.edit { putBoolean(prefsNewUser, value) }

    var isViewedOnboard
        get() = mPrefs.getBoolean(prefsViewedOnboard, false)
        set(value) = mPrefs.edit { putBoolean(prefsViewedOnboard, value) }

    var locale
        get() = mPrefs.getString(prefsLocale, "en")
        set(value) = mPrefs.edit { putString(prefsLocale, value) }

    fun setValueBool(key: String?, value: Boolean) {
        mPrefs.edit()
            .putBoolean(key, value)
            .apply()
    }

    var token
        get() = mPrefs.getString(studentToken, "")
        set(value) = mPrefs.edit { putString(studentToken, value) }


    fun getValueBool(key: String?, def: Boolean): Boolean {
        return mPrefs.getBoolean(key, def)
    }

    var enginePosition
        get() = mPrefs.getInt(searchEnginePosition,-1)
        set(value) = mPrefs.edit().putInt(searchEnginePosition, value).apply()
    var engineName
        get() = mPrefs.getInt(searchEngineName,R.string.google)
        set(value) = mPrefs.edit().putInt(searchEngineName, value).apply()
    var switchBeep
        get() = mPrefs.getBoolean(switch_beep,false)
        set(value) = mPrefs.edit().putBoolean(switch_beep, value).apply()
    var switchVibration
        get() = mPrefs.getBoolean(switch_vibration,false)
        set(value) = mPrefs.edit().putBoolean(switch_vibration, value).apply()
    var isAdmin
        get() = mPrefs.getBoolean(is_Admin,false)
        set(value) = mPrefs.edit().putBoolean(is_Admin, value).apply()



    companion object {
        @Volatile
        private var INSTANCE: QRCodePreferences? = null

        fun initPrefs(context: Context): QRCodePreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = QRCodePreferences(context)
                INSTANCE = instance
                // return instance
                instance
            }
        }

        fun getPrefsInstance(): QRCodePreferences {
            return INSTANCE ?: error("GoPreferences not initialized!")
        }
    }
}
