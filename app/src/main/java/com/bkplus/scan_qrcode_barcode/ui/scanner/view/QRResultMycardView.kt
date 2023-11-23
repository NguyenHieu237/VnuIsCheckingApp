package com.bkplus.scan_qrcode_barcode.ui.scanner.view

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.content.ClipboardManager
import android.provider.ContactsContract
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.base.BaseCustomViewLinearLayout
import com.bkplus.scan_qrcode_barcode.ui.scanner.ScanResultFragment


class QRResultMycardView : BaseCustomViewLinearLayout {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val txtNameView: TextView by lazy {
        layout.findViewById(R.id.txtName)
    }
    private val txtPhoneView: TextView by lazy {
        layout.findViewById(R.id.txtPhone)
    }
    private val txtMailView: TextView by lazy {
        layout.findViewById(R.id.txtMail)
    }
    private val txtBDayView: TextView by lazy {
        layout.findViewById(R.id.txtCake)
    }
    private val txtAddressView: TextView by lazy {
        layout.findViewById(R.id.txtLocation)
    }
    private val txtWorkView: TextView by lazy {
        layout.findViewById(R.id.txtWork)
    }
    private val bottom: LinearLayout by lazy {
        layout.findViewById(R.id.bottom)
    }
    override val layoutId: Int
        get() = R.layout.layout_result_qr_mycard

    fun setContent(
        name: String?,
        phone: String?,
        mail: String?,
        address: String?,
        work: String?,
        birthday: String?
    ) {
        txtNameView.text = name
        txtPhoneView.text = phone
        txtMailView.text = mail
        txtAddressView.text = address
        txtWorkView.text = work
        txtBDayView.text = birthday
        init()
    }

    private fun init() {
        layout.findViewById<LinearLayout>(R.id.add).setOnClickListener {
            insertContact()
        }
        layout.findViewById<LinearLayout>(R.id.map).setOnClickListener {
            openMap()
        }
        layout.findViewById<LinearLayout>(R.id.email).setOnClickListener {
            composeEmail()
        }
        layout.findViewById<LinearLayout>(R.id.call).setOnClickListener {
            call()
        }
        layout.findViewById<LinearLayout>(R.id.copy).setOnClickListener {
            copyToClipboard(
                txtNameView.text.toString() +
                        txtPhoneView.text.toString() +
                        txtMailView.text.toString() +
                        txtAddressView.text.toString() +
                        txtWorkView.text.toString()
            )
        }
        if (ScanResultFragment.isCreate) {
            bottom.visibility = View.GONE
        }
    }

    private fun openMap() {
        val map = "http://maps.google.co.in/maps?q=" + txtAddressView.text.toString()
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(map))
        context.startActivity(intent)
    }

    private fun insertContact() {
        val intent = Intent(
            ContactsContract.Intents.SHOW_OR_CREATE_CONTACT,
            ContactsContract.Contacts.CONTENT_URI
        )
        intent.data = Uri.parse("tel:" + txtPhoneView.text.toString()) //specify your number here
        intent.putExtra(ContactsContract.Intents.Insert.COMPANY, txtWorkView.text.toString())
        intent.putExtra(ContactsContract.Intents.Insert.NAME, txtNameView.text.toString())
        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, txtMailView.text.toString())
        intent.putExtra(ContactsContract.Intents.Insert.POSTAL, txtAddressView.text.toString())
        context.startActivity(intent)
    }

    private fun composeEmail() {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:" + txtMailView.text.toString())
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    }

    private fun copyToClipboard(text: CharSequence) {
        val clipboard = ContextCompat.getSystemService(context, ClipboardManager::class.java)
        val clip = ClipData.newPlainText("label", text)
        clipboard?.setPrimaryClip(clip)
        Toast.makeText(context, context.getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT).show()
    }

    private fun call() {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:" + txtPhoneView.text.toString())
        context.startActivity(intent)
    }
}
