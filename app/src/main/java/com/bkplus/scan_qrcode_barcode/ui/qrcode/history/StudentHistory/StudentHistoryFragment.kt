package com.bkplus.scan_qrcode_barcode.ui.qrcode.history.StudentHistory

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.base.BaseFragment
import com.bkplus.scan_qrcode_barcode.databinding.FragmentStudentHistotyBinding
import com.bkplus.scan_qrcode_barcode.ui.Login.LoginViewModel
import com.bkplus.scan_qrcode_barcode.ui.qrcode.history.QRCodeHistoryFragment
import com.bkplus.scan_qrcode_barcode.ui.scanner.ScanResultViewModel
import kotlin.math.log

class StudentHistoryFragment() : BaseFragment<FragmentStudentHistotyBinding>() {

    var historyAdapter = StudentHistoryAdapter()
    private val viewModel by activityViewModels<ScanResultViewModel>()
    private val loginViewModel by activityViewModels<LoginViewModel>()

    companion object {
        fun newInstance(): StudentHistoryFragment {
            val args = Bundle()
            val fragment = StudentHistoryFragment()
            fragment.arguments = args
            return fragment
        }
    }
    override val layoutId: Int
        get() = R.layout.fragment_student_histoty

    override fun setupUI() {
        super.setupUI()
        binding.toolbar.tvTitle.text = "Student's history"
        loginViewModel.eventsList.observe(viewLifecycleOwner) { events ->
            // Update the RecyclerView adapter with the new list of events
            if (events != null) {
                historyAdapter.elist = events
            }else{
                binding.containerEmpty.visibility = View.VISIBLE
            }

        }
        binding.rcvHistory.adapter = historyAdapter
        viewModel.checkEventResponse.observe(viewLifecycleOwner) { checkEventResponse ->
            var code = checkEventResponse.status
            if (code == 200){
                historyAdapter.notifyDataSetChanged()
            }

        }

    }


}