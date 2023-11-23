package com.bkplus.scan_qrcode_barcode.ui.qrcode.view

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.Toast
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.base.BaseCustomViewLinearLayout
import com.bkplus.scan_qrcode_barcode.manager.qrcode.GenerateQRCodeResult
import com.bkplus.scan_qrcode_barcode.manager.qrcode.QRCodeManager
import com.bkplus.scan_qrcode_barcode.ui.qrcode.widget.BaseInputFieldView

class QRCodeBarEAN8 : BaseCustomViewLinearLayout {
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

    private val viewBarCode: BaseInputFieldView by lazy {
        layout.findViewById(R.id.viewNamePhone)
    }
    private val invalid: ImageView by lazy {
        layout.findViewById(R.id.invalid)
    }


    override val layoutId: Int
        get() = R.layout.layout_qrcode_phone

    private fun setupUI() {
        viewBarCode.setInputTypeNumber()
        viewBarCode.countCharacter(8, invalid)
        viewBarCode.setHint(context.getString(R.string.hint_ean8))
        viewBarCode.setIcon(R.drawable.ic_null)
        viewBarCode.edtContent.addTextChangedListener(mTextEditorWatcher)

    }

    fun generateQRCode() {
        if (QRCodeManager.isValidEANandUCP(viewBarCode.getContent())) {
            val result = QRCodeManager.instance().generateBarEAN8Code(
                value = viewBarCode.getContent()
            )
            onGenerateQRCodeCompletion?.invoke(result)
        } else {
            Toast.makeText(
                this.context,
                context.getString(R.string.invalid_form_ean8),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
    }

    private val mTextEditorWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (viewBarCode.edtContent.length() == 8 ) {
                onActivatedInput?.invoke()
            } else {
                onEmptyInput?.invoke()
            }
        }

        override fun afterTextChanged(s: Editable) {}
    }
}