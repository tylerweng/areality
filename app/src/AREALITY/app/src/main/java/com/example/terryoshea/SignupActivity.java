package com.example.terryoshea;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.dianezheng.areality.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

//    @InjectView(R.id.signupName)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
