import i18n from "i18next";
import { initReactI18next } from "react-i18next";
import en from "./en.json";
import jp from "./jp.json";

const resources = {
  en: { translation: en },
  jp: { translation: jp },
};

i18n.use(initReactI18next).init({
  compatibilityJSON: "v3",
  resources,
  fallbackLng: "en",
  interpolation: {
    escapeValue: false,
  },
});

export default i18n;
