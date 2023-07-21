/*
 * Copyright (c) 2020-present, salesforce.com, inc.
 * All rights reserved.
 * Redistribution and use of this software in source and binary forms, with or
 * without modification, are permitted provided that the following conditions
 * are met:
 * - Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * - Neither the name of salesforce.com, inc. nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission of salesforce.com, inc.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package rn.sf;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;

import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanIntentResult;
import com.journeyapps.barcodescanner.ScanOptions;
import com.salesforce.androidsdk.reactnative.ui.SalesforceReactActivity;
import com.salesforce.androidsdk.rest.RestClient;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collection;

import rn.sf.user.SalesforceUser;
import rn.sf.user.SalesforceUserHelper;
import rn.sf.zxing.ContinuousScannerActivity;
import rn.sf.zxing.NormalScannerActivity;
import rn.sf.zxing.ScannerActivity;

public class MainActivity extends SalesforceReactActivity {
	private ScannerType _scannerType = ScannerType.Normal;
	private SalesforceUser _currentUser;
	private AppStorage _storage;
	private String TAG = "MainActivity";
    /**
     *
     * @return true if you want login to happen when application launches
     *         false otherwise
     */
	@Override
	public boolean shouldAuthenticate() {
//		Uri fromUri = getReferrer();
		Log.i(TAG, "shouldAuthenticate called");
		try {
			SaveUserInfo();
		} catch (JSONException e) {
			return true;
		}
		return true;
	}

	/**
	 * Returns the name of the main component registered from JavaScript.
	 * This is used to schedule rendering of the component.
	 */
	@Override
	protected String getMainComponentName() {
		return "rn-sf";
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		customizeTitleView();
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate called");
		_storage = new AppStorage();
	}

	private void customizeTitleView() {
		try {
			// retrieve value for com.android.internal.R.id.title_container(=0x1020149)
			int titleContainerId = (Integer) Class.forName(
					"com.android.internal.R$id").getField("title_container").get(null);

			// remove all views from titleContainer
			((ViewGroup) getWindow().findViewById(titleContainerId)).removeAllViews();
			getWindow().setFeatureInt( Window.FEATURE_CUSTOM_TITLE, R.layout.custom_toolbar);
		} catch (Exception exception) {
			Log.e(TAG, exception.getMessage());
		}
	}

	private void SaveUserInfo() throws JSONException {
		RestClient client = getRestClient();
		Log.i(TAG, "SaveUserInfo called");
		Log.i(TAG, client == null ? "client is null" : client.toString());
		if(client != null) {
			SalesforceUser user = SalesforceUserHelper.GetSalesforceUser(client);
			_storage.SaveLoggedUser(user);
		}
	}

	private ScanOptions getScanOptions() {
		ScanOptions options = new ScanOptions();
		Collection<String> formats = new ArrayList(ScanOptions.ONE_D_CODE_TYPES);
		formats.add(ScanOptions.QR_CODE);
		formats.add(ScanOptions.PDF_417);
		formats.add(ScanOptions.DATA_MATRIX);
		options.setDesiredBarcodeFormats(formats);
		options.setOrientationLocked(true);
		options.setBeepEnabled(true);

		options.setCaptureActivity(ScannerActivity.class);

		switch (_scannerType) {
			case Normal:
				options.setPrompt("Normal Scanner");
				break;
			case Single:
				options.setPrompt("Single Scanner");
				break;
			case Continuous:
				options.setPrompt("Continuous Scanner");
				break;
			default:
				break;
		}

		return  options;
	}

	private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
			(ScanIntentResult result) -> {
				if(result.getContents() == null) {
					Intent originalIntent = result.getOriginalIntent();
					if (originalIntent == null) {
						Log.d("MainActivity", "Cancelled scan");
						Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
					} else if(originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)) {
						Log.d("MainActivity", "Cancelled scan due to missing camera permission");
						Toast.makeText(MainActivity.this, "Cancelled due to missing camera permission", Toast.LENGTH_LONG).show();
					}
				} else {
					Log.d("MainActivity", "Scanned");
					Toast.makeText(MainActivity.this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
				}
			});

	public void scan() {
		ScanOptions options = getScanOptions();
		barcodeLauncher.launch(options);
	}

	public void launchNormalScanner() {
		Intent intent = new Intent(this, NormalScannerActivity.class);
		startActivity(intent);
	}

	public void launchContinuousScanner() {
		Intent intent = new Intent(this, ContinuousScannerActivity.class);
		startActivity(intent);
	}
}
