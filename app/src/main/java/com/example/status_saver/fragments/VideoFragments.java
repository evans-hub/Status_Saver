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

import com.example.status_saver.Adapters.VideoAdapter;
import com.example.status_saver.Config.Constants;
import com.example.status_saver.Entities.Model;
import com.example.status_saver.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoFragments extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    @BindView(R.id.recyclervideo)
    RecyclerView recyclerView;
    @BindView(R.id.progressvideo)
    ProgressBar loading;
    @BindView(R.id.refreshv)
    SwipeRefreshLayout swipe;
    ArrayList<Model> videoArrayList;
    Handler handler = new Handler();
    VideoAdapter videoAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        videoArrayList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        swipe.setOnRefreshListener(this);
        getVideoStatus();
    }

    private void getVideoStatus() {
        if (Constants.STATUS_DIR.exists()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    File[] statusFiles = Constants.STATUS_DIR.listFiles();
                    if (statusFiles != null && statusFiles.length > 0) {
                        Arrays.sort(statusFiles);
                        for (final File statusFile : statusFiles) {
                            Model model = new Model(statusFile, statusFile.getName(), statusFile.getPath(), statusFile.getAbsolutePath());
                            if (model.isVideo() && !model.isNoMedia()) {
                                videoArrayList.add(model);
                            }
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                loading.setVisibility(View.GONE);
                                videoAdapter = new VideoAdapter(videoArrayList, getContext(), VideoFragments.this);
                                recyclerView.setAdapter(videoAdapter);
                                videoAdapter.notifyDataSetChanged();
                            }
                        });
                    } else {
                        Toast.makeText(getContext(), "No Files Available!!!", Toast.LENGTH_SHORT).show();
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
//        getVideoStatus();
    }
}
