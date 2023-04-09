package com.example.status_saver.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.status_saver.Config.Constants;
import com.example.status_saver.Entities.Model;
import com.example.status_saver.NextImageActivity;
import com.example.status_saver.R;
import com.example.status_saver.fragments.ImageFragments;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private final List<Model> imagelist;
    Context context;
    ImageFragments imageFragments;

    public ImageAdapter(List<Model> imagelist, Context context, ImageFragments imageFragments) {
        this.imagelist = imagelist;
        this.context = context;
        this.imageFragments = imageFragments;
    }

    @NonNull
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        try {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trial, parent, false);
            return new ImageAdapter.ViewHolder(view);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ViewHolder holder, int position) {
        Model statusItem = imagelist.get(position);
        Glide.with(context).load(statusItem.getFilePath()).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return imagelist.size();
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
                Toast.makeText(context, "Picture Saved", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Already saved", Toast.LENGTH_SHORT).show();
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
        context.startActivity(Intent.createChooser(intent, "Share using"));
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageView)
        ImageView thumbnail;
        @BindView(R.id.download_image)
        ImageView save;
        @BindView(R.id.share_image)
        ImageView share;

        public ViewHolder(@NonNull View itemView) throws IOException {
            super(itemView);
            ButterKnife.bind(this, itemView);

            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Model model = imagelist.get(getAdapterPosition());
                    share(model.getFilePath());
                }
            });

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Model model = imagelist.get(getAdapterPosition());
                    copyFileOrDirectory(model.getFilePath(), Constants.APP_DIR);
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Model model = imagelist.get(getAdapterPosition());
                    Intent intent = new Intent(context, NextImageActivity.class);
                    intent.putExtra("image", model.getFilePath());
                    intent.putExtra("type", model.isVideo());
                    context.startActivity(intent);
                }
            });

        }
    }
}
