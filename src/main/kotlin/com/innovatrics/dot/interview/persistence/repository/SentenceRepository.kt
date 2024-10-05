package com.innovatrics.dot.interview.persistence.repository

import com.innovatrics.dot.interview.persistence.entity.Sentence
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

internal interface SentenceRepository : JpaRepository<Sentence, String> {

    @Transactional(propagation = Propagation.REQUIRED)
    fun getAllByBookId(bookId: String)
}