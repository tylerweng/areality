package com.example.dianezheng.areality;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LandmarkPage extends Activity {

    private MyGLSurfaceView glView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_gl);

//        glView = new MyGLSurfaceView(this);           // Allocate a GLSurfaceView
//        setContentView(glView);

        setContentView(R.layout.activity_landmark_page);
        glView = (MyGLSurfaceView) findViewById(R.id.glsurfaceview);

//        glView.setMyGLRenderer(this);
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
