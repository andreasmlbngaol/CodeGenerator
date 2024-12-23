package com.andreasmlbngaol.codegenerator.model.db

import android.graphics.Bitmap

data class CodeState(
    val codes: List<Code> = emptyList(),
    val title: String = "",
    val content: String = "",
    val isAddingCode: Boolean = false,
    val sortType: SortType = SortType.Date,
    val qrCode: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888),
    val barCode: Bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888),
    val searchQuery: String = ""
)