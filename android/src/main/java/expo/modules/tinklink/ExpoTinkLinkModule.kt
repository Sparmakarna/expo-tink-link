package expo.modules.tinklink

import androidx.core.os.OperationCanceledException
import expo.modules.kotlin.activityresult.AppContextActivityResultLauncher
import expo.modules.kotlin.functions.Coroutine
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExpoTinkLinkModule : Module() {
  // Each module class must implement the definition function. The definition consists of components
  // that describes the module's functionality and behavior.
  // See https://docs.expo.dev/modules/module-api for more details about available components.
  override fun definition() = ModuleDefinition {
    // Sets the name of the module that JavaScript code will use to refer to the module. Takes a string as an argument.
    // Can be inferred from module's class name, but it's recommended to set it explicitly for clarity.
    // The module will be accessible from `requireNativeModule('ExpoTinkLink')` in JavaScript.
    Name("ExpoTinkLink")

    // Sets constant properties on the module. Can take a dictionary or a closure that returns a dictionary.
    AsyncFunction("init") Coroutine { authCode: String, clientId: String, redirectUri: String ->
        val contractOptions = ExpoTinkLinkContractOptions(authCode, clientId, redirectUri)
        launchContract({ tinkLinkLauncher.launch(contractOptions) }, contractOptions)
    }

    AsyncFunction("updateConsent") Coroutine { authCode: String, clientId: String, redirectUri: String, credentialsId: String ->
        val contractOptions = ExpoTinkLinkContractOptions(authCode, clientId, redirectUri, credentialsId)
        launchContract({ tinkLinkLauncher.launch(contractOptions) }, contractOptions)
    }


    RegisterActivityContracts {
        tinkLinkLauncher = registerForActivityResult(
            ExpoTinkLinkContract(this@ExpoTinkLinkModule)
        ) { input, result -> handleResultUponActivityDestruction(result, input) }
    }
  }

   private lateinit var tinkLinkLauncher: AppContextActivityResultLauncher<ExpoTinkLinkContractOptions, ExpoTinkLinkContractResult>

    private fun handleResultUponActivityDestruction(result: ExpoTinkLinkContractResult, options: ExpoTinkLinkContractOptions) {
        if (result is ExpoTinkLinkContractResult.Success) {
            // do nothing
        }
    }

    // result
    private suspend fun launchPicker(
        pickerLauncher: suspend () -> ExpoTinkLinkContractResult,
    ): ExpoTinkLinkContractResult.Success = withContext(Dispatchers.Main) {
        when (val pickingResult = pickerLauncher()) {
            is ExpoTinkLinkContractResult.Success -> pickingResult
            is ExpoTinkLinkContractResult.Cancelled -> throw OperationCanceledException()
            is ExpoTinkLinkContractResult.Error -> throw OperationCanceledException()
        }
    }

    private suspend fun launchContract(
        pickerLauncher: suspend () -> ExpoTinkLinkContractResult,
        options: ExpoTinkLinkContractOptions,
    ): String {
        return try {
            var result = launchPicker(pickerLauncher)
            if(result.data != null) {
                return result.data
            } else {
                return "error"
            }
        } catch (cause: OperationCanceledException) {
            return "error"
        }
    }
}
