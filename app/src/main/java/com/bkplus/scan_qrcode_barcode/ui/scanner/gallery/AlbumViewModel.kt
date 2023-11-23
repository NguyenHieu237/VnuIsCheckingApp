package com.bkplus.scan_qrcode_barcode.ui.scanner.gallery

import android.content.Context
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


class AlbumViewModel @Inject constructor() : ViewModel() {
    private val successGetAllAlbum = MutableLiveData<List<Album>>()
    fun getSuccessGetAllAlbum(): MutableLiveData<List<Album>> {
        return successGetAllAlbum
    }


    fun getPhoneAlbums(context: Context) {
        viewModelScope.launch {


            // Creating vectors to hold the final albums objects and albums names
            val phoneAlbums: Vector<Album> = Vector<Album>()
            val albumsNames = Vector<String>()

            // which image properties are we querying
            val projection = arrayOf(
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID
            )

            // content: style URI for the "primary" external storage volume
            val images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

            // Make the query.
            val cur = context.contentResolver.query(
                images,
                projection,  // Which columns to return
                null,  // Which rows to return (all rows)
                null,  // Selection arguments (none)
                null // Ordering
            )
            if (cur != null && cur.count > 0) {
                Log.i("DeviceImageManager", " query count=" + cur.count)
                if (cur.moveToFirst()) {
                    var bucketName: String?
                    var data: String?
                    var imageId: String?
                    val bucketNameColumn = cur.getColumnIndex(
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME
                    )
                    val imageUriColumn = cur.getColumnIndex(
                        MediaStore.Images.Media.DATA
                    )
                    val imageIdColumn = cur.getColumnIndex(
                        MediaStore.Images.Media._ID
                    )
                    do {
                        // Get the field values
                        bucketName = cur.getString(bucketNameColumn)
                        data = cur.getString(imageUriColumn)
                        imageId = cur.getString(imageIdColumn)

                        // Adding a new PhonePhoto object to phonePhotos vector
                        val phonePhoto = Picture()
                        phonePhoto.albumName = bucketName
                        phonePhoto.photoUri = data
                        imageId?.let {
                            phonePhoto.id = Integer.valueOf(it)
                        }
                        if (albumsNames.contains(bucketName)) {
                            for (album in phoneAlbums) {
                                if (album.name.equals(bucketName)) {
                                    album.albumPhotos?.add(phonePhoto)
                                    Log.i("DeviceImageManager", "A photo was added to album => $bucketName")
                                    break
                                }
                            }
                        } else {
                            val album = Album()
                            Log.i("DeviceImageManager", "A new album was created => $bucketName")
                            album.id = phonePhoto.id
                            album.name = bucketName
                            album.coverUri = phonePhoto.photoUri
                            album.albumPhotos?.add(phonePhoto)
                            Log.i("DeviceImageManager", "A photo was added to album => $bucketName")
                            phoneAlbums.add(album)
                            albumsNames.add(bucketName)
                        }
                    } while (cur.moveToNext())
                }
                cur.close()
                for (album in phoneAlbums) {
                    album.albumPhotos?.reverse()
                    album.coverUri = album.albumPhotos?.get(0)?.photoUri
                }
                successGetAllAlbum.postValue(phoneAlbums)
            }
        }
    }
}
