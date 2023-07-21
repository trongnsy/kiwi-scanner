package rn.sf.zxing;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.salesforce.androidsdk.reactnative.app.SalesforceReactSDKManager;
import com.salesforce.androidsdk.rest.RestClient;
import com.salesforce.androidsdk.rest.RestRequest;
import com.salesforce.androidsdk.rest.RestResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * This sample performs continuous scanning, displaying the barcode and source image whenever
 * a barcode is scanned.
 */
public class ContinuousScannerActivity extends SequenceScannerActivity {
    private final String TAG = ContinuousScannerActivity.class.toString();
    private final ArrayList<String> scannedContents = new ArrayList<>();
    private final ArrayList<String> scannedTypes = new ArrayList<>();
    private String scannedContent = "";
    private String scannedType = "";
    private boolean lastScanCompleted = true;

    @Override
    protected BarcodeCallback createBarcodeCallback() {
        return new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                if(!lastScanCompleted || inDelayTime() || result.getText() == null) {
                    return;
                }

                lastScanCompleted = false;
                scannedType = result.getBarcodeFormat().toString();
                scannedContent = result.getText();

                Log.d(TAG, scannedType + ": " + scannedContent);
                beepManager.playBeepSoundAndVibrate();

                validateResultByContinuousScanService();
            }

            private void validateResultByContinuousScanService() {
                RestClient client = SalesforceReactSDKManager.getInstance().getClientManager().peekRestClient();
                // TODO: get the url path from salesforce user info and query params
                String path = "https://freelancer-1c4-dev-ed.my.salesforce.com/services/apexrest/gcbc_continuousscandemo/";

                String content = new Gson().toJson(createBarcodeScanData());
                RequestBody body = RequestBody.create(content, MediaType.parse("application/json"));

                Map<String, String> additionalHttpHeaders = new HashMap<>();
                String authorization = "Bearer " + storage.GetCurrentUser().AccessToken;
                additionalHttpHeaders.put("Authorization", authorization);

                RestRequest request = new RestRequest(RestRequest.RestMethod.POST, path, body, additionalHttpHeaders);
                client.sendAsync(request, new RestClient.AsyncRequestCallback() {
                    @Override
                    public void onSuccess(RestRequest request, RestResponse response) {
                        try {
                            Log.d(TAG, "ContinuousScanService onSuccess: " + response.asString());
                            BarcodeScanResponse barcodeScanResponse = new Gson().fromJson(response.asString(), BarcodeScanResponse.class);
                            onContinuousScanServiceCompleted(barcodeScanResponse);
                        } catch (IOException e) {
                            onError(e);
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.d(TAG, "ContinuousScanService onError: " + e.getMessage());
                        BarcodeScanResponse barcodeScanResponse = new BarcodeScanResponse(true, false);
                        onContinuousScanServiceCompleted(barcodeScanResponse);
                    }
                });
            }

            private BarcodeScanData createBarcodeScanData() {
                BarcodeScanData data = new BarcodeScanData();
                data.scannedResult = scannedContent;
                data.scannedType = scannedType;
                data.preservedScannedContent = scannedContents.toArray(new String[0]);
                data.preservedScannedType = scannedTypes.toArray(new String[0]);

                return data;
            }

            private void onContinuousScanServiceCompleted(BarcodeScanResponse response) {
                Handler mainHandler = new Handler(Looper.getMainLooper());
                Runnable runnable = () -> {
                    Toast.makeText(ContinuousScannerActivity.this, response.displayMessage, Toast.LENGTH_LONG).show();
                    if(response.shouldContinueScan) {
                        scannedTypes.add(scannedType);
                        scannedContents.add(scannedContent);

                        lastScanCompleted = true;
                    } else {
                        scannedTypes.clear();
                        scannedContents.clear();

                        barcodeView.getBarcodeView().stopDecoding();
                        ContinuousScannerActivity.this.finish();
                    }
                };

                mainHandler.post(runnable);
            }
        };
    }
}

