package AuthFlow

import Queries.DatabaseValues
import co.paralleluniverse.fibers.Suspendable
import com.template.flows.Initiator
import jdk.nashorn.internal.runtime.logging.DebugLogger
import net.corda.core.StubOutForDJVM
import net.corda.core.flows.*
import net.corda.core.utilities.ProgressTracker
import net.corda.core.utilities.contextLogger
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.io.UnsupportedEncodingException
import java.lang.Exception
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.security.Security
import javax.crypto.*
import javax.crypto.spec.SecretKeySpec

@InitiatingFlow
@StartableByRPC
class Validation(val Username: String, val AuthSecret: String) : FlowLogic<Unit>() {
    override val progressTracker = ProgressTracker()

    @Suspendable
    override fun call() : Unit {

        val database = serviceHub.cordaService(DatabaseValues::class.java)

        val encrypteddata = database.queryName(Username)

        val decrypteddata =  Decryption(AuthSecret,encrypteddata)

        if(decrypteddata.equals(AuthSecret))

        {
            throw Exception("You are validated user")
        }

        else {
            throw Exception("You are not validated user")
        }



    }
}

//@JvmOverloads @DeleteForDJVM
@StubOutForDJVM
private fun Decryption (key: String, strToDecrypt: String)  :  String {


    Security.addProvider(BouncyCastleProvider())
    var keyBytes: ByteArray

    try {
        keyBytes = key.toByteArray(charset("UTF8"))
        val skey = SecretKeySpec(keyBytes, "AES")
        val input = org.bouncycastle.util.encoders.Base64
                .decode(strToDecrypt?.trim { it <= ' ' }?.toByteArray(charset("UTF8")))

        synchronized(Cipher::class.java) {
            val cipher = Cipher.getInstance("AES/ECB/PKCS7Padding")
            cipher.init(Cipher.DECRYPT_MODE, skey)

            val plainText = ByteArray(cipher.getOutputSize(input.size))
            var ptLength = cipher.update(input, 0, input.size, plainText, 0)
            ptLength += cipher.doFinal(plainText, ptLength)
            val decryptedString = String(plainText)
            return decryptedString.trim { it <= ' ' }
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


    return "Error"

}

