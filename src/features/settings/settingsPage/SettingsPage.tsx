import React, { useCallback, useEffect, useState } from "react";
import {
  SectionListRenderItemInfo,
  SectionListData,
  SectionList,
  Text,
  View,
  Linking,
  Pressable,
} from "react-native";
import {
  SettingsItemViewModel,
  SettingsItemType,
} from "../SettingsItemViewModel";
import { Props } from "../../../navigation";
import { Links } from "../../../contants";
import { styles } from "./SettingsPage.style";
import i18n from "../../../translations";
import { StringFormat, SettingsHelper } from "../../../utils";

const SettingsPage = function ({ navigation }: Props) {
  const versionInfo = StringFormat(
    i18n.t("VersionInfoFormat"),
    "1.0",
    "122121",
  );
  const [delay, setDelay] = useState(
    SettingsHelper.getInitialSequenceScanDelay(),
  );
  const delayValue = `${delay} ${i18n.t("SequenceScanDelayUnit")}`;

  const onPageActivated = () =>
    setDelay(SettingsHelper.getInitialSequenceScanDelay());
  useEffect(
    () => navigation.addListener("focus", onPageActivated),
    [navigation],
  );

  const settingsData: ReadonlyArray<SectionListData<SettingsItemViewModel>> = [
    {
      data: [
        new SettingsItemViewModel(
          SettingsItemType.Editable,
          i18n.t("SequenceScanDelay"),
          delayValue,
        ),
      ],
    },
    {
      data: [
        new SettingsItemViewModel(
          SettingsItemType.Link,
          i18n.t("Help"),
          Links.help,
        ),
        new SettingsItemViewModel(
          SettingsItemType.Link,
          i18n.t("Privacy"),
          Links.privacy,
        ),
      ],
    },
  ];

  const renderLinkItem = (item: SettingsItemViewModel): React.ReactElement => {
    const handlePress = useCallback(async () => {
      await Linking.openURL(item.value);
    }, []);

    return (
      <Pressable style={styles.item} onPress={handlePress}>
        <Text style={[styles.text]}>{item.label}</Text>
      </Pressable>
    );
  };

  const renderEditableItem = (
    item: SettingsItemViewModel,
  ): React.ReactElement => {
    const handlePress = useCallback(() => {
      navigation.navigate("SettingsEditor");
    }, []);

    return (
      <Pressable
        style={({ pressed }) => [styles.item, { opacity: pressed ? 0.3 : 1 }]}
        onPress={handlePress}
      >
        <Text style={[styles.text, styles.textLeft]}>{item.label}</Text>
        <Text style={[styles.text, styles.textRight]}>{item.value}</Text>
      </Pressable>
    );
  };

  const renderSeparatorLine = (): React.ReactElement => {
    return <View style={styles.separator}></View>;
  };

  const renderItem = ({
    item,
  }: SectionListRenderItemInfo<SettingsItemViewModel>) => {
    return item.type === SettingsItemType.Link
      ? renderLinkItem(item)
      : renderEditableItem(item);
  };

  return (
    <View style={styles.container}>
      <SectionList
        sections={settingsData}
        renderItem={renderItem}
        renderSectionHeader={renderSeparatorLine}
        scrollEnabled={false}
        style={styles.itemsContainer}
      />

      <View style={styles.versionInfoContainer}>
        <Text style={[styles.text, styles.versionInfoText]}>{versionInfo}</Text>
      </View>
    </View>
  );
};

export default SettingsPage;
