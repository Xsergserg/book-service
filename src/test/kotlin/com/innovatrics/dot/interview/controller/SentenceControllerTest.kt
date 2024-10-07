package com.innovatrics.dot.interview.controller

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.innovatrics.dot.interview.dto.SentenceRequestDto
import com.innovatrics.dot.interview.dto.SentenceResponseDto
import com.innovatrics.dot.interview.helper.clearAll
import com.innovatrics.dot.interview.persistence.entity.Book
import com.innovatrics.dot.interview.persistence.entity.Sentence
import com.innovatrics.dot.interview.persistence.repository.BookRepository
import com.innovatrics.dot.interview.persistence.repository.SentenceRepository
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.maps.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cache.CacheManager
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class SentenceControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var bookRepository: BookRepository

    @Autowired
    lateinit var sentenceRepository: SentenceRepository

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var cacheManager: CacheManager

    @AfterEach
    fun tearDown() {
        sentenceRepository.deleteAll()
        bookRepository.deleteAll()
        cacheManager.clearAll()
    }

    @Nested
    inner class GetWordCountById {
        @Test
        fun `getWordCountById should return correct response when request is correct`() {
            val book = bookRepository.save(Book.create("Atlas Shrugged"))
            val sentence = sentenceRepository.save(Sentence.create(text = "Who is John Galt?", book = book))

            mockMvc
                .perform(
                    get("/sentences/${sentence.id}/wordCount"),
                ).andExpect(status().isOk)
                .andExpect(jsonPath("$").value(4))
        }

        @Test
        fun `getWordCountById should return 404 when sentence does not exist`() {
            mockMvc
                .perform(get("/sentences/1/wordCount"))
                .andExpect(status().isNotFound)
        }
    }

    @Nested
    inner class FindAll {
        @Test
        fun `findAll should return correct response when request is correct`() {
            val firstBook = bookRepository.save(Book.create("Atlas Shrugged"))
            val firstSentence = sentenceRepository.save(Sentence.create(text = "Who is John Galt?", book = firstBook))
            val secondSentence = sentenceRepository.save(Sentence.create(text = "I am John Galt", book = firstBook))
            val secondBook = bookRepository.save(Book.create("East of Eden"))
            val thirdSentence =
                sentenceRepository.save(
                    Sentence.create(
                        text = "And now that you don't have to be perfect, you can be good.",
                        book = secondBook,
                    ),
                )
            val forthSentence =
                sentenceRepository.save(
                    Sentence.create(
                        text = "Sometimes a man wants to be stupid if it lets him do a thing his cleverness forbids.",
                        book = secondBook,
                    ),
                )

            val result =
                mockMvc
                    .perform(
                        get("/sentences"),
                    ).andExpect(status().isOk)
                    .andReturn()

            val response =
                objectMapper.readValue(
                    result.response.contentAsString,
                    object : TypeReference<Map<String, List<SentenceResponseDto>>>() {},
                )
            response.shouldContainExactly(
                mapOf(
                    firstBook.id to
                        listOf(
                            SentenceResponseDto(firstSentence.id, "Who is John Galt?", 4),
                            SentenceResponseDto(secondSentence.id, "I am John Galt", 4),
                        ),
                    secondBook.id to
                        listOf(
                            SentenceResponseDto(
                                thirdSentence.id,
                                "And now that you don't have to be perfect, you can be good.",
                                13,
                            ),
                            SentenceResponseDto(
                                forthSentence.id,
                                "Sometimes a man wants to be stupid if it lets him do a thing his cleverness forbids.",
                                17,
                            ),
                        ),
                ),
            )
        }

        @Test
        fun `findAll should return empty response when there are no sentences`() {
            val result =
                mockMvc
                    .perform(
                        get("/sentences"),
                    ).andExpect(status().isOk)
                    .andReturn()
            val response =
                objectMapper.readValue(
                    result.response.contentAsString,
                    object : TypeReference<Map<String, List<SentenceResponseDto>>>() {},
                )

            response.shouldBeEmpty()
        }
    }

    @Nested
    inner class SaveSentence {
        @Test
        fun `saveSentence should return correct response when request is correct`() {
            val book = bookRepository.save(Book.create("Atlas Shrugged"))
            val sentenceRequest = SentenceRequestDto(bookId = book.id, text = "Who is John Galt?")

            val result =
                mockMvc
                    .perform(
                        post("/sentences")
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(sentenceRequest)),
                    ).andExpect(status().isCreated)
                    .andReturn()
            val response = objectMapper.readValue(result.response.contentAsString, SentenceResponseDto::class.java)
            val sentenceEntity =
                sentenceRepository
                    .findById(response.id)
                    .get()

            assertSoftly {
                with(response) {
                    id shouldBe sentenceEntity.id
                    text shouldBe "Who is John Galt?"
                    wordCount shouldBe 4
                }
                with(sentenceEntity) {
                    value shouldBe "Who is John Galt?"
                    wordCount shouldBe 4
                    book.id shouldBe book.id
                }
            }
        }

        @Test
        fun `saveSentence should return 404 when book does not exist`() {
            val sentenceRequest = SentenceRequestDto("Who is John Galt?", "1")

            mockMvc
                .perform(
                    post("/sentences")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(sentenceRequest)),
                ).andExpect(status().isNotFound)
        }

        @Test
        fun `saveSentence should return 400 when book id is empty`() {
            val sentenceRequest = SentenceRequestDto(bookId = "", text = "Who is John Galt?")

            mockMvc
                .perform(
                    post("/sentences")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(sentenceRequest)),
                ).andExpect(status().isBadRequest)
        }
    }

    @Test
    fun `saveSentence should return 400 when text is not provided`() {
        val book = bookRepository.save(Book.create("Atlas Shrugged"))

        mockMvc
            .perform(
                post("/sentences")
                    .contentType("application/json")
                    .content("""{"bookId": "${book.id}"}"""),
            ).andExpect(status().isBadRequest)
    }
}
