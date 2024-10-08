package com.innovatrics.dot.interview.exception

import com.innovatrics.dot.interview.persistence.repository.SentenceRepository
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.dao.DataAccessResourceFailureException
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class GlobalExceptionHandlerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var sentenceRepository: SentenceRepository

    @Test
    fun `should throw 500 when unexpected error occurs`() {
        `when`(sentenceRepository.findAll()).thenThrow(DataAccessResourceFailureException("Database connection error"))

        mockMvc
            .perform(get("/sentences"))
            .andExpect(status().isInternalServerError)
            .andExpect(content().string("An unexpected error occurred"))
    }
}
