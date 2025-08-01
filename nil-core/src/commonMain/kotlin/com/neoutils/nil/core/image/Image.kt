package com.neoutils.nil.core.image

import com.neoutils.nil.core.model.Resize

expect fun resizeImage(data: ByteArray, resize: Resize): ByteArray
