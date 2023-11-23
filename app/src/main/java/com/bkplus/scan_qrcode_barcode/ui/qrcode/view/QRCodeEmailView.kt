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
import com.bkplus.scan_qrcode_barcode.utils.restrictEmailInput

class QRCodeEmailView : BaseCustomViewLinearLayout {
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

    private val viewNameEmail: BaseInputFieldView by lazy {
        layout.findViewById(R.id.viewNameEmail)
    }

    override val layoutId: Int
        get() = R.layout.layout_qrcode_email

    private fun setupUI() {
        viewNameEmail.setHint(context.getString(R.string.email))
        viewNameEmail.setIcon(R.drawable.ic_email_gray)
        viewNameEmail.setInputTypeEmailAddress()
        viewNameEmail.edtContent.addTextChangedListener(mTextEditorWatcher)
        onClickDelete(viewNameEmail)
    }

    private fun setupListener() {

    }

    var onGenerateQRCodeCompletion: ((GenerateQRCodeResult) -> Unit)? = null
    var onEmptyInput: (() -> Unit)? = null
    var onActivatedInput: (() -> Unit)? = null

    fun generateQRCode() {
        if (!QRCodeManager.isValidEmail(viewNameEmail.getContent()) || restrictEmailInput(viewNameEmail.edtContent)) {
            Toast.makeText(context, "Not a valid email", Toast.LENGTH_SHORT).show()
            return
        }
        val result = QRCodeManager.instance().generateQRCodeEmail(
            email = viewNameEmail.getContent()
        )
        onGenerateQRCodeCompletion?.invoke(result)
    }

    private val mTextEditorWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            //This sets a textview to the current length
            if (viewNameEmail.edtContent.text.isNotEmpty()) {
                onActivatedInput?.invoke()
            } else {
                onEmptyInput?.invoke()
            }
        }

        override fun afterTextChanged(s: Editable) {}
    }
}