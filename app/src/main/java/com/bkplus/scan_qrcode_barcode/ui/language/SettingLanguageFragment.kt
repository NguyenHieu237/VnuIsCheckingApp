package com.bkplus.scan_qrcode_barcode.ui.language

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.ads.control.admob.AppOpenManager
import com.bkplus.scan_qrcode_barcode.ui.home.HomeViewModel
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.databinding.FragmentLanguageBinding
import com.bkplus.scan_qrcode_barcode.model.Language
import com.bkplus.scan_qrcode_barcode.preferences.QRCodePreferences
import com.bkplus.scan_qrcode_barcode.ui.first_language.SettingLanguageAdapter
import com.bkplus.scan_qrcode_barcode.ui.first_language.SettingLanguageViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingLanguageFragment : Fragment(), SettingLanguageAdapter.OnClickItemListener {

    private lateinit var settingLanguageBinding: FragmentLanguageBinding
    private val homeViewModel: HomeViewModel by activityViewModels()
    private val settingLanguageViewModel: SettingLanguageViewModel by viewModels()
    private lateinit var settingLanguageAdapter: SettingLanguageAdapter
    private var selectedLanguage: Language? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        settingLanguageBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_language, container, false)
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
        activity?.let {
            it.window.statusBarColor = ContextCompat.getColor(it, R.color.white)

            val decor: View = it.window.decorView
            decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        settingLanguageBinding.choseBtn.setOnClickListener {

            activity?.let {
                selectedLanguage?.let { language ->
                    homeViewModel.setLanguageApp(language)
                }
                // Go on from settings
                refreshLayout()
            }
        }

        settingLanguageBinding.actionBackBtn.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                .popBackStack()
        }

        settingLanguageAdapter = SettingLanguageAdapter(requireContext(), this, activity)

        settingLanguageBinding.languageSettingRcv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        settingLanguageBinding.languageSettingRcv.adapter = settingLanguageAdapter
    }

    private fun setupViewModel() {
        settingLanguageViewModel.onLanguageListReady.observe(viewLifecycleOwner) { listLanguage ->
            settingLanguageAdapter.updateAllData(listLanguage)
            selectedLanguage =
                QRCodePreferences.getPrefsInstance().locale?.let { Language(it, null, null) }
        }
        context?.let { settingLanguageViewModel.fetchLanguageList() }

    }

    private fun refreshLayout() {
        val intent = requireActivity().intent
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        requireActivity().finish()
        startActivity(intent)
    }

    override fun onClickItem(item: Language) {
        selectedLanguage = item
    }
}
