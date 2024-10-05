package com.innovatrics.dot.interview.persistence.repository

import com.innovatrics.dot.interview.persistence.entity.Book
import org.springframework.data.jpa.repository.JpaRepository

internal interface BookRepository : JpaRepository<Book, String>