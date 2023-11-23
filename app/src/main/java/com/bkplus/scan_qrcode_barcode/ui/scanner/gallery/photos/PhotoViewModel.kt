package com.bkplus.scan_qrcode_barcode.ui.scanner.gallery.photos

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bkplus.scan_qrcode_barcode.ui.scanner.gallery.Picture
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject

class PhotoViewModel @Inject constructor(): ViewModel() {
    private var imagesLiveData: MutableLiveData<ArrayList<Picture>> = MutableLiveData()

    @SuppressLint("Recycle")
    private fun getAllShownImagesPath(context: Context): ArrayList<Picture> {
        val cursor: Cursor?
        val listOfAllImages = ArrayList<Picture>()
        var absolutePathOfImage: String?
        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.MediaColumns.DATA,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        )
        cursor = context.contentResolver.query(
            uri, projection, null,
            null, null
        )
        if (cursor == null) {
            return listOfAllImages
        }
        val columnIndexData: Int = cursor.getColumnIndex(MediaStore.MediaColumns.DATA)

        var id = 0
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(columnIndexData)
            listOfAllImages.add(Picture(id++, null, absolutePathOfImage))
        }
        val result = arrayListOf<Picture>()
        result.addAll(listOfAllImages.asReversed())
        return result
    }

}