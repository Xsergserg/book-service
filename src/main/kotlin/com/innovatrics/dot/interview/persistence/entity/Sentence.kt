package com.innovatrics.dot.interview.persistence.entity

import org.hibernate.validator.constraints.Length
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.ManyToOne
import javax.validation.constraints.NotNull

@Entity
internal class Sentence(
    @ManyToOne @NotNull val book: Book,
    @NotNull @Length(max = 128) @Column(nullable = false, length = 128) val value: String,
    @NotNull @Column(nullable = false) val wordCount: Int
) : EntityBase()
