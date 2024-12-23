package com.andreasmlbngaol.codegenerator.model

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.util.DisplayMetrics
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import kotlin.math.min

fun generateQRCodeBitmap(
    content: String,
    backgroundColor: Color = Color.White,
    contentColor: Color = Color.Black,
    size: Int = 720,
    logo: Bitmap? = null
): Bitmap {
    val qrCodeWriter = QRCodeWriter()
    val bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, size, size)
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)

    for (x in 0 until size) {
        for (y in 0 until size) {
            bitmap.setPixel(x, y, if (bitMatrix[x, y]) contentColor.toArgb() else backgroundColor.toArgb())
        }
    }

    logo?.let {
        val logoSize = size / 3 // Ukuran logo (20% dari ukuran QR code)
        val left = (size - logoSize) / 2
        val top = (size - logoSize) / 2
        val right = left + logoSize
        val bottom = top + logoSize
        Log.d("QRCodeWithLogo", "Logo size: $logoSize, Left: $left, Top: $top, Right: $right, Bottom: $bottom")

        // Menggambar logo di atas QR code menggunakan Canvas
        val canvas = Canvas(bitmap)

        // Mengubah ukuran logo sesuai ukuran yang diinginkan dan menggambar di tengah
        val logoScaled = Bitmap.createScaledBitmap(logo, logoSize, logoSize, false)
        canvas.drawBitmap(
            logoScaled,
            Rect(left, top, right, bottom),
            Rect(left, top, right, bottom),
            null
        )
    }
    return bitmap
}

fun generateBarcodeBitmap(
    content: String,
    backgroundColor: Color = Color.White,
    contentColor: Color = Color.Black,
    width: Int = 720,
): Bitmap {
    val height = width / 2
    val barcodeWriter = MultiFormatWriter()
    val bitMatrix: BitMatrix = barcodeWriter.encode(content, BarcodeFormat.CODE_128, width, height)
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

    for (x in 0 until width) {
        for (y in 0 until height) {
            bitmap.setPixel(x, y, if (bitMatrix[x, y]) contentColor.toArgb() else backgroundColor.toArgb())
        }
    }
    return bitmap
}

fun Application.getThinnestResolution(): Int {
    val displayMetrics = DisplayMetrics()
    val windowManager = this.getSystemService(Context.WINDOW_SERVICE) as android.view.WindowManager
    windowManager.defaultDisplay.getMetrics(displayMetrics)

    val width = displayMetrics.widthPixels
    val height = displayMetrics.heightPixels

    return min(width, height)
}
