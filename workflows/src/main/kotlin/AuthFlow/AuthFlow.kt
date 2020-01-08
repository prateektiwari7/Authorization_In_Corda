package com.template.flows

import co.paralleluniverse.fibers.Suspendable
import com.r3.corda.lib.accounts.workflows.accountService
import com.r3.corda.lib.ci.workflows.RequestKeyForAccount
import com.template.contracts.AuthContract
import com.template.states.AuthState
import net.corda.core.StubOutForDJVM
import net.corda.core.contracts.Command
import net.corda.core.contracts.requireThat
import net.corda.core.flows.*
import net.corda.core.identity.Party
import net.corda.core.transactions.SignedTransaction
import net.corda.core.transactions.TransactionBuilder
import net.corda.core.utilities.ProgressTracker
import net.corda.core.utilities.getOrThrow
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.io.UnsupportedEncodingException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.security.Security
import java.util.*
import javax.crypto.*
import javax.crypto.spec.SecretKeySpec


// Create Unique user account on node and encryption with Key generated.
@InitiatingFlow
@StartableByRPC
class CreateAuthFlow(val  accountName: String, val toparty: Party, val Authsecret: String) : FlowLogic<SignedTransaction>() {
    override val progressTracker = ProgressTracker()

    @Suspendable
    override fun call() : SignedTransaction {

        val notary = serviceHub.networkMapCache.notaryIdentities[0]


        try {
            val newAccount = accountService.createAccount(name = accountName).toCompletableFuture().getOrThrow()
            val acct = newAccount.state.data
            var key =  subFlow(RequestKeyForAccount(toparty,newAccount.state.data.identifier.id))

            var hash = Encryption(Authsecret,Authsecret)

            var State =  AuthState(accountName,hash,toparty,acct.linearId)

            var Command = Command(AuthContract.Commands.Action(),State.participants.map { it.owningKey })

            val txBuilder = TransactionBuilder(notary = notary)
                    .addOutputState(State, AuthContract.ID)
                    .addCommand(Command)

            txBuilder.verify(serviceHub)

            val partSignedTx = serviceHub.signInitialTransaction(txBuilder)

            val otherPartySession = initiateFlow(toparty)

            val fullySignedTx = subFlow(CollectSignaturesFlow(partSignedTx, setOf(otherPartySession)))

             //return ""+acct.name + " team's account was created. UUID is : " + acct.identifier+ "Public Key of your account as"+key

            return subFlow(FinalityFlow(fullySignedTx, setOf(otherPartySession)))


        }
        catch (e : Exception)
        {
            throw FlowException(e)
        }



    }
}

@InitiatedBy(CreateAuthFlow::class)
class CreateAuthFlow_Responder(val counterpartySession: FlowSession) : FlowLogic<SignedTransaction>() {
    @Suspendable
    override fun call() : SignedTransaction {
        val signedTransactionFlow = object : SignTransactionFlow(counterpartySession) {
            override fun checkTransaction(stx: SignedTransaction) = requireThat{
                val output = stx.tx.outputs.single().data

            }
        }
        val txWeJustSignedId = subFlow(signedTransactionFlow)
        return subFlow(ReceiveFinalityFlow(counterpartySession, txWeJustSignedId.id))

    }
}



//AES Encryption Algorithm
@StubOutForDJVM
private fun Encryption( strToEncrypt: String,  secret_key: String) : String {


    Security.addProvider(BouncyCastleProvider())

    var keyBytes: ByteArray

    try {
        keyBytes = secret_key.toByteArray(charset("UTF8"))
        val skey = SecretKeySpec(keyBytes, "AES")
        val input = strToEncrypt.toByteArray(charset("UTF8"))

        synchronized(Cipher::class.java) {
            val cipher = Cipher.getInstance("AES/ECB/PKCS7Padding")
            cipher.init(Cipher.ENCRYPT_MODE, skey)

            val cipherText = ByteArray(cipher.getOutputSize(input.size))
            var ctLength = cipher.update(
                    input, 0, input.size,
                    cipherText, 0
            )
            ctLength += cipher.doFinal(cipherText, ctLength)
            return String(
                    Base64.getEncoder().encode(cipherText)
            )
        }
    } catch (uee: UnsupportedEncodingException) {
        uee.printStackTrace()
    } catch (ibse: IllegalBlockSizeException) {
        ibse.printStackTrace()
    } catch (bpe: BadPaddingException) {
        bpe.printStackTrace()
    } catch (ike: InvalidKeyException) {
        ike.printStackTrace()
    } catch (nspe: NoSuchPaddingException) {
        nspe.printStackTrace()
    } catch (nsae: NoSuchAlgorithmException) {
        nsae.printStackTrace()
    } catch (e: ShortBufferException) {
        e.printStackTrace()
    }

    return  "working"

}
















