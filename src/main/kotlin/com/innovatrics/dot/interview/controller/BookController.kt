package com.innovatrics.dot.interview.controller

import com.innovatrics.dot.interview.dto.BookRequestDto
import com.innovatrics.dot.interview.dto.BookResponseDto
import com.innovatrics.dot.interview.service.BookService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid
import io.swagger.v3.oas.annotations.parameters.RequestBody as RequestBodyOperation

@RestController
@RequestMapping("/books")
@Tag(name = "Book Controller", description = "API for managing books")
class BookController(
    private val bookService: BookService,
) {
    @PostMapping
    @Operation(
        summary = "Save a new book",
        description = "Creates a new book and returns the saved book details",
        requestBody =
            RequestBodyOperation(
                description = "Book request data",
                required = true,
                content = [Content(schema = Schema(implementation = BookRequestDto::class))],
            ),
        responses = [
            ApiResponse(
                responseCode = "201",
                description = "Book created successfully",
                content = [Content(schema = Schema(implementation = BookResponseDto::class))],
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid input data",
                content = [Content()],
            ),
        ],
    )
    fun saveBook(
        @Valid @RequestBody bookRequest: BookRequestDto,
    ): ResponseEntity<BookResponseDto> {
        val bookResponse = bookService.saveBook(bookRequest)
        return ResponseEntity.status(HttpStatus.CREATED).body(bookResponse)
    }
}
