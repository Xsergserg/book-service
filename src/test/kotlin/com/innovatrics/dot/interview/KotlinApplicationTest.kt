package com.innovatrics.dot.interview

import io.kotest.assertions.throwables.shouldNotThrowAny
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class KotlinApplicationTest {
    @Test
    fun `main should not throw any exceptions`() {
        shouldNotThrowAny {
            main(arrayOf())
        }
    }
}
