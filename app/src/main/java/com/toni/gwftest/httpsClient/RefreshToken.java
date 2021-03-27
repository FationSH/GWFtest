package com.toni.gwftest.httpsClient;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.toni.gwftest.login.SharedPrefManager;
import com.toni.gwftest.login.model.User;
import com.toni.gwftest.view.MetersFragment;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RefreshToken {

    private Context context;

    public RefreshToken(Context context){
        this.context = context;
    }

    /**
     * Refresh token
     */
    public void refreshToken(String refresh_url, Fragment frag, FragmentTransaction ft) {
        RequestQueue mQueue = VolleySingleton.getInstance(context).getRequestQueue();

        StringRequest request = new StringRequest(Request.Method.POST, refresh_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject objson = new JSONObject(response);
                    if (objson.has("access")) {
                        SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(context);
                        User user = sharedPrefManager.getUser();
                        user.setToken(objson.getString("access"));
                        sharedPrefManager.userLogin(user);
                    }
                } catch (Throwable t) {
                    Log.e("My App", "Could not parse refresh token: \"" + response + "\"");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                SharedPrefManager.getInstance(context).logout();
                ft.detach(frag);
                ft.commit();
            }
        }) {
            @Override
            public Priority getPriority() {
                return Priority.HIGH;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("refresh", SharedPrefManager.getInstance(context).getUser().getRefresh());
                return params;
            }
        };
        mQueue.add(request);
    }

}
