package com.innovatrics.dot.interview.dto

data class BookResponseDto(
    val id: String,
    val name: String,
    val sentences: List<SentenceResponseDto>,
)
