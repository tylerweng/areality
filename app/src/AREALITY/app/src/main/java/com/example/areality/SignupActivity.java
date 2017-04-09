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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.nio.CharBuffer;
import java.util.ArrayList;

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

        String[] data = new String[3];
        data[0] = name;
        data[1] = email;
        data[2] = password;

        PostUser pu = new PostUser();
        pu.execute(data);
    }

    private class PostUser extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... data) {
            try {
                URL areality = new URL("https://areality.herokuapp.com/api/signup");
                HttpsURLConnection con = (HttpsURLConnection) areality.openConnection();

                con.setRequestMethod("POST");
                con.setRequestProperty("User-Agent", "Mozilla/5.0");
                con.setDoOutput(true);

                char[] emailChars = data[1].toCharArray();
                int numPeriods = 0;
                for (int i = 0; i < emailChars.length; i++) {
                    if (emailChars[i] == '.') numPeriods++;
                }

                CharBuffer newEmail = CharBuffer.allocate(emailChars.length + (2 * numPeriods));
                for (int i = 0; i < emailChars.length; i++) {
                    if (emailChars[i] == '.') {
                        newEmail.append('%');
                        newEmail.append('2');
                        newEmail.append('E');
                    } else {
                        newEmail.append(emailChars[i]);
                    }
                }

                Log.d(TAG, "CharBuffer toString(): " + newEmail.toString());

                String urlParameters = "username=" + URLEncoder.encode(data[0], "UTF-8")
                                     + "&email=" + URLEncoder.encode(newEmail.toString(), "UTF-8")
                                     + "&password=" + URLEncoder.encode(data[2], "UTF-8");

                con.setDoOutput(true);
                DataOutputStream dos = new DataOutputStream(con.getOutputStream());
                dos.writeBytes(urlParameters);
                dos.flush();
                dos.close();

                int responseCode = con.getResponseCode();
                Log.d(TAG, "\nSending 'POST' request to URL: " + areality);
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
                return "yes";
            } catch (Exception e) {
                return "no";
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
