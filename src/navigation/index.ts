import { NativeStackScreenProps } from "@react-navigation/native-stack";

export type RootStackParamList = {
  Scanner: undefined,
  Settings: undefined;
  SettingsEditor: undefined;
  MobileSdkSample: undefined,
};

export type Props = NativeStackScreenProps<RootStackParamList>;
