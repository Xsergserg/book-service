package com.innovatrics.dot.interview.persistence.repository

import com.innovatrics.dot.interview.persistence.entity.Sentence
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

interface SentenceRepository : JpaRepository<Sentence, String> {
    @Transactional(propagation = Propagation.REQUIRED)
    fun getAllByBookId(bookId: String): List<Sentence>

    @Transactional(propagation = Propagation.REQUIRED)
    @Query("SELECT s.wordCount FROM Sentence s WHERE s.id = :id")
    fun findWordCountById(
        @Param("id") id: String,
    ): Int?
}
