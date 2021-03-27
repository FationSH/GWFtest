package com.toni.gwftest.httpsClient;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
    private static VolleySingleton mInstance;
    private RequestQueue mRequestQueue;
    private boolean error;

    /**
     * Private constructor, only initialization from getInstance.
     * @param context parent context
     */
    private VolleySingleton(Context context){
        mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    /**
     * Singleton construct design pattern.
     * @param context parent context
     * @return single instance of VolleySingleton
     */
    public static synchronized VolleySingleton getInstance(Context context){
        if (mInstance == null){
            mInstance = new VolleySingleton(context);
        }
        return mInstance;
    }

    /**
     * Get current request queue.
     * @return RequestQueue
     */
    public RequestQueue getRequestQueue(){
        return mRequestQueue;
    }

    /**
     * Add new request depend on type like string, json object, json array request.
     * @param req new request
     * @param <T> request type
     */
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public void setError(boolean error){
        this.error = error;
    }

    public boolean getError(){
        return error;
    }
}
