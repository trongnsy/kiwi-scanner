package rn.sf.zxing;

class BarcodeScanResponse {
    // A boolean value, true means current scan will be preserved, otherwise, false. If set to true, the current scan result
    // will be stored in the preserved scanned content.
    Boolean preserveScanResult;
    // The string message that could echo to the scanner screen.
    String displayMessage;
    // A boolean value, true indicates continue with another scan, otherwise, false.
    Boolean shouldContinueScan;
    // A boolean value, true to notify an error beep to the device.
    Boolean notifyErrorFeedback;

    public BarcodeScanResponse() {
        // By default, continue scan.
        this.shouldContinueScan = true;
        this.preserveScanResult = false;
        this.displayMessage = "";
        this.notifyErrorFeedback = false;
    }

    public BarcodeScanResponse(boolean shouldContinueScan, boolean preserveScanResult) {
        this.shouldContinueScan = shouldContinueScan;
        this.preserveScanResult = preserveScanResult;
    }
}
