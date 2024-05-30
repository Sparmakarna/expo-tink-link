import ExpoTinkLinkModule from "./ExpoTinkLinkModule";

export async function init(
    authCode: string,
    clientId: string,
    redirectUri: string
): Promise<string> {
    return await ExpoTinkLinkModule.init(authCode, clientId, redirectUri);
}

export async function updateConsent(
    authCode: string,
    clientId: string,
    redirectUri: string,
    credentialsId: string
): Promise<string> {
    return await ExpoTinkLinkModule.updateConsent(
        authCode,
        clientId,
        redirectUri,
        credentialsId
    );
}
