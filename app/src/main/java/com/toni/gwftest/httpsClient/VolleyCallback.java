package com.toni.gwftest.httpsClient;

/**
 * Interface to get response from Request
 */
public interface VolleyCallback {
    void onSuccess(String result) throws Exception;
    void onError(String result) throws Exception;
}
