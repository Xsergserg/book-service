package com.innovatrics.dot.interview

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class KotlinApplication

@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<KotlinApplication>(*args)
}
