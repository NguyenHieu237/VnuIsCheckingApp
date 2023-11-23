package com.bkplus.scan_qrcode_barcode.ui.scanner.view

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.CalendarContract
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.base.BaseCustomViewLinearLayout
import com.bkplus.scan_qrcode_barcode.ui.scanner.ScanResultFragment
import com.google.mlkit.vision.barcode.common.Barcode
import net.glxn.qrgen.core.scheme.SchemeUtil
import java.text.SimpleDateFormat
import java.util.Calendar


class QRResultCalendarView : BaseCustomViewLinearLayout {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var startDate: Calendar = Calendar.getInstance()
    private var endDate: Calendar = Calendar.getInstance()
    private val txtTitleView: TextView by lazy {
        layout.findViewById(R.id.txtTitle)
    }
    private val txtStartView: TextView by lazy {
        layout.findViewById(R.id.txtPhone)
    }
    private val txtEndView: TextView by lazy {
        layout.findViewById(R.id.txtEnd)
    }
    private val txtNoteView: TextView by lazy {
        layout.findViewById(R.id.txtNote)
    }
    private val bottom: LinearLayout by lazy {
        layout.findViewById(R.id.bottom)
    }
    override val layoutId: Int
        get() = R.layout.layout_result_qr_calendar

    fun setContent(
        title: String?,
        start: Barcode.CalendarDateTime?,
        end: Barcode.CalendarDateTime?,
        note: String?
    ) {
        txtTitleView.text = title
        txtStartView.text = getStartDate(start)
        txtEndView.text = getEndDate(end)
        txtNoteView.text = note
        init()
    }

    private fun getStartDate(date: Barcode.CalendarDateTime?): String {
        if (date != null) {
            startDate.set(date.year, date.month, date.day, date.hours, date.minutes, date.seconds)
            return SimpleDateFormat.getDateTimeInstance().format(startDate.time)
        }

        return ""

    }

    private fun getEndDate(date: Barcode.CalendarDateTime?): String {
        if (date != null) {
            endDate.set(date.year, date.month, date.day, date.hours, date.minutes, date.seconds)
            return SimpleDateFormat.getDateTimeInstance().format(endDate.time)
        }

        return ""
    }

    private fun init() {
        layout.findViewById<LinearLayout>(R.id.add).setOnClickListener {
            val intent = Intent(Intent.ACTION_EDIT)
            intent.type = "vnd.android.cursor.item/event"
            intent.putExtra(CalendarContract.Events.DTSTART, startDate.timeInMillis)
            intent.putExtra(CalendarContract.Events.DTEND, endDate.timeInMillis + 60 * 60 * 1000)
            intent.putExtra(CalendarContract.Events.TITLE, txtTitleView.text)
            intent.putExtra(CalendarContract.Events.DESCRIPTION, txtNoteView.text)
            context.startActivity(intent)
        }
        layout.findViewById<LinearLayout>(R.id.send).setOnClickListener {
            composeEmail(null, txtTitleView.text.toString())
        }
        layout.findViewById<LinearLayout>(R.id.copy).setOnClickListener {
            val sb = StringBuilder()
            sb.append("Title").append(": ").append(txtTitleView.text.toString()).append(SchemeUtil.LINE_FEED)
            sb.append("Start Date").append(": ").append(txtStartView.text.toString()).append(SchemeUtil.LINE_FEED)
            sb.append("End Date").append(": ").append(txtEndView.text.toString()).append(SchemeUtil.LINE_FEED)
            sb.append("Note").append(": ").append(txtNoteView.text.toString()).append(SchemeUtil.LINE_FEED)
            copyToClipboard(sb.toString())
        }
        if(ScanResultFragment.isCreate){
            bottom.visibility = View.GONE
        }
    }

    private fun composeEmail(addresses: Array<String?>?, subject: String?) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:") // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses)
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    }

    private fun copyToClipboard(text: CharSequence){
        val clipboard = getSystemService(context, ClipboardManager::class.java)
        val clip = ClipData.newPlainText("label",text)
        clipboard?.setPrimaryClip(clip)
        Toast.makeText(context,context.getString(R.string.copied_to_clipboard),Toast.LENGTH_SHORT).show()
    }


}
