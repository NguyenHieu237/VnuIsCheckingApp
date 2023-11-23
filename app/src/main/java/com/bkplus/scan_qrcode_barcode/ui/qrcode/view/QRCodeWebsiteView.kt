package com.bkplus.scan_qrcode_barcode.ui.qrcode.view

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.webkit.URLUtil
import android.widget.Toast
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.base.BaseCustomViewLinearLayout
import com.bkplus.scan_qrcode_barcode.manager.qrcode.GenerateQRCodeResult
import com.bkplus.scan_qrcode_barcode.manager.qrcode.QRCodeManager
import com.bkplus.scan_qrcode_barcode.ui.qrcode.widget.BaseInputFieldView
import com.bkplus.scan_qrcode_barcode.utils.checkIfUrlInput
import com.bkplus.scan_qrcode_barcode.utils.haveWhiteSpace
import com.bkplus.scan_qrcode_barcode.utils.onClickDelete

class QRCodeWebsiteView : BaseCustomViewLinearLayout {
	constructor(context: Context?) : this(context, null)
	constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
	constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
		this.setupUI()
	}

	var onGenerateQRCodeCompletion: ((GenerateQRCodeResult) -> Unit)? = null
	var onEmptyInput: (() -> Unit)? = null
	var onActivatedInput: (() -> Unit)? = null

	private val viewWebsite: BaseInputFieldView by lazy {
		layout.findViewById(R.id.viewWebsite)
	}

	override val layoutId: Int
		get() = R.layout.layout_qrcode_website

	private fun setupUI() {
		viewWebsite.setIcon(R.drawable.ic_website_edit_text)
		viewWebsite.setEndIcon(R.drawable.ic_delete)
		viewWebsite.setText("https://")
		viewWebsite.edtContent.addTextChangedListener(mTextEditorWatcher)
		onClickDelete(viewWebsite)
	}

	fun generateQRCode() {
		if (viewWebsite.getContent().isEmpty()) {
			Toast.makeText(this.context, context.getString(R.string.qr_code_error_website), Toast.LENGTH_SHORT).show()
			return
		}
		if(haveWhiteSpace(viewWebsite.getContent())){
			Toast.makeText(this.context, context.getString(R.string.url_contain_space), Toast.LENGTH_SHORT).show()
			return
		}
		if(!URLUtil.isValidUrl(viewWebsite.getContent())){
			Toast.makeText(this.context, context.getString(R.string.invalid_url), Toast.LENGTH_SHORT).show()
			return
		}
		onGenerateQRCodeCompletion?.invoke(
			QRCodeManager.instance().generateQRCodeWebsite(viewWebsite.getContent())
		)
	}

	private val mTextEditorWatcher: TextWatcher = object : TextWatcher {
		override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
		override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
			//This sets a textview to the current length
			if(checkIfUrlInput(viewWebsite.edtContent.text.toString())){
				onActivatedInput?.invoke()
			} else {
				onEmptyInput?.invoke()
			}
		}

		override fun afterTextChanged(s: Editable) {}
	}
}