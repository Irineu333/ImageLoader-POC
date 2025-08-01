package com.neoutils.nil.core.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.neoutils.nil.core.model.Resize
import java.io.ByteArrayOutputStream

actual fun resizeImage(data: ByteArray, resize: Resize): ByteArray {
    val options = BitmapFactory.Options().apply {
        inJustDecodeBounds = true
    }
    BitmapFactory.decodeByteArray(data, 0, data.size, options)

    options.inSampleSize = calculateInSampleSize(options, resize.width, resize.height)
    options.inJustDecodeBounds = false

    val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size, options)
    val scaledBitmap = Bitmap.createScaledBitmap(bitmap, resize.width, resize.height, true)

    val outputStream = ByteArrayOutputStream()
    scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    return outputStream.toByteArray()
}

private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    val (height: Int, width: Int) = options.outHeight to options.outWidth
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {
        val halfHeight: Int = height / 2
        val halfWidth: Int = width / 2

        while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
            inSampleSize *= 2
        }
    }

    return inSampleSize
}
