package Queries


import DatabaseConnection.DatabaseConnection
import net.corda.core.node.ServiceHub
import net.corda.core.node.services.CordaService

const val TABLE_NAME = "Auth"


@CordaService
class DatabaseValues(services: ServiceHub) : DatabaseConnection(services) {

    fun Addinfo(name: String, age: String) {
        val query = "insert into $TABLE_NAME values(?, ?)"

        val params = mapOf(1 to name, 2 to age)

        executeUpdate(query, params)
    }


    fun queryName(UserName: String): String {
        val query = "select Authtoken from $TABLE_NAME where UserName = ?"

        val params = mapOf(1 to UserName)

        val results = executeQuery(query, params) { it -> it.getString("Authtoken") }

        if (results.isEmpty()) {
            throw IllegalArgumentException("User $UserName not present in database.")
        }

        val value = results.single()
        log.info("Name $UserName read from Infodatabase table.")
        return value
    }


}