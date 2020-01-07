package com.template.schema

import net.corda.core.schemas.MappedSchema
import net.corda.core.schemas.PersistentState
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

object AuthSchema

/**
 * An IOUState schema.
 */
object AuthSchemaV1 : MappedSchema(
        schemaFamily = AuthSchema.javaClass,
        version = 1,
        mappedTypes = listOf(PersistentIOU::class.java)) {
    @Entity
    @Table(name = "Auth")
    class PersistentIOU(
            @Column(name = "UserName")
            var Username: String,

            @Column(name = "linear_id")
            var linearId: UUID,

            @Column(name = "Authtoken")
            var value: String

    ) : PersistentState() {
        // Default constructor required by hibernate.
        constructor(): this("",UUID.randomUUID(),"")
    }
}