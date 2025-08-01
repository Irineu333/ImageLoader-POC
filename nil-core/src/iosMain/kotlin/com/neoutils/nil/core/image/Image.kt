package com.neoutils.nil.core.image

import com.neoutils.nil.core.model.Resize
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.CoreGraphics.CGBitmapContextCreate
import platform.CoreGraphics.CGColorSpaceCreateDeviceRGB
import platform.CoreGraphics.CGContextDrawImage
import platform.CoreGraphics.CGContextRelease
import platform.CoreGraphics.CGRectMake
import platform.CoreGraphics.CGImageAlphaInfo
import platform.CoreGraphics.CGImageRef
import platform.Foundation.NSData
import platform.Foundation.dataWithBytes
import platform.UIKit.UIImage
import platform.UIKit.UIImagePNGRepresentation

actual fun resizeImage(data: ByteArray, resize: Resize): ByteArray {
    val image = UIImage(data = data.toNSData())
    val newRect = CGRectMake(0.0, 0.0, resize.width.toDouble(), resize.height.toDouble())
    val imageRef = image.CGImage
    val colorSpace = CGColorSpaceCreateDeviceRGB()
    val context = CGBitmapContextCreate(
        null,
        resize.width.toULong(),
        resize.height.toULong(),
        8.toULong(),
        0.toULong(),
        colorSpace,
        CGImageAlphaInfo.kCGImageAlphaPremultipliedLast.value
    )
    CGContextDrawImage(context, newRect, imageRef)
    val resizedImageRef = CGBitmapContextCreateImage(context)
    val resizedImage = resizedImageRef?.let { UIImage(it) }
    CGContextRelease(context)
    return resizedImage?.let { UIImagePNGRepresentation(it)?.toByteArray() } ?: data
}

private fun ByteArray.toNSData(): NSData {
    return this.usePinned {
        NSData.dataWithBytes(it.addressOf(0), this.size.toULong())
    }
}

private fun NSData.toByteArray(): ByteArray {
    return this.bytes?.readBytes(this.length.toInt()) ?: byteArrayOf()
}
