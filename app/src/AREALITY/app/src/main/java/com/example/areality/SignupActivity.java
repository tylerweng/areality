package com.example.areality;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import javax.net.ssl.HttpsURLConnection;

public class SignupActivity extends Activity {
    private static final String TAG = "SignupActivity";
    private static final int REQUEST_LOGIN = 0;

    @BindView(R.id.signupName) EditText _nameText;
    @BindView(R.id.signupEmail) EditText _emailText;
    @BindView(R.id.signupPassword) EditText _passwordText;
    @BindView(R.id.signupButton) Button _signupButton;

    @OnClick(R.id.loginLink) void switchToLogin() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivityForResult(intent, REQUEST_LOGIN);
    }

    @OnClick(R.id.signupButton) void submit() {
        try {
            signup();
        } catch(Exception e) {
            Log.d(TAG, "request could not be completed: " + e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
    }

    public void signup() throws Exception {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        // add a new style theme to the progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this, R.style.AppTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating account...");
        progressDialog.show();

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        Log.d(TAG, name);
        Log.d(TAG, email);
        Log.d(TAG, password);

        // make HTTP post request

//        String url = "https://areality.herokuapp.com/api/signup";
//        URL areality = new URL(url);
//        HttpsURLConnection con = (HttpsURLConnection) areality.openConnection();
//
//        con.setRequestMethod("POST");
//        con.setRequestProperty("User-Agent", "Mozilla/5.0");
//
//        String urlParameters = "username=" + name + "&password=" + password;
//
//        con.setDoOutput(true);
//        DataOutputStream dos = new DataOutputStream(con.getOutputStream());
//        dos.writeBytes(urlParameters);
//        dos.flush();
//        dos.close();
//
//        int responseCode = con.getResponseCode();
//        Log.d(TAG, "\nSending 'POST' request to URL: " + url);
//        Log.d(TAG, "Post parameters: " + urlParameters);
//        Log.d(TAG, "Response code: " + responseCode);
//
//        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//        String inputLine;
//        StringBuffer response = new StringBuffer();
//
//        while ((inputLine = in.readLine()) != null) {
//            response.append(inputLine);
//        }
//        in.close();
//
//        Log.d(TAG, response.toString());

        // implement signup logic
    }

    private class PostUser extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... data) {
//            HttpClient httpclient = new DefaultHttpClient();
//            HttpPost httppost = new HttpPost("https://areality.herokuapp.com/api/signup");

            try {
                URL areality = new URL("https://areality.herokuapp.com/api/signup");
                HttpsURLConnection con = (HttpsURLConnection) areality.openConnection();

                con.setRequestMethod("POST");
                con.setRequestProperty("User-Agent", "Mozilla/5.0");
                con.setDoOutput(true);
                con.setRequestProperty("User-Agent", "Mozilla/5.0");

                String urlParameters = "username=" + data[0] + "&password=" + data[1];

                con.setDoOutput(true);
                DataOutputStream dos = new DataOutputStream(con.getOutputStream());
                dos.writeBytes(urlParameters);
                dos.flush();
                dos.close();

                int responseCode = con.getResponseCode();
                Log.d(TAG, "\nSending 'POST' request to URL: " + url);
                Log.d(TAG, "Post parameters: " + urlParameters);
                Log.d(TAG, "Response code: " + responseCode);

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                Log.d(TAG, response.toString());
            }
        }
    }

    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("At least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

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
