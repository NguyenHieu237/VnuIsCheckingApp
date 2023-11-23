package com.bkplus.scan_qrcode_barcode.base

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout

abstract class BaseCustomViewLinearLayout: LinearLayout {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        attrs?.let {
            initStyleable(it)
        }
        setLayout()
        initView()
    }

    protected abstract val layoutId: Int
    protected lateinit var layout: View
    private fun initView() {}

    private fun initStyleable(attrs: AttributeSet) {

    }

    private fun setLayout() {
        val inflater = LayoutInflater.from(context)
        layout = inflater.inflate(layoutId, this, true)
    }
}