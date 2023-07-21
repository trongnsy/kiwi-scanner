import { StyleSheet } from "react-native";
import { Colors } from "../../../contants";

export const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: Colors.dimGray,
  },
  text: {
    height: 40,
    fontSize: 16,
    fontWeight: "bold",
    textAlign: "center",
    color: Colors.white,
    backgroundColor: Colors.black,
  },
  label: {
    borderBottomColor: Colors.dimGray,
    borderBottomWidth: StyleSheet.hairlineWidth,
  },
  input: {
    padding: 10,
    fontWeight: "normal",
  },
  button: {
    height: 40,
    margin: 10,
    borderRadius: 5,
    borderColor: Colors.dimGray,
  },
  buttonText: {
    width: "100%",
    backgroundColor: Colors.primary,
  },
});
