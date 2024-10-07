package com.innovatrics.dot.interview.persistence.repository

import com.innovatrics.dot.interview.persistence.entity.Book
import com.innovatrics.dot.interview.persistence.entity.Sentence
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.inspectors.shouldForAtLeastOne
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class SentenceRepositoryTest
    @Autowired
    constructor(
        private val sentenceRepository: SentenceRepository,
        private val bookRepository: BookRepository,
    ) {
        val book = Book.create("Atlas Shrugged")

        @AfterEach
        fun tearDown() {
            bookRepository.deleteAll()
            sentenceRepository.deleteAll()
        }

        @Nested
        inner class GetAllByBookId {
            @Test
            fun `getAllByBookId, save and SaveAll methods work as expected`() {
                val savedBook = bookRepository.save(book)
                val anotherBook = bookRepository.save(Book.create("East of Eden"))
                sentenceRepository.saveAll(
                    listOf(
                        getSentence1(savedBook),
                        getSentence2(savedBook),
                    ),
                )
                sentenceRepository.save(
                    getSentence3(anotherBook),
                )

                assertSoftly {
                    with(sentenceRepository.getAllByBookId(savedBook.id)) {
                        shouldHaveSize(2)
                        shouldForAtLeastOne {
                            it.value shouldBe "Who is John Galt?"
                            it.wordCount shouldBe 4
                        }
                        shouldForAtLeastOne {
                            it.value shouldBe "If you don't know, the thing to do is not to get scared, but to learn."
                            it.wordCount shouldBe 16
                        }
                    }
                    with(sentenceRepository.getAllByBookId(anotherBook.id)) {
                        shouldHaveSize(1)
                        first().value shouldBe "All great and precious things are lonely."
                        first().wordCount shouldBe 7
                    }
                }
            }

            @Test
            fun `getAllByBookId method returns empty list when no sentences are found`() {
                val savedBook = bookRepository.save(book)

                sentenceRepository.getAllByBookId(savedBook.id) shouldBe emptyList()
            }

            @Test
            fun `getAllByBookId method returns empty list when book does not exist`() {
                sentenceRepository.getAllByBookId("non-existing-id") shouldBe emptyList()
            }

            @Test
            fun `save method with not existing book id throws exception`() {
                val sentence = getSentence1(Book.create("non-existing-book"))

                shouldThrow<JpaObjectRetrievalFailureException> {
                    sentenceRepository.save(sentence) shouldBe sentence
                }
            }
        }

        @Nested
        inner class FindWordCountById {
            @Test
            fun `findWordCountById method works as expected`() {
                val savedBook = bookRepository.save(book)
                val sentence = getSentence1(savedBook)
                val savedSentence = sentenceRepository.save(sentence)

                sentenceRepository.findWordCountById(savedSentence.id) shouldBe 4
            }

            @Test
            fun `findWordCountById method returns null when sentence does not exist`() {
                sentenceRepository.findWordCountById("non-existing-id").shouldBeNull()
            }
        }

        private fun getSentence1(book: Book) =
            Sentence(
                book = book,
                value = "Who is John Galt?",
                wordCount = 4,
            )

        private fun getSentence2(book: Book) =
            Sentence(
                book = book,
                value = "If you don't know, the thing to do is not to get scared, but to learn.",
                wordCount = 16,
            )

        private fun getSentence3(book: Book) =
            Sentence(
                book = book,
                value = "All great and precious things are lonely.",
                wordCount = 7,
            )
    }
