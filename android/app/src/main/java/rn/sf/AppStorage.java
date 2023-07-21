package rn.sf;

import android.util.Log;

import com.salesforce.androidsdk.smartstore.app.SmartStoreSDKManager;
import com.salesforce.androidsdk.smartstore.store.IndexSpec;
import com.salesforce.androidsdk.smartstore.store.QuerySpec;
import com.salesforce.androidsdk.smartstore.store.SmartStore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import rn.sf.user.SalesforceUser;
import rn.sf.user.SalesforceUserHelper;

public class AppStorage {
    private final int DEFAULT_SEQUENCE_SCAN_DELAY_IN_MILLISECONDS = 2000;
    private final String USER_SOUP = "User";
    private final String CURRENT_USER_SOUP = "CurrentUser";
    private final String USER_CONFIG_SOUP = "UserConfig";
    private final IndexSpec[] USERS_INDEX_SPEC = {
        new IndexSpec("UserName", SmartStore.Type.string),
        new IndexSpec("UseId", SmartStore.Type.string)
    };

    private final IndexSpec[] USER_CONFIGS_INDEX_SPEC = {
        new IndexSpec("UseId", SmartStore.Type.string)
    };

    private final SmartStore smartStore;

    public AppStorage() {
        smartStore = SmartStoreSDKManager.getInstance().getGlobalSmartStore();
        if(!smartStore.hasSoup(USER_SOUP)) {
            smartStore.registerSoup(USER_SOUP, USERS_INDEX_SPEC);
        }
        if(!smartStore.hasSoup(CURRENT_USER_SOUP)) {
            smartStore.registerSoup(CURRENT_USER_SOUP, USERS_INDEX_SPEC);
        }
        if(!smartStore.hasSoup(USER_CONFIG_SOUP)) {
            smartStore.registerSoup(USER_CONFIG_SOUP, USER_CONFIGS_INDEX_SPEC);
        }
    }

    public SalesforceUser GetCurrentUser() {
        try {
            QuerySpec querySpec = QuerySpec.buildAllQuerySpec(CURRENT_USER_SOUP, "UserName", QuerySpec.Order.ascending, 1);
            JSONArray userObjects = smartStore.query(querySpec, 0);
            if(userObjects.length() == 0) {
                return  null;
            }

            JSONObject userObject = userObjects.getJSONObject(0);
            return SalesforceUserHelper.ParseFromJSONObject(userObject);
        } catch (JSONException e) {
            Log.e("GetCurrentUser Exception", e.getMessage());
            return null;
        }
    }

    public void SaveLoggedUser(SalesforceUser user) {
        try {
            QuerySpec querySpec = QuerySpec.buildExactQuerySpec(USER_SOUP, "UserName", user.UserName, "UserName", QuerySpec.Order.ascending, 1);
            JSONArray exists = smartStore.query(querySpec, 1);

            if(exists.length() == 0) {
                smartStore.upsert(CURRENT_USER_SOUP, user.ToJSON());
            }
        } catch (JSONException e) {
            Log.e("SaveLoggedUser", e.getMessage());
            return;
        }

        SaveCurrentUser(user);
    }

    public void SaveCurrentUser(SalesforceUser user) {
        smartStore.clearSoup(CURRENT_USER_SOUP);
        if(user != null) {
            try {
                smartStore.upsert(CURRENT_USER_SOUP, user.ToJSON());
            } catch (JSONException e) {
                Log.e("SaveCurrentUser Exception", e.getMessage());
            }
        }
    }

    public int GetSequenceScanDelay() {
        try {
            QuerySpec querySpec = QuerySpec.buildAllQuerySpec(USER_CONFIG_SOUP, "UseId", QuerySpec.Order.ascending, 1);
            JSONArray userConfigs = smartStore.query(querySpec, 0);
            if(userConfigs.length() == 0) {
                return DEFAULT_SEQUENCE_SCAN_DELAY_IN_MILLISECONDS;
            }

            JSONObject userConfig = userConfigs.getJSONObject(0);
            return userConfig.getInt("SequenceScanDelay");
        } catch (JSONException e) {
            Log.e("GetSequenceScanDelay Exception", e.getMessage());
            return DEFAULT_SEQUENCE_SCAN_DELAY_IN_MILLISECONDS;
        }
    }

//    public void SetSequenceScanDelay(int value) {
//        QuerySpec querySpec = QuerySpec.buildAllQuerySpec(USER_CONFIG_SOUP, "UseId", QuerySpec.Order.ascending, 1);
//        try {
//            JSONArray userConfigs = smartStore.query(querySpec, 0);
//            if(userConfigs.length() == 0) {
//                return DEFAULT_SEQUENCE_SCAN_DELAY;
//            }
//
//            JSONObject userConfig = userConfigs.getJSONObject(0);
//            return userConfig.getInt("SequenceScanDelay");
//        } catch (JSONException e) {
//            Log.e("GetCurrentUser Exception", e.getLocalizedMessage());
//            return DEFAULT_SEQUENCE_SCAN_DELAY;
//        }
//    }
}
