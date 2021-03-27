package com.toni.gwftest.httpsClient;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.toni.gwftest.login.SharedPrefManager;
import com.toni.gwftest.login.model.User;

import java.util.HashMap;
import java.util.Map;

public class GetMetersRequest {

    private Context context;
    private User prefs;

    public GetMetersRequest(Context context){
        this.context = context;
    }

    /**
     * Get Data
     */
    public void getMeters(String get_url, final VolleyCallback callback) {
        RequestQueue mQueue = VolleySingleton.getInstance(context).getRequestQueue();
        prefs = SharedPrefManager.getInstance(context).getUser();

        StringRequest request = new StringRequest(Request.Method.GET, get_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Response from POST
                if (response != null) {
                    try {
                        callback.onSuccess(response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO get refresh token
            }
        }) {
            @Override
            public Priority getPriority() {
                return Priority.HIGH;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer "+ prefs.getToken());
                return headers;
            }
        };
        mQueue.add(request);
    }

}
