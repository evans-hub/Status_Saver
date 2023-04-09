package com.example.status_saver.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.status_saver.Adapters.ImageAdapter;
import com.example.status_saver.Config.Constants;
import com.example.status_saver.Entities.Model;
import com.example.status_saver.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageFragments extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.progress)
    ProgressBar loading;
    @BindView(R.id.refreshi)
    SwipeRefreshLayout swipe;
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
        swipe.setOnRefreshListener(this);
        getStatus();
    }
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
        swipe.setRefreshing(false);
    }


    @Override
    public void onRefresh() {
//getStatus();

    }
}
