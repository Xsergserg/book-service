package com.innovatrics.dot.interview.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class SentenceResponseDto(
    val id: String,
    val text: String,
    @JsonProperty("word_count")
    val wordCount: Int,
)
