package com.toni.gwftest.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.toni.gwftest.login.model.User;
import com.toni.gwftest.view.LoginActivity;

/**
 * We use singleton pattern
 * SharedPrefManager for storing User information and Token
 */

public class SharedPrefManager {

    //the constants
    private static final String SHARED_PREF_NAME = "user_info";
    private static final String KEY_USERNAME = "keyusername";
    private static final String KEY_TOKEN = "keytoken";
    private static final String KEY_REFRESH = "keyrefresh";

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    /**
     * Method to let the user login
     * this method will store the user data in shared preferences
     */
    public void userLogin(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_TOKEN, user.getToken());
        editor.putString(KEY_REFRESH, user.getRefresh());
        editor.apply();
    }

    /**
     * this method will check whether user is already logged in or not
     */
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_TOKEN, null) != null;
    }

    /**
     * this method will give the logged in user
     */
    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getString(KEY_USERNAME, null),
                sharedPreferences.getString(KEY_TOKEN, null),
                sharedPreferences.getString(KEY_REFRESH, null)
        );
    }

    /**
     * this method will logout the user
     */
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, LoginActivity.class));
    }
}
