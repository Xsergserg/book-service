package com.innovatrics.dot.interview.persistence.repository

import com.innovatrics.dot.interview.persistence.entity.Book
import org.springframework.data.jpa.repository.JpaRepository

interface BookRepository : JpaRepository<Book, String>
