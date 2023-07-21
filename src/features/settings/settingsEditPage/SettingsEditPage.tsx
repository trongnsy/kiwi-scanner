import React, { useState } from "react";
import { View, Text, TextInput, Pressable } from "react-native";
import { useDispatch } from "react-redux";
import i18n from "../../../translations";
import { SettingsHelper } from "../../../utils/SettingsHelper";
import { set } from "../SettingsSlice";
import { Props } from "../../../navigation";
import { styles } from "./SettingsEditPage.style.";

const SettingsEditPage = function ({ navigation }: Props) {
  const initialDelayValue = SettingsHelper.getInitialSequenceScanDelay() + "";
  const [delay, setDelay] = useState(initialDelayValue);

  const dispatch = useDispatch();
  const saveDelay = () => {
    if (SettingsHelper.trySaveSequenceScanDelay(delay)) {
      dispatch(set(delay));
      navigation.goBack();
    }
  };

  return (
    <View style={styles.container}>
      <View style={[styles.label]}>
        <Text style={[styles.text]}>{i18n.t("SequenceScanDelay")}</Text>
      </View>

      <TextInput
        style={[styles.text, styles.input]}
        value={delay}
        onChangeText={setDelay}
        keyboardType="numeric"
      />

      <Pressable style={styles.button} onPress={saveDelay}>
        <Text style={[styles.text, styles.buttonText]}>{i18n.t("Save")}</Text>
      </Pressable>
    </View>
  );
};

export default SettingsEditPage;
