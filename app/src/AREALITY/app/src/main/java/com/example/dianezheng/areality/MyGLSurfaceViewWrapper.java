package com.example.dianezheng.areality;

import android.content.Context;
import android.view.View;

/**
 * Created by dianezheng on 4/4/17.
 */


public class MyGLSurfaceViewWrapper extends MyGLSurfaceView {
    private MyGLSurfaceView glView;

    public MyGLSurfaceViewWrapper(Context context){
        super(context);           // Allocate a GLSurfaceView
    }
}
