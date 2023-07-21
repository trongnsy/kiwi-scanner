package rn.sf.zxing;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.google.zxing.client.android.Intents;

import java.util.ArrayList;
import java.util.List;

import rn.sf.MainActivity;

public class ScannerModule extends ReactContextBaseJavaModule implements ActivityEventListener {

    private final ReactApplicationContext mReactContext;
    private Callback mCallback;

    public ScannerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mReactContext = reactContext;
    }

    @Override
    public String getName() {
        return "ScannerModule";
    }

    @ReactMethod
    void openScanner(Callback callback) {
        mCallback = callback;
        Activity activity = getCurrentActivity();

        if (activity instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) activity;
            mainActivity.launchNormalScanner();

            mReactContext.addActivityEventListener(this);
        }
    }

    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        Log.d("NormalScannerActivity", String.valueOf(resultCode));
        if (resultCode == Activity.RESULT_OK) {
            String contents = data.getStringExtra(Intents.Scan.RESULT);
            String formatName = data.getStringExtra(Intents.Scan.RESULT_FORMAT);
            mCallback.invoke(formatName, contents);

            // Remove the listener since we are removing this activity.
            mReactContext.removeActivityEventListener(this);
        }
    }

    @Override
    public void onNewIntent(Intent intent) {}
}
