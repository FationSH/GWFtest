package com.toni.gwftest.httpsClient;

import com.android.volley.VolleyError;

/**
 * Interface to get response from Request
 */
public interface VolleyCallback {
    void onSuccess(String result) throws Exception;
    void onError(VolleyError result) throws Exception;
}
