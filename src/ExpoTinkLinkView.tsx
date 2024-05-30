import { requireNativeViewManager } from 'expo-modules-core';
import * as React from 'react';

import { ExpoTinkLinkViewProps } from './ExpoTinkLink.types';

const NativeView: React.ComponentType<ExpoTinkLinkViewProps> =
  requireNativeViewManager('ExpoTinkLink');

export default function ExpoTinkLinkView(props: ExpoTinkLinkViewProps) {
  return <NativeView {...props} />;
}
