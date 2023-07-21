export enum SettingsItemType {
  Editable = "Editable",
  Link = "Link",
}

export class SettingsItemViewModel {
  label: string;
  value: string;
  type: SettingsItemType;

  constructor(type: SettingsItemType, label: string, value: string) {
    this.type = type;
    this.label = label;
    this.value = value;
  }
}
