package com.example.ttran.android;

public class GameLibJNIWrapper {
    static {
        System.loadLibrary("game");
    }

    public static native void on_surface_created();

    public static native void on_surface_changed(int width, int height);

    public static native String on_draw_frame( float [] vertices, long vertices_array_size);
}
