package com.example.areality;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URLEncoder;

import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends Activity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_MAP = 0;
    private static final int REQUEST_SIGNUP = 0;

    @BindView(R.id.loginEmail) EditText _emailText;
    @BindView(R.id.loginPassword) EditText _passwordText;
    @BindView(R.id.loginButton) Button _loginButton;

    @OnClick(R.id.loginButton) void submit() {
        try {
            login();
        } catch(Exception e) {
            Log.d(TAG, "request could not be completed: " + e);
        }
    }

    @OnClick(R.id.signupLink) void switchToSignup() {
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivityForResult(intent, REQUEST_SIGNUP);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    public void login() throws Exception {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        // add a new style theme to the progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Logging in...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        Log.d(TAG, email);
        Log.d(TAG, password);

        // make HTTP post request
        String[] emailChars = email.split("");
        StringBuilder newEmail = new StringBuilder();

        for (int i = 0; i < emailChars.length; i++) {
            if (emailChars[i].equals(".")) {
                newEmail.append("%2E");
            } else {
                newEmail.append(URLEncoder.encode(emailChars[i], "UTF-8"));
            }
        }

        String urlParameters = "email=" + newEmail.toString()
                             + "&password=" + URLEncoder.encode(password, "UTF-8");

        PostRequest pr = new PostRequest("https://areality.herokuapp.com/api/signup", urlParameters);

        String resultString = pr.execute();
        JSONObject result = new JSONObject(resultString);

        Log.d(TAG, "result string: " + resultString);

        if (result.has("error")) {
            Log.d(TAG, "error: " + result.getString("error"));
            progressDialog.hide();
            if (result.getString("error").equals("That user could not be found")) {
                onUserNotFound();
            } else {
                onIncorrectPassword();
            }
        } else {
            Log.d(TAG, "user: " + result);
            onLoginSuccess();
        }
    }

    public void onUserNotFound() {
        Toast.makeText(getBaseContext(), "That user could not be found", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

    public void onIncorrectPassword() {
        _passwordText.setError("That username is taken");
        _loginButton.setEnabled(true);
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

    public void onLoginSuccess() {
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        startActivityForResult(intent, REQUEST_MAP);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("invalid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 15) {
            _passwordText.setError("between 6 and 15 characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
