package com.bkplus.scan_qrcode_barcode.ui.first_language

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bkplus.scan_qrcode_barcode.common.DataBoundAdapter
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.databinding.ItemLanguageBinding
import com.bkplus.scan_qrcode_barcode.model.Language

class SettingLanguageAdapter(
    val context: Context, val listener: OnClickItemListener, val activity: Activity?
) : DataBoundAdapter<Language, ItemLanguageBinding>() {

    // use for change selected language
    private var previousSelectedPosition = 0

    interface OnClickItemListener {
        fun onClickItem(item: Language)
    }

    override fun createBinding(parent: ViewGroup): ItemLanguageBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(context), R.layout.item_language, parent, false
        )
    }

    override fun bind(binding: ItemLanguageBinding, item: Language, position: Int) {
        binding.root.setOnClickListener {
            listener.onClickItem(item)

            items[previousSelectedPosition].isSelected = false
            items[position].isSelected = true

            notifyItemChanged(position)
            notifyItemChanged(previousSelectedPosition)

            previousSelectedPosition = position
        }

        item.nameLanguageRes?.let { binding.languageNameTxt.setText(it) }
        item.imageRes?.let { binding.flagImg.setImageResource(it) }

        if (item.isSelected) {
            binding.checkbox.setImageResource(R.drawable.ic_radio_checked)
        } else {
            binding.checkbox.setImageResource(R.drawable.ic_radio_unchecked)
        }

        binding.containerMain.setBackgroundResource(R.drawable.bg_border_first_language)
        binding.languageNameTxt.setTextColor(Color.parseColor("#3A4049"))
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateAllData(newData: List<Language>) {
        items.clear()
        items.addAll(newData)
        notifyDataSetChanged()
    }
}
