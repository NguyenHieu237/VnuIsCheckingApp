package com.example.snap_time_picker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.snap_time_picker.databinding.LayoutSnapTimePickerNumberItemBinding

class TimePickerAdapter : RecyclerView.Adapter<TimeNumberViewHolder>() {
    private var itemList: List<Int>? = null
    private var _positionSelected: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): TimeNumberViewHolder =
        TimeNumberViewHolder(
            LayoutSnapTimePickerNumberItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )

    override fun getItemCount(): Int = itemList?.size ?: 0

    override fun onBindViewHolder(holder: TimeNumberViewHolder, position: Int) {
        val item = itemList?.get(position)
        holder.setNumber(item?.toString()?.padStart(2, '0'), isSelected = _positionSelected == position)
    }

    fun updatePositionSelected(positionSelected: Int) {
        this._positionSelected = positionSelected
        notifyDataSetChanged()
    }

    fun setItemList(itemList: List<Int>?) {
        this.itemList = itemList
        notifyDataSetChanged()
    }

    fun getPositionByValue(value: Int): Int {
        itemList?.forEachIndexed { index, item ->
            if (value == item) {
                return index
            }
        }
        return -1
    }

    fun getValueByPosition(position: Int): Int {
        itemList?.forEachIndexed { index, item ->
            if (position == index) {
                return item
            }
        }
        return -1
    }
}