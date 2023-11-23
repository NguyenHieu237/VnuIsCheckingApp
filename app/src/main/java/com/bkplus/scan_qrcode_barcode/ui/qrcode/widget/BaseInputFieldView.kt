package com.bkplus.scan_qrcode_barcode.ui.qrcode.widget

import android.annotation.SuppressLint
import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.base.BaseCustomViewLinearLayout
import com.bkplus.scan_qrcode_barcode.utils.extension.disposedBy
import com.bkplus.scan_qrcode_barcode.utils.extension.rxTextChange
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

class BaseInputFieldView: BaseCustomViewLinearLayout {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override val layoutId: Int
        get() = R.layout.layout_base_input_field

    override fun onAttachedToWindow() {
        setupObservers()
        super.onAttachedToWindow()
    }

    override fun onDetachedFromWindow() {
        bag.clear()
        _needSaveStateContent = true
        super.onDetachedFromWindow()
    }

    val edtContent: EditText by lazy {
        layout.findViewById(R.id.edtContent)
    }

    val imgIconStart: ImageView by lazy {
        layout.findViewById(R.id.imgIconStart)
    }

    val imgIconEnd: ImageView by lazy {
        layout.findViewById(R.id.imgIconEnd)
    }

    private val textLength : TextView by lazy {
        layout.findViewById(R.id.txtLength)
    }

        private val bag: CompositeDisposable by lazy {
        CompositeDisposable()
    }


    private var _needSaveStateContent: Boolean = false /// Value check if user clear view
    private var _contentSubject = BehaviorSubject.create<String>()

    private fun setupObservers() {
        edtContent.rxTextChange()
            .subscribe {
                /// Check if view is cleared
                if (it.isEmpty() &&
                    !_contentSubject.value.isNullOrEmpty() &&
                    _needSaveStateContent)
                {
                    /// Reset state
                    _needSaveStateContent = false

                    setText(text = _contentSubject.value)
                } else {
                    _contentSubject.onNext(it)
                }
            }
            .disposedBy(bag = bag)
    }

    fun setHint(hint: String) {
        edtContent.hint = hint
    }

    fun setText(text: String?) {
        edtContent.setText(text)
    }
    
    @SuppressLint("SetTextI18n")
    fun countCharacter(count: Int,imageView: ImageView){
        textLength.visibility = View.VISIBLE
        edtContent.rxTextChange().map { it.count() }.subscribe{
            textLength.text = "$it/$count"
            if(it != count){
                imageView.visibility = View.VISIBLE
            }else{
                imageView.visibility = View.GONE
            }
        }.disposedBy(bag)
    }

    fun setInputTypeNumber(){
        edtContent.inputType = InputType.TYPE_CLASS_NUMBER
    }

    fun setInputTypeEmailAddress(){
        edtContent.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun setIcon(drawableId: Int) {
        imgIconStart.setImageDrawable(context.getDrawable(drawableId))
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun setEndIcon(drawableId: Int) {
        imgIconEnd.setImageDrawable(context.getDrawable(drawableId))
    }
    
    fun getContent(): String {
        return edtContent.text.toString()
    }
}