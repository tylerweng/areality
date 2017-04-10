package areality.game;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignupActivity extends Activity {
    private static final String TAG = "SignupActivity";
    private static final int REQUEST_LOGIN = 0;
    private static final int REQUEST_MAP = 0;

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

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        if (pref.getString("username", "").length() == 0) {
            setContentView(R.layout.activity_signup);
            ButterKnife.bind(this);
        } else {
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            startActivityForResult(intent, REQUEST_MAP);
        }
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
        String[] emailChars = email.split("");
        StringBuilder newEmail = new StringBuilder();

        for (int i = 0; i < emailChars.length; i++) {
            if (emailChars[i].equals(".")) {
                newEmail.append("%2E");
            } else {
                newEmail.append(URLEncoder.encode(emailChars[i], "UTF-8"));
            }
        }

        String urlParameters = "username=" + URLEncoder.encode(name, "UTF-8")
                             + "&email=" + newEmail.toString()
                             + "&password=" + URLEncoder.encode(password, "UTF-8");

        HttpRequest pr = new HttpRequest("https://areality.herokuapp.com/api/signup", urlParameters, "POST");
        JSONObject result = new JSONObject(pr.execute());

        if (result.has("error")) {
            Log.d(TAG, "error: " + result.getString("error"));
            progressDialog.hide();
            if (result.getString("error").equals("That username is taken")) {
                onUsernameTaken();
            } else {
                onEmailRegistered();
            }
        } else {
            Log.d(TAG, "user: " + result);

            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
            SharedPreferences.Editor editor = pref.edit();
            Log.d(TAG, "points: " + result.get("points"));
            editor.putString("username", result.get("username").toString());
            editor.putString("email", result.get("email").toString());
            editor.putInt("points", Integer.valueOf(result.get("points").toString()));

            // add landmark ids
            editor.putInt("landmark_ids_size", 0);
            editor.apply();

            onSignupSuccess();
        }
    }

    public void onSignupSuccess() {
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        startActivityForResult(intent, REQUEST_MAP);
    }

    public void onUsernameTaken() {
        _nameText.setError("That username is taken");
        _signupButton.setEnabled(true);
    }

    public void onEmailRegistered() {
        _emailText.setError("That email is already registered");
        _signupButton.setEnabled(true);
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
