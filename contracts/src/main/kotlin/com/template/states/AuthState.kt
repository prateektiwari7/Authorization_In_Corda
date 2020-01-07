package com.template.states

import com.template.contracts.AuthContract
import com.template.schema.AuthSchemaV1
import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.LinearState
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.AbstractParty
import net.corda.core.identity.Party
import net.corda.core.schemas.MappedSchema
import net.corda.core.schemas.PersistentState
import net.corda.core.schemas.QueryableState

@BelongsToContract(AuthContract::class)
data class AuthState( val Username: String,
                    val Authtoken: String,
                    val loginto: Party,
                    override val linearId: UniqueIdentifier = UniqueIdentifier()):
        LinearState, QueryableState {
    /** The public keys of the involved parties. */
    override val participants: List<AbstractParty> get() = listOf(loginto)

    override fun generateMappedObject(schema: MappedSchema): PersistentState {
        return when (schema) {
            is AuthSchemaV1 -> AuthSchemaV1.PersistentIOU(
                    this.Username,
                    this.linearId.id,
                    this.Authtoken

            )
            else -> throw IllegalArgumentException("Unrecognised schema $schema")
        }
    }

    override fun supportedSchemas(): Iterable<MappedSchema> = listOf(AuthSchemaV1)
}