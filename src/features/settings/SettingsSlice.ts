import { PayloadAction, createSlice } from "@reduxjs/toolkit";

interface ISettingsState {
  sequenceScanDelay: string;
}

export const SettingsSlice = createSlice({
  name: "settings",
  initialState: <ISettingsState>{
    sequenceScanDelay: "2",
  },
  reducers: {
    set: (state: ISettingsState, action: PayloadAction<string>) => {
      state.sequenceScanDelay = action.payload;
    },
  },
});

export const { set } = SettingsSlice.actions;
export const sequenceScanDelay = (state: { settings: ISettingsState }) =>
  state.settings.sequenceScanDelay;

export default SettingsSlice.reducer;
