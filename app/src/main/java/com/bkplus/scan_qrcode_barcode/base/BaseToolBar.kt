package com.bkplus.scan_qrcode_barcode.base

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.TextView
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.utils.extension.invisible
import com.bkplus.scan_qrcode_barcode.utils.extension.visible

class BaseToolBar: BaseCustomViewLinearLayout {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        this.setupListener()
    }


    val imgBack: ImageView by lazy {
        layout.findViewById(R.id.ic_back)
    }

    val imgIconRight1: ImageView by lazy {
        layout.findViewById(R.id.imgIconRight1)
    }

    val imgIconRight2: ImageView by lazy {
        layout.findViewById(R.id.imgIconRight2)
    }
    val imgIconRight3: ImageView by lazy {
        layout.findViewById(R.id.imgIconRight3)
    }

    val tvTitle: TextView by lazy {
        layout.findViewById(R.id.tvTitle)
    }

    var onTapBackListener: (() -> Unit)? = null
    var onTapIconRight1Listener: (() -> Unit)? = null
    var onTapIconRight2Listener: (() -> Unit)? = null
    var onTapIconRight3Listener: (() -> Unit)? = null

    override val layoutId: Int
        get() = R.layout.layout_base_toolbar

    private fun setupListener() {
        imgBack.setOnClickListener {
            onTapBackListener?.invoke()
        }

        imgIconRight1.setOnClickListener {
            onTapIconRight1Listener?.invoke()
        }

        imgIconRight2.setOnClickListener {
            onTapIconRight2Listener?.invoke()
        }
        imgIconRight3.setOnClickListener {
            onTapIconRight3Listener?.invoke()
        }
    }

    fun hideBackButton(){
        imgBack.invisible()
    }

    fun setTitle(title: String?) {
        tvTitle.text = title
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun setIconRight1(resId: Int) {
        imgIconRight1.visible()
        imgIconRight1.setImageDrawable(context.getDrawable(resId))
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun setIconRight2(resId: Int) {
        imgIconRight2.visible()
        imgIconRight2.setImageDrawable(context.getDrawable(resId))
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    fun setIconRight3(resId: Int) {
        imgIconRight3.visible()
        imgIconRight3.setImageDrawable(context.getDrawable(resId))
    }
}
