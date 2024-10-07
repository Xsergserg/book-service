package com.innovatrics.dot.interview.exception

import com.innovatrics.dot.interview.logger.getLogger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {
    private val log = getLogger()

    @ExceptionHandler(ItemNotFoundException::class)
    fun handleItemNotFoundException(ex: ItemNotFoundException): ResponseEntity<String> =
        ResponseEntity(
            ex.message,
            HttpStatus.NOT_FOUND,
        )

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<List<String>> {
        val errors =
            ex.bindingResult.allErrors.map {
                "Field '${(it as FieldError).field}' ${it.defaultMessage ?: "Invalid value"}"
            }
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(errors)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleInvalidJson(): ResponseEntity<String> =
        ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body("Invalid JSON format")

    @ExceptionHandler(Exception::class)
    fun handleGlobalException(ex: Exception): ResponseEntity<String> {
        log.error("An unexpected error occurred", ex)
        return ResponseEntity("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
