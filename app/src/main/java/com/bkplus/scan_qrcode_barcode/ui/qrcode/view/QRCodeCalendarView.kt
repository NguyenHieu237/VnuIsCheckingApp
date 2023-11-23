package com.bkplus.scan_qrcode_barcode.ui.qrcode.view

import android.app.DatePickerDialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.base.BaseCustomViewLinearLayout
import com.bkplus.scan_qrcode_barcode.ui.qrcode.widget.BaseInputFieldView
import com.bkplus.scan_qrcode_barcode.ui.qrcode.widget.BaseInputSelectionView
import com.bkplus.scan_qrcode_barcode.ui.qrcode.widget.TimeUtils
import com.bkplus.scan_qrcode_barcode.manager.qrcode.GenerateQRCodeResult
import com.bkplus.scan_qrcode_barcode.manager.qrcode.QRCodeManager
import com.bkplus.scan_qrcode_barcode.ui.qrcode.widget.BaseInputDescriptionView
import com.example.snap_time_picker.SnapTimePickerDialog
import com.google.android.material.switchmaterial.SwitchMaterial
import com.bkplus.scan_qrcode_barcode.utils.extension.guardLet
import com.bkplus.scan_qrcode_barcode.utils.onClickDelete
import io.reactivex.disposables.CompositeDisposable
import java.util.*

class QRCodeCalendarView : BaseCustomViewLinearLayout {
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

    private val bag: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    private val viewTitle: BaseInputFieldView by lazy {
        layout.findViewById(R.id.viewTitle)
    }

    private val viewDayStart: BaseInputSelectionView by lazy {
        layout.findViewById(R.id.viewDayStart)
    }

    private val viewDayEnd: BaseInputSelectionView by lazy {
        layout.findViewById(R.id.viewDayEnd)
    }

    private val viewDescription: BaseInputDescriptionView by lazy {
        layout.findViewById(R.id.viewDescription)
    }

    private val switchButton: SwitchMaterial by lazy {
        layout.findViewById(R.id.switchButton)
    }

    private val datePickerDialog: DatePickerDialog by lazy {
        DatePickerDialog(context)
    }

    private var _startDate: Calendar? = null
    private var _endDate: Calendar? = null
    private var date: Calendar = Calendar.getInstance()
    var onGenerateQRCodeCompletion: ((GenerateQRCodeResult) -> Unit)? = null
    var onEmptyInput: (() -> Unit)? = null
    var onActivatedInput: (() -> Unit)? = null

    override fun onDetachedFromWindow() {
        bag.clear()
        bag.dispose()
        super.onDetachedFromWindow()
    }

    override val layoutId: Int
        get() = R.layout.layout_qrcode_calendar

    private fun setupUI() {
        viewTitle.setHint(context.getString(R.string.qr_code_title))
        viewTitle.setIcon(R.drawable.ic_title_qr)
        viewDayStart.setTitle(context.getString(R.string.qr_code_day_start))
        viewDayEnd.setTitle(context.getString(R.string.qr_code_day_end))
        onClickDelete(viewTitle)

        val states = arrayOf(
            intArrayOf(android.R.attr.state_checked), // checked
            intArrayOf(-android.R.attr.state_checked), // unchecked
        )

        val thumbColors = intArrayOf(
            Color.parseColor("#1B9B60"), // checked
            Color.parseColor("#E6E6E6"), // unchecked
        )
        val trackColors = intArrayOf(
            Color.parseColor("#46DF98"), // checked
            Color.parseColor("#A4A4A4"), // unchecked
        )
        switchButton.thumbTintList = ColorStateList(states, thumbColors)
        switchButton.trackTintList = ColorStateList(states, trackColors)
    }

    private fun setupListener() {
        viewDayStart.onTapListener = {
            openDatePicker(type = DatePickerType.DAY_START)
        }

        viewDayEnd.onTapListener = {
            openDatePicker(type = DatePickerType.DAY_END)
        }

        switchButton.setOnClickListener {
            if (switchButton.isChecked) {
                _startDate = date
                setCurrentDate(_startDate!!)
                viewDayStart.setContent(
                    content = TimeUtils.getTime(
                        timeFormat = TimeUtils.TimeFormat.TimeFormat5,
                        time = _startDate!!.time
                    )
                )
                _endDate = date
                setCurrentDate(_endDate!!)
                viewDayEnd.setContent(
                    content = TimeUtils.getTime(
                        timeFormat = TimeUtils.TimeFormat.TimeFormat5,
                        time = _endDate!!.time
                    )
                )
            }
        }

        viewTitle.edtContent.addTextChangedListener(mTextEditorWatcher)
        viewDescription.edtContent.addTextChangedListener(mTextEditorWatcher)
    }

    // TODO Date picker and time picker
    private fun openDatePicker(type: DatePickerType) {
        /// Validate
        if (datePickerDialog.isShowing) {
            return
        }

        /// Show date picker
        val returnCalendar = Calendar.getInstance()
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

            /// Choose time
            if (!switchButton.isChecked)
                openTimePicker(type = type, calendar = returnCalendar)
            else {
                bindDate(
                    type = type,
                    date = TimeUtils.getTime(
                        timeFormat = TimeUtils.TimeFormat.TimeFormat5,
                        time = returnCalendar.time
                    )
                )
                saveLastCalendar(
                    type = type,
                    returnCalendar
                )
            }
        }
        datePickerDialog.show()
    }

    private fun bindDate(type: DatePickerType, date: String) {
        when (type) {
            DatePickerType.DAY_START -> {
                viewDayStart.setContent(content = date)
            }

            DatePickerType.DAY_END -> {
                viewDayEnd.setContent(content = date)
            }
        }
    }

    private fun saveLastCalendar(type: DatePickerType, calendar: Calendar) {
        when (type) {
            DatePickerType.DAY_START -> {
                _startDate = calendar
            }

            DatePickerType.DAY_END -> {
                _endDate = calendar
            }
        }
    }

    private fun openTimePicker(type: DatePickerType, calendar: Calendar) {
        (context as? AppCompatActivity)?.supportFragmentManager?.let {
            SnapTimePickerDialog.Builder().apply {
                setNegativeButtonColor(R.color.white)
                setPositiveButtonColor(R.color.white)
                setButtonTextAllCaps(false)
            }.build().apply {
                setListener { hour, minute ->
                    /// Set time
                    calendar.set(Calendar.HOUR_OF_DAY, hour)
                    calendar.set(Calendar.MINUTE, minute)
                    calendar.set(Calendar.SECOND, 0)

                    /// Save calendar
                    saveLastCalendar(type = type, calendar = calendar)

                    /// Bind data
                    bindDate(
                        type = type,
                        date = TimeUtils.getTime(
                            timeFormat = TimeUtils.TimeFormat.TimeFormat1,
                            time = calendar.time
                        )
                    )
                }
            }.show(it, SnapTimePickerDialog.TAG)
        }
    }

    fun generateQRCode() {
        /// Validate
        val startDate = _startDate.guardLet {
            Toast.makeText(
                context,
                context.getString(R.string.qr_code_error_start_date),
                Toast.LENGTH_SHORT
            ).show()
            return
        }!!

        val endDate = _endDate.guardLet {
            Toast.makeText(
                context,
                context.getString(R.string.qr_code_error_end_date),
                Toast.LENGTH_SHORT
            ).show()
            return
        }!!

        if (startDate > endDate || endDate < date) {
            Toast.makeText(
                context,
                context.getString(R.string.qr_code_error_range_date),
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if(startDate < date){
            Toast.makeText(
                context,
                context.getString(R.string.please_choose_start_date_again),
                Toast.LENGTH_SHORT
            ).show()
            return
        }


        /// Handle generate
        val result = QRCodeManager.instance().generateQRCodeCalendar(
            title = viewTitle.getContent(),
            startDate = startDate,
            endDate = endDate,
            description = viewDescription.getContent()
        )
        onGenerateQRCodeCompletion?.invoke(result)
    }

    // TODO Enum class
    enum class DatePickerType {
        DAY_START,
        DAY_END
    }

    private val mTextEditorWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            //This sets a textview to the current length
            if (viewTitle.edtContent.text.isNotEmpty() || viewDescription.edtContent.text.isNotEmpty()) {
                onActivatedInput?.invoke()
            } else {
                onEmptyInput?.invoke()
            }
        }

        override fun afterTextChanged(s: Editable) {}
    }

    private fun setCurrentDate(calendar: Calendar) {
        calendar.set(Calendar.YEAR, TimeUtils.getCurrentYear())
        calendar.set(Calendar.MONTH, TimeUtils.getCurrentMonth())
        calendar.set(Calendar.DAY_OF_MONTH, TimeUtils.getCurrentDay())
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
    }
}