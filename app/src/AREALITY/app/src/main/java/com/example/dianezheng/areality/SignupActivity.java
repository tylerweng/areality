package com.example.dianezheng.areality;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

import java.net.*;
import java.io.*;

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
        signup();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
    }

    public void signup() {
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

        // implement signup logic
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
