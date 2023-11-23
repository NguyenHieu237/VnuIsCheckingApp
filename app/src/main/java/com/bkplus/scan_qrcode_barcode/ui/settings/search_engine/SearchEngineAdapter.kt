package com.bkplus.scan_qrcode_barcode.ui.settings.search_engine

import android.annotation.SuppressLint
import android.content.ClipData.Item
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.databinding.ItemSearchEngineBinding
import com.bkplus.scan_qrcode_barcode.preferences.QRCodePreferences.Companion.getPrefsInstance

class SearchEngineAdapter(
    private val slist: ArrayList<ItemSearchEngine>,
    val listener: OnClickItemListener
) : RecyclerView.Adapter<SearchEngineAdapter.ViewHolder>() {
    var mCheckPosition: Int = -1

    interface OnClickItemListener {
        fun onClickItem(item: ItemSearchEngine)
    }

    inner class ViewHolder(binding: ItemSearchEngineBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val binding: ItemSearchEngineBinding

        init {
            this.binding = binding
        }

        @SuppressLint("NotifyDataSetChanged")
        fun bind(item: ItemSearchEngine, position: Int) {
            item.let {
                binding.engineName.setText(it.name)
            }

            binding.root.setOnClickListener {
                listener.onClickItem(item)
                mCheckPosition = position
                getPrefsInstance().enginePosition = -1
                notifyDataSetChanged()
            }

            if (mCheckPosition == position) {
                getPrefsInstance().enginePosition = position
                binding.cbEngine.setImageResource(R.drawable.ic_choosen_engine)
            }

            if (position == getPrefsInstance().enginePosition) {
                binding.cbEngine.setImageResource(R.drawable.ic_choosen_engine)
            } else {
                binding.cbEngine.setImageResource(R.drawable.ic_unchoosen_engine)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val bindingItem: ItemSearchEngineBinding =
            DataBindingUtil.inflate(inflater, R.layout.item_search_engine, parent, false)

        return ViewHolder(bindingItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
         val item: ItemSearchEngine = slist[position]
         holder.bind(item = item, position)
    }

    override fun getItemCount(): Int {
        return slist.size
    }
}
