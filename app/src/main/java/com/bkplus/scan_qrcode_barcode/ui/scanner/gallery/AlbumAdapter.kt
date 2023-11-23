package com.bkplus.scan_qrcode_barcode.ui.scanner.gallery

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bkplus.scan_qrcode_barcode.R
import com.bkplus.scan_qrcode_barcode.databinding.ItemRvAlbumBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class AlbumAdapter(private val listener: OnItemClickListener): RecyclerView.Adapter<AlbumAdapter.AlbumViewHolder>() {
    private val items = ArrayList<Album>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Album>) {
        val diffUtilResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return items.size
            }

            override fun getNewListSize(): Int {
                return data.size
            }

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return items[oldItemPosition].id == data[newItemPosition].id
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return items[oldItemPosition].id == data[newItemPosition].id &&
                        items[oldItemPosition].name == data[newItemPosition].name &&
                        items[oldItemPosition].coverUri == data[newItemPosition].coverUri
            }
        })
        diffUtilResult.dispatchUpdatesTo(this)
        items.clear()
        items.addAll(data)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        return AlbumViewHolder(
            ItemRvAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }
    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val item : Album = items[position]
        holder.bind(item = item, position)
    }

    inner class AlbumViewHolder(binding:ItemRvAlbumBinding) : RecyclerView.ViewHolder(binding.root) {
        private val binding: ItemRvAlbumBinding

        init {
            this.binding = binding
        }
        fun bind(item: Album, position: Int) {
            Glide.with(binding.root).load(item.coverUri).override(400, 400).into(binding.image)
            binding.albumTitle.text= item.name
            binding.albumSize.text = item.albumPhotos?.size.toString()
            binding.root. setOnClickListener {
                listener.onAlbumClick(item)
            }
        }
    }
    interface OnItemClickListener {
        fun onAlbumClick(album: Album)
    }
}