package com.bkplus.scan_qrcode_barcode.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.ads.control.admob.Admob
import com.ads.control.admob.AppOpenManager
import com.ads.control.ads.AperoAd
import com.ads.control.ads.AperoAdCallback
import com.bkplus.scan_qrcode_barcode.BuildConfig
import com.bkplus.scan_qrcode_barcode.utils.MyContextWrapper
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.databinding.ActivitySplashBinding
import com.bkplus.scan_qrcode_barcode.preferences.QRCodePreferences
import com.bkplus.scan_qrcode_barcode.ui.SplashActivity.Companion.isAdInterShowed
import com.bkplus.scan_qrcode_barcode.ui.home.HomeActivity
import com.bkplus.scan_qrcode_barcode.utils.RemoteUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var splashBinding: ActivitySplashBinding

    private val splashViewModel: SplashViewModel by viewModels()

    private var isDestroyActivity: Boolean = false

    private val timeout: Long = 30000
    private val timeDelay = 1000L
    private val timeInterval = 20L

    private var restart = false
    private var timer: CountDownTimer? = null
    private var onNextScreen = false
    private var mPrevConfig: Configuration? = null

    companion object {

        var isAdInterShowed = false
        var isInAppRunning = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        mPrevConfig = Configuration(resources.configuration)

        startSplash()
    }

    override fun onResume() {
        super.onResume()
        isInAppRunning = true
        if (!onNextScreen) {
            AperoAd.getInstance().onCheckShowSplashWhenFail(this, adListener, timeDelay.toInt())
        }
    }

    private fun startSplash() {
        timer = object : CountDownTimer(timeDelay, timeInterval) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                startMain()
            }
        }
        (timer as CountDownTimer).start()
    }

    private val adListener: AperoAdCallback = object : AperoAdCallback() {

        override fun onNextAction() {
            super.onNextAction()
            startMain()
        }

        override fun onAdClosed() {
            super.onAdClosed()
            isAdInterShowed = false
            AppOpenManager.getInstance().enableAppResume()
        }

        override fun onAdLoaded() {
            super.onAdLoaded()
            isAdInterShowed = true
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUI()
        }
    }

    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(window, splashBinding.root).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }


    override fun onStop() {
        super.onStop()
        restart = true
    }

    override fun onDestroy() {
        isDestroyActivity = true
        AppOpenManager.getInstance().enableAppResume()
        super.onDestroy()
    }

    override fun onRestart() {
        super.onRestart()
        timer?.cancel()
        timer = null
        restart = false
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(
            MyContextWrapper.wrap(
                newBase, QRCodePreferences.getPrefsInstance().locale ?: "en"
            )
        )
    }

    private fun startMain() {
        if (!restart) {
            val navController = Navigation.findNavController(this, R.id.nav_host_splash_fragment)
            if (!isDestroyActivity) {
                onNextScreen = true
                AppOpenManager.getInstance().enableAppResume()
                splashViewModel.isNewUser().let {
                    if (it) {
                        navController.navigate(R.id.openFirstLanguage)
                    } else {
                        if (!QRCodePreferences.getPrefsInstance().isViewedOnboard) {
                            navController.navigate(R.id.openOnboarding)
                        } else {
                            navController.navigate(R.id.openMain)
                            finish()
                        }
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean =
        findNavController(R.id.nav_host_splash_fragment).navigateUp()

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        configurationChanged(newConfig)
        mPrevConfig = Configuration(newConfig)
    }

    private fun configurationChanged(newConfig: Configuration) {
        if (isNightConfigChanged(newConfig)) { // night mode has changed
            // do your thing
        }
    }

    private fun isNightConfigChanged(newConfig: Configuration): Boolean {
        return newConfig.diff(mPrevConfig) and ActivityInfo.CONFIG_UI_MODE !== 0 && isOnDarkMode(
            newConfig
        ) != isOnDarkMode(mPrevConfig)
    }

    private fun isOnDarkMode(configuration: Configuration?): Boolean {
        configuration?.let {
            return configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK === Configuration.UI_MODE_NIGHT_YES
        }
        return false
    }

    override fun onBackPressed() {
        exitProcess(0)
    }
}
