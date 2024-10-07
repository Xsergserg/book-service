package com.innovatrics.dot.interview.controller

import com.innovatrics.dot.interview.dto.SentenceRequestDto
import com.innovatrics.dot.interview.dto.SentenceResponseDto
import com.innovatrics.dot.interview.service.SentenceService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import io.swagger.v3.oas.annotations.parameters.RequestBody as RequestBodyOperation

@RestController
@RequestMapping("/sentences")
@Tag(name = "Sentence Controller", description = "API for managing sentences")
class SentenceController(
    private val sentenceService: SentenceService,
) {
    @GetMapping("/{id}/wordCount")
    @Operation(
        summary = "Get word count by sentence ID",
        description = "Returns the word count of a sentence by its ID",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Word count retrieved successfully",
                content = [Content(schema = Schema(implementation = Int::class))],
            ),
            ApiResponse(
                responseCode = "404",
                description = "Sentence not found",
                content = [Content()],
            ),
        ],
    )
    fun getWordCountById(
        @PathVariable id: String,
    ): Int = sentenceService.getWordCountById(id)

    @GetMapping
    @Operation(
        summary = "Get all sentences",
        description = "Returns all sentences associated with a specific book ID",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Sentences retrieved successfully",
                content = [Content(schema = Schema(implementation = Map::class))],
            ),
        ],
    )
    fun findAll(): Map<String, List<SentenceResponseDto>> = sentenceService.findAll()

    @PostMapping
    @Operation(
        summary = "Save a new sentence",
        description = "Creates a new sentence and returns the saved sentence details",
        requestBody =
            RequestBodyOperation(
                description = "Sentence request data",
                required = true,
                content = [Content(schema = Schema(implementation = SentenceRequestDto::class))],
            ),
        responses = [
            ApiResponse(
                responseCode = "201",
                description = "Sentence created successfully",
                content = [Content(schema = Schema(implementation = SentenceResponseDto::class))],
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid input data",
                content = [Content()],
            ),
            ApiResponse(
                responseCode = "404",
                description = "Book with requested ID not found",
                content = [Content()],
            ),
        ],
    )
    fun saveSentence(
        @RequestBody @Validated sentenceRequest: SentenceRequestDto,
    ): ResponseEntity<SentenceResponseDto> {
        val sentence = sentenceService.save(sentenceRequest)
        return ResponseEntity.status(HttpStatus.CREATED).body(sentence)
    }
}
