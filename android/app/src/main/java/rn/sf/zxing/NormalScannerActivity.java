package rn.sf.zxing;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.journeyapps.barcodescanner.BarcodeCallback;

public class NormalScannerActivity extends SequenceScannerActivity {
    private final String TAG = NormalScannerActivity.class.toString();

    @Override
    protected BarcodeCallback createBarcodeCallback() {
        return result -> {
            if (inDelayTime() || result.getText() == null) {
                return;
            }

            String message = result.getBarcodeFormat() + ": " + result.getText();
            Log.d(TAG, message);
            Toast.makeText(NormalScannerActivity.this, message, Toast.LENGTH_LONG).show();
            beepManager.playBeepSoundAndVibrate();
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
