import { configureStore } from "@reduxjs/toolkit";
import { SettingsReducer } from "./features/settings";

export default configureStore({
  reducer: {
    settings: SettingsReducer,
  },
});
