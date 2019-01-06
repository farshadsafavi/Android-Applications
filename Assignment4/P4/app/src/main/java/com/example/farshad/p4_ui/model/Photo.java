package com.example.farshad.p4_ui.model;

import io.realm.RealmObject;

/**
 * Created by Farshad on 10/22/2016.
 */
public class Photo extends RealmObject {
    String path;
    int face_x;
    int face_y;
    int height;
    int width;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getFace_x() {
        return face_x;
    }

    public void setFace_x(int face_x) {
        this.face_x = face_x;
    }

    public int getFace_y() {
        return face_y;
    }

    public void setFace_y(int face_y) {
        this.face_y = face_y;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "path='" + path + '\'' +
                ", face_x=" + face_x +
                ", face_y=" + face_y +
                ", height=" + height +
                ", width=" + width +
                '}';
    }
}