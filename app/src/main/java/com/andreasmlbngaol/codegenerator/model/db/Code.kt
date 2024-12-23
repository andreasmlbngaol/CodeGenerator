package com.andreasmlbngaol.codegenerator.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("code")
data class Code(
    @PrimaryKey(true) val id: Int = 0,
    val title: String,
    val createdAt: Long,
    val content: String,
    val qrCodeBitmap: ByteArray,
    val barCodeBitmap: ByteArray
)