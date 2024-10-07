package com.innovatrics.dot.interview.persistence.entity

import java.time.Instant
import java.util.UUID
import javax.persistence.Id
import javax.persistence.MappedSuperclass
import javax.validation.constraints.NotNull

@MappedSuperclass
abstract class EntityBase {
    @Id
    val id: String = UUID.randomUUID().toString()

    @NotNull
    val createdAt: Instant = Instant.now()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EntityBase

        if (id != other.id) return false
        if (createdAt != other.createdAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + createdAt.hashCode()
        return result
    }
}
