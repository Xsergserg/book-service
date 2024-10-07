package com.innovatrics.dot.interview.controller

import com.innovatrics.dot.interview.persistence.repository.BookRepository
import com.innovatrics.dot.interview.persistence.repository.SentenceRepository
import io.kotest.assertions.assertSoftly
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var bookRepository: BookRepository

    @Autowired
    lateinit var sentenceRepository: SentenceRepository

    @AfterEach
    fun tearDown() {
        sentenceRepository.deleteAll()
        bookRepository.deleteAll()
    }

    @Test
    fun `save endpoint should return correct response when request is correct`() {
        mockMvc
            .perform(
                post("/books")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        "{\"name\": \"Atlas Shrugged\", \"sentences\": [\"Who is John Galt?\", \"I am John Galt.\"]}",
                    ),
            ).andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").isNotEmpty)
            .andExpect(jsonPath("$.name").value("Atlas Shrugged"))
            .andExpect(jsonPath("$.sentences").isArray)
            .andExpect(jsonPath("$.sentences[0].id").isNotEmpty)
            .andExpect(jsonPath("$.sentences[0].text").value("Who is John Galt?"))
            .andExpect(jsonPath("$.sentences[0].word_count").value(4))
            .andExpect(jsonPath("$.sentences[1].id").isNotEmpty)
            .andExpect(jsonPath("$.sentences[1].text").value("I am John Galt."))
            .andExpect(jsonPath("$.sentences[1].word_count").value(4))

        val books = bookRepository.findAll()

        assertSoftly {
            books.size shouldBe 1
            books.first().name shouldBe "Atlas Shrugged"
            sentenceRepository
                .findAll()
                .map { it.value } shouldContainExactly
                listOf(
                    "Who is John Galt?",
                    "I am John Galt.",
                )
        }
    }

    @Test
    fun `save endpoint should return correct value without sentences`() {
        mockMvc
            .perform(
                post("/books")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\": \"Atlas Shrugged\"}"),
            ).andExpect(status().isCreated)
            .andExpect(jsonPath("$.sentences").isEmpty())

        val books = bookRepository.findAll()

        assertSoftly {
            books.size shouldBe 1
            books.first().name shouldBe "Atlas Shrugged"
        }
    }

    @Test
    fun `save endpoint should return correct value with null sentences`() {
        mockMvc
            .perform(
                post("/books")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\": \"Atlas Shrugged\", \"sentences\": null}"),
            ).andExpect(status().isCreated)
            .andExpect(jsonPath("$.sentences").isEmpty())

        val books = bookRepository.findAll()

        assertSoftly {
            books.size shouldBe 1
            books.first().name shouldBe "Atlas Shrugged"
        }
    }

    @Test
    fun `save endpoint should return correct value with empty list of sentences`() {
        mockMvc
            .perform(
                post("/books")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\": \"Atlas Shrugged\", \"sentences\": []}"),
            ).andExpect(status().isCreated)
            .andExpect(jsonPath("$.sentences").isEmpty())

        val books = bookRepository.findAll()

        assertSoftly {
            books.size shouldBe 1
            books.first().name shouldBe "Atlas Shrugged"
        }
    }

    @Test
    fun `save endpoint return 400 when name is empty`() {
        mockMvc
            .perform(
                post("/books")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\": \"\"}"),
            ).andExpect(status().isBadRequest)
    }

    @Test
    fun `save endpoint return 400 when name is not provided`() {
        mockMvc
            .perform(
                post("/books")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"sentences\": [\"Who is John Galt?\", \"I am John Galt.\"]}"),
            ).andExpect(status().isBadRequest)
    }

    @Test
    fun `save endpoint return 400 when name is null`() {
        mockMvc
            .perform(
                post("/books")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        "{\"name\": null, \"sentences\": [\"Who is John Galt?\", \"I am John Galt.\"]}",
                    ),
            ).andExpect(status().isBadRequest)
    }
}
