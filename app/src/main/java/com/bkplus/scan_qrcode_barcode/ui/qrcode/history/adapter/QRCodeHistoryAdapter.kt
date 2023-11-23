package com.bkplus.scan_qrcode_barcode.ui.qrcode.history.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.model.QRCodeHistoryItem
import com.bkplus.scan_qrcode_barcode.ui.qrcode.history.QRCodeHistoryFragment.Companion.isAllItemsSelected
import com.bkplus.scan_qrcode_barcode.ui.qrcode.widget.TimeUtils
import com.bkplus.scan_qrcode_barcode.ui.scanner.ScanResultViewModel

class QRCodeHistoryAdapter(
    private val activity: Activity,
    private val listener: QRCodeHistoryItemListener,
) : RecyclerView.Adapter<QRCodeHistoryAdapter.ViewHolder>() {
    private var mLists: List<QRCodeHistoryItem> = arrayListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setListItem(items: List<QRCodeHistoryItem>) {
        this.mLists = items
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rclListItem: RecyclerView = itemView.findViewById(R.id.rcvHistory)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val generatorView = inflater.inflate(R.layout.item_history, parent, false)
        return ViewHolder(generatorView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item: QRCodeHistoryItem = mLists[position]

        /// Bind data
        viewHolder.tvDate.text =
            TimeUtils.getTime(timeFormat = TimeUtils.TimeFormat.TimeFormat5, time = item.date)

        /// Bind data items
        val adapter = QRCodeHistoryChildAdapter(activity = activity, listener)
        viewHolder.rclListItem.adapter = adapter
        adapter.setListItem(items = item.items)
    }

    // Returns the total count of items in the list
    override fun getItemCount(): Int {
        return mLists.size
    }
}