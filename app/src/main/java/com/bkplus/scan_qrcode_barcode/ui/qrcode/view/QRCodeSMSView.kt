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
import com.bkplus.scan_qrcode_barcode.ui.qrcode.widget.BaseInputDescriptionView
import com.bkplus.scan_qrcode_barcode.ui.qrcode.widget.BaseInputFieldView
import com.bkplus.scan_qrcode_barcode.utils.isWhiteSpaceRepetitive
import com.bkplus.scan_qrcode_barcode.utils.onClickDelete

class QRCodeSMSView : BaseCustomViewLinearLayout {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        this.setupUI()
        this.setupListener()
    }

    private val viewPhoneSMS: BaseInputFieldView by lazy {
        layout.findViewById(R.id.viewPhoneSMS)
    }

    private val viewTextSMS: BaseInputDescriptionView by lazy {
        layout.findViewById(R.id.viewTextSMS)
    }

    override val layoutId: Int
        get() = R.layout.layout_qrcode_sms

    private fun setupUI() {
        viewPhoneSMS.setHint(context.getString(R.string.phone_number))
        viewPhoneSMS.setIcon(R.drawable.ic_phone_gray)
        viewTextSMS.setHint(context.getString(R.string.text_message))
        viewPhoneSMS.setInputTypeNumber()
        viewTextSMS.edtContent.addTextChangedListener(mTextEditorWatcher)
        viewPhoneSMS.edtContent.addTextChangedListener(mTextEditorWatcher)
        viewTextSMS.edtContent.isSaveEnabled = false
        viewPhoneSMS.edtContent.isSaveEnabled = false
        onClickDelete(viewPhoneSMS)
    }

    private fun setupListener() {

    }

    var onGenerateQRCodeCompletion: ((GenerateQRCodeResult) -> Unit)? = null
    var onEmptyInput: (() -> Unit)? = null
    var onActivatedInput: (() -> Unit)? = null
    fun generateQRCode() {
        if (!QRCodeManager.isPhoneNumber(viewPhoneSMS.getContent())) {
            Toast.makeText(context, context.getString(R.string.not_a_telephone_number), Toast.LENGTH_SHORT).show()
            return
        }
        if (viewTextSMS.getContent().isEmpty()) {
            Toast.makeText(context, context.getString(R.string.please_input_sms), Toast.LENGTH_SHORT).show()
            return
        }
        if (isWhiteSpaceRepetitive(viewTextSMS.getContent())) {
            Toast.makeText(context, context.getString(R.string.duplicate_white_space), Toast.LENGTH_SHORT).show()
            return
        }
        val result = QRCodeManager.instance().generateQRCodeSms(
            phone = viewPhoneSMS.getContent(),
            text = viewTextSMS.getContent(),
        )
        onGenerateQRCodeCompletion?.invoke(result)
    }

    private val mTextEditorWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            //This sets a textview to the current length
            if(viewPhoneSMS.edtContent.text.isNotEmpty() && viewTextSMS.edtContent.text.isNotEmpty()){
                onActivatedInput?.invoke()
            } else {
                onEmptyInput?.invoke()
            }
        }

        override fun afterTextChanged(s: Editable) {}
    }
}