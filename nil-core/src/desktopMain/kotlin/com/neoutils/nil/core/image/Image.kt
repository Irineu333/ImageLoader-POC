package com.neoutils.nil.core.image

import com.neoutils.nil.core.model.Resize
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

actual fun resizeImage(data: ByteArray, resize: Resize): ByteArray {
    val image = ImageIO.read(ByteArrayInputStream(data))
    val scaledImage = image.getScaledInstance(resize.width, resize.height, java.awt.Image.SCALE_SMOOTH)
    val bufferedImage = BufferedImage(resize.width, resize.height, BufferedImage.TYPE_INT_ARGB)

    val graphics = bufferedImage.createGraphics()
    graphics.drawImage(scaledImage, 0, 0, null)
    graphics.dispose()

    val outputStream = ByteArrayOutputStream()
    ImageIO.write(bufferedImage, "png", outputStream)
    return outputStream.toByteArray()
}
