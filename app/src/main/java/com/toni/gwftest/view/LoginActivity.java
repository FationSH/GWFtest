package com.toni.gwftest.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.google.android.material.textfield.TextInputLayout;
import com.toni.gwftest.R;
import com.toni.gwftest.httpsClient.LoginRequest;
import com.toni.gwftest.httpsClient.VolleyCallback;
import com.toni.gwftest.login.SharedPrefManager;
import com.toni.gwftest.login.model.User;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private TextInputLayout emailError, passError;
    private TextView pass_frg;
    private String login_url;

    private SharedPreferences username;
    private SharedPreferences.Editor editor;
    private LoginRequest loginRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginRequest = new LoginRequest(getApplicationContext());

        username = this.getPreferences(Context.MODE_PRIVATE);
        editor = username.edit();

        Button login;
        login_url = getString(R.string.login_url);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        emailError = (TextInputLayout) findViewById(R.id.emailError);
        passError = (TextInputLayout) findViewById(R.id.passError);
        pass_frg = (TextView) findViewById(R.id.passRecover);

        login = (Button) findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetValidation();
            }
        });

        email.setText(username.getString("email", ""));

        // Login if Enter key pressed on keyboard
        email.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                //If the keyevent is a key-down event on the "enter" button
                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    SetValidation();
                    return true;
                }
                return false;
            }
        });
        // Login if Enter key pressed on keyboard
        password.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent keyevent) {
                //If the keyevent is a key-down event on the "enter" button
                if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    SetValidation();
                    return true;
                }
                return false;
            }
        });

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        pass_frg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setMessage("Please contact an authorized GWF personnel.")
                        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Do nothing
                            }
                        });
                // Create the AlertDialog object and return it
                builder.create();
                builder.setTitle("Password Recovery");
                builder.show();
            }
        });
    }

    /**
     * Email and Password Validator
     */
    private void SetValidation() {
        boolean isEmailValid, isPasswordValid;
        // Check for a valid email address.
        if (email.getText().toString().isEmpty()) {
            emailError.setError(getResources().getString(R.string.email_error));
            isEmailValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            emailError.setError(getResources().getString(R.string.error_invalid_email));
            isEmailValid = false;
        } else if (email.getText().length() < 4){
            emailError.setError(getResources().getString(R.string.error_invalid_email));
            isEmailValid = false;
        } else  {
            isEmailValid = true;
            emailError.setErrorEnabled(false);
        }

        // Check for a valid password.
        if (password.getText().toString().isEmpty()) {
            passError.setError(getResources().getString(R.string.password_error));
            isPasswordValid = false;
        } else if (password.getText().length() < 6) {
            passError.setError(getResources().getString(R.string.error_invalid_password));
            isPasswordValid = false;
        } else  {
            isPasswordValid = true;
            passError.setErrorEnabled(false);
        }

        if (isEmailValid && isPasswordValid) {
            // if internet connection exist
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()) {
                login();
            } else {
                Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Handle login response
     */
    private void login(){
        // Check internet connection
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            Toast.makeText(getApplicationContext(), "No internet.", Toast.LENGTH_SHORT).show();
        } else {

            loginRequest.userLogin(login_url, email.getText().toString(), password.getText().toString(), new VolleyCallback() {
                @Override
                public void onSuccess(String response) {
                    try {
                        // get response
                        JSONObject objson = new JSONObject(response);
                        if (objson.has("access")) {
                            //creating a new user object
                            User user = new User(email.getText().toString(),
                                    objson.getString("access"),
                                    objson.getString("refresh"));
                            //store the user in shared preferences
                            SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);

                            // Save Username
                            editor.putString("email", email.getText().toString());
                            editor.apply();

                            // Start Main Activity
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            finish();
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Error occurred", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Throwable t) {
                        Log.e("My App", "Could not parse malformed JSON: \"" + response + "\"");
                    }
                }

                @Override
                public void onError(VolleyError result) {
                    Toast.makeText(getApplicationContext(), "Something went wrong, try again.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
