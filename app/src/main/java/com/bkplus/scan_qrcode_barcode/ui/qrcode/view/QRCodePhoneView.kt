package com.bkplus.scan_qrcode_barcode.ui.qrcode.view


import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.Toast
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.base.BaseCustomViewLinearLayout
import com.bkplus.scan_qrcode_barcode.manager.qrcode.GenerateQRCodeResult
import com.bkplus.scan_qrcode_barcode.manager.qrcode.QRCodeManager
import com.bkplus.scan_qrcode_barcode.ui.qrcode.widget.BaseInputFieldView
import com.bkplus.scan_qrcode_barcode.utils.onClickDelete

class QRCodePhoneView: BaseCustomViewLinearLayout {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.setupUI()
        this.setupListener()
    }

    private val viewNamePhone: BaseInputFieldView by lazy {
        layout.findViewById(R.id.viewNamePhone)
    }

    override val layoutId: Int
        get() = R.layout.layout_qrcode_phone

    private fun setupUI() {
        viewNamePhone.setHint(context.getString(R.string.phone_number))
        viewNamePhone.setIcon(R.drawable.ic_phone_gray)
        viewNamePhone.setInputTypeNumber()
        viewNamePhone.edtContent.addTextChangedListener(mTextEditorWatcher)
        onClickDelete(viewNamePhone)
    }

    private fun setupListener() {

    }

    var onGenerateQRCodeCompletion: ((GenerateQRCodeResult) -> Unit)? = null
    var onEmptyInput: (() -> Unit)? = null
    var onActivatedInput: (() -> Unit)? = null
    fun generateQRCode() {
        if(QRCodeManager.isPhoneNumber(viewNamePhone.getContent()))
        {
            val result = QRCodeManager.instance().generateQRCodePhone(
                phone = viewNamePhone.getContent()
            )
            onGenerateQRCodeCompletion?.invoke(result)
        }
        else
        {
            Toast.makeText(context, context.getString(R.string.not_a_telephone_number), Toast.LENGTH_SHORT).show()
            return
        }
    }

    private val mTextEditorWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            //This sets a textview to the current length
            if(viewNamePhone.edtContent.text.isNotEmpty()){
                onActivatedInput?.invoke()
            } else {
                onEmptyInput?.invoke()
            }
        }

        override fun afterTextChanged(s: Editable) {}
    }
}