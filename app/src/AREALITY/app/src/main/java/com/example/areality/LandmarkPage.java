package com.example.areality;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class LandmarkPage extends Activity {

    private MyGLSurfaceView glView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_gl);

//        glView = new MyGLSurfaceView(this);           // Allocate a GLSurfaceView
//        setContentView(glView);

        setContentView(R.layout.activity_landmark_page);
        glView = (MyGLSurfaceView) findViewById(R.id.glsurfaceview);
        setInfo("Information about landmarks. Notes: " +
                "1. center image" +
                "2. Does line breaks work" +
                "3. ");

//        glView.setMyGLRenderer(this);
    }
    private void setInfo(String message){
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(message);
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
