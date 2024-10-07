package com.innovatrics.dot.interview.persistence.repository

import com.innovatrics.dot.interview.persistence.entity.Book
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class BookRepositoryTest
    @Autowired
    constructor(
        private val bookRepository: BookRepository,
    ) {
        val book = Book.create("Atlas Shrugged")

        @AfterEach
        fun tearDown() {
            bookRepository.deleteAll()
        }

        @Test
        fun `save method works as expected`() {
            val savedBook = bookRepository.save(book)

            assertSoftly(bookRepository.findById(savedBook.id).get()) {
                id shouldBe savedBook.id
                name shouldBe "Atlas Shrugged"
                sentences.shouldBeEmpty()
            }
        }
    }
