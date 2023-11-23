package com.bkplus.scan_qrcode_barcode.ui.qrcode.create_qrcode

import android.content.ClipboardManager
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.base.BaseFragment
import com.bkplus.scan_qrcode_barcode.databinding.FragmentCreateQrcodeBinding
import com.bkplus.scan_qrcode_barcode.manager.qrcode.GenerateQRCodeResult
import com.bkplus.scan_qrcode_barcode.model.QRCodeTypeEnum
import com.bkplus.scan_qrcode_barcode.ui.qrcode.view.QRCodeBar128
import com.bkplus.scan_qrcode_barcode.ui.qrcode.view.QRCodeBar39
import com.bkplus.scan_qrcode_barcode.ui.qrcode.view.QRCodeBar93
import com.bkplus.scan_qrcode_barcode.ui.qrcode.view.QRCodeBarAZTEC
import com.bkplus.scan_qrcode_barcode.ui.qrcode.view.QRCodeBarCODABAR
import com.bkplus.scan_qrcode_barcode.ui.qrcode.view.QRCodeBarDataMatric
import com.bkplus.scan_qrcode_barcode.ui.qrcode.view.QRCodeBarEAN13
import com.bkplus.scan_qrcode_barcode.ui.qrcode.view.QRCodeBarEAN8
import com.bkplus.scan_qrcode_barcode.ui.qrcode.view.QRCodeBarITF
import com.bkplus.scan_qrcode_barcode.ui.qrcode.view.QRCodeBarPDF417
import com.bkplus.scan_qrcode_barcode.ui.qrcode.view.QRCodeBarUCPA
import com.bkplus.scan_qrcode_barcode.ui.qrcode.view.QRCodeBarUCPE
import com.bkplus.scan_qrcode_barcode.ui.qrcode.view.QRCodeCalendarView
import com.bkplus.scan_qrcode_barcode.ui.qrcode.view.QRCodeCardView
import com.bkplus.scan_qrcode_barcode.ui.qrcode.view.QRCodeContactView
import com.bkplus.scan_qrcode_barcode.ui.qrcode.view.QRCodeEmailView
import com.bkplus.scan_qrcode_barcode.ui.qrcode.view.QRCodePhoneView
import com.bkplus.scan_qrcode_barcode.ui.qrcode.view.QRCodeSMSView
import com.bkplus.scan_qrcode_barcode.ui.qrcode.view.QRCodeSocialView
import com.bkplus.scan_qrcode_barcode.ui.qrcode.view.QRCodeTextView
import com.bkplus.scan_qrcode_barcode.ui.qrcode.view.QRCodeWebsiteView
import com.bkplus.scan_qrcode_barcode.ui.qrcode.view.QRCodeWifiView
import com.bkplus.scan_qrcode_barcode.ui.scanner.ScanResultViewModel
import com.bkplus.scan_qrcode_barcode.utils.extension.click
import com.bkplus.scan_qrcode_barcode.utils.extension.disposedBy
import com.bkplus.scan_qrcode_barcode.utils.extension.ignoreFastTap
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

class CreateQRCodeFragment : BaseFragment<FragmentCreateQrcodeBinding>() {
    private val params: LinearLayout.LayoutParams by lazy {
        val paramsSetting = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        paramsSetting.setMargins(0, 0, 0, 0)
        return@lazy paramsSetting
    }

    private val qrCodeCalendarView: QRCodeCalendarView by lazy {
        QRCodeCalendarView(context = requireContext())
    }

    private val qrCodeWebsiteView: QRCodeWebsiteView by lazy {
        QRCodeWebsiteView(context = requireContext())
    }

    private val qrCodeWifiView: QRCodeWifiView by lazy {
        QRCodeWifiView(context = requireContext())
    }

    private val qrCodeCardView: QRCodeCardView by lazy {
        QRCodeCardView(context = requireContext())
    }

    private val qrCodeContactView: QRCodeContactView by lazy {
        QRCodeContactView(context = requireContext())
    }

    private val qrCodeTextView: QRCodeTextView by lazy {
        QRCodeTextView(context = requireContext())
    }

    private val qrCodeEmailView: QRCodeEmailView by lazy {
        QRCodeEmailView(context = requireContext())
    }

    private val qrCodePhoneView: QRCodePhoneView by lazy {
        QRCodePhoneView(context = requireContext())
    }

    private val qrCodeSMSView: QRCodeSMSView by lazy {
        QRCodeSMSView(context = requireContext())
    }

    private val qrCodeSocialView: QRCodeSocialView by lazy {
        QRCodeSocialView(context = requireContext())
    }
    private val qrCodeBarEAN8View: QRCodeBarEAN8 by lazy {
        QRCodeBarEAN8(context = requireContext())
    }
    private val qrCodeBarPDF417View: QRCodeBarPDF417 by lazy {
        QRCodeBarPDF417(context = requireContext())
    }
    private val qrCodeBarDataMatricView: QRCodeBarDataMatric by lazy {
        QRCodeBarDataMatric(context = requireContext())
    }
    private val qrCodeBarAZTECView: QRCodeBarAZTEC by lazy {
        QRCodeBarAZTEC(context = requireContext())
    }
    private val qrCodeBar39view: QRCodeBar39 by lazy {
        QRCodeBar39(context = requireContext())
    }
    private val qrCodeBar93view: QRCodeBar93 by lazy {
        QRCodeBar93(context = requireContext())
    }
    private val qrCodeBar128view: QRCodeBar128 by lazy {
        QRCodeBar128(context = requireContext())
    }
    private val qrCodeBarCodaBarview: QRCodeBarCODABAR by lazy {
        QRCodeBarCODABAR(context = requireContext())
    }
    private val qrCodeBarEAN13view: QRCodeBarEAN13 by lazy {
        QRCodeBarEAN13(context = requireContext())
    }
    private val qrCodeBarITFview: QRCodeBarITF by lazy {
        QRCodeBarITF(context = requireContext())
    }
    private val qrCodeBarUPCAview: QRCodeBarUCPA by lazy {
        QRCodeBarUCPA(context = requireContext())
    }
    private val qrCodeBarUPCEview: QRCodeBarUCPE by lazy {
        QRCodeBarUCPE(context = requireContext())
    }


    private val args: CreateQRCodeFragmentArgs by navArgs()
    private val viewModel by viewModels<CreateQRCodeViewModel>()
    private val resultViewModel by activityViewModels<ScanResultViewModel>()

    override fun onDestroyView() {
        binding.lnContainer.removeAllViews()
        super.onDestroyView()
    }

    override val layoutId: Int
        get() = R.layout.fragment_create_qrcode

    override fun setupData() {
        viewModel.setItem(item = args.generatorItem)
    }

    override fun setupUI() {
        super.setupUI()
        viewModel.getGeneratorItem()?.let {
            val qrCodeTypeEnum = QRCodeTypeEnum.instance(it.type)

            /// Bind title
            binding.toolbar.setTitle(title = context?.getString(it.stringResourceId))

            /// Bind content
            createContentQRCode(type = qrCodeTypeEnum)
        }
    }

    override fun setupListener() {
        binding.btnCreateQRCode.click()
            .ignoreFastTap()
            .map { viewModel.getGeneratorItem()?.type }
            .filter { it != null }
            .map { QRCodeTypeEnum.instance(it) }
            .subscribe {
                generateQRCode(type = it)
            }
            .disposedBy(bag = bag)

        binding.toolbar.onTapBackListener = {
            findNavController().popBackStack()
        }
    }

    private fun createContentQRCode(type: QRCodeTypeEnum) {
        when (type) {
            QRCodeTypeEnum.CLIPBOARD -> {
                qrCodeTextView.layoutParams = params
                qrCodeTextView.onActivatedInput = activateCreateButton
                qrCodeTextView.onEmptyInput = deActivateCreateButton
                qrCodeTextView.setText(fromClipboard())
                qrCodeTextView.onGenerateQRCodeCompletion = { result ->
                    makeUIScanResult(
                        result = result
                    )
                }
                binding.lnContainer.addView(qrCodeTextView)
            }

            QRCodeTypeEnum.CALENDER -> {
                qrCodeCalendarView.layoutParams = params
                qrCodeCalendarView.onActivatedInput = activateCreateButton
                qrCodeCalendarView.onEmptyInput = deActivateCreateButton
                qrCodeCalendarView.onGenerateQRCodeCompletion = { result ->
                    makeUIScanResult(
                        result = result
                    )
                }
                binding.lnContainer.addView(qrCodeCalendarView)
            }

            QRCodeTypeEnum.WEBSITE -> {
                qrCodeWebsiteView.layoutParams = params
                qrCodeWebsiteView.onActivatedInput = activateCreateButton
                qrCodeWebsiteView.onEmptyInput = deActivateCreateButton
                qrCodeWebsiteView.onGenerateQRCodeCompletion = { result ->
                    makeUIScanResult(
                        result = result
                    )
                }
                binding.lnContainer.addView(qrCodeWebsiteView)
            }

            QRCodeTypeEnum.WIFI -> {
                qrCodeWifiView.layoutParams = params
                qrCodeWifiView.onActivatedInput = activateCreateButton
                qrCodeWifiView.onEmptyInput = deActivateCreateButton
                qrCodeWifiView.onGenerateQRCodeCompletion = { result ->
                    makeUIScanResult(
                        result = result
                    )
                }
                binding.lnContainer.addView(qrCodeWifiView)
            }

            QRCodeTypeEnum.CARD -> {
                qrCodeCardView.layoutParams = params
                qrCodeCardView.onActivatedInput = activateCreateButton
                qrCodeCardView.onEmptyInput = deActivateCreateButton
                qrCodeCardView.onGenerateQRCodeCompletion = { result ->
                    makeUIScanResult(
                        result = result
                    )
                }
                binding.lnContainer.addView(qrCodeCardView)
            }

            QRCodeTypeEnum.CONTACT -> {
                qrCodeContactView.layoutParams = params
                qrCodeContactView.onActivatedInput = activateCreateButton
                qrCodeContactView.onEmptyInput = deActivateCreateButton
                qrCodeContactView.onGenerateQRCodeCompletion = { result ->
                    makeUIScanResult(
                        result = result
                    )
                }
                binding.lnContainer.addView(qrCodeContactView)
            }

            QRCodeTypeEnum.TEXT -> {
                qrCodeTextView.layoutParams = params
                qrCodeTextView.onActivatedInput = activateCreateButton
                qrCodeTextView.onEmptyInput = deActivateCreateButton
                qrCodeTextView.onGenerateQRCodeCompletion = { result ->
                    makeUIScanResult(
                        result = result
                    )
                }
                binding.lnContainer.addView(qrCodeTextView)
            }

            QRCodeTypeEnum.EMAIL -> {
                qrCodeEmailView.layoutParams = params
                qrCodeEmailView.onActivatedInput = activateCreateButton
                qrCodeEmailView.onEmptyInput = deActivateCreateButton
                qrCodeEmailView.onGenerateQRCodeCompletion = { result ->
                    makeUIScanResult(
                        result = result
                    )
                }
                binding.lnContainer.addView(qrCodeEmailView)
            }

            QRCodeTypeEnum.PHONE -> {
                qrCodePhoneView.layoutParams = params
                qrCodePhoneView.onActivatedInput = activateCreateButton
                qrCodePhoneView.onEmptyInput = deActivateCreateButton
                qrCodePhoneView.onGenerateQRCodeCompletion = { result ->
                    makeUIScanResult(
                        result = result
                    )
                }
                binding.lnContainer.addView(qrCodePhoneView)
            }

            QRCodeTypeEnum.SMS -> {
                qrCodeSMSView.layoutParams = params
                qrCodeSMSView.onActivatedInput = activateCreateButton
                qrCodeSMSView.onEmptyInput = deActivateCreateButton
                qrCodeSMSView.onGenerateQRCodeCompletion = { result ->
                    makeUIScanResult(
                        result = result
                    )
                }
                binding.lnContainer.addView(qrCodeSMSView)
            }

            QRCodeTypeEnum.SOCIAL_MEDIA -> {
                qrCodeSocialView.layoutParams = params
                qrCodeSocialView.onActivatedInput = activateCreateButton
                qrCodeSocialView.onEmptyInput = deActivateCreateButton
                qrCodeSocialView.onGenerateQRCodeCompletion = { result ->
                    makeUIScanResult(
                        result = result
                    )
                }
                binding.lnContainer.addView(qrCodeSocialView)
            }

            QRCodeTypeEnum.EAN_8 -> {
                qrCodeBarEAN8View.layoutParams = params
                qrCodeBarEAN8View.onActivatedInput = activateCreateButton
                qrCodeBarEAN8View.onEmptyInput = deActivateCreateButton
                qrCodeBarEAN8View.onGenerateQRCodeCompletion = { result ->
                    makeUIScanResult(
                        result = result
                    )
                }
                binding.lnContainer.addView(qrCodeBarEAN8View)
            }

            QRCodeTypeEnum.EAN_13 -> {
                qrCodeBarEAN13view.layoutParams = params
                qrCodeBarEAN13view.onActivatedInput = activateCreateButton
                qrCodeBarEAN13view.onEmptyInput = deActivateCreateButton
                qrCodeBarEAN13view.onGenerateQRCodeCompletion = { result ->
                    makeUIScanResult(
                        result = result
                    )
                }
                binding.lnContainer.addView(qrCodeBarEAN13view)
            }

            QRCodeTypeEnum.CODE_39 -> {
                qrCodeBar39view.layoutParams = params
                qrCodeBar39view.onActivatedInput = activateCreateButton
                qrCodeBar39view.onEmptyInput = deActivateCreateButton
                qrCodeBar39view.onGenerateQRCodeCompletion = { result ->
                    makeUIScanResult(
                        result = result
                    )
                }
                binding.lnContainer.addView(qrCodeBar39view)
            }

            QRCodeTypeEnum.CODE_93 -> {
                qrCodeBar93view.layoutParams = params
                qrCodeBar93view.onActivatedInput = activateCreateButton
                qrCodeBar93view.onEmptyInput = deActivateCreateButton
                qrCodeBar93view.onGenerateQRCodeCompletion = { result ->
                    makeUIScanResult(
                        result = result
                    )
                }
                binding.lnContainer.addView(qrCodeBar93view)
            }

            QRCodeTypeEnum.CODE_128 -> {
                qrCodeBar128view.layoutParams = params
                qrCodeBar128view.onActivatedInput = activateCreateButton
                qrCodeBar128view.onEmptyInput = deActivateCreateButton
                qrCodeBar128view.onGenerateQRCodeCompletion = { result ->
                    makeUIScanResult(
                        result = result
                    )
                }
                binding.lnContainer.addView(qrCodeBar128view)
            }

            QRCodeTypeEnum.CODABAR -> {
                qrCodeBarCodaBarview.layoutParams = params
                qrCodeBarCodaBarview.onActivatedInput = activateCreateButton
                qrCodeBarCodaBarview.onEmptyInput = deActivateCreateButton
                qrCodeBarCodaBarview.onGenerateQRCodeCompletion = { result ->
                    makeUIScanResult(
                        result = result
                    )
                }
                binding.lnContainer.addView(qrCodeBarCodaBarview)
            }

            QRCodeTypeEnum.ITF -> {
                qrCodeBarITFview.layoutParams = params
                qrCodeBarITFview.onActivatedInput = activateCreateButton
                qrCodeBarITFview.onEmptyInput = deActivateCreateButton
                qrCodeBarITFview.onGenerateQRCodeCompletion = { result ->
                    makeUIScanResult(
                        result = result
                    )
                }
                binding.lnContainer.addView(qrCodeBarITFview)
            }

            QRCodeTypeEnum.UPC_A -> {
                qrCodeBarUPCAview.layoutParams = params
                qrCodeBarUPCAview.onActivatedInput = activateCreateButton
                qrCodeBarUPCAview.onEmptyInput = deActivateCreateButton
                qrCodeBarUPCAview.onGenerateQRCodeCompletion = { result ->
                    makeUIScanResult(
                        result = result
                    )
                }
                binding.lnContainer.addView(qrCodeBarUPCAview)
            }

            QRCodeTypeEnum.UPC_E -> {
                qrCodeBarUPCEview.layoutParams = params
                qrCodeBarUPCEview.onActivatedInput = activateCreateButton
                qrCodeBarUPCEview.onEmptyInput = deActivateCreateButton
                qrCodeBarUPCEview.onGenerateQRCodeCompletion = { result ->
                    makeUIScanResult(
                        result = result
                    )
                }
                binding.lnContainer.addView(qrCodeBarUPCEview)
            }


            QRCodeTypeEnum.PDF_417 -> {
                qrCodeBarPDF417View.layoutParams = params
                qrCodeBarPDF417View.onActivatedInput = activateCreateButton
                qrCodeBarPDF417View.onEmptyInput = deActivateCreateButton
                qrCodeBarPDF417View.onGenerateQRCodeCompletion = { result ->
                    makeUIScanResult(
                        result = result
                    )
                }
                binding.lnContainer.addView(qrCodeBarPDF417View)
            }

            QRCodeTypeEnum.DATA_MATRIX -> {
                qrCodeBarDataMatricView.layoutParams = params
                qrCodeBarDataMatricView.onActivatedInput = activateCreateButton
                qrCodeBarDataMatricView.onEmptyInput = deActivateCreateButton
                qrCodeBarDataMatricView.onGenerateQRCodeCompletion = { result ->
                    makeUIScanResult(
                        result = result
                    )
                }
                binding.lnContainer.addView(qrCodeBarDataMatricView)
            }

            QRCodeTypeEnum.AZTEC -> {
                qrCodeBarAZTECView.layoutParams = params
                qrCodeBarAZTECView.onActivatedInput = activateCreateButton
                qrCodeBarAZTECView.onEmptyInput = deActivateCreateButton
                qrCodeBarAZTECView.onGenerateQRCodeCompletion = { result ->
                    makeUIScanResult(
                        result = result
                    )
                }
                binding.lnContainer.addView(qrCodeBarAZTECView)
            }

            else -> {}
        }
    }

    // TODO Generate QR Code
    private fun generateQRCode(type: QRCodeTypeEnum) {
        when (type) {
            QRCodeTypeEnum.CLIPBOARD -> {
                qrCodeTextView.generateQRCode()
            }

            QRCodeTypeEnum.CALENDER -> {
                qrCodeCalendarView.generateQRCode()
            }

            QRCodeTypeEnum.WEBSITE -> {
                qrCodeWebsiteView.generateQRCode()
            }

            QRCodeTypeEnum.WIFI -> {
                qrCodeWifiView.generateQRCode()
            }

            QRCodeTypeEnum.CARD -> {
                qrCodeCardView.generateQRCode()
            }

            QRCodeTypeEnum.CONTACT -> {
                qrCodeContactView.generateQRCode()
            }

            QRCodeTypeEnum.TEXT -> {
                qrCodeTextView.generateQRCode()
            }

            QRCodeTypeEnum.EMAIL -> {
                qrCodeEmailView.generateQRCode()
            }

            QRCodeTypeEnum.PHONE -> {
                qrCodePhoneView.generateQRCode()
            }

            QRCodeTypeEnum.SMS -> {
                qrCodeSMSView.generateQRCode()
            }

            QRCodeTypeEnum.SOCIAL_MEDIA -> {
                qrCodeSocialView.generateQRCode()
            }

            QRCodeTypeEnum.EAN_8 -> {
                qrCodeBarEAN8View.generateQRCode()
            }

            QRCodeTypeEnum.PDF_417 -> {
                qrCodeBarPDF417View.generateQRCode()
            }

            QRCodeTypeEnum.DATA_MATRIX -> {
                qrCodeBarDataMatricView.generateQRCode()
            }

            QRCodeTypeEnum.AZTEC -> {
                qrCodeBarAZTECView.generateQRCode()
            }

            QRCodeTypeEnum.CODE_39 -> {
                qrCodeBar39view.generateQRCode()
            }

            QRCodeTypeEnum.CODE_93 -> {
                qrCodeBar93view.generateQRCode()
            }

            QRCodeTypeEnum.CODE_128 -> {
                qrCodeBar128view.generateQRCode()
            }

            QRCodeTypeEnum.CODABAR -> {
                qrCodeBarCodaBarview.generateQRCode()
            }

            QRCodeTypeEnum.EAN_13 -> {
                qrCodeBarEAN13view.generateQRCode()
            }

            QRCodeTypeEnum.ITF -> {
                qrCodeBarITFview.generateQRCode()
            }

            QRCodeTypeEnum.UPC_A -> {
                qrCodeBarUPCAview.generateQRCode()
            }

            QRCodeTypeEnum.UPC_E -> {
                qrCodeBarUPCEview.generateQRCode()
            }

            else -> {

            }
        }
    }

    private fun makeUIScanResult(result: GenerateQRCodeResult) {
        val bitmap = result.qrCodeBitmap
        val barcodeOption = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
            .build()
        val barcodeScanner = BarcodeScanning.getClient(barcodeOption)

        barcodeScanner.process(InputImage.fromBitmap(bitmap, 0))
            .addOnSuccessListener {
                if (it.isNotEmpty()) {
                    resultViewModel.barcode = it[0]
                    val direction =
                        CreateQRCodeFragmentDirections.actionGoToScanResultFragment(result)
                    findNavController().navigate(direction)
                }
            }

    }

    private fun fromClipboard(): String {
        val clipboard =
            ContextCompat.getSystemService(requireContext(), ClipboardManager::class.java)
        return clipboard?.primaryClip?.getItemAt(0)?.text.toString()
    }

    private val activateCreateButton = {
        binding.btnCreateQRCode.visibility = View.VISIBLE
        binding.btnCreateQRCodeFalse.visibility = View.GONE
    }

    private val deActivateCreateButton = {
        binding.btnCreateQRCode.visibility = View.GONE
        binding.btnCreateQRCodeFalse.visibility = View.VISIBLE
    }
}