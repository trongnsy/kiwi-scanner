package rn.sf.zxing;

import static android.view.View.INVISIBLE;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;
import com.journeyapps.barcodescanner.camera.CameraSettings;

import java.util.Arrays;
import java.util.Collection;

import rn.sf.AppStorage;
import rn.sf.R;

/**
 * This sample performs continuous scanning, displaying the barcode and source image whenever
 * a barcode is scanned.
 */
public abstract class SequenceScannerActivity extends Activity {
    protected DecoratedBarcodeView barcodeView;
    protected BeepManager beepManager;
    protected AppStorage storage;

    private Boolean isTorchOn = false;
    private long lastScanCompletedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.continuous_scan);

        storage = new AppStorage();
        beepManager = new BeepManager(this);
        beepManager.setVibrateEnabled(true);

        View customTitleView = findViewById(R.id.custom_toolbar);
        customTitleView.bringToFront();

        BarcodeCallback callback = createBarcodeCallback();
        DefaultDecoderFactory decoderFactory = new DefaultDecoderFactory(getBarcodeFormats());
        barcodeView = findViewById(R.id.barcode_scanner);
        barcodeView.getBarcodeView().setDecoderFactory(decoderFactory);
        barcodeView.getStatusView().setVisibility(INVISIBLE);
        barcodeView.initializeFromIntent(getIntent());
        barcodeView.decodeContinuous(callback);
    }

    @Override
    public void onResume() {
        super.onResume();
        barcodeView.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        barcodeView.pause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    protected Collection<BarcodeFormat> getBarcodeFormats() {
        return Arrays.asList(BarcodeFormat.CODE_39,
            BarcodeFormat.CODE_93,
            BarcodeFormat.CODE_128,
            BarcodeFormat.DATA_MATRIX,
            BarcodeFormat.CODABAR,
            BarcodeFormat.ITF,
            BarcodeFormat.EAN_8,
            BarcodeFormat.EAN_13,
            BarcodeFormat.QR_CODE,
            BarcodeFormat.RSS_14,
            BarcodeFormat.RSS_EXPANDED,
            BarcodeFormat.AZTEC,
            BarcodeFormat.PDF_417,
            BarcodeFormat.UPC_A,
            BarcodeFormat.UPC_E,
            BarcodeFormat.UPC_EAN_EXTENSION,
            BarcodeFormat.MAXICODE);
    }

    protected BarcodeCallback createBarcodeCallback() {
        return result -> { };
    }

    protected boolean inDelayTime() {
        int sequenceScanDelay = storage.GetSequenceScanDelay();
        long currentTime = System.currentTimeMillis();
        boolean inDelayTime = lastScanCompletedTime > 0 && currentTime - lastScanCompletedTime < sequenceScanDelay;

        if(!inDelayTime) {
            lastScanCompletedTime = currentTime;
        }

        return inDelayTime;
    }

    public void switchCamera(View view) {
        barcodeView.pause();

        CameraSettings cameraSettings = barcodeView.getCameraSettings();
        int nextCameraId = cameraSettings.getRequestedCameraId() == 1 ? 0 : 1;
        cameraSettings.setRequestedCameraId(nextCameraId);
        barcodeView.setCameraSettings(cameraSettings);

        barcodeView.resume();
    }

    public void toggleTorch(View view) {
        isTorchOn = !isTorchOn;
        if (isTorchOn) {
            barcodeView.setTorchOn();
        } else {
            barcodeView.setTorchOff();
        }
    }
}
