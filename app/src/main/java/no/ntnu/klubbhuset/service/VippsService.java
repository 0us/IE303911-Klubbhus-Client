package no.ntnu.klubbhuset.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VippsService {
    public static final String CLIENT_ID = ""; // todo get id
    public static final String CLIENT_SECRET = ""; // todo
    public static final String OCP_APIM_SUBSCRIPTION_KEY = ""; //todo
    public static final String VIPPS_API_URL = "https://apitest.vipps.no";
    public static final String AUTHORIZATION = "Authorization";

    private RequestQueue queue;
    private Context context; // calling class needs to give context;
    SharedPreferences preferences = context.getSharedPreferences("vipps", Context.MODE_PRIVATE);
    String authToken = preferences.getString("token", null);

    public VippsService(Context context) {
        this.context = context;
    }

    /**
     * Gets access token from vipps and stores in the sharedPreferenses vipps under token. This is basically an json object stored as a string
     */
    public void getAccessToken() {
        final String METHOD_URL = VIPPS_API_URL + "/accessToken/get";
        JsonObjectRequest request = new JsonObjectRequest(METHOD_URL, null,
                response -> {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("token", response.toString());
                },
                error -> {
                }) // todo implement error handling
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("client_id", CLIENT_ID);
                headers.put("client_secret", CLIENT_SECRET);
                headers.put("Ocp-Apim-Subscription-Key", OCP_APIM_SUBSCRIPTION_KEY);
                return headers;
            }
        };

        queue.add(request);
    }

    public void initiatePayment() {
        final String METHOD_URL = VIPPS_API_URL + "/ecomm/v2/payments/";
        JSONObject details = new JSONObject();

        if (authToken != null) {
            try {
                JSONObject merchantInfo = new JSONObject();
                merchantInfo.put("merchantSerialNumber", merchantSerialNumber);
                merchantInfo.put("callbackPrefix", callbackPrefix);
                merchantInfo.put("fallBack", fallBack);
                merchantInfo.put("authToken", authToken);
                merchantInfo.put("isApp", true);

                JSONObject customerInfo = new JSONObject();
                customerInfo.put("mobileNumber", mobileNumber);

                JSONObject transaction = new JSONObject();
                transaction.put("orderId", orderId);
                transaction.put("amount", amount);
                transaction.put("transactionText", transactionText);
                transaction.put("skipLandingPage", false);

                details.put("merchantInfo", merchantInfo);
                details.put("customerInfo", customerInfo);
                details.put("transaction", transaction);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest request = new JsonObjectRequest(METHOD_URL, details,
                    response -> {
                        try {
                            String vippsURL = response.getString("url"); // todo don't know what we do with this. Can try to open it via intent or pase it to calling class
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {
                    }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Ocp-Apim-Subscription-Key", OCP_APIM_SUBSCRIPTION_KEY);
                    headers.put(AUTHORIZATION, "Bearer " + authToken);
                    return headers;
                }
            };
            queue.add(request);
        }

    }

    public void getOrderStatus(String orderId) {
        final String METHOD_URL = VIPPS_API_URL + "/ecomm/v2/payments/" + orderId + "/status";
        JsonObjectRequest request = new JsonObjectRequest(METHOD_URL, null,
                response -> {

                },
                error -> {
                }) // todo implement error handling
        {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put(AUTHORIZATION, authToken);
                headers.put("Ocp-Apim-Subscription-Key", OCP_APIM_SUBSCRIPTION_KEY);
                return headers;
            }
        };

        queue.add(request);
    }
}
