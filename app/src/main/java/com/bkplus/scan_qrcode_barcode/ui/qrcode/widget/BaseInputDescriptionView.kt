package com.bkplus.scan_qrcode_barcode.ui.qrcode.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.EditText
import android.widget.TextView
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.base.BaseCustomViewLinearLayout

class BaseInputDescriptionView: BaseCustomViewLinearLayout {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override val layoutId: Int
        get() = R.layout.layout_base_input_description

    val edtContent: EditText by lazy {
        layout.findViewById(R.id.edtContent)
    }

    val counter: TextView by lazy {
        layout.findViewById(R.id.counter)
    }

    fun setHint(hint: String) {
        edtContent.hint = hint
    }

    fun setLine(lines: Int) {
        edtContent.setLines(lines)
    }

    fun getContent(): String {
        return edtContent.text.toString()
    }

    fun setContent(content: String?){
        edtContent.setText(content, TextView.BufferType.EDITABLE)
    }
}