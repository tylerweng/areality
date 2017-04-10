package areality.game;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by dianezheng on 4/4/17.
 */

public class MyGLSurfaceView extends GLSurfaceView {
    private MyGLRenderer mRenderer;
    private Context ctx;

    public MyGLSurfaceView(Context context) {
        super(context);
        ctx = context;
    }

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        ctx = context;

    }

    private void init(Context context, String[] photos) {

        mRenderer = new MyGLRenderer(context, photos);
        setRenderer(mRenderer);
    }

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;
    private float mPreviousY;

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:

                float dx = x - mPreviousX;
                float dy = y - mPreviousY;
                mRenderer.setRot(dy/10, dx/10);

                mRenderer.setAngle(
                        mRenderer.getAngle() +
                                ((dx + dy) * TOUCH_SCALE_FACTOR));
                requestRender();
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }

    public void setPhotos(String[] photos){
        init(ctx, photos);

    }


}