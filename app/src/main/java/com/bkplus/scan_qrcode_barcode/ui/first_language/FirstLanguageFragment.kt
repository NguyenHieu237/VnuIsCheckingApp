package com.bkplus.scan_qrcode_barcode.ui.first_language

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.ads.control.admob.AppOpenManager
import com.bkplus.scan_qrcode_barcode.utils.MyContextWrapper
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.databinding.FragmentFirstLanguageBinding
import com.bkplus.scan_qrcode_barcode.model.Language
import com.bkplus.scan_qrcode_barcode.preferences.QRCodePreferences
import com.bkplus.scan_qrcode_barcode.ui.SplashActivity
import com.bkplus.scan_qrcode_barcode.ui.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FirstLanguageFragment : Fragment(), SettingLanguageAdapter.OnClickItemListener {

    private lateinit var settingLanguageBinding: FragmentFirstLanguageBinding
    private val splashViewModel: SplashViewModel by activityViewModels()
    private val settingLanguageViewModel: SettingLanguageViewModel by viewModels()
    private lateinit var settingLanguageAdapter: SettingLanguageAdapter
    private var selectedLanguage: Language? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        settingLanguageBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_first_language, container, false)
        return settingLanguageBinding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        initiateView()
        setupViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        AppOpenManager.getInstance().enableAppResume()
    }

    private fun initiateView() {

        if (activity is SplashActivity) (activity as SplashActivity).apply {
            window.statusBarColor = ContextCompat.getColor(this, R.color.transparent)
        }

        activity?.let {
            it.window.statusBarColor = ContextCompat.getColor(it, R.color.colorPrimary)

            val decor: View = it.window.decorView
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        settingLanguageBinding.choseBtn.setOnClickListener {

            activity?.let {
                if (activity is SplashActivity) {
                    selectedLanguage?.let { language ->
                        splashViewModel.setLanguageApp(language)
                    }
                    context?.let { ctx ->
                        val context: Context = MyContextWrapper.wrap(
                            ctx, splashViewModel.getLanguageApp()
                        )
                        resources.updateConfiguration(
                            context.resources.configuration, context.resources.displayMetrics
                        )
                    }
                    navigateToOnBoarding(it)
                }
            }
        }

        settingLanguageAdapter = SettingLanguageAdapter(requireContext(), this, activity)

        settingLanguageBinding.languageSettingRcv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        settingLanguageBinding.languageSettingRcv.adapter = settingLanguageAdapter
    }

    private fun setupViewModel() {
        settingLanguageViewModel.onLanguageListReady.observe(viewLifecycleOwner) { listLanguage ->
            settingLanguageAdapter.updateAllData(listLanguage)
            selectedLanguage = if (activity is SplashActivity) {
                listLanguage[0]
            } else {
                QRCodePreferences.getPrefsInstance().locale?.let { Language(it, null, null) }
            }
        }
        if (activity is SplashActivity) {
            context?.let { settingLanguageViewModel.fetchFirstLanguageList() }
        } else {
            context?.let { settingLanguageViewModel.fetchLanguageList() }
        }

    }

    private fun navigateToOnBoarding(fragmentActivity: FragmentActivity) {
        splashViewModel.setNewUser(false)
        Navigation.findNavController(fragmentActivity, R.id.nav_host_splash_fragment)
            .navigate(R.id.openOnboarding)
    }

    override fun onClickItem(item: Language) {
        selectedLanguage = item
    }
}
