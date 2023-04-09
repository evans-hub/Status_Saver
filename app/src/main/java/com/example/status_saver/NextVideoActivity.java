package com.example.status_saver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.status_saver.Config.Constants;

import net.bramp.ffmpeg.FFmpeg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class NextVideoActivity extends AppCompatActivity {
    ImageButton share, download, full,back;
    VideoView videoView;
    String videoPath,path;
    private boolean isLandscape = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_video);
        videoView = findViewById(R.id.videoView);
        share = findViewById(R.id.vshareButton);
        download = findViewById(R.id.vdownloadButton);
        full = findViewById(R.id.vfullscreenButton);
        back=findViewById(R.id.vback);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Intent intent = getIntent();
        if (intent != null) {
            videoPath = intent.getStringExtra("video");
            videoView.setVideoPath(videoPath);
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });
        }

        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
       // videoView.start();
        path = Constants.APP_DIR;

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share(videoPath);
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyFileOrDirectory(videoPath, Constants.APP_DIR);
            }
        });
        full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLandscape) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
                isLandscape = !isLandscape;

            }
        });
    }
    private void copyFiles(File sourceFile, File destinationFile) throws IOException {
        if (!destinationFile.getParentFile().exists()) {
            destinationFile.getParentFile().mkdirs();
        }
        if (!destinationFile.exists()) {
            destinationFile.createNewFile();
            try (FileChannel source = new FileInputStream(sourceFile).getChannel();
                 FileChannel destination = new FileOutputStream(destinationFile).getChannel()) {
                destination.transferFrom(source, 0, source.size());
                Toast.makeText(getApplicationContext(), "Video Saved", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Already saved", Toast.LENGTH_SHORT).show();
        }


    }

    public void copyFileOrDirectory(String source, String destination) {
        try {
            File src = new File(source);
            File dest = new File(destination, src.getName());
            if (src.isDirectory()) {
                String files[] = src.list();
                int fileLength = files.length;
                for (int i = 0; i < fileLength; i++) {
                    String src1 = (new File(src, files[i]).getPath());
                    String dest1 = dest.getPath();
                    copyFileOrDirectory(src1, dest1);
                }
            } else {
                copyFiles(src, dest);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void share(String path) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("video/mp4");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
        startActivity(Intent.createChooser(intent, "Share using"));
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            videoView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            videoView.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
            int margin = 32;
            videoView.setPadding(margin, margin, margin, margin);
            int videoWidth = videoView.getWidth();
            int videoHeight = videoView.getHeight();
            float videoProportion = (float) videoWidth / (float) videoHeight;

            int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
            int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
            float screenProportion = (float) screenWidth / (float) screenHeight;

            android.view.ViewGroup.LayoutParams lp = videoView.getLayoutParams();
            if (videoProportion > screenProportion) {
                lp.width = screenWidth;
                lp.height = (int) ((float) screenWidth / videoProportion);
            } else {
                lp.width = (int) (videoProportion * (float) screenHeight);
                lp.height = screenHeight;
            }
            videoView.setLayoutParams(lp);

        } else {
            videoView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            videoView.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
    }



}