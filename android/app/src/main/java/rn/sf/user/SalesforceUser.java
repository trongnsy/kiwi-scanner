package rn.sf.user;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SalesforceUser {
    public String AccessToken;
    public String RefreshToken;
    public String UserId;
    public String UserName;
    public String OrgId;
    // Change to URI type if need
    public String LoginUrl;
    // Change to URI type if need
    public String IdentityUrl;
    // Change to URI type if need
    public String InstanceUrl;
    public String UserAgent;
    public String CommunityId;
    // Change to URI type if need
    public String CommunityUrl;

    public JSONObject ToJSON() {
        Map<String, String> data = new HashMap<>();
        data.put("AccessToken", AccessToken);
        data.put("RefreshToken", RefreshToken);
        data.put("UserId", UserId);
        data.put("UserName", UserName);
        data.put("OrgId", OrgId);
        data.put("LoginUrl", LoginUrl);
        data.put("IdentityUrl", IdentityUrl);
        data.put("InstanceUrl", InstanceUrl);
        data.put("UserAgent", UserAgent);
        data.put("CommunityId", CommunityId);
        data.put("CommunityUrl", CommunityUrl);
        return new JSONObject(data);
    }
}
