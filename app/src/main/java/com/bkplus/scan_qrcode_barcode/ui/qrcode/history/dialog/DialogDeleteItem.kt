package com.bkplus.scan_qrcode_barcode.ui.qrcode.history.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.databinding.PopupDeleteItemBinding
import com.bkplus.scan_qrcode_barcode.manager.database.QRCodeDAO
import com.bkplus.scan_qrcode_barcode.manager.database.QRCodeTable
import com.bkplus.scan_qrcode_barcode.manager.qrcode.GenerateQRCodeResult
import com.bkplus.scan_qrcode_barcode.model.QRCodeHistoryItemChild
import com.bkplus.scan_qrcode_barcode.ui.qrcode.history.QRCodeHistoryViewModel

class DialogDeleteItem : DialogFragment() {
    private lateinit var binding: PopupDeleteItemBinding
    private val historyViewModel by activityViewModels<QRCodeHistoryViewModel>()
    var item: QRCodeHistoryItemChild? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PopupDeleteItemBinding.inflate(inflater, container, false)
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
                item?.itemData?.getId()?.let { it1 -> QRCodeDAO.instance.deleteItem(it1) }
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
