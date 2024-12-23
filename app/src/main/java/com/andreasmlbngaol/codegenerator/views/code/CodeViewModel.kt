package com.andreasmlbngaol.codegenerator.views.code

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreasmlbngaol.codegenerator.model.db.Code
import com.andreasmlbngaol.codegenerator.model.db.CodeDao
import com.andreasmlbngaol.codegenerator.model.db.CodeEvent
import com.andreasmlbngaol.codegenerator.model.db.CodeState
import com.andreasmlbngaol.codegenerator.model.db.SortType
import com.andreasmlbngaol.codegenerator.model.generateBarcodeBitmap
import com.andreasmlbngaol.codegenerator.model.generateQRCodeBitmap
import com.andreasmlbngaol.codegenerator.model.getThinnestResolution
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class CodeViewModel @Inject constructor(
    private val dao: CodeDao,
    private val application: Application,
    private val converters: Converters
): ViewModel() {
    private val _sortType = MutableStateFlow(SortType.Date)

    private val _codes = _sortType
        .flatMapLatest { sortType ->
            when(sortType) {
                SortType.Date -> dao.getCodeSortedByDate()
                SortType.Title -> dao.getCodeSortedByTitle()
                SortType.Content -> dao.getCodeSortedByContent()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _state = MutableStateFlow(CodeState())
    val state = combine(_state, _sortType, _codes) { state, sortType, codes ->
        state.copy(
            codes = codes,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CodeState())

    fun onEvent(event: CodeEvent) {
        when(event) {
            is CodeEvent.DeleteCode -> {
                viewModelScope.launch {
                    dao.deleteCode(event.code)
                }
            }

            CodeEvent.HideDialog -> {
                _state.update {
                    it.copy(
                        isAddingCode = false
                    )
                }
            }

            CodeEvent.SaveCode -> {
                val title = state.value.title.trim()
                val content = state.value.content.trim()
                val createdAt = System.currentTimeMillis()
                if(title.isBlank() || content.isBlank()) {
                    return
                }
                viewModelScope.launch {
                    val qrCodeBitmap = generateQRCodeBitmap(
                        content,
                        size = application.getThinnestResolution()
                    )
                    val barCodeBitmap = generateBarcodeBitmap(
                        content,
                        width = application.getThinnestResolution()
                    )
                    val code = Code(
                        title = title,
                        content = content,
                        createdAt = createdAt,
                        qrCodeBitmap = converters.fromBitmap(qrCodeBitmap),
                        barCodeBitmap = converters.fromBitmap(barCodeBitmap)
                    )
                    dao.upsertCode(code)
                }
                _state.update {
                    it.copy(
                        isAddingCode = false,
                        title = "",
                        content = ""
                    )
                }
            }
            is CodeEvent.SearchCode -> {
                _state.update {
                    it.copy(
                        searchQuery = event.query
                    )
                }
            }
            is CodeEvent.SetContent -> {
                _state.update {
                    it.copy(
                        content = event.content
                    )
                }
            }
            is CodeEvent.SetTitle -> {
                _state.update {
                    it.copy(
                        title = event.title
                    )
                }
            }
            CodeEvent.ShowDialog -> {
                _state.update {
                    it.copy(
                        isAddingCode = true
                    )
                }
            }
            is CodeEvent.SortCodes -> {
                _sortType.value = event.sortType
            }

            is CodeEvent.ShowQrCode -> {
                _state.update {
                    it.copy(
                        title = event.code.title,
                        content = event.code.content,
                        qrCode = converters.toBitmap(event.code.qrCodeBitmap)
                    )
                }
            }
            CodeEvent.HideQrCode -> {
                _state.update {
                    it.copy(
                        title = "",
                        qrCode = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888),
                        content = ""
                    )
                }
            }
            is CodeEvent.ShowBarCode -> {
                _state.update {
                    it.copy(
                        title = event.code.title,
                        content = event.code.content,
                        barCode = converters.toBitmap(event.code.barCodeBitmap)
                    )
                }
            }
            CodeEvent.HideBarCode -> {
                _state.update {
                    it.copy(
                        title = "",
                        barCode = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888),
                        content = ""
                    )
                }
            }
        }
    }
}