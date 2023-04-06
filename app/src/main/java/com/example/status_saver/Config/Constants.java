package com.example.status_saver.Config;

import android.os.Environment;

import java.io.File;

public class Constants {
    public static final File STATUS_DIR = new File(Environment.getExternalStorageDirectory() +
            File.separator + "Android/media/com.whatsapp/WhatsApp/Media/.Statuses/");
    public static final String APP_DIR = Environment.getExternalStorageDirectory() + File.separator + "WhatsAppSavedStatus/";
    public static final int THUMBSIZE = 120;
}
