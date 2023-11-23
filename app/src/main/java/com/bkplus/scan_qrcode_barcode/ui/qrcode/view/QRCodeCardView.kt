package com.bkplus.scan_qrcode_barcode.ui.qrcode.view

import android.app.DatePickerDialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.base.BaseCustomViewLinearLayout
import com.bkplus.scan_qrcode_barcode.manager.qrcode.GenerateQRCodeResult
import com.bkplus.scan_qrcode_barcode.manager.qrcode.QRCodeManager
import com.bkplus.scan_qrcode_barcode.ui.qrcode.widget.BaseInputFieldView
import com.bkplus.scan_qrcode_barcode.ui.qrcode.widget.BaseInputSelectionView
import com.bkplus.scan_qrcode_barcode.ui.qrcode.widget.TimeUtils
import com.bkplus.scan_qrcode_barcode.utils.isValidEmail
import com.bkplus.scan_qrcode_barcode.utils.extension.guardLet
import com.bkplus.scan_qrcode_barcode.utils.onClickDelete
import com.bkplus.scan_qrcode_barcode.utils.restrictEmailInput
import java.util.*

class QRCodeCardView: BaseCustomViewLinearLayout {
	constructor(context: Context?) : this(context, null)
	constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
	constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
		this.setupUI()
		this.setupListener()
	}

	private var _dateOfBirth: Calendar? = null
	private val viewName: BaseInputFieldView by lazy {
		layout.findViewById(R.id.viewName)
	}
	private val viewPhoneNumber: BaseInputFieldView by lazy {
		layout.findViewById(R.id.viewPhoneNumber)
	}
	private val viewEmail: BaseInputFieldView by lazy {
		layout.findViewById(R.id.viewEmail)
	}
	private val viewAddress: BaseInputFieldView by lazy {
		layout.findViewById(R.id.viewAddress)
	}
	private val viewJob: BaseInputFieldView by lazy {
		layout.findViewById(R.id.viewJob)
	}
	private val viewDateOfBirth: BaseInputSelectionView by lazy {
		layout.findViewById(R.id.viewDateOfBirth)
	}
	var onGenerateQRCodeCompletion: ((GenerateQRCodeResult) -> Unit)? = null
	var onEmptyInput: (() -> Unit)? = null
	var onActivatedInput: (() -> Unit)? = null
	override val layoutId: Int
		get() = R.layout.layout_qrcode_card
	private fun setupUI() {
		viewName.setIcon(R.drawable.ic_profile)
		viewName.setHint(context.getString(R.string.name))
		viewPhoneNumber.setIcon(R.drawable.ic_phone_qr)
		viewPhoneNumber.setHint(context.getString(R.string.phone_number))
		viewEmail.setIcon(R.drawable.ic_envelope)
		viewEmail.setHint(context.getString(R.string.email))
		viewAddress.setIcon(R.drawable.ic_location)
		viewAddress.setHint(context.getString(R.string.address))
		viewDateOfBirth.setIcon(R.drawable.ic_calendar_qr)
		viewDateOfBirth.setTitle(context.getString(R.string.dob))
		viewDateOfBirth.setTextColor(ContextCompat.getColor(this.context, R.color.color606060))
		viewJob.setIcon(R.drawable.ic_bag)
		viewJob.setHint(context.getString(R.string.job))
		viewName.edtContent.addTextChangedListener(mTextEditorWatcher)
		viewPhoneNumber.edtContent.addTextChangedListener(mTextEditorWatcher)
		viewEmail.edtContent.addTextChangedListener(mTextEditorWatcher)
		viewAddress.edtContent.addTextChangedListener(mTextEditorWatcher)
		viewJob.edtContent.addTextChangedListener(mTextEditorWatcher)
		viewName.edtContent.isSaveEnabled = false
		viewPhoneNumber.edtContent.isSaveEnabled = false
		viewEmail.edtContent.isSaveEnabled = false
		viewAddress.edtContent.isSaveEnabled = false
		viewJob.edtContent.isSaveEnabled = false
		onClickDelete(viewName)
		onClickDelete(viewPhoneNumber)
		onClickDelete(viewEmail)
		onClickDelete(viewAddress)
		onClickDelete(viewJob)
	}
	fun generateQRCode() {
		val date = _dateOfBirth.guardLet {
			Toast.makeText(
				context,
				context.getString(R.string.qr_code_error_date_of_birth),
				Toast.LENGTH_SHORT
			).show()
			return
		}!!
		if (date > Calendar.getInstance()) {
			Toast.makeText(this.context, context.getString(R.string.please_choose_birthday_again), Toast.LENGTH_SHORT).show()
			return
		}
		if (viewName.getContent().isEmpty()) {
			Toast.makeText(this.context, context.getString(R.string.qr_code_error_name_contact), Toast.LENGTH_SHORT).show()
			return
		}
		if (viewPhoneNumber.getContent().isEmpty()) {
			Toast.makeText(this.context, context.getString(R.string.qr_code_error_phone_contact), Toast.LENGTH_SHORT).show()
			return
		}
		if (viewEmail.getContent().isEmpty()) {
			Toast.makeText(this.context, context.getString(R.string.qr_code_error_email_contact), Toast.LENGTH_SHORT).show()
			return
		}
		if (viewAddress.getContent().isEmpty()) {
			Toast.makeText(this.context, context.getString(R.string.please_enter_address), Toast.LENGTH_SHORT).show()
			return
		}
		if (viewJob.getContent().isEmpty()) {
			Toast.makeText(this.context, context.getString(R.string.please_enter_job), Toast.LENGTH_SHORT).show()
			return
		}
		if (!QRCodeManager.isPhoneNumber(viewPhoneNumber.getContent())) {
			Toast.makeText(context, context.getString(R.string.not_a_telephone_number), Toast.LENGTH_SHORT).show()
			return
		}
		if (!isValidEmail(viewEmail.getContent()) || restrictEmailInput(viewEmail.edtContent)) {
			Toast.makeText(context, context.getString(R.string.invalid_email), Toast.LENGTH_SHORT).show()
			return
		}
		val name = viewName.getContent()
		val email = viewEmail.getContent()
		val address = viewAddress.getContent()
		val phoneNumber = viewPhoneNumber.getContent()
		val company = viewJob.getContent()
		val dateOfBirth = TimeUtils.getTime(TimeUtils.TimeFormat.TimeFormat3, date.time)
		onGenerateQRCodeCompletion?.invoke(
			QRCodeManager.instance().generateQRCodeCard(
				name, email, address, "", company, phoneNumber, dateOfBirth
			)
		)
	}
	private fun setupListener() {
		viewDateOfBirth.onTapListener = {
			openDatePicker()
		}
	}
	private fun openDatePicker() {
		val returnCalendar = Calendar.getInstance()
		val datePickerDialog = DatePickerDialog(context)
		datePickerDialog.updateDate(
			TimeUtils.getCurrentYear(),
			TimeUtils.getCurrentMonth(),
			TimeUtils.getCurrentDay()
		)
		datePickerDialog.setOnDateSetListener { _, year, month, day ->
			/// Set date
			returnCalendar.set(Calendar.YEAR, year)
			returnCalendar.set(Calendar.MONTH, month)
			returnCalendar.set(Calendar.DAY_OF_MONTH, day)
			// save date of birth
			_dateOfBirth = returnCalendar
			// bind date
			viewDateOfBirth.setContent(
				TimeUtils.getTime(
					timeFormat = TimeUtils.TimeFormat.TimeFormat3,
					time = returnCalendar.time
				)
			)
		}
		datePickerDialog.show()
	}
	private val mTextEditorWatcher: TextWatcher = object : TextWatcher {
		override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
		}
		override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
			//This sets a textview to the current length
			if (viewAddress.edtContent.text.isNotEmpty() ||
				viewEmail.edtContent.text.isNotEmpty() ||
				viewJob.edtContent.text.isNotEmpty() ||
				viewName.edtContent.text.isNotEmpty() ||
				viewPhoneNumber.edtContent.text.isNotEmpty()
			) {
				onActivatedInput?.invoke()
			} else {
				onEmptyInput?.invoke()
			}
		}
		override fun afterTextChanged(s: Editable) {}
	}
}