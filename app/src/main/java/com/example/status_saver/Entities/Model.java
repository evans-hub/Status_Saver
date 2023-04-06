package com.example.status_saver.Entities;

import android.graphics.Bitmap;

import java.io.File;

public class Model {
    private static final String mp4 = ".mp4";
    private static final String nomedia = ".nomedia";
    private File file;
    private String title, path;
    private String filePath;
    private boolean isVideo;
    private boolean isNoMedia;

    public Model(File file, String title, String path, String filePath) {
        this.file = file;
        this.title = title;
        this.path = path;
        this.filePath = filePath;
        this.isVideo = file.getName().endsWith(mp4);
        this.isNoMedia = file.getName().endsWith(nomedia);
    }

    public Model(File file, String title, String path) {
        this.file = file;
        this.title = title;
        this.path = path;
        this.isVideo = file.getName().endsWith(mp4);
    }

    public boolean isNoMedia() {
        return isNoMedia;
    }

    public void setNoMedia(boolean noMedia) {
        isNoMedia = noMedia;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getTitle() {
        return title;
    }

    public String getPath() {
        return path;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }
}
