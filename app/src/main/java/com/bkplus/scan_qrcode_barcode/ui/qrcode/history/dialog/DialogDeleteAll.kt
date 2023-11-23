package com.bkplus.scan_qrcode_barcode.ui.qrcode.history.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.databinding.PopupDeleteAllBinding
import com.bkplus.scan_qrcode_barcode.databinding.PopupDeleteItemBinding
import com.bkplus.scan_qrcode_barcode.manager.database.QRCodeDAO
import com.bkplus.scan_qrcode_barcode.ui.qrcode.history.QRCodeHistoryViewModel
import com.bkplus.scan_qrcode_barcode.ui.qrcode.history.adapter.QRCodeHistoryAdapter
import com.bkplus.scan_qrcode_barcode.ui.qrcode.history.adapter.QRCodeHistoryChildAdapter
import io.realm.Realm

class DialogDeleteAll :DialogFragment() {
    private lateinit var binding: PopupDeleteAllBinding
    private val historyViewModel by activityViewModels<QRCodeHistoryViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PopupDeleteAllBinding.inflate(inflater, container, false)
        initiateView()
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (dialog != null) {
            dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        dialog?.setCanceledOnTouchOutside(true)

    }
    private fun initiateView() {
        activity?.let {
            it.window.statusBarColor = ContextCompat.getColor(it, R.color.white)
        }
        binding.apply {
            btnDelete.setOnClickListener {
                QRCodeDAO.instance.deleteAll()
                historyViewModel.startFetchData()
                dialog?.dismiss()
            }

            btnCancel.setOnClickListener {
                dialog?.dismiss()
            }
        }
    }
    override fun onResume() {
        super.onResume()
        val params = dialog!!.window?.attributes
        if (params != null) {
            params.width = RecyclerView.LayoutParams.MATCH_PARENT
            params.height = RecyclerView.LayoutParams.WRAP_CONTENT
            params.gravity = Gravity.CENTER
            dialog?.window?.attributes = params
        }
    }
}
