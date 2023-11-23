package com.bkplus.scan_qrcode_barcode.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

/** Create a File for saving an image or video  */
@SuppressLint("SimpleDateFormat")
fun getOutputMediaFile(context: Context): File? {
    // To be safe, you should check that the SDCard is mounted
    // using Environment.getExternalStorageState() before doing this.
    val mediaStorageDir = File(
        Environment.getExternalStorageDirectory()
            .toString() + "/Android/data/"
                + context.packageName
                + "/Files"
    )

    // This location works best if you want the created images to be shared
    // between applications and persist after your app has been uninstalled.

    // Create the storage directory if it does not exist
    if (!mediaStorageDir.exists()) {
        if (!mediaStorageDir.mkdirs()) {
            return null
        }
    }
    // Create a media file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    var mediaFile: File? = null
    val mImageName = "MI_$timeStamp.jpg"
    mediaFile = File(mediaStorageDir.path + File.separator + mImageName)
    return mediaFile
}

fun storeImage(image: Bitmap, context: Context): String? {
    val pictureFile = getOutputMediaFile(context = context) ?: return null
    try {
        val fos = FileOutputStream(pictureFile)
        image.compress(Bitmap.CompressFormat.PNG, 100, fos)
        fos.close()
    } catch (e: FileNotFoundException) {
        Log.d("storeImage", "File not found: " + e.message)
    } catch (e: IOException) {
        Log.d("storeImage", "Error accessing file: " + e.message)
    }
    return pictureFile.absolutePath
}

fun createFileImageShare(image: Bitmap, context: Context): File {
    val icon: Bitmap = image
    val bytes = ByteArrayOutputStream()
    icon.compress(Bitmap.CompressFormat.PNG, 100, bytes)
    val f = File(context.filesDir,"image_share.png")
    try {
        if(f.exists()) f.delete()
        f.createNewFile()
        val fo = FileOutputStream(f)
        fo.write(bytes.toByteArray())
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return f
}

fun getBitmapFromPath(path: String): Bitmap? {
    val imgFile = File(path)

    if (imgFile.exists()) {
        return BitmapFactory.decodeFile(imgFile.absolutePath)
    }

    return null
}