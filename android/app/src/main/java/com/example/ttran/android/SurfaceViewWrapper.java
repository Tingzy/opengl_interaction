package com.example.ttran.android;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

public class SurfaceViewWrapper extends GLSurfaceView{
    RendererWrapper renderer_;
    public SurfaceViewWrapper (Context context){
        super(context);
        renderer_ = new RendererWrapper();
        setEGLContextClientVersion(2);
        setRenderer(renderer_);
        //System.out.println(context.getResources().getDisplayMetrics().heightPixels);
        //System.out.println(context.getResources().getDisplayMetrics().widthPixels);

    }
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = 2.0f*e.getRawX()/1080f -1f;
        float y = 1f - 2.0f*e.getRawY()/2094f;
        //renderer_.state = renderer_.updateState(renderer_.state);
        renderer_.updateVerticesOntouch(x,y);
        //System.out.println(x);
        //System.out.println(y);
        return super.onTouchEvent(e);
    }
}
