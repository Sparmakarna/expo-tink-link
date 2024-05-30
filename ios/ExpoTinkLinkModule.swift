import ExpoModulesCore
import TinkLink

public class ExpoTinkLinkModule: Module {
  public func definition() -> ModuleDefinition {
    Name("ExpoTinkLink")

        AsyncFunction("init") { (authCode: String, clientId: String, redirectUri: String, promise: Promise) in
            self.presentTinkLink(authCode: authCode, clientId: clientId, redirectUri: redirectUri, promise: promise)
        }.runOnQueue(DispatchQueue.main)
        
        AsyncFunction("updateConsent") { (authCode: String, clientId: String, redirectUri: String, credentialsId: String, promise: Promise) in
            self.presentUpdateTinkLink(authCode: authCode, clientId: clientId, redirectUri: redirectUri, credentialsId: credentialsId, promise: promise)
        }.runOnQueue(DispatchQueue.main)
    }
    
    private func presentTinkLink(authCode: String, clientId: String, redirectUri: String, promise: Promise) {
        guard let currentViewController = self.appContext?.utilities?.currentViewController() else {
            return
        }
        
        let configuration = Configuration(clientID: clientId, redirectURI: redirectUri)
        let controller = Tink.Transactions.connectAccountsForContinuousAccess(configuration: configuration, market: Market(code: "SE"), authorizationCode: AuthorizationCode(authCode)) { [] result in
            currentViewController.presentedViewController?.dismiss(animated: true)
            switch result {
            case .success(let credentials):
                promise.resolve(credentials.value)
            case .failure(_):
                promise.resolve("error")
            }
        }
        
        currentViewController.present(controller, animated: true)
    }
    
    private func presentUpdateTinkLink(authCode: String, clientId: String, redirectUri: String, credentialsId: String, promise: Promise) {
        guard let currentViewController = self.appContext?.utilities?.currentViewController() else {
            return
        }
        
        let configuration = Configuration(clientID: clientId, redirectURI: redirectUri)
        let controller = Tink.Transactions.updateConsent(configuration: configuration, authorizationCode: AuthorizationCode(authCode), credentialsID: Credentials.ID(stringLiteral: credentialsId)) { [] result in
            currentViewController.presentedViewController?.dismiss(animated: true)
            switch result {
            case .success(let credentials):
                promise.resolve(credentials.value)
            case .failure(_):
                promise.resolve("error")
            }
        }
        
        currentViewController.present(controller, animated: true)
    }
}
