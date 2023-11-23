package com.example.snap_time_picker

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.example.snap_time_picker.databinding.LayoutSnapTimePickerNumberItemBinding

class TimeNumberViewHolder(
    private val binding: LayoutSnapTimePickerNumberItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("ResourceAsColor")
    fun setNumber(number: String?, isSelected: Boolean = false) {
        binding.textViewNumber.text = number ?: "-"
        binding.textViewNumber.setTextColor(binding.root.resources.getColor(if (isSelected) R.color.colorFFFFFF else R.color.color626262))
    }
}
