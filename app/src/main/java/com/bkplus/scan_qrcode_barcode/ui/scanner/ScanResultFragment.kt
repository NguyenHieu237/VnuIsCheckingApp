package com.bkplus.scan_qrcode_barcode.ui.scanner

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ads.control.admob.AppOpenManager
import com.bkplus.scan_qrcode_barcode.BuildConfig
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.base.BaseFragment
import com.bkplus.scan_qrcode_barcode.databinding.FragmentScanResultBinding
import com.bkplus.scan_qrcode_barcode.manager.database.QRCodeResult
import com.bkplus.scan_qrcode_barcode.ui.home.HomeActivity
import com.bkplus.scan_qrcode_barcode.ui.qrcode.history.QRCodeHistoryViewModel
import com.bkplus.scan_qrcode_barcode.ui.scanner.parser.Parser
import com.bkplus.scan_qrcode_barcode.ui.scanner.view.QRResultCalendarView
import com.bkplus.scan_qrcode_barcode.ui.scanner.view.QRResultContactView
import com.bkplus.scan_qrcode_barcode.ui.scanner.view.QRResultEmailView
import com.bkplus.scan_qrcode_barcode.ui.scanner.view.QRResultMycardView
import com.bkplus.scan_qrcode_barcode.ui.scanner.view.QRResultPhoneView
import com.bkplus.scan_qrcode_barcode.ui.scanner.view.QRResultSMSView
import com.bkplus.scan_qrcode_barcode.ui.scanner.view.QRResultTextView
import com.bkplus.scan_qrcode_barcode.ui.scanner.view.QRResultWebsiteView
import com.bkplus.scan_qrcode_barcode.ui.scanner.view.QRResultWifiView
import com.bkplus.scan_qrcode_barcode.ui.scanner.view.ResultScanBarcodeView
import com.bkplus.scan_qrcode_barcode.utils.checkSocialMedia
import com.bkplus.scan_qrcode_barcode.utils.checkUrlIcon
import com.bkplus.scan_qrcode_barcode.utils.extension.disposedBy
import com.bkplus.scan_qrcode_barcode.utils.extension.gone
import com.bkplus.scan_qrcode_barcode.utils.extension.guardLet
import com.bkplus.scan_qrcode_barcode.utils.extension.visible
import com.google.mlkit.vision.barcode.common.Barcode
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date


class ScanResultFragment : BaseFragment<FragmentScanResultBinding>() {
    private val args: ScanResultFragmentArgs by navArgs()
    private val viewModel by activityViewModels<ScanResultViewModel>()
    private val historyViewModel by activityViewModels<QRCodeHistoryViewModel>()
    private val params: LinearLayout.LayoutParams by lazy {
        val paramsSetting = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        paramsSetting.setMargins(0, 0, 0, 0)
        return@lazy paramsSetting
    }

    private val qrResultCalendarView: QRResultCalendarView by lazy {
        QRResultCalendarView(context = requireContext())
    }

    private val qrResultMycardView: QRResultMycardView by lazy {
        QRResultMycardView(context = requireContext())
    }

    private val qrResultContactView: QRResultContactView by lazy {
        QRResultContactView(context = requireContext())
    }

    private val qrResultTextView: QRResultTextView by lazy {
        QRResultTextView(context = requireContext())
    }

    private val qrResultEmailView: QRResultEmailView by lazy {
        QRResultEmailView(context = requireContext())
    }

    private val qrResultPhoneView: QRResultPhoneView by lazy {
        QRResultPhoneView(context = requireContext())
    }

    private val qrResultSMSView: QRResultSMSView by lazy {
        QRResultSMSView(context = requireContext())
    }

    private val qrResultWifiView: QRResultWifiView by lazy {
        QRResultWifiView(context = requireContext())
    }

    private val qrResultWebsiteView: QRResultWebsiteView by lazy {
        QRResultWebsiteView(context = requireContext())
    }

    companion object {
        var isCreate: Boolean = false
    }

    override val layoutId: Int
        get() = R.layout.fragment_scan_result

    @SuppressLint("SetTextI18n")
    override fun setupUI() {
        viewModel.setResult(args.result)
        viewModel.getQRCodeBitmap()?.let {
            binding.qrcodeImage.setImageBitmap(it)
        }
        if (args.result?.resultType == QRCodeResult.SCAN.type) {
            isCreate = false
            binding.download.visibility = View.GONE
        } else {
            isCreate = true
            binding.download.visibility = View.VISIBLE
        }
        binding.txtDate.text = SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(Date())
        when (viewModel.barcode?.format) {
            Barcode.FORMAT_QR_CODE -> {
                when (viewModel.barcode?.valueType) {
                    Barcode.TYPE_URL -> {
                        binding.icon.setImageDrawable(
                            checkUrlIcon(
                                requireContext(),
                                viewModel.barcode?.rawValue
                            )
                        )
                        binding.txtName.text = checkSocialMedia(viewModel.barcode?.rawValue)
                        qrResultWebsiteView.setBarcode(viewModel.barcode!!)
                        qrResultWebsiteView.layoutParams = params
                        addView(qrResultWebsiteView)
                    }

                    Barcode.TYPE_CALENDAR_EVENT -> {
                        binding.icon.setImageDrawable(
                            resources.getDrawable(
                                R.drawable.ic_calendar,
                                null
                            )
                        )
                        binding.txtName.text = getString(R.string.calendar)
                        val calendar = viewModel.barcode?.calendarEvent
                        qrResultCalendarView.setContent(
                            calendar?.summary,
                            calendar?.start,
                            calendar?.end,
                            calendar?.description
                        )
                        qrResultCalendarView.layoutParams = params
                        addView(qrResultCalendarView)
                    }

                    Barcode.TYPE_CONTACT_INFO -> {
                        val raw = viewModel.barcode?.rawValue
                        if (raw != null)
                            if (raw.startsWith("BEGIN:")) {
                                binding.icon.setImageDrawable(
                                    resources.getDrawable(
                                        R.drawable.ic_mycard,
                                        null
                                    )
                                )
                                binding.txtName.text = getString(R.string.my_card)
                                val card = Parser.parseVcard(raw)
                                qrResultMycardView.setContent(
                                    card.name,
                                    card.phoneNumber,
                                    card.email,
                                    card.address,
                                    card.company,
                                    card.birthday,
                                )
                                qrResultMycardView.layoutParams = params
                                addView(qrResultMycardView)
                            } else {
                                binding.icon.setImageDrawable(
                                    resources.getDrawable(
                                        R.drawable.ic_contacts,
                                        null
                                    )
                                )
                                binding.txtName.text = getString(R.string.contacts)
                                val contact = Parser.parseMeCard(raw)
                                qrResultContactView.setContent(
                                    contact.name,
                                    contact.telephone,
                                    contact.email
                                )
                                qrResultContactView.layoutParams = params
                                addView(qrResultContactView)
                            }
                    }

                    Barcode.TYPE_SMS -> {
                        binding.icon.setImageDrawable(
                            resources.getDrawable(
                                R.drawable.ic_sms,
                                null
                            )
                        )
                        binding.txtName.text = getString(R.string.sms)
                        val sms = viewModel.barcode?.sms
                        qrResultSMSView.setContent(sms?.phoneNumber, sms?.message)
                        qrResultSMSView.layoutParams = params
                        addView(qrResultSMSView)
                    }

                    Barcode.TYPE_WIFI -> {
                        binding.icon.setImageDrawable(
                            resources.getDrawable(
                                R.drawable.ic_wifi,
                                null
                            )
                        )
                        binding.txtName.text = getString(R.string.wifi)
                        val wifi = viewModel.barcode?.wifi
                        var encryptionType = ""
                        when (wifi?.encryptionType) {
                            1 -> encryptionType = "No encryption"
                            2 -> encryptionType = "WPA/WPA2"
                            3 -> encryptionType = "WEP"
                        }
                        qrResultWifiView.setContent(wifi?.ssid, encryptionType, wifi?.password)
                        qrResultWifiView.layoutParams = params
                        addView(qrResultWifiView)
                    }

                    Barcode.TYPE_PHONE -> {
                        binding.icon.setImageDrawable(
                            resources.getDrawable(
                                R.drawable.ic_phone,
                                null
                            )
                        )
                        binding.txtName.text = getString(R.string.phone)
                        val phone = viewModel.barcode?.phone
                        qrResultPhoneView.setContent(phone?.number)
                        qrResultPhoneView.layoutParams = params
                        addView(qrResultPhoneView)
                    }

                    Barcode.TYPE_TEXT -> {
                        binding.icon.setImageDrawable(
                            resources.getDrawable(
                                R.drawable.ic_text,
                                null
                            )
                        )
                        binding.txtName.text = getString(R.string.text)
                        val text = viewModel.barcode?.rawValue
                        qrResultTextView.setContent(text)
                        qrResultTextView.layoutParams = params
                        addView(qrResultTextView)
                    }

                    Barcode.TYPE_EMAIL -> {
                        binding.icon.setImageDrawable(
                            resources.getDrawable(
                                R.drawable.ic_email,
                                null
                            )
                        )
                        binding.txtName.text = getString(R.string.email)
                        val email = viewModel.barcode?.email
                        qrResultEmailView.setContent(email?.address)
                        qrResultEmailView.layoutParams = params
                        addView(qrResultEmailView)
                    }
                }
            }

            Barcode.FORMAT_EAN_8-> {
                binding.icon.setImageDrawable(
                    resources.getDrawable(R.drawable.ic_barcode_green, null)
                )
                binding.txtName.text = getString(R.string.ean8)
                val view = ResultScanBarcodeView(context)
                view.setBarcode(viewModel.barcode!!)
                view.layoutParams = params
                addView(view)
            }
            Barcode.FORMAT_EAN_13 -> {
            binding.icon.setImageDrawable(
                resources.getDrawable(R.drawable.ic_data_matrix, null)

            )
            binding.txtName.text = getString(R.string.ean13)
            val view = ResultScanBarcodeView(context)
            view.setBarcode(viewModel.barcode!!)
            view.layoutParams = params
            addView(view)
        }
            Barcode.FORMAT_CODE_39 -> {
                binding.icon.setImageDrawable(
                    resources.getDrawable(R.drawable.ic_data_matrix, null)

                )
                binding.txtName.text = getString(R.string.code39)
                val view = ResultScanBarcodeView(context)
                view.setBarcode(viewModel.barcode!!)
                view.layoutParams = params
                addView(view)
            }
            Barcode.FORMAT_CODE_93 -> {
                binding.icon.setImageDrawable(
                    resources.getDrawable(R.drawable.ic_data_matrix, null)

                )
                binding.txtName.text = getString(R.string.code93)
                val view = ResultScanBarcodeView(context)
                view.setBarcode(viewModel.barcode!!)
                view.layoutParams = params
                addView(view)
            }
            Barcode.FORMAT_CODE_128 -> {
                binding.icon.setImageDrawable(
                    resources.getDrawable(R.drawable.ic_data_matrix, null)

                )
                binding.txtName.text = getString(R.string.code128)
                val view = ResultScanBarcodeView(context)
                view.setBarcode(viewModel.barcode!!)
                view.layoutParams = params
                addView(view)
            }
            Barcode.FORMAT_UPC_E -> {
                binding.icon.setImageDrawable(
                    resources.getDrawable(R.drawable.ic_data_matrix, null)

                )
                binding.txtName.text = getString(R.string.upce)
                val view = ResultScanBarcodeView(context)
                view.setBarcode(viewModel.barcode!!)
                view.layoutParams = params
                addView(view)
            }
            Barcode.FORMAT_UPC_A -> {
                binding.icon.setImageDrawable(
                    resources.getDrawable(R.drawable.ic_data_matrix, null)

                )
                binding.txtName.text = getString(R.string.upca)
                val view = ResultScanBarcodeView(context)
                view.setBarcode(viewModel.barcode!!)
                view.layoutParams = params
                addView(view)
            }
            Barcode.FORMAT_CODABAR -> {
                binding.icon.setImageDrawable(
                    resources.getDrawable(R.drawable.ic_data_matrix, null)

                )
                binding.txtName.text = getString(R.string.codabar)
                val view = ResultScanBarcodeView(context)
                view.setBarcode(viewModel.barcode!!)
                view.layoutParams = params
                addView(view)
            }
            Barcode.FORMAT_ITF -> {
                binding.icon.setImageDrawable(
                    resources.getDrawable(R.drawable.ic_data_matrix, null)

                )
                binding.txtName.text = getString(R.string.itf)
                val view = ResultScanBarcodeView(context)
                view.setBarcode(viewModel.barcode!!)
                view.layoutParams = params
                addView(view)
            }


            Barcode.FORMAT_DATA_MATRIX -> {
                binding.icon.setImageDrawable(
                    resources.getDrawable(R.drawable.ic_data_matrix, null)

                )
                binding.txtName.text = getString(R.string.barcode_data_matrix)
                val view = ResultScanBarcodeView(context)
                view.setBarcode(viewModel.barcode!!)
                view.layoutParams = params
                addView(view)
            }

            Barcode.FORMAT_PDF417 -> {
                binding.icon.setImageDrawable(
                    resources.getDrawable(R.drawable.ic_pdf_417, null)

                )
                binding.txtName.text = getString(R.string.barcode_pdf_147)
                val view = ResultScanBarcodeView(context)
                view.setBarcode(viewModel.barcode!!)
                view.layoutParams = params
                addView(view)
            }

            Barcode.FORMAT_AZTEC -> {
                binding.icon.setImageDrawable(
                    resources.getDrawable(R.drawable.ic_aztec, null)

                )
                binding.txtName.text = getString(R.string.barcode_aztec)
                val view = ResultScanBarcodeView(context)
                view.setBarcode(viewModel.barcode!!)
                view.layoutParams = params
                addView(view)
            }
        }
        setupUIToolbar()
    }

    override fun setupListener() {
        binding.toolbar.onTapBackListener = {
            findNavController().popBackStack()
        }

        binding.toolbar.onTapIconRight1Listener = {
            handleActionShare()
        }

        binding.toolbar.onTapIconRight2Listener = {
            handleActionCopy()
        }
        binding.download.setOnClickListener {
            imageSavingLauncher()
        }
    }

    override fun setupViewModel() {
        viewModel.saveDataQRCode(context = requireContext())
        historyViewModel.startFetchData()
    }

    private fun setupUIToolbar() {
        if (viewModel.barcode?.format == Barcode.FORMAT_QR_CODE) {
            binding.toolbar.setTitle(context?.getString(R.string.qr_code))
        } else {
            binding.toolbar.setTitle(context?.getString(R.string.barcode))
        }

        binding.toolbar.setIconRight1(R.drawable.ic_share)
    }

    private fun handleActionShare() {
        val file = viewModel.getFileToShare(requireContext()).guardLet { return }!!
//		val file = File(path)
        if (!file.exists()) {
            return
        }
        val uri = FileProvider.getUriForFile(
            requireContext(),
            BuildConfig.APPLICATION_ID + ".provider",
            file
        )
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "image/*"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        val shareIntent = Intent.createChooser(intent, "Share")
        AppOpenManager.getInstance().disableAppResume()
        startActivity(shareIntent)
    }

    override fun onResume() {
        AppOpenManager.getInstance().enableAppResume()
        super.onResume()
    }

    private fun handleActionCopy() {
        val clipboard =
            (activity as HomeActivity).getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("barcode", viewModel.getContent())
        clipboard.setPrimaryClip(clipData)
        Toast.makeText(this.context, getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT)
            .show()
    }

    private fun addView(layout: LinearLayout) {
        binding.lnContainer.removeAllViews()
        val parent = layout.parent as ViewGroup?
        parent?.removeAllViews()
        binding.lnContainer.addView(layout)
    }

    private var imageSavingCallback =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data = result.data
            try {
                val os: OutputStream? =
                    data?.data?.let { context?.contentResolver?.openOutputStream(it) }
                if (os != null) {
                    val stream = ByteArrayOutputStream()
                    viewModel.getQRCodeBitmap()?.compress(Bitmap.CompressFormat.PNG, 90, stream)
                    val image = stream.toByteArray()
                    os.write(image)
                    os.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

    private fun imageSavingLauncher() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/jpeg"
            putExtra(Intent.EXTRA_TITLE, "image.jpeg")
        }
        imageSavingCallback.launch(intent)
    }
}