package com.example.status_saver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.status_saver.Config.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class NextImageActivity extends AppCompatActivity {
    private ImageView imageView,back;
    private ImageButton shareButton, downloadButton, fullscreenButton;
    String imagePath;
    boolean type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        imageView = findViewById(R.id.imageView);
        shareButton = findViewById(R.id.shareButton);
        downloadButton = findViewById(R.id.downloadButton);
        fullscreenButton = findViewById(R.id.fullscreenButton);
        back=findViewById(R.id.iback);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        if (intent != null){
            imagePath = intent.getStringExtra("image");
            type = intent.getBooleanExtra("type",false);

            if (imagePath != null) {
                Glide.with(this).load(imagePath).into(imageView);
            }
        }
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share(imagePath);
            }
        });

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyFileOrDirectory(imagePath, Constants.APP_DIR);
            }
        });

        fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                Toast.makeText(getApplicationContext(), "Picture Saved", Toast.LENGTH_SHORT).show();
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

    public void share(String path) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpg");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
        startActivity(Intent.createChooser(intent, "Share using"));
    }
}