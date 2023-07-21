import { Alert, Button, View } from "react-native"
import { NativeModules } from 'react-native'

const ScannerModule = NativeModules.ScannerModule

const onScanCompleted = (...args: any[]) => {
    console.log(args);
    // Alert.alert(args);
};

const scan = () => {
    ScannerModule.openScanner(onScanCompleted);
};

const Scanner = () => {
    return (
        <View>
            <Button title="Scan" onPress={scan}/>
        </View>
    );
};

export default Scanner;