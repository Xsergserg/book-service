package com.innovatrics.dot.interview.service

import com.innovatrics.dot.interview.dto.SentenceResponseDto
import com.innovatrics.dot.interview.exception.ItemNotFoundException
import com.innovatrics.dot.interview.persistence.entity.Book
import com.innovatrics.dot.interview.persistence.entity.Sentence
import com.innovatrics.dot.interview.persistence.repository.BookRepository
import com.innovatrics.dot.interview.persistence.repository.SentenceRepository
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.maps.shouldContainExactly
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.Optional

class SentenceServiceTest {
    private val sentenceRepository: SentenceRepository = mockk()
    private val bookRepository: BookRepository = mockk()
    private val sentenceService = SentenceService(sentenceRepository, bookRepository)

    @Nested
    inner class Save {
        @Test
        fun `save sentence should return expected result`() {
            val text = "Test sentence"
            val book = Book.create("Test Book")
            val savedSentence = Sentence(book, text, 2)

            every { bookRepository.findById(book.id) } returns Optional.of(book)
            every { sentenceRepository.save(any()) } returns savedSentence

            val response = sentenceService.save(text, book.id)

            assertSoftly(response) {
                id shouldBe savedSentence.id
                text shouldBe "Test sentence"
                wordCount shouldBe 2
            }
        }

        @Test
        fun `save sentence should throw ItemNotFoundException when no book with requested id`() {
            every { bookRepository.findById(any()) } returns Optional.empty()

            shouldThrow<ItemNotFoundException> {
                sentenceService.save("Test sentence", "1")
            }.run {
                message shouldBe "Book with id '1' not found"
            }
        }
    }

    @Nested
    inner class GetWordCountById {
        @Test
        fun `get word count by id should return expected result`() {
            every { sentenceRepository.findWordCountById(any()) } returns 2

            sentenceService.getWordCountById("1") shouldBe 2
        }

        @Test
        fun `get word count by id should throw ItemNotFoundException when no sentence with requested id`() {
            every { sentenceRepository.findWordCountById(any()) } returns null

            shouldThrow<ItemNotFoundException> {
                sentenceService.getWordCountById("1")
            }.run {
                message shouldBe "Sentence with id '1' not found"
            }
        }
    }

    @Nested
    inner class FindAll {
        @Test
        fun `find all sentences should return expected result`() {
            val book1 = Book.create("Test Book 1")
            val book2 = Book.create("Test Book 2")
            val sentence1 = Sentence(book1, "Test sentence 1", 2)
            val sentence2 = Sentence(book2, "Test sentence 2", 3)

            every { sentenceRepository.findAll() } returns listOf(sentence1, sentence2)

            val response = sentenceService.findAll()

            response.shouldContainExactly(
                mapOf(
                    book1.id to listOf(SentenceResponseDto(sentence1.id, "Test sentence 1", sentence1.wordCount)),
                    book2.id to listOf(SentenceResponseDto(sentence2.id, "Test sentence 2", sentence2.wordCount)),
                ),
            )
        }
    }
}
