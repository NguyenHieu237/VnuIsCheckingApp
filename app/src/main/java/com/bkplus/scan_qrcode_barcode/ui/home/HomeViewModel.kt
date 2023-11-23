package com.bkplus.scan_qrcode_barcode.ui.home

import androidx.lifecycle.ViewModel
import com.bkplus.scan_qrcode_barcode.model.Language
import com.bkplus.scan_qrcode_barcode.preferences.QRCodePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    fun setLanguageApp(item: Language) {
        QRCodePreferences.getPrefsInstance().locale = item.codeLanguage
    }

}
