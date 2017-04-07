package com.example.areality;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MyGLActivity extends AppCompatActivity {


    private MyGLSurfaceView glView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_gl);

        glView = new MyGLSurfaceView(this);           // Allocate a GLSurfaceView
//        glView.setRenderer(new MyGLRenderer(this)); // Use a custom renderer
        this.setContentView(glView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        glView.onPause();
    }

    // Call back after onPause()
    @Override
    protected void onResume() {
        super.onResume();
        glView.onResume();
    }
}
