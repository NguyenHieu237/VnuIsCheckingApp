package com.bkplus.scan_qrcode_barcode.ui.generator

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.model.GeneratorItem

class GeneratorAdapter (
    private val mLists: List<GeneratorItem>,
    private val onTapItemListener: ((GeneratorItem) -> Unit)? = null
) : RecyclerView.Adapter<GeneratorAdapter.ViewHolder>() {
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        // Your holder should contain and initialize a member variable
        // for any view that will be set as you render a row
        val textView: TextView = itemView.findViewById(R.id.text_gen_menu)
        val imageView: ImageView = itemView.findViewById(R.id.button_qr_create)
    }

    // ... constructor and member variables
    // Usually involves inflating a layout from XML and returning the holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val generatorView = inflater.inflate(R.layout.item_create, parent, false)
        // Return a new holder instance
        return ViewHolder(generatorView)
    }

    // Involves populating data into the item through holder
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get the data model based on position
        val item: GeneratorItem = mLists[position]

        // Set item views based on your views and data model
        val textView = viewHolder.textView
        val button = viewHolder.imageView

        /// Listener
        viewHolder.itemView.rootView.setOnClickListener {
            onTapItemListener?.invoke(item)
        }

        /// Bind value
        textView.setText(item.stringResourceId)
        button.setImageResource(item.imageResourceId)
    }

    // Returns the total count of items in the list
    override fun getItemCount(): Int {
        return mLists.size
    }
}