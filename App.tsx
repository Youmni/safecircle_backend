import 'react-native-gesture-handler';
import * as React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import MainPage from './screens/MainPage';
import TabTwoScreen from './screens/TabTwoScreen'; // Zorg ervoor dat je het juiste pad gebruikt

const Stack = createNativeStackNavigator();

export default function App() {
  return (
    <NavigationContainer>
      <Stack.Navigator initialRouteName="MainPage">
        <Stack.Screen name="MainPage" component={MainPage} />
        <Stack.Screen name="TabTwoScreen" component={TabTwoScreen} />
      </Stack.Navigator>
    </NavigationContainer>
  );
}