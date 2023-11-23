package com.bkplus.scan_qrcode_barcode.ui.settings

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.ads.control.admob.AppOpenManager
import com.bkplus.scan_qrcode_barcode.BuildConfig
import com.bkplus.scan_qrcode_barcode.Constants.LINK_STORE
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.base.BaseFragment
import com.bkplus.scan_qrcode_barcode.databinding.FragmentSettingsBinding
import com.bkplus.scan_qrcode_barcode.preferences.QRCodePreferences
import com.bkplus.scan_qrcode_barcode.ui.PrivacyDialog
import com.bkplus.scan_qrcode_barcode.ui.ratting.RattingDialog
import com.bkplus.scan_qrcode_barcode.ui.scanner.ScannerFragment
import com.bkplus.scan_qrcode_barcode.ui.settings.search_engine.SearchEngineDialog
import com.google.android.gms.tasks.Task
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory

class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    private var reviewManager: ReviewManager? = null
    private var reviewInfo: ReviewInfo? = null
    private val requestCodeRate = 1234
    private lateinit var searchEngineDialog: SearchEngineDialog
    private var dialogPrivacy: PrivacyDialog? = null
    private val requestCodeShare = 102

    override val layoutId: Int
        get() = R.layout.fragment_settings

    companion object {
        fun newInstance(): SettingsFragment {
            val args = Bundle()
            val fragment = SettingsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun setupUI() {
        getReviewInfo()
        binding.switchBeep.isChecked = QRCodePreferences.getPrefsInstance().switchBeep
        binding.switchVibration.isChecked = QRCodePreferences.getPrefsInstance().switchVibration

    }

    @SuppressLint("ResourceAsColor")
    override fun setupListener() {

        binding.settingLanguage.setOnClickListener {
            findNavController().navigate(R.id.fragment_language)
        }

        binding.settingPrivacy.setOnClickListener {
            AppOpenManager.getInstance().disableAppResume()
            dialogPrivacy = PrivacyDialog()
            dialogPrivacy?.show(parentFragmentManager, null)
        }

        binding.settingRate.setOnClickListener {
            startReviewFlow()
            showRattingDialog()
        }

        binding.switchBeep.setOnCheckedChangeListener { _, isChecked ->
            ScannerFragment.scanSound = isChecked
            QRCodePreferences.getPrefsInstance().switchBeep = isChecked
        }

        binding.switchVibration.setOnCheckedChangeListener { _, isChecked ->
            ScannerFragment.vibrator = isChecked
            QRCodePreferences.getPrefsInstance().switchVibration = isChecked
        }

        binding.txtSearchEngine.setText(QRCodePreferences.getPrefsInstance().engineName)

        binding.settingSearch.setOnClickListener {
            searchEngineDialog = SearchEngineDialog {
                QRCodePreferences.getPrefsInstance().engineName = it.name
                binding.txtSearchEngine.setText(QRCodePreferences.getPrefsInstance().engineName)

            }
            searchEngineDialog.show(parentFragmentManager, null)
        }

        binding.textVersion.text = String.format("v%s", BuildConfig.VERSION_NAME)

        binding.settingShare.setOnClickListener {
            AppOpenManager.getInstance().disableAppResume()
            val text =
                "Your friend ${getString(R.string.app_name)} wants you to check out our app!\nVisit us at: $LINK_STORE"
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_TEXT, text)
            intent.type = "text/plain"
            startActivityForResult(
                Intent.createChooser(intent, "Choose a way to share:"), requestCodeShare
            )
        }
    }


    /**
     * Call this method at app start etc to pre-cache the reviewInfo object to use to show
     * in-app review dialog later.
     */
    private fun getReviewInfo() {
        reviewManager = ReviewManagerFactory.create(requireContext().applicationContext)
        val manager = reviewManager?.requestReviewFlow()
        manager?.addOnCompleteListener { task: Task<ReviewInfo?> ->
            if (task.isSuccessful) {
                reviewInfo = task.result
            } else {
                Toast.makeText(
                    requireContext().applicationContext,
                    getString(R.string.in_app_reviewflow_fail),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    /**
     * Call this method when you want to show the in-app rating dialog
     */
    private fun startReviewFlow() {
        if (reviewInfo != null) {
            val flow = reviewManager!!.launchReviewFlow(requireActivity(), reviewInfo!!)
            flow.addOnCompleteListener {
                Toast.makeText(
                    requireContext().applicationContext,
                    getString(R.string.rating_complete),
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            Toast.makeText(requireContext().applicationContext, getString(R.string.rating_failed), Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun showRattingDialog() {
        val rateDialog = RattingDialog { isReview ->
            if (isReview) {
                goToReview()
            }
        }
        rateDialog.show(childFragmentManager, "showRatingDialog")
    }

    private fun goToReview() {
        try {
            startActivityForResult(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=${requireContext().packageName}")
                ), requestCodeRate
            )
        } catch (e: ActivityNotFoundException) {
            startActivityForResult(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=${requireContext().packageName}")
                ), requestCodeRate
            )
        }
    }
}