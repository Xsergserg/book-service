package com.innovatrics.dot.interview.persistence.entity

import com.innovatrics.dot.interview.dto.SentenceResponseDto
import org.hibernate.validator.constraints.Length
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.ManyToOne
import javax.validation.constraints.NotNull

@Entity
class Sentence(
    @ManyToOne @NotNull val book: Book,
    @NotNull @Length(max = 128) @Column(nullable = false, length = 128) val value: String,
    @NotNull @Column(nullable = false) val wordCount: Int,
) : EntityBase() {
    fun toDto() = SentenceResponseDto(id, value, wordCount)

    companion object {
        fun create(
            book: Book,
            text: String,
        ) = Sentence(book, text, calculateWordCount(text))

        private fun calculateWordCount(text: String) =
            text
                .split(Regex("[^a-zA-Z0-9'’-]+"))
                .filterNot { it.isEmpty() }
                .size
    }
}
