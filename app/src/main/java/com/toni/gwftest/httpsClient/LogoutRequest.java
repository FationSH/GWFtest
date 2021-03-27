package com.toni.gwftest.httpsClient;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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

public class LogoutRequest {

    private Context context;
    private User prefs;

    public LogoutRequest(Context context){
        this.context = context;
    }

    /**
     * No Logout link (DELETE)
     */
    public void userLogout(String logout_url, final VolleyCallback callback) {
        RequestQueue mQueue = VolleySingleton.getInstance(context).getRequestQueue();
        prefs = SharedPrefManager.getInstance(context).getUser();

        StringRequest request = new StringRequest(Request.Method.DELETE, logout_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error occurred", Toast.LENGTH_SHORT).show();
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
