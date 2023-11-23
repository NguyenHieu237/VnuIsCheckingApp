package com.bkplus.scan_qrcode_barcode.ui.qrcode.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.base.BaseCustomViewLinearLayout

class BaseInputSelectionView: BaseCustomViewLinearLayout {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setupListener()
    }

    override val layoutId: Int
        get() = R.layout.layout_base_input_selection

    private val tvTitle: TextView by lazy {
        layout.findViewById(R.id.tvTitle)
    }

    private val tvContent: TextView by lazy {
        layout.findViewById(R.id.tvContent)
    }

    private val imgIcon: ImageView by lazy {
        layout.findViewById(R.id.imgIcon)
    }

    var onTapListener: (() -> Unit)? = null

    private fun setupListener() {
        layout.rootView.setOnClickListener {
            onTapListener?.invoke()
        }
    }

    fun setTitle(title: String) {
        tvTitle.text = title
    }

    fun setTextColor(color: Int) {
        tvTitle.setTextColor(color)
    }

    fun setIcon(resource: Int) {
        imgIcon.visibility = visibility
        imgIcon.setImageResource(resource)
        val params = ConstraintLayout.LayoutParams(tvTitle.layoutParams)
        params.setMargins(resources.getDimension(R.dimen.dp_16).toInt(), 0, 0, 0)
        params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        params.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        params.startToEnd = R.id.imgIcon
        tvTitle.layoutParams = params
        tvTitle.requestLayout()
    }

    fun setContent(content: String) {
        tvContent.text = content
    }
}