package com.bkplus.scan_qrcode_barcode.ui.qrcode.ManagerTab

import android.os.Bundle
import android.view.View
import androidx.fragment.app.ListFragment
import androidx.fragment.app.activityViewModels
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.base.BaseFragment
import com.bkplus.scan_qrcode_barcode.databinding.FragmentListEventBinding
import com.bkplus.scan_qrcode_barcode.ui.qrcode.ManagerTab.ViewModel.ManagerViewModel
import com.bkplus.scan_qrcode_barcode.ui.qrcode.history.StudentHistory.StudentHistoryFragment

class ListEventsFragment(): BaseFragment<FragmentListEventBinding>() {

    private val viewModel by activityViewModels<ManagerViewModel>()
    val adapter = ListEventAdapter()
    companion object {
        fun newInstance(): ListEventsFragment {
            val args = Bundle()
            val fragment = ListEventsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override val layoutId: Int
        get() = R.layout.fragment_list_event

    override fun setupUI() {
        super.setupUI()
        viewModel.getEventFromAPI()
        binding.toolbar.tvTitle.text = "List recent event"
        viewModel.eventsList.observe(viewLifecycleOwner) { events ->
            // Update the RecyclerView adapter with the new list of events
            if (events != null) {
                adapter.elist = events
            }else{
                binding.containerEmpty.visibility = View.VISIBLE
            }

        }
        binding.rcvEvent.adapter = adapter
        }
    }
