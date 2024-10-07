package com.innovatrics.dot.interview.service

import com.innovatrics.dot.interview.persistence.repository.BookRepository
import com.innovatrics.dot.interview.persistence.repository.SentenceRepository
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.Optional

@SpringBootTest
@ExtendWith(SpringExtension::class)
class SentenceServiceCacheTest {
    @Autowired
    lateinit var sentenceService: SentenceService

    @MockBean
    lateinit var sentenceRepository: SentenceRepository

    @MockBean
    lateinit var bookRepository: BookRepository

    @Test
    fun `cache works as expected`() {
        whenever(sentenceRepository.findWordCountById("1")).thenReturn(2)
        whenever(sentenceRepository.findAll()).thenReturn(emptyList())
        whenever(bookRepository.findById("1")).thenReturn(Optional.of(mockk()))
        whenever(sentenceRepository.save(any())).thenReturn(mockk(relaxed = true))

        repeat(5) {
            sentenceService.getWordCountById("1")
            sentenceService.findAll()
        }
        verify(sentenceRepository, Mockito.times(1)).findWordCountById("1")
        verify(sentenceRepository, Mockito.times(1)).findAll()

        sentenceService.save("text", "1")

        sentenceService.getWordCountById("1")
        sentenceService.findAll()
        verify(sentenceRepository, Mockito.times(2)).findWordCountById("1")
        verify(sentenceRepository, Mockito.times(2)).findAll()
    }
}
