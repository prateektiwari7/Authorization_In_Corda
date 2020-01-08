package AccountsonNode

import co.paralleluniverse.fibers.Suspendable
import com.r3.corda.lib.accounts.contracts.states.AccountInfo
import com.r3.corda.lib.accounts.workflows.accountService
import com.template.flows.Initiator
import net.corda.core.contracts.StateRef
import net.corda.core.flows.*
import net.corda.core.utilities.ProgressTracker
import net.corda.core.utilities.getOrThrow

//Flow that returns the accounts created by hosts
@InitiatingFlow
@StartableByRPC
class AccountsonNode : FlowLogic<List<String>>() {
    override val progressTracker = ProgressTracker()

    @Suspendable
    override fun call() :  List<String> {
        val newAccount = accountService.allAccounts()

        var r = listOf<String>()

        for(new in newAccount)
        {
        r+= new.state.data.name
        }

        return r
    }
}

@InitiatedBy(AccountsonNode::class)
class AccountsonNode_Responder(val counterpartySession: FlowSession) : FlowLogic<Unit>() {
    @Suspendable
    override fun call() {
        // Responder flow logic goes here.
    }
}
