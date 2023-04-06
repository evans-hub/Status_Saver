package com.example.status_saver.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.status_saver.Adapters.ImageAdapter;
import com.example.status_saver.Config.Constants;
import com.example.status_saver.Entities.Model;
import com.example.status_saver.R;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageFragments extends Fragment {
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.progress)
    ProgressBar loading;
    ArrayList<Model> imageArrayList;
    Handler handler = new Handler();
    ImageAdapter imageAdapter;

    public ImageFragments() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        imageArrayList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        getStatus();
    }

    /*  private void getStatus() {
          if (Constants.STATUS_DIR.exists()){
                      File[] statusFiles=Constants.STATUS_DIR.listFiles();



              if (statusFiles !=null && statusFiles.length>0){
                      Arrays.sort(statusFiles);
                      for (final File statusFile:statusFiles){
                          Model model=new Model(statusFile,statusFile.getName(),statusFile.getAbsolutePath());
                          model.setThumbnail(getThumbnail(model));
                          if (!model.isVideo()){
                              imageArrayList.add(model);
                          }
                      }
                              loading.setVisibility(View.GONE);
  imageAdapter=new ImageAdapter(imageArrayList, getContext(),ImageFragments.this);
                         recyclerView.setAdapter(imageAdapter);
                         imageAdapter.notifyDataSetChanged();

                  }
              else {
                  Toast.makeText(getContext(), "No Status Available !!!", Toast.LENGTH_SHORT).show();

              }


          }
          else{
              loading.setVisibility(View.GONE);
              Toast.makeText(getContext(), "opps Check Directory!!!", Toast.LENGTH_SHORT).show();
          }
      }*/
    private void getStatus() {
        if (Constants.STATUS_DIR.exists()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    File[] statusFiles = Constants.STATUS_DIR.listFiles();

                    if (statusFiles != null && statusFiles.length > 0) {

                        Arrays.sort(statusFiles);
                        for (final File statusFile : statusFiles) {
                            Model model = new Model(statusFile, statusFile.getName(), statusFile.getPath(), statusFile.getAbsolutePath());
                            if (!model.isVideo() && !model.isNoMedia()) {
                                imageArrayList.add(model);
                            }
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                loading.setVisibility(View.GONE);
                                imageAdapter = new ImageAdapter(imageArrayList, getContext(), ImageFragments.this);
                                recyclerView.setAdapter(imageAdapter);
                                imageAdapter.notifyDataSetChanged();
                            }
                        });

                    } else {
                        Toast.makeText(getContext(), "No Files Available", Toast.LENGTH_SHORT).show();
                    }


                }
            }).start();
        } else {
            loading.setVisibility(View.GONE);
            Toast.makeText(getContext(), "Oops Directory Doesn't exist!!!", Toast.LENGTH_SHORT).show();
        }
    }


}
