/*
 * Copyright (c) 2020-present, salesforce.com, inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the
 * following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * Neither the name of salesforce.com, inc. nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

import React, { useEffect } from 'react';
import {
    Alert, AppState,
    useColorScheme
} from 'react-native';

import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from "@react-navigation/native-stack";
import { SafeAreaProvider } from "react-native-safe-area-context";
import { Provider } from "react-redux";

import { Colors } from "./src/contants";
import AccountListPage from './src/features/accounts/AccountListPage';
import Scanner from './src/features/scanner/Scanner';
import { SettingsEditPage, SettingsPage } from "./src/features/settings";
import { RootStackParamList } from "./src/navigation";
import store from "./src/Store";

const Stack = createNativeStackNavigator<RootStackParamList>();

const App = () => {
    const onAppActivated = function () {
        const subscription = AppState.addEventListener("change", (state) => {
            if (state === "active") {
                // update state
                Alert.alert("App has come to the foreground!");
            }
        });

        return () => subscription.remove();
    };

    useEffect(onAppActivated, []);

    return (
        <SafeAreaProvider>
            <NavigationContainer>
                <Stack.Navigator
                    screenOptions={{
                        headerStyle: { backgroundColor: Colors.black },
                        headerTitleStyle: {
                            fontSize: 16,
                            fontWeight: "bold",
                            color: Colors.white,
                        },
                    }}>
                    <Stack.Screen
                        name="Scanner"
                        component={Scanner}
                        options={{ title: "Scanner" }}
                    />
                    <Stack.Screen
                        name="MobileSdkSample"
                        component={AccountListPage}
                        options={{ title: "Mobile SDK Sample" }}
                    />
                    <Stack.Screen
                        name="Settings"
                        component={SettingsPage}
                        options={{ title: "Settings" }}
                    />
                    <Stack.Screen
                        name="SettingsEditor"
                        component={SettingsEditPage}
                        options={{ title: "" }}
                    />
                </Stack.Navigator>
            </NavigationContainer>
        </SafeAreaProvider>
    );
};

export default () => {
    return (
        <Provider store={store}>
            <App />
        </Provider>
    );
};
