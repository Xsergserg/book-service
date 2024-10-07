package com.innovatrics.dot.interview.service

import com.innovatrics.dot.interview.dto.BookRequestDto
import com.innovatrics.dot.interview.dto.BookResponseDto
import com.innovatrics.dot.interview.persistence.entity.Book
import com.innovatrics.dot.interview.persistence.repository.BookRepository
import org.springframework.stereotype.Service

@Service
class BookService(
    private val bookRepository: BookRepository,
    private val sentenceService: SentenceService,
) {
    fun saveBook(bookRequest: BookRequestDto): BookResponseDto {
        val book = bookRepository.save(Book.create(bookRequest.name))
        val sentences = bookRequest.sentences?.map { sentenceService.save(it, book.id) } ?: emptyList()
        return BookResponseDto(
            id = book.id,
            name = book.name,
            sentences = sentences,
        )
    }
}
