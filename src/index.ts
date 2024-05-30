import { NativeModulesProxy, EventEmitter, Subscription } from 'expo-modules-core';

// Import the native module. On web, it will be resolved to ExpoTinkLink.web.ts
// and on native platforms to ExpoTinkLink.ts
import ExpoTinkLinkModule from './ExpoTinkLinkModule';
import ExpoTinkLinkView from './ExpoTinkLinkView';
import { ChangeEventPayload, ExpoTinkLinkViewProps } from './ExpoTinkLink.types';

// Get the native constant value.
export const PI = ExpoTinkLinkModule.PI;

export function hello(): string {
  return ExpoTinkLinkModule.hello();
}

export async function setValueAsync(value: string) {
  return await ExpoTinkLinkModule.setValueAsync(value);
}

const emitter = new EventEmitter(ExpoTinkLinkModule ?? NativeModulesProxy.ExpoTinkLink);

export function addChangeListener(listener: (event: ChangeEventPayload) => void): Subscription {
  return emitter.addListener<ChangeEventPayload>('onChange', listener);
}

export { ExpoTinkLinkView, ExpoTinkLinkViewProps, ChangeEventPayload };
