import { StyleSheet } from "react-native";
import { Colors } from "../../../contants";

export const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: Colors.dimGray,
  },
  itemsContainer: {
    flexGrow: 0,
  },
  item: {
    height: 50,
    width: "100%",
    paddingHorizontal: 10,
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    backgroundColor: Colors.black,
    borderBottomColor: Colors.dimGray,
    borderBottomWidth: StyleSheet.hairlineWidth,
  },
  text: {
    fontSize: 16,
    color: Colors.white,
  },
  textLeft: {
    fontWeight: "bold",
  },
  textRight: {
    color: Colors.silver,
    textAlign: "right",
  },
  separator: {
    width: "100%",
    height: 10,
    backgroundColor: Colors.dimGray,
  },
  versionInfoContainer: {
    minHeight: 30,
    marginTop: 10,
    flex: 1,
  },
  versionInfoText: {
    textAlign: "center",
  },
});
