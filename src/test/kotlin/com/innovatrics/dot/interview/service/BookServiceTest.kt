package com.innovatrics.dot.interview.service

import com.innovatrics.dot.interview.dto.BookRequestDto
import com.innovatrics.dot.interview.dto.SentenceResponseDto
import com.innovatrics.dot.interview.persistence.entity.Book
import com.innovatrics.dot.interview.persistence.repository.BookRepository
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

class BookServiceTest {
    private val bookRepository: BookRepository = mockk()
    private val sentenceService: SentenceService = mockk()
    private val bookService = BookService(bookRepository, sentenceService)

    @Test
    fun `save book with sentences should return expected result`() {
        val bookRequest =
            BookRequestDto(
                name = "Test Book",
                sentences = listOf("Test sentence"),
            )
        val savedBook = Book.create("Test Book")
        val savedSentence =
            SentenceResponseDto(id = "1", text = "Test sentence", wordCount = 2)

        every { bookRepository.save(any()) } returns savedBook
        every { sentenceService.save(any(), any()) } returns savedSentence

        val response = bookService.saveBook(bookRequest)

        assertSoftly(response) {
            id shouldBe savedBook.id
            name shouldBe "Test Book"
            sentences shouldHaveSize 1
            with(sentences[0]) {
                id shouldBe "1"
                text shouldBe "Test sentence"
                wordCount shouldBe 2
            }
        }
    }

    @Test
    fun `save book without sentences should should return expected result`() {
        val bookRequest =
            BookRequestDto(
                name = "Test Book",
                sentences = emptyList(),
            )
        val savedBook = Book.create("Test Book")

        every { bookRepository.save(any()) } returns savedBook

        val response = bookService.saveBook(bookRequest)

        assertSoftly(response) {
            id shouldBe savedBook.id
            name shouldBe "Test Book"
            sentences shouldHaveSize 0
        }
    }
}
