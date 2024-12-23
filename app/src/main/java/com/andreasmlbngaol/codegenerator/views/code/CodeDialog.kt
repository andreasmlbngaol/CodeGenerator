package com.andreasmlbngaol.codegenerator.views.code

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.andreasmlbngaol.codegenerator.model.db.CodeState
import java.io.OutputStream

fun saveBitmapToGallery(context: Context, bitmap: Bitmap, title: String) {
    val fileNameWithExtension = "$title.png"
    val outputStream: OutputStream?

    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, fileNameWithExtension)
        put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/MyApp")
    }
    val imageUri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    outputStream = imageUri?.let { context.contentResolver.openOutputStream(it) }

    try {
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream!!) // Simpan bitmap sebagai PNG
        outputStream.flush()
        outputStream.close()
        Toast.makeText(context, "$title berhasil disimpan ke galeri", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        e.printStackTrace()
    }

}

@Composable
fun QrCodeDialog(
    onDismiss: () -> Unit,
    state: CodeState,
) {
    val context = LocalContext.current

    Dialog(
        onDismissRequest = onDismiss,
    ) {
        val backgroundColor = MaterialTheme.colorScheme.background
        val contentColor = contentColorFor(backgroundColor)
        Column {
            Column(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.large)
                    .background(backgroundColor)
                    .padding(vertical = 8.dp)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    bitmap = state.qrCode.asImageBitmap(),
                    contentDescription = "QR Code"
                )
                Text(
                    state.content,
                    color = contentColor,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(Modifier.height(24.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    saveBitmapToGallery(
                        context,
                        state.qrCode,
                        state.title
                    )
                }
            ) {
                Text("Save")
            }
        }
    }
}

@Composable
fun BarCodeDialog(
    onDismiss: () -> Unit,
    state: CodeState,
) {
    Dialog(
        onDismissRequest = onDismiss,
    ) {
        val backgroundColor = MaterialTheme.colorScheme.background
        val contentColor = contentColorFor(backgroundColor)
        Column(
            modifier = Modifier
                .clip(MaterialTheme.shapes.large)
                .background(backgroundColor)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                bitmap = state.barCode.asImageBitmap(),
                contentDescription = "Bar Code"
            )
            Text(
                state.content,
                textAlign = TextAlign.Center,
                color = contentColor,
                letterSpacing = 2.sp
            )
        }
    }
}