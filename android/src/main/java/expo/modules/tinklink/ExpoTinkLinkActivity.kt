package expo.modules.tinklink

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.tink.link.core.base.Tink
import com.tink.link.core.data.request.configuration.BaseDomain
import com.tink.link.core.data.request.configuration.Configuration
import com.tink.link.core.data.request.transactions.ConnectAccountsForContinuousAccess
import com.tink.link.core.data.request.transactions.UpdateConsent
import com.tink.link.core.data.response.error.TinkError
import com.tink.link.core.data.response.success.transactions.TinkTransactionsSuccess
import com.tink.link.core.navigator.FullScreen
import com.tink.link.core.themes.TinkAppearance
import com.tink.link.core.themes.TinkAppearanceXml
import java.io.Serializable


class ExpoTinkLinkActivity : FragmentActivity(), Serializable  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Add XML view.
        setContentView(R.layout.activity_empty)

        // Restore SDK state if possible
        savedInstanceState?.let {
            Tink.restoreState(it)
        }
    }

    override fun onStart() {
        super.onStart()

        val authCode = intent.getStringExtra("authCode")
        val clientId = intent.getStringExtra("clientId")
        val redirectUri = intent.getStringExtra("redirectUri")
        val credentialsId = intent.getStringExtra("credentialsId")

        if (authCode == null || clientId == null || redirectUri == null) {
            return
        } else {
            // Present the SDK.
            if(credentialsId == null) {
                showTransactionsWithContinuousAccess(authCode, clientId, redirectUri)
            } else {
                showUpdateConsent(authCode, clientId, redirectUri, credentialsId)
            }
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // Save SDK state to bundle
        Tink.saveState(outState)
    }

    // Example of one time access to Transactions presented in a fullscreen.
    // For launching other flows, please find implementation guidance in this link: https://github.com/tink-ab/tink-link-android/blob/master/sample-app/src/main/java/com/tink/link/app/navToFlows/FlowCases.kt
    private fun showTransactionsWithContinuousAccess(
        authCode: String,
        clientId: String,
        redirectUri: String,
    ) {
        Log.d(TAG, "authCode: $authCode")
        Log.d(TAG, "clientId: $clientId")
        Log.d(TAG, "redirectUri: $redirectUri")

        // Add basic required parameters.
        val configuration = Configuration(
            clientId = clientId,
            redirectUri = redirectUri,
            baseDomain = BaseDomain.EU
        )

        // Get Fullscreen UI.
        val fullScreen = FullScreen(getTinkAppearance())

        // More parameters can be added to ConnectAccountsForOneTimeAccess().
        val request = ConnectAccountsForContinuousAccess(authCode, "SE")

        // Call this method to trigger the flow.
        Tink.Transactions.connectAccountsForContinuousAccess(
            this,
            configuration,
            request,
            fullScreen,
            { success: TinkTransactionsSuccess ->
                val credentialsIdIntent = Intent()
                credentialsIdIntent.putExtra("credentialsId", success.credentialsId)
                setResult(RESULT_OK, credentialsIdIntent)
                finish()
            },
            { error: TinkError ->
                Log.d(TAG, "Error: $error")
                finish()
            }
        )
    }

    /**
     * This is an example for updating consent on continuous access to Transactions.
    * Both authorizationCode and credentialsId are required for starting this flow.
    * authorizationCode can be retrieved by following this link:[authorization code](https://docs.tink.com/api#general/oauth/create-authorization).
    * credentialsId can be retrieved from callback from previous flow -- Connecting continuous access to Transactions:  Tink.Transactions.connectContinuousAccess.
    * */
    private fun showUpdateConsent(
        authCode: String,
        clientId: String,
        redirectUri: String,
        credentialsId: String,
    ) {
        val fullScreen = FullScreen(getTinkAppearance())
        val continuousAccessUpdate = UpdateConsent(
            authorizationCode = authCode, // Required (Replace with your value).
            credentialsId = credentialsId // Required (Replace with your value).
        )

        val configuration = Configuration(
            clientId = clientId,
            redirectUri = redirectUri,
            baseDomain = BaseDomain.EU
        )

        // Call this method to trigger the flow.
        Tink.Transactions.updateConsent(
            this,
            configuration,
            continuousAccessUpdate,
            fullScreen,
            { success: TinkTransactionsSuccess ->
                val credentialsIdIntent = Intent()
                credentialsIdIntent.putExtra("credentialsId", success.credentialsId)
                setResult(RESULT_OK, credentialsIdIntent)
                finish()

            },
            { error: TinkError ->
                Log.d(TAG, "error message = ${error.errorDescription}")
                finish()
            }
        )
    }

    // Color, icons and title can be customized for oth Light/Dark mode by changing the values on TinkAppearanceXml.ThemeAttributes as below:
    private fun getTinkAppearance(): TinkAppearance {
        return TinkAppearanceXml(
            light = TinkAppearanceXml.ThemeAttributes(
                toolbarColorId = R.color.white,
                windowBackgroundColorId = R.color.white,
                iconBackId = R.drawable.ic_back,
                iconBackTint = R.color.black,
                iconBackDescriptionId = R.string.app_name,
                iconCloseId = R.drawable.ic_cross,
                iconCloseTint = R.color.black,
                iconCloseDescriptionId = R.string.app_name,

                // ToolbarTitle can be changed by adding parameter in TinkAppearanceXml.ToolbarTitle(toolbarTextId = R.string.title)
                toolbarTitleObj = TinkAppearanceXml.ToolbarTitle()
            ),
            dark = TinkAppearanceXml.ThemeAttributes(
                toolbarColorId = R.color.black,
                windowBackgroundColorId = R.color.white,
                iconBackId = R.drawable.ic_back,
                iconBackTint = R.color.white,
                iconBackDescriptionId = R.string.app_name,
                iconCloseId = R.drawable.ic_cross,
                iconCloseTint = R.color.white,
                iconCloseDescriptionId = R.string.app_name,
                toolbarTitleObj = TinkAppearanceXml.ToolbarTitle()
            )
        )
    }



}
