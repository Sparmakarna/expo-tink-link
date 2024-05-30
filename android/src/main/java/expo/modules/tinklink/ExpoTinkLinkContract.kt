package expo.modules.tinklink

import android.app.Activity
import android.content.Context
import android.content.Intent
import expo.modules.kotlin.activityresult.AppContextActivityResultContract
import expo.modules.kotlin.providers.AppContextProvider


/**
 * Data required to be returned upon successful contract completion
 */
sealed class ExpoTinkLinkContractResult private constructor() {
    class Success(val data: String) : ExpoTinkLinkContractResult()
    class Cancelled : ExpoTinkLinkContractResult()
    class Error : ExpoTinkLinkContractResult()
}

class ExpoTinkLinkContract(
    private val appContextProvider: AppContextProvider
) : AppContextActivityResultContract<ExpoTinkLinkContractOptions, ExpoTinkLinkContractResult> {
    override fun createIntent(context: Context, input: ExpoTinkLinkContractOptions): Intent =
        Intent(context, ExpoTinkLinkActivity::class.java)
            .putExtra("authCode", input.authCode)
            .putExtra("clientId", input.clientId)
            .putExtra("redirectUri", input.redirectUri)
            .putExtra("credentialsId", input.credentialsId)


    override fun parseResult(
        input: ExpoTinkLinkContractOptions,
        resultCode: Int,
        intent: Intent?
    ): ExpoTinkLinkContractResult =
        if (resultCode == Activity.RESULT_CANCELED) {
            ExpoTinkLinkContractResult.Cancelled()
        } else {
            val credentials = intent?.getStringExtra("credentialsId")
            if (credentials != null) {
                ExpoTinkLinkContractResult.Success(credentials)
            } else {
                ExpoTinkLinkContractResult.Error()
            }
        }
}

data class ExpoTinkLinkContractOptions(
    val authCode: String,
    val clientId: String,
    val redirectUri: String,
    val credentialsId: String? = null
) : java.io.Serializable
