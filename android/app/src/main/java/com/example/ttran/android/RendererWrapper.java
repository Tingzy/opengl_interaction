package com.example.ttran.android;

import static java.lang.Boolean.TRUE;
import static java.lang.Boolean.FALSE;
import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

import android.opengl.GLSurfaceView.Renderer;

import java.util.Arrays;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.egl.EGLConfig;

public class RendererWrapper implements Renderer {
    boolean state= TRUE;
    int count =0;
    float vertices[] = {
            -0.1f, 1.0f, 0.0f,
            0.1f, 1.0f, 0.0f,
            0.0f,  1.1f, 0.0f,
    };
    long vertices_array_size = vertices.length;
    float xH = (vertices[0] + vertices[3])/2;
    float yH = vertices[1];
    float area= (float) (1f/2f*(sqrt(pow(vertices[3] - vertices[0],2) + pow(vertices[4] - vertices[1],2)) * sqrt(pow(xH - vertices[6],2) + pow(yH - vertices[7],2)))) ;

    public float [] updatePosition(float [] vertices){
        float [] new_vertices = new float [(int) vertices_array_size];
        for (int i = 0; i<(int) vertices_array_size; i++){
            if(i%3==1){
                new_vertices[i]=vertices[i]-0.005f;
            }
            else{
                new_vertices[i]=vertices[i];
            }
        }
        return new_vertices;
    }

    public static boolean updateState(boolean state){
        boolean state_return;
        if(state){
            state_return =FALSE;
        }
        else{
            state_return=TRUE;
        }
        return state_return;
    }

    public void updateVerticesOntouch(float xO, float yO){
        for(int i=0; i<=vertices_array_size-9;i=i+9){
            float area_sum =1f/2f * (abs((vertices[i+6]-xO)*(vertices[i+1]-yO) - (vertices[i]-xO)*(vertices[i+7]-yO)) + abs((vertices[i+6]-xO)*(vertices[i+4]-yO) - (vertices[i+3]-xO)*(vertices[i+7]-yO)) + abs((vertices[i]-xO)*(vertices[i+4]-yO) - (vertices[i+3]-xO)*(vertices[i+1]-yO)));
            if(area_sum>area-0.00001 && area_sum<area+0.00001){
                for(int k=i;k<vertices_array_size-9;k++){
                    vertices[k]=vertices[k+9];
                }
                vertices = Arrays.copyOf(vertices,vertices.length -9);
                vertices_array_size = vertices.length;
                break;
            }
            else{
                continue;
            }
        }
    }

    public void updateVertices(float x){
        long vertices_array_size_before = vertices.length;
        vertices = Arrays.copyOf(vertices,vertices.length +9);
        vertices_array_size = vertices.length;
        vertices[(int)vertices_array_size_before]=x;
        vertices[(int)vertices_array_size_before+1]=1.0f;
        vertices[(int)vertices_array_size_before+2]=0.0f;
        vertices[(int)vertices_array_size_before+3]=x+0.2f;
        vertices[(int)vertices_array_size_before+4]=1.0f;
        vertices[(int)vertices_array_size_before+5]=0.0f;
        vertices[(int)vertices_array_size_before+6]=x+0.1f;
        vertices[(int)vertices_array_size_before+7]=1.1f;
        vertices[(int)vertices_array_size_before+8]=0.0f;
        for (int i=0;i<=vertices_array_size-9;i=i+9){
            if(vertices[i+1]<-1.0f && vertices[i+4]<-1.0f && vertices[i+7]<-1.0f){
                for(int k=i;k<vertices_array_size-9;k++){
                    vertices[k]=vertices[k+9];
                }
                vertices = Arrays.copyOf(vertices,vertices.length -9);
                vertices_array_size = vertices.length;
                //System.out.println(vertices_array_size);
            }
        }
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GameLibJNIWrapper.on_surface_created();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GameLibJNIWrapper.on_surface_changed(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        Random rand = new Random();
        float x;
        if (count ==15) {
            x = (float) (rand.nextInt() % 11.0) / 10;
            updateVertices(x);
            count =0;
        }
        String infoLog = GameLibJNIWrapper.on_draw_frame(vertices, vertices_array_size);
        if(state) {
            vertices = updatePosition(vertices);
        }
        //System.out.println();
        count++;
    }
}

