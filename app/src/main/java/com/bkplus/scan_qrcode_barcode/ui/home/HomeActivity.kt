package com.bkplus.scan_qrcode_barcode.ui.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.base.BaseActivity
import com.bkplus.scan_qrcode_barcode.databinding.ActivityHomeBinding
import com.bkplus.scan_qrcode_barcode.extensions.applyEdgeToEdge
import com.google.android.gms.ads.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.text.Typography.dagger

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>() {
    private val permissionRequestCode = 200

    override val layoutId: Int
        get() = R.layout.activity_home

    override fun setupUI() {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        binding.root.applyEdgeToEdge()
    }

    @SuppressLint("MissingInflatedId")
    fun checkCameraPermission(action: () -> Unit) {
        var result1 = ContextCompat.checkSelfPermission(
            this, Manifest.permission.CAMERA
        )
        var result2 = ContextCompat.checkSelfPermission(
            this, Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (result1 != PackageManager.PERMISSION_GRANTED && result2 != PackageManager.PERMISSION_GRANTED) {
            val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_access_camera, null)
            val dialog = AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create()
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)

            dialogView.findViewById<ImageView>(R.id.btn_allow).setOnClickListener {
                dialog.dismiss()
                result1 = PackageManager.PERMISSION_GRANTED
                result2 = PackageManager.PERMISSION_GRANTED
                requestPermission()
            }

            dialogView.findViewById<TextView>(R.id.btn_cancel).setOnClickListener {
                dialog.dismiss()
                // Handle the deny action here, e.g., show a message or disable camera functionality
            }

            dialog.show()
        } else {
            requestPermission()
            // Permission already granted
            // You can start camera-related operations here
        }
    }

     fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            permissionRequestCode
        )
    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUI()
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action === MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

}


