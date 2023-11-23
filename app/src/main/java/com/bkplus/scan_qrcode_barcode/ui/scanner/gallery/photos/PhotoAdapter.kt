package com.bkplus.scan_qrcode_barcode.ui.scanner.gallery.photos

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bkplus.scan_qrcode_barcode.databinding.ItemRvPhotoBinding
import com.bkplus.scan_qrcode_barcode.ui.scanner.gallery.Picture
import com.bumptech.glide.Glide
import java.io.File

class PhotoAdapter
    (private val listener: OnItemClickListener) :
    RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    private val items = ArrayList<Picture>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(
            ItemRvPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val item : Picture = items[position]
        holder.bind(item = item, position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Picture>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }


    inner class PhotoViewHolder(private val binding: ItemRvPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Picture, position: Int) {
            Glide.with(binding.root).load(item.photoUri).override(400, 400).into(binding.image)
            binding.image.setOnClickListener {
                listener.onPhotoClick(Uri.fromFile(item.photoUri?.let { it1 -> File(it1) }))
            }
        }
    }

    interface OnItemClickListener {
        fun onPhotoClick(uri: Uri)
    }
}
