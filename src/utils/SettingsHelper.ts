import { Settings } from "react-native";
import { AppConstants } from "../contants";

export class SettingsHelper {
  static getInitialSequenceScanDelay(): number {
    const delayFromSettings = Settings.get(
      AppConstants.sequenceScanDelaySettingsKey,
    );
    const defaultDelayValue = () =>
      parseInt(AppConstants.defaultSequenceScanDelay);
    if (delayFromSettings == null) {
      return defaultDelayValue();
    }

    const delayValue = parseInt(delayFromSettings);
    return isNaN(delayValue) || delayValue <= 0
      ? defaultDelayValue()
      : delayValue;
  }

  static trySaveSequenceScanDelay(value: string): boolean {
    const delayValue = parseInt(value);
    if (isNaN(delayValue) || delayValue <= 0) {
      return false;
    }

    Settings.set({ [AppConstants.sequenceScanDelaySettingsKey]: delayValue });
    return true;
  }
}
