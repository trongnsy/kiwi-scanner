package rn.sf.user;

import com.salesforce.androidsdk.app.SalesforceSDKManager;
import com.salesforce.androidsdk.rest.RestClient;

import org.json.JSONObject;

public final class SalesforceUserHelper {
    public static SalesforceUser GetSalesforceUser(RestClient client) {
        RestClient.ClientInfo clientInfo = client.getClientInfo();
        client.getJSONCredentials();

        SalesforceUser user = new SalesforceUser();
        user.AccessToken = client.getAuthToken();
        user.RefreshToken = client.getRefreshToken();
        user.UserId = clientInfo.userId;
        user.UserName = clientInfo.username;
        user.OrgId = clientInfo.orgId;
        user.LoginUrl = clientInfo.loginUrl.toString();
        user.InstanceUrl = clientInfo.instanceUrl.toString();
        user.IdentityUrl = clientInfo.identityUrl.toString();
        user.UserAgent = SalesforceSDKManager.getInstance().getUserAgent();
        user.CommunityId = clientInfo.communityId;
        user.CommunityUrl = clientInfo.communityUrl;

        return user;
    }

    public static SalesforceUser ParseFromJSONObject(JSONObject userObject) {
        SalesforceUser user = new SalesforceUser();

        user.AccessToken = userObject.optString("AccessToken");
        user.RefreshToken = userObject.optString("RefreshToken");
        user.UserId = userObject.optString("UserId");
        user.UserName = userObject.optString("UserName");
        user.OrgId = userObject.optString("OrgId");
        user.LoginUrl = userObject.optString("LoginUrl");
        user.IdentityUrl = userObject.optString("IdentityUrl");
        user.InstanceUrl = userObject.optString("InstanceUrl");
        user.UserAgent = userObject.optString("UserAgent");
        user.CommunityId = userObject.optString("CommunityId");
        user.CommunityUrl = userObject.optString("CommunityUrl");

        return user;
    }
}
