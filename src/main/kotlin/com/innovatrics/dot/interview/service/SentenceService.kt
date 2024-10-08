package com.innovatrics.dot.interview.service

import com.innovatrics.dot.interview.dto.SentenceRequestDto
import com.innovatrics.dot.interview.dto.SentenceResponseDto
import com.innovatrics.dot.interview.exception.ItemNotFoundException
import com.innovatrics.dot.interview.persistence.entity.Sentence
import com.innovatrics.dot.interview.persistence.repository.BookRepository
import com.innovatrics.dot.interview.persistence.repository.SentenceRepository
import org.springframework.stereotype.Service

@Service
class SentenceService(
    private val sentenceRepository: SentenceRepository,
    private val bookRepository: BookRepository,
) {
    fun getWordCountById(id: String) =
        sentenceRepository.findWordCountById(id) ?: throw ItemNotFoundException("Sentence with id '$id' not found")

    fun findAll(): Map<String, List<SentenceResponseDto>> =
        sentenceRepository
            .findAll()
            .groupBy { it.book.id }
            .mapValues { it.value.map { sentence -> sentence.toDto() } }

    fun save(
        text: String,
        bookId: String,
    ): SentenceResponseDto {
        val book =
            bookRepository
                .findById(bookId)
                .orElseThrow { ItemNotFoundException("Book with id '$bookId' not found") }
        val entity = sentenceRepository.save(Sentence.create(book, text))
        return entity.toDto()
    }

    fun save(sentence: SentenceRequestDto) = save(sentence.text, sentence.bookId)
}
