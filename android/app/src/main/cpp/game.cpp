//
// Created by ttran on 13.11.2018.
//
#include "game.h"
#include "glwrapper.h"

void on_surface_created() {
    glClearColor(0.0, 0.0, 0.3, 1.0); //dark blue
}

void on_surface_changed() {
}

char* on_draw_frame(float * vertices_cpp_pointer, long vertices_array_size) {
    unsigned int VBO, vao;
    unsigned int index[vertices_array_size/3];
    float vertices_cpp[vertices_array_size];
    for(int i=0;i<vertices_array_size/3;i++){
        index[i]=i;
    }
    for(int i=0;i<vertices_array_size;i++){
        vertices_cpp[i]=vertices_cpp_pointer[i];
    }

    glGenBuffers(1, &VBO);
    glBindBuffer(GL_ARRAY_BUFFER, VBO);
    glBufferData(GL_ARRAY_BUFFER, sizeof(vertices_cpp), vertices_cpp, GL_STATIC_DRAW);
    glBindBuffer(GL_ARRAY_BUFFER, VBO);
    GLuint ibo;
    glGenBuffers(1, &ibo);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, sizeof(index), index, GL_STATIC_DRAW);
    glGenVertexArrays(1, &vao);
    glBindVertexArray(vao);
    int success;
    char * infoLog = (char*)malloc(512);
    strcpy(infoLog, "OK");
    const char *vertexShaderSource ="#version 300 es\n"
                                    "layout (location = 0) in vec3 aPos;\n"
                                    "void main()\n"
                                    "{\n"
                                    "   gl_Position = vec4(aPos, 1.0);\n"
                                    "}\0";

    const char *fragmentShaderSource = "#version 300 es\n"
                                       "precision mediump float;\n"
                                       "out vec4 color;\n"
                                       "void main()\n"
                                       "{\n"
                                       "   color = vec4(1.0,1.0,0.0,1.0);\n"
                                       "}\n\0";
    unsigned int vertexShader;
    vertexShader = glCreateShader(GL_VERTEX_SHADER);
    glShaderSource(vertexShader, 1, &vertexShaderSource, NULL);
    glCompileShader(vertexShader);

    unsigned int fragmentShader;
    fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
    glShaderSource(fragmentShader, 1, &fragmentShaderSource, NULL);
    glCompileShader(fragmentShader);

    unsigned int shaderProgram;
    shaderProgram = glCreateProgram();

    glAttachShader(shaderProgram, vertexShader);
    glAttachShader(shaderProgram, fragmentShader);
    glLinkProgram(shaderProgram);
    glGetProgramiv(shaderProgram, GL_LINK_STATUS, &success);
    if(!success)
    {
        //glGetProgramInfoLog(shaderProgram, 512, NULL,infoLog);
        strcpy(infoLog, "ERRORRRRRR");
    }

    glDeleteShader(vertexShader);
    glDeleteShader(fragmentShader);
    glClear(GL_COLOR_BUFFER_BIT);
    glUseProgram(shaderProgram);

    glEnableVertexAttribArray(0);
    glBindVertexArray(vao);
    glVertexAttribPointer(
            0,                  // attribute 0. No particular reason for 0, but must match the layout in the shader.
            3,                  // number of components per generic vertex attribute (eg: (x,y,z)=3)
            GL_FLOAT,           // type
            GL_FALSE,           // normalized?
            0,                  // stride
            (void*)0            // array buffer offset
    );
    glDrawElements(GL_TRIANGLES,vertices_array_size/3,GL_UNSIGNED_INT,index);
    glDisableVertexAttribArray(0);
    //sleep(1);
    return infoLog;
}