package com.bkplus.scan_qrcode_barcode.base

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.bkplus.scan_qrcode_barcode.preferences.ContextUtils
import com.bkplus.scan_qrcode_barcode.preferences.QRCodePreferences
import java.util.Locale

abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        setupData()
        setupUI()
        setupListener()
    }

    protected abstract val layoutId: Int
    protected lateinit var binding: T
    protected open fun initBinding() {
        binding = createBinding()
    }

    protected open fun setupUI() {}
    protected open fun setupData() {}
    protected open fun setupListener() {}

    private fun createBinding(): T {
        return DataBindingUtil.setContentView(this, layoutId)
    }

    override fun attachBaseContext(newBase: Context?) {
        newBase?.let { ctx ->
            val preferences = QRCodePreferences.initPrefs(ctx)
            if (preferences.locale == null) {
                val localeUpdatedContext = ContextUtils.updateLocale(ctx, Locale.getDefault())
                super.attachBaseContext(localeUpdatedContext)
            } else {
                val locale = Locale.forLanguageTag(preferences.locale!!)
                val localeUpdatedContext = ContextUtils.updateLocale(ctx, locale)
                super.attachBaseContext(localeUpdatedContext)
            }
        }
    }

    protected fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(window, binding.root).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}
