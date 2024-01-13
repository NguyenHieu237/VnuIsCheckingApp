package com.bkplus.scan_qrcode_barcode.ui.qrcode.create_event

import android.app.DatePickerDialog
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.applovin.impl.sdk.c.b.ev
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.base.BaseFragment
import com.bkplus.scan_qrcode_barcode.databinding.FragmentCreateEventBinding
import com.bkplus.scan_qrcode_barcode.manager.qrcode.GenerateQRCodeResult
import com.bkplus.scan_qrcode_barcode.manager.qrcode.QRCodeManager
import com.bkplus.scan_qrcode_barcode.ui.Login.LoginViewModel
import com.bkplus.scan_qrcode_barcode.ui.qrcode.create_qrcode.CreateQRCodeFragmentDirections
import com.bkplus.scan_qrcode_barcode.ui.qrcode.widget.TimeUtils
import com.bkplus.scan_qrcode_barcode.ui.scanner.ScanResultViewModel
import com.bkplus.scan_qrcode_barcode.ui.scanner.ScannerFragment
import com.bkplus.scan_qrcode_barcode.utils.extension.guardLet
import com.facebook.appevents.codeless.internal.ViewHierarchy.setOnClickListener
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.Calendar

class CreateEventFragment: BaseFragment<FragmentCreateEventBinding>() {

    private val viewModel by activityViewModels<CreateEventViewModel>()
    private val resultViewModel by activityViewModels<ScanResultViewModel>()
    override val layoutId: Int
        get() = R.layout.fragment_create_event

    companion object {
        fun newInstance(): CreateEventFragment {
            val args = Bundle()
            val fragment = CreateEventFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var _dateOfEvent: Calendar? = null
    override fun setupUI() {
        super.setupUI()
        binding.eventTitle.setHint(this.getString(R.string.title))
        binding.eventCreate.setHint(this.getString(R.string.creator))
        binding.eventDate.setTitle("Date")
        binding.eventAddress.setHint("Address")
    }

    override fun setupListener() {
        super.setupListener()
        binding.btnCreateQRCode.setOnClickListener {
            createEvent()
        }
        binding.eventDate.setOnClickListener {
            openDatePicker()
        }
    }

    fun createEvent() {
        if( binding.eventAddress.getContent().isEmpty()){
            Toast.makeText(this.context,getString(R.string.qr_code_error_name_contact), Toast.LENGTH_SHORT).show()
            return
        }
       if( binding.eventTitle.getContent().isEmpty()){
           Toast.makeText(this.context,getString(R.string.qr_code_error_name_contact), Toast.LENGTH_SHORT).show()
           return
       }
        if( binding.eventCreate.getContent().isEmpty()){
            Toast.makeText(this.context,getString(R.string.qr_code_error_name_contact), Toast.LENGTH_SHORT).show()
            return
        }
        val date = _dateOfEvent.guardLet {
            Toast.makeText(
                context,
                requireContext().getString(R.string.qr_code_error_date_of_birth),
                Toast.LENGTH_SHORT
            ).show()
            return
        }!!
        val title = binding.eventTitle.getContent()
        val dateTime = TimeUtils.getTime(TimeUtils.TimeFormat.TimeFormat3, date.time)
        val creator =  binding.eventCreate.getContent()
        val address = binding.eventAddress.getContent()

        viewModel.createEvent(title,creator,dateTime, address)

        viewModel.addEventResponse.observe(viewLifecycleOwner){ addEventResponse ->
            // Handle the data in the response
            val eventId = addEventResponse?.data?.id
            val eventName = addEventResponse?.data?.name
            val eventDate = addEventResponse?.data?.date
            val eventOrganizer = addEventResponse?.data?.organizer

           var result = QRCodeManager.instance().generateQRCodeText(eventId.toString())
            makeUIScanResult(result)
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

    private fun openDatePicker() {
        val returnCalendar = Calendar.getInstance()
        val datePickerDialog = context?.let { DatePickerDialog(it) }
        datePickerDialog?.updateDate(
            TimeUtils.getCurrentYear(),
            TimeUtils.getCurrentMonth(),
            TimeUtils.getCurrentDay()
        )
        datePickerDialog?.setOnDateSetListener { _, year, month, day ->
            /// Set date
            returnCalendar.set(Calendar.YEAR, year)
            returnCalendar.set(Calendar.MONTH, month)
            returnCalendar.set(Calendar.DAY_OF_MONTH, day)
            // save date of birth
            _dateOfEvent = returnCalendar
            // bind date
            binding.eventDate.setContent(
                TimeUtils.getTime(
                    timeFormat = TimeUtils.TimeFormat.TimeFormat3,
                    time = returnCalendar.time
                )
            )
        }
        datePickerDialog?.show()
    }


}