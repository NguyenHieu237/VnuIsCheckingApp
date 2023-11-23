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
import com.bkplus.scan_qrcode_barcode.utils.isValidEmail
import com.bkplus.scan_qrcode_barcode.utils.isWhiteSpaceRepetitive
import com.bkplus.scan_qrcode_barcode.utils.onClickDelete
import com.bkplus.scan_qrcode_barcode.utils.restrictEmailInput

class QRCodeContactView: BaseCustomViewLinearLayout {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.setupUI()

    }

    private val viewNameContact: BaseInputFieldView by lazy {
        layout.findViewById(R.id.viewNameContact)
    }

    private val viewNumberContact: BaseInputFieldView by lazy {
        layout.findViewById(R.id.viewNumberContact)
    }

    private val viewEmailContact: BaseInputFieldView by lazy {
        layout.findViewById(R.id.viewEmailContact)
    }

    override val layoutId: Int
        get() = R.layout.layout_qrcode_contact

    private fun setupUI() {
        viewNameContact.setHint(context.getString(R.string.qr_code_name_contact))
        viewNameContact.setIcon(R.drawable.ic_namecontact)
        viewNumberContact.setHint(context.getString(R.string.qr_code_number_contact))
        viewNumberContact.setIcon(R.drawable.ic_numbercontact)
        viewNumberContact.setInputTypeNumber()
        viewEmailContact.setHint(context.getString(R.string.qr_code_email_contact))
        viewEmailContact.setIcon(R.drawable.ic_mailcontact)
        viewNameContact.edtContent.addTextChangedListener(mTextEditorWatcher)
        viewNumberContact.edtContent.addTextChangedListener(mTextEditorWatcher)
        viewEmailContact.edtContent.addTextChangedListener(mTextEditorWatcher)
        viewNameContact.edtContent.isSaveEnabled = false
        viewNumberContact.edtContent.isSaveEnabled = false
        viewEmailContact.edtContent.isSaveEnabled = false
        onClickDelete(viewNameContact)
        onClickDelete(viewNumberContact)
        onClickDelete(viewEmailContact)
    }

    var onGenerateQRCodeCompletion: ((GenerateQRCodeResult) -> Unit)? = null
    var onEmptyInput: (() -> Unit)? = null
    var onActivatedInput: (() -> Unit)? = null

    fun generateQRCode() {
        if (viewNameContact.getContent().isEmpty()) {
            Toast.makeText(this.context, context.getString(R.string.qr_code_error_name_contact), Toast.LENGTH_SHORT).show()
            return
        }
        if (viewNumberContact.getContent().isEmpty()) {
            Toast.makeText(this.context, context.getString(R.string.qr_code_error_phone_contact), Toast.LENGTH_SHORT).show()
            return
        }
        if (viewEmailContact.getContent().isEmpty()) {
            Toast.makeText(this.context, context.getString(R.string.qr_code_error_email_contact), Toast.LENGTH_SHORT).show()
            return
        }
        if(isWhiteSpaceRepetitive(viewNameContact.getContent()) ||
            isWhiteSpaceRepetitive(viewNumberContact.getContent()) ||
            isWhiteSpaceRepetitive(viewEmailContact.getContent()) ){
            Toast.makeText(context, context.getString(R.string.duplicate_white_space), Toast.LENGTH_SHORT).show()
            return
        }
        if(!isValidEmail(viewEmailContact.getContent())){
            Toast.makeText(context, context.getString(R.string.invalid_email), Toast.LENGTH_SHORT).show()
            return
        }
        val name = viewNameContact.getContent()
        val phone = viewNumberContact.getContent()
        val email = viewEmailContact.getContent()
        onGenerateQRCodeCompletion?.invoke(
            QRCodeManager.instance().generateQRCodeContact(
                 name, phone, email
            )
        )
    }

    private val mTextEditorWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            //This sets a textview to the current length
            if(viewNameContact.edtContent.text.isNotEmpty() ||
                viewEmailContact.edtContent.text.isNotEmpty() || viewNumberContact.edtContent.text.isNotEmpty()){
                onActivatedInput?.invoke()
            } else {
                onEmptyInput?.invoke()
            }
        }

        override fun afterTextChanged(s: Editable) {}
    }
}