package com.bkplus.scan_qrcode_barcode.ui.qrcode.history.StudentHistory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.Response.EventData

class StudentHistoryAdapter(): RecyclerView.Adapter<StudentHistoryAdapter.ViewHolder>() {

    var elist : List<EventData> = arrayListOf()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView = itemView.findViewById(R.id.tvTitleEvent)
        var tvTime: TextView = itemView.findViewById(R.id.tvTimeEvent)
        var tvOrganizor: TextView = itemView.findViewById(R.id.orginzerEvent)
        fun bind(eventData: EventData){
            tvOrganizor.text = eventData.organizer
            tvTitle.text = eventData.name
            tvTime.text = eventData.date

        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StudentHistoryAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val generatorView = inflater.inflate(R.layout.layout_event_item, parent, false)
        return ViewHolder(generatorView)
    }

    override fun onBindViewHolder(holder: StudentHistoryAdapter.ViewHolder, position: Int) {
        var item = elist[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
       return elist.count()
    }
}
