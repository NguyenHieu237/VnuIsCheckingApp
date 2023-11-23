package com.bkplus.scan_qrcode_barcode

import android.app.Application
import androidx.core.content.res.ResourcesCompat
import com.ads.control.admob.Admob
import com.ads.control.ads.AperoAd
import com.ads.control.config.AdjustConfig
import com.ads.control.config.AperoAdConfig
import com.bkplus.scan_qrcode_barcode.preferences.QRCodePreferences
import com.bkplus.scan_qrcode_barcode.utils.RemoteUtils
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import dagger.hilt.android.HiltAndroidApp
import io.realm.Realm
import io.realm.RealmConfiguration

@HiltAndroidApp
class QRCodeApplication : Application() {

    private var aperoAdConfig: AperoAdConfig? = null

    override fun onCreate() {
        super.onCreate()
        ResourcesCompat.getFont(this.applicationContext, R.font.source_sans_3_regular)

        QRCodePreferences.initPrefs(applicationContext)
        initRemoteConfig()
        initAds()
        fetchRemoteConfig()
        configRealm()
    }

    private fun initRemoteConfig() {
        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {}
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)

        remoteConfig.fetchAndActivate()
    }

    private fun fetchRemoteConfig() {
        val config = FirebaseRemoteConfig.getInstance()
        val configSettings =
            FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(3600).build()
        config.setConfigSettingsAsync(configSettings)

        config.fetchAndActivate().addOnCompleteListener {
            if (it.isSuccessful) {
                val updated = it.result
                if (updated) {
                    RemoteUtils.isShowInterSplash =
                        config.getBoolean(RemoteUtils.REMOTE_INTER_SPLASH)
                    RemoteUtils.isShowInterHistory =
                        config.getBoolean(RemoteUtils.REMOTE_INTER_HISTORY)
                    RemoteUtils.isShowInterSelectPhoto =
                        config.getBoolean(RemoteUtils.REMOTE_INTER_SELECT_PHOTO)
                    RemoteUtils.isShowInterNewQRCode =
                        config.getBoolean(RemoteUtils.REMOTE_INTER_NEW_QRCODE)
                    RemoteUtils.isShowInterQRCodeMenu =
                        config.getBoolean(RemoteUtils.REMOTE_INTER_QRCODE_MENU)
                    RemoteUtils.isShowNativeLanguage =
                        config.getBoolean(RemoteUtils.REMOTE_NATIVE_LANGUAGE)
                    RemoteUtils.isShowNativeWifi = config.getBoolean(RemoteUtils.REMOTE_NATIVE_WIFI)
                    RemoteUtils.isShowNativeWebsite =
                        config.getBoolean(RemoteUtils.REMOTE_NATIVE_WEBSITE)
                    RemoteUtils.isShowNativeText = config.getBoolean(RemoteUtils.REMOTE_NATIVE_TEXT)
                    RemoteUtils.isShowNativeContact =
                        config.getBoolean(RemoteUtils.REMOTE_NATIVE_CONTACT)
                    RemoteUtils.isShowNativePhone =
                        config.getBoolean(RemoteUtils.REMOTE_NATIVE_PHONE)
                    RemoteUtils.isShowNativeEmail =
                        config.getBoolean(RemoteUtils.REMOTE_NATIVE_EMAIL)
                    RemoteUtils.isShowNativeDetail =
                        config.getBoolean(RemoteUtils.REMOTE_NATIVE_DETAIL)
                    RemoteUtils.isShowNativeHistoryList = config.getBoolean(
                        RemoteUtils.REMOTE_NATIVE_HISTORY_LIST
                    )
                    RemoteUtils.isShowScanBanner = config.getBoolean(RemoteUtils.REMOTE_SCAN_BANNER)
                    RemoteUtils.isShowQRCodeMenuBanner =
                        config.getBoolean(RemoteUtils.REMOTE_QRCODE_MENU_BANNER)
                    RemoteUtils.isShowAdsResume = config.getBoolean(RemoteUtils.REMOTE_ADS_RESUME)
                }
            }
        }
    }

    private fun initAdjust() {
        val adjustConfig = AdjustConfig("ADJUST_TOKEN")
        aperoAdConfig?.adjustConfig = adjustConfig
    }

    private fun initAds() {
        val env =
            if (BuildConfig.build_debug) AperoAdConfig.ENVIRONMENT_DEVELOP else AperoAdConfig.ENVIRONMENT_PRODUCTION

        aperoAdConfig = AperoAdConfig(this, AperoAdConfig.PROVIDER_ADMOB, env)
        initAdjust()
        Admob.getInstance().setOpenActivityAfterShowInterAds(true)
        Admob.getInstance().setFan(true)
        Admob.getInstance().setAppLovin(true)
        Admob.getInstance().setColony(true)

        aperoAdConfig?.idAdResume = BuildConfig.Ads_resume

        AperoAd.getInstance().init(this, aperoAdConfig, false)

        Admob.getInstance().setDisableAdResumeWhenClickAds(true)
    }

    private fun configRealm() {
        Realm.init(this)
        val realmConfig = RealmConfiguration.Builder()
            .name("bkplus.realm")
            .schemaVersion(0)
            .build()
        Realm.setDefaultConfiguration(realmConfig)
    }
}
