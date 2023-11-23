package com.bkplus.scan_qrcode_barcode.utils

import com.bkplus.scan_qrcode_barcode.preferences.QRCodePreferences

object RemoteUtils {
    const val REMOTE_INTER_SPLASH = "Inter_Splash"
    const val REMOTE_NATIVE_LANGUAGE = "Native_Language"
    const val REMOTE_INTER_HISTORY = "Inter_History"
    const val REMOTE_INTER_SELECT_PHOTO = "Inter_Select_Photo"
    const val REMOTE_INTER_NEW_QRCODE = "Inter_New_QRCode"
    const val REMOTE_INTER_QRCODE_MENU = "QRCode_Inter_Menu"
    const val REMOTE_NATIVE_WIFI = "Native_Wifi"
    const val REMOTE_NATIVE_WEBSITE = "Native_Website"
    const val REMOTE_NATIVE_TEXT = "Native_Text"
    const val REMOTE_NATIVE_CONTACT = "Native_Contact"
    const val REMOTE_NATIVE_PHONE = "Native_Phone"
    const val REMOTE_NATIVE_EMAIL = "Native_Email"
    const val REMOTE_NATIVE_DETAIL = "Native_Detail"
    const val REMOTE_NATIVE_HISTORY_LIST = "Native_History_List"
    const val REMOTE_SCAN_BANNER = "Scan_Banner"
    const val REMOTE_QRCODE_MENU_BANNER = "QRCode_Menu_Banner"
    const val REMOTE_ADS_RESUME = "Ads_resume"

    var isShowInterSplash: Boolean
        get() = QRCodePreferences.getPrefsInstance().getValueBool(REMOTE_INTER_SPLASH, true)
        set(value) = QRCodePreferences.getPrefsInstance().setValueBool(REMOTE_INTER_SPLASH, value)

    var isShowInterHistory: Boolean
        get() = QRCodePreferences.getPrefsInstance().getValueBool(REMOTE_INTER_HISTORY, true)
        set(value) = QRCodePreferences.getPrefsInstance().setValueBool(REMOTE_INTER_HISTORY, value)

    var isShowInterSelectPhoto: Boolean
        get() = QRCodePreferences.getPrefsInstance().getValueBool(REMOTE_INTER_SELECT_PHOTO, true)
        set(value) = QRCodePreferences.getPrefsInstance().setValueBool(REMOTE_INTER_SELECT_PHOTO, value)

    var isShowInterNewQRCode: Boolean
        get() = QRCodePreferences.getPrefsInstance().getValueBool(REMOTE_INTER_NEW_QRCODE, true)
        set(value) = QRCodePreferences.getPrefsInstance().setValueBool(REMOTE_INTER_NEW_QRCODE, value)

    var isShowInterQRCodeMenu: Boolean
        get() = QRCodePreferences.getPrefsInstance().getValueBool(REMOTE_INTER_QRCODE_MENU, true)
        set(value) = QRCodePreferences.getPrefsInstance().setValueBool(REMOTE_INTER_QRCODE_MENU, value)

    var isShowNativeLanguage: Boolean
        get() = QRCodePreferences.getPrefsInstance().getValueBool(REMOTE_NATIVE_LANGUAGE, true)
        set(value) = QRCodePreferences.getPrefsInstance().setValueBool(REMOTE_NATIVE_LANGUAGE, value)

    var isShowNativeWifi: Boolean
        get() = QRCodePreferences.getPrefsInstance().getValueBool(REMOTE_NATIVE_WIFI, true)
        set(value) = QRCodePreferences.getPrefsInstance().setValueBool(REMOTE_NATIVE_WIFI, value)

    var isShowNativeWebsite: Boolean
        get() = QRCodePreferences.getPrefsInstance().getValueBool(REMOTE_NATIVE_WEBSITE, true)
        set(value) = QRCodePreferences.getPrefsInstance().setValueBool(REMOTE_NATIVE_WEBSITE, value)

    var isShowNativeText: Boolean
        get() = QRCodePreferences.getPrefsInstance().getValueBool(REMOTE_NATIVE_TEXT, true)
        set(value) = QRCodePreferences.getPrefsInstance().setValueBool(REMOTE_NATIVE_TEXT, value)

    var isShowNativeContact: Boolean
        get() = QRCodePreferences.getPrefsInstance().getValueBool(REMOTE_NATIVE_CONTACT, true)
        set(value) = QRCodePreferences.getPrefsInstance().setValueBool(REMOTE_NATIVE_CONTACT, value)

    var isShowNativePhone: Boolean
        get() = QRCodePreferences.getPrefsInstance().getValueBool(REMOTE_NATIVE_PHONE, true)
        set(value) = QRCodePreferences.getPrefsInstance().setValueBool(REMOTE_NATIVE_PHONE, value)

    var isShowNativeEmail: Boolean
        get() = QRCodePreferences.getPrefsInstance().getValueBool(REMOTE_NATIVE_EMAIL, true)
        set(value) = QRCodePreferences.getPrefsInstance().setValueBool(REMOTE_NATIVE_EMAIL, value)

    var isShowNativeDetail: Boolean
        get() = QRCodePreferences.getPrefsInstance().getValueBool(REMOTE_NATIVE_DETAIL, true)
        set(value) = QRCodePreferences.getPrefsInstance().setValueBool(REMOTE_NATIVE_DETAIL, value)

    var isShowNativeHistoryList: Boolean
        get() = QRCodePreferences.getPrefsInstance().getValueBool(REMOTE_NATIVE_HISTORY_LIST, true)
        set(value) = QRCodePreferences.getPrefsInstance().setValueBool(REMOTE_NATIVE_HISTORY_LIST, value)

    var isShowScanBanner: Boolean
        get() = QRCodePreferences.getPrefsInstance().getValueBool(REMOTE_SCAN_BANNER, true)
        set(value) = QRCodePreferences.getPrefsInstance().setValueBool(REMOTE_SCAN_BANNER, value)

    var isShowQRCodeMenuBanner: Boolean
        get() = QRCodePreferences.getPrefsInstance().getValueBool(REMOTE_QRCODE_MENU_BANNER, true)
        set(value) = QRCodePreferences.getPrefsInstance().setValueBool(REMOTE_QRCODE_MENU_BANNER, value)

    var isShowAdsResume: Boolean
        get() = QRCodePreferences.getPrefsInstance().getValueBool(REMOTE_ADS_RESUME, true)
        set(value) = QRCodePreferences.getPrefsInstance().setValueBool(REMOTE_ADS_RESUME, value)
}