package com.andreasmlbngaol.codegenerator.model.db

sealed interface CodeEvent {
    data object SaveCode: CodeEvent
    data class SetTitle(val title: String): CodeEvent
    data class SetContent(val content: String): CodeEvent
    data object ShowDialog: CodeEvent
    data object HideDialog: CodeEvent
    data class SortCodes(val sortType: SortType): CodeEvent
    data class SearchCode(val query: String): CodeEvent
    data class ShowQrCode(val code: Code): CodeEvent
    data object HideQrCode: CodeEvent
    data class ShowBarCode(val code: Code): CodeEvent
    data object HideBarCode: CodeEvent
    data class DeleteCode(val code: Code): CodeEvent
}