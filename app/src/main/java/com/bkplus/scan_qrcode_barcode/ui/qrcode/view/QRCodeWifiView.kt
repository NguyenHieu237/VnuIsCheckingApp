package com.bkplus.scan_qrcode_barcode.ui.qrcode.view

import android.animation.LayoutTransition
import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.base.BaseCustomViewLinearLayout
import com.bkplus.scan_qrcode_barcode.manager.qrcode.GenerateQRCodeResult
import com.bkplus.scan_qrcode_barcode.manager.qrcode.QRCodeManager
import com.bkplus.scan_qrcode_barcode.ui.qrcode.widget.BaseInputFieldView
import com.bkplus.scan_qrcode_barcode.ui.qrcode.widget.CustomSpinner
import com.bkplus.scan_qrcode_barcode.utils.checkIfUrlInput
import com.bkplus.scan_qrcode_barcode.utils.isWhiteSpaceRepetitive
import com.bkplus.scan_qrcode_barcode.utils.onClickDelete

class QRCodeWifiView : BaseCustomViewLinearLayout {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        this.setupUI()
    }

    var onGenerateQRCodeCompletion: ((GenerateQRCodeResult) -> Unit)? = null
    var onEmptyInput: (() -> Unit)? = null
    var onActivatedInput: (() -> Unit)? = null

    private val viewSSID: BaseInputFieldView by lazy {
        layout.findViewById(R.id.viewSSID)
    }

    private val viewPassword: BaseInputFieldView by lazy {
        layout.findViewById(R.id.viewPassword)
    }

    private val dropdownIcon: AppCompatImageView by lazy {
        layout.findViewById(R.id.dropdownIcon)
    }

    private val spinnerContainer: ConstraintLayout by lazy {
        layout.findViewById(R.id.spinnerContainer)
    }

    private val wifiTypeList: CustomSpinner by lazy {
        layout.findViewById(R.id.wifiTypeList)
    }

    override val layoutId: Int
        get() = R.layout.layout_qrcode_wifi

    private fun setupUI() {
        viewSSID.setIcon(R.drawable.ic_wifi_qr)
        viewSSID.setHint(context.getString(R.string.network_name))
        viewPassword.setIcon(R.drawable.ic_lock)
        viewPassword.setHint(context.getString(R.string.password))
        viewSSID.edtContent.addTextChangedListener(mTextEditorWatcher)
        viewPassword.edtContent.addTextChangedListener(mTextEditorWatcher)
        dropdownIcon.rotation = 180F
        viewSSID.edtContent.isSaveEnabled = false
        viewPassword.edtContent.isSaveEnabled = false
        onClickDelete(viewSSID)
        onClickDelete(viewPassword)

        spinnerContainer.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

        ArrayAdapter.createFromResource(
            this.context,
            R.array.wifi_types_list,
            R.layout.custom_spinner
        ).also {
            it.setDropDownViewResource(R.layout.custom_dropdown_item)
            wifiTypeList.adapter = it

            wifiTypeList.setSpinnerEventsListener(object : CustomSpinner.OnSpinnerEventsListener {
                override fun onSpinnerOpened(spinner: Spinner?) {
                    dropdownIcon.animate().rotationBy(90F).duration = 150
                    spinnerContainer.layoutParams.height =
                        spinnerContainer.measuredHeight * (it.count + 1)
                    spinnerContainer.requestLayout()
                    wifiTypeList.dropDownVerticalOffset = wifiTypeList.measuredHeight
                }

                override fun onSpinnerClosed(spinner: Spinner?) {
                    dropdownIcon.animate().rotationBy(-90f).duration = 200
                    spinnerContainer.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    spinnerContainer.requestLayout()
                }
            })
        }
    }

    fun generateQRCode() {
        val ssid = viewSSID.getContent()
        val pass = viewPassword.getContent()
        var type = wifiTypeList.getSelectedValue()
        if (type == "WPA/WPA2") type = "WPA"
        if (isWhiteSpaceRepetitive(ssid) || isWhiteSpaceRepetitive(pass)) {
            Toast.makeText(context, context.getString(R.string.duplicate_white_space), Toast.LENGTH_SHORT).show()
            return
        }
        if (ssid.isEmpty() || pass.isEmpty()) {
            Toast.makeText(context, context.getString(R.string.ssid_pass_is_empty), Toast.LENGTH_SHORT).show()
            return
        }
        onGenerateQRCodeCompletion?.invoke(
            QRCodeManager.instance().generateQRCodeWifi(
                ssid, pass, type
            )
        )
    }

    private val mTextEditorWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            //This sets a textview to the current length
            if(viewSSID.edtContent.text.isNotEmpty() && viewPassword.edtContent.text.isNotEmpty()){
                onActivatedInput?.invoke()
            } else {
                onEmptyInput?.invoke()
            }
        }

        override fun afterTextChanged(s: Editable) {}
    }
}