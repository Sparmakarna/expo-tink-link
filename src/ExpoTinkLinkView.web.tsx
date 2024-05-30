import * as React from 'react';

import { ExpoTinkLinkViewProps } from './ExpoTinkLink.types';

export default function ExpoTinkLinkView(props: ExpoTinkLinkViewProps) {
  return (
    <div>
      <span>{props.name}</span>
    </div>
  );
}
