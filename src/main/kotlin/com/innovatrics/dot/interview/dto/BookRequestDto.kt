package com.innovatrics.dot.interview.dto

import javax.validation.constraints.NotBlank

data class BookRequestDto(
    @field:NotBlank val name: String,
    val sentences: List<String>? = emptyList(),
)
