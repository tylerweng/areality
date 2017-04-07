package com.example.areality;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by dianezheng on 4/4/17.
 */

class MyGLSurfaceView extends GLSurfaceView {
    private MyGLRenderer mRenderer;

    public MyGLSurfaceView(Context context) {
        super(context);
        init(context);
    }

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {

        mRenderer = new MyGLRenderer(context);
        setRenderer(mRenderer);
    }


//
//    public MyGLSurfaceView(Context context){
//            super(context);
//
//    // Create an OpenGL ES 2.0 context
////        setEGLContextClientVersion(2);
//
//    mRenderer = new MyGLRenderer(context);
//
//    //         Set the Renderer for drawing on the GLSurfaceView
//    setRenderer(mRenderer);
//
//
//    }

    public void setMyGLRenderer(Context context){
        mRenderer = new MyGLRenderer(context);
        setRenderer(mRenderer);
    }


    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;
    private float mPreviousY;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:

                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

//                // reverse direction of rotation above the mid-line
//                if (y > getHeight() / 2) {
//                    dx = dx * -1 ;
//                }
//
//                // reverse direction of rotation to left of the mid-line
//                if (x < getWidth() / 2) {
//                    dy = dy * -1 ;
//                }
                mRenderer.setRot(dy/100, dx/10);
                mRenderer.setAngle(
                        mRenderer.getAngle() +
                                ((dx + dy) * TOUCH_SCALE_FACTOR));
                requestRender();
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }

    public void autoStart(){
        float speedCube = 0.5f; // rotational speed for cube
        mRenderer.setSpeedCube(speedCube);
    }


}