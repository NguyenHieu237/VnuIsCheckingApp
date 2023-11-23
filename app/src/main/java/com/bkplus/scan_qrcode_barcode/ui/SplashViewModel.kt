package com.bkplus.scan_qrcode_barcode.ui

import androidx.lifecycle.ViewModel
import com.bkplus.scan_qrcode_barcode.model.Language
import com.bkplus.scan_qrcode_barcode.preferences.QRCodePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() : ViewModel() {

    fun setNewUser(isFirstLogin: Boolean) {
        QRCodePreferences.getPrefsInstance().newUser = isFirstLogin
    }

    fun isNewUser(): Boolean {
        return QRCodePreferences.getPrefsInstance().newUser
    }

    fun setLanguageApp(item: Language) {
        QRCodePreferences.getPrefsInstance().locale = item.codeLanguage
    }

    fun getLanguageApp(): String {
        return QRCodePreferences.getPrefsInstance().locale ?: "en"
    }
}
