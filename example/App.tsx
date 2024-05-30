import { StyleSheet, Text, View } from 'react-native';

import * as ExpoTinkLink from 'expo-tink-link';

export default function App() {
  return (
    <View style={styles.container}>
      <Text>{ExpoTinkLink.hello()}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});
