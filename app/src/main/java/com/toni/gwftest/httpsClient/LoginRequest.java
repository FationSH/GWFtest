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

import java.util.HashMap;
import java.util.Map;

public class LoginRequest {

    private Context context;

    public LoginRequest(Context context){
        this.context = context;
    }

    /**
     * Login and retrieve token
     * with Volley Android Library
     * after v19 (Android 5), TLS 1.2 is enabled by default.
     */
    public void userLogin(String login_url, String email, String password, final VolleyCallback callback) {
        RequestQueue mQueue = VolleySingleton.getInstance(context).getRequestQueue();

        StringRequest request = new StringRequest(Request.Method.POST, login_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // Response from POST
                    if (response != null) {
                        callback.onSuccess(response);
                    }
                } catch (Throwable t) {
                    Log.e("My App", "Could not parse token: \"" + response + "\"");
                }
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
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };
        mQueue.add(request);
    }

}
