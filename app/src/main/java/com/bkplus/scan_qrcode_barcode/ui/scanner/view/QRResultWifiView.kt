package com.bkplus.scan_qrcode_barcode.ui.scanner.view

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSpecifier
import android.os.Build
import android.os.PatternMatcher
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.base.BaseCustomViewLinearLayout
import com.bkplus.scan_qrcode_barcode.ui.scanner.ScanResultFragment


class QRResultWifiView : BaseCustomViewLinearLayout {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val txtWifiView: TextView by lazy {
        layout.findViewById(R.id.txtWifi)
    }
    private val txtTypeView: TextView by lazy {
        layout.findViewById(R.id.txtType)
    }
    private val txtPasswordView: TextView by lazy {
        layout.findViewById(R.id.txtPassword)
    }
    private val bottom: LinearLayout by lazy {
        layout.findViewById(R.id.bottom)
    }
    override val layoutId: Int
        get() = R.layout.layout_result_qr_wifi

    fun setContent(
        name: String?,
        type: String?,
        pass: String?,
    ) {
        txtWifiView.text = name
        txtTypeView.text = type
        txtPasswordView.text = pass
        init()

    }


    private fun init() {
        layout.findViewById<LinearLayout>(R.id.wifi).setOnClickListener {
            connect()
        }
        layout.findViewById<LinearLayout>(R.id.password).setOnClickListener {
            copyToClipboard(txtPasswordView.text.toString())
        }
        layout.findViewById<LinearLayout>(R.id.copy).setOnClickListener {
            copyToClipboard(
                txtWifiView.text.toString() +
                        txtTypeView.text.toString() +
                        txtPasswordView.text.toString()
            )
        }
        if (ScanResultFragment.isCreate) {
            bottom.visibility = View.GONE
        }
    }

    private fun connect() {
        val networkSSID = txtWifiView.text.toString()
        val networkPass = txtPasswordView.text.toString()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as
                        ConnectivityManager
            val specifier = WifiNetworkSpecifier.Builder()
                .setSsid(networkSSID)
                .setWpa2Passphrase(networkPass)
                .setSsidPattern(PatternMatcher(networkSSID, PatternMatcher.PATTERN_PREFIX))
                .build()
            val request = NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .setNetworkSpecifier(specifier)
                .build()
            val networkCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    showToast(context.getString(R.string.success))
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    showToast(context.getString(R.string.failure))
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    showToast(context.getString(R.string.out_of_range))
                }
            }
            connectivityManager.requestNetwork(request, networkCallback)
        } else {
            val wifiConfig = WifiConfiguration()
            wifiConfig.SSID = java.lang.String.format("\"%s\"", networkSSID)
            wifiConfig.preSharedKey = java.lang.String.format("\"%s\"", networkPass)
            val wifiManager = context.applicationContext
                .getSystemService(WIFI_SERVICE) as WifiManager
            if (!wifiManager.isWifiEnabled) wifiManager.isWifiEnabled = true
            val netId: Int = wifiManager.addNetwork(wifiConfig)
            wifiManager.disconnect()
            wifiManager.enableNetwork(netId, true)
            wifiManager.reconnect()
        }
    }

    private fun copyToClipboard(text: CharSequence) {
        val clipboard = ContextCompat.getSystemService(context, ClipboardManager::class.java)
        val clip = ClipData.newPlainText("label", text)
        clipboard?.setPrimaryClip(clip)
        Toast.makeText(context,context.getString(R.string.copied_to_clipboard),Toast.LENGTH_SHORT).show()
    }

    private fun showToast(s: String) {
        Toast.makeText(context.applicationContext, s, Toast.LENGTH_LONG).show()
    }
}

