package com.innovatrics.dot.interview.persistence.entity

import org.hibernate.validator.constraints.Length
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.OneToMany
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
class Book(
    @NotBlank @Length(max = 128) @Column(nullable = false, length = 128) val name: String,
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER) @NotNull val sentences: List<Sentence>,
) : EntityBase() {
    companion object {
        fun create(
            name: String,
            sentences: List<Sentence> = emptyList(),
        ): Book = Book(name, sentences)
    }
}
