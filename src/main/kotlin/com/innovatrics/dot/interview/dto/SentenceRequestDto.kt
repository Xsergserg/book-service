package com.innovatrics.dot.interview.dto

import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotBlank

data class SentenceRequestDto(
    @JsonProperty("book_id")
    @field:NotBlank val bookId: String,
    val text: String,
)
