package AuthFlow

import co.paralleluniverse.fibers.Suspendable
import net.corda.core.StubOutForDJVM
import net.corda.core.flows.FlowLogic
import net.corda.core.flows.InitiatingFlow
import net.corda.core.flows.StartableByRPC
import net.corda.core.utilities.ProgressTracker


@InitiatingFlow
@StartableByRPC
class CreateKey : FlowLogic<String>() {
    override val progressTracker = ProgressTracker()

    @Suspendable
    override fun call() : String {
       var Key = generateRandomPassword()

        return Key
    }
}



@StubOutForDJVM
private fun generateRandomPassword(): String {
    val chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
    var passWord = ""
    for (i in 0..31) {
        passWord += chars[Math.floor(Math.random() * chars.length).toInt()]
    }
    return passWord
}