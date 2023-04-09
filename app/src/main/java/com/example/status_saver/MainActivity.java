package com.example.status_saver;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.status_saver.Adapters.PagerAdapter;
import com.example.status_saver.Entities.Model;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private List<Model> statusList;
    private PagerAdapter adapter;
    @BindView(R.id.toolBarMainActivity)
    Toolbar toolbar;
    @BindView(R.id.tabLayouts)
    TabLayout tabLayout;
    @BindView(R.id.ViewPagers)
    ViewPager viewPager;
    private ToggleButton mToggleButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setTheme(R.style.AppTheme_Light);*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_main);
       /* mToggleButton = findViewById(R.id.toggle_button);
       mToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mToggleButton.isChecked()) {
                    setTheme(R.style.AppTheme_Dark);
                } else {
                    setTheme(R.style.AppTheme_Light);
                }

                recreate();
            }
        });*/


        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);


    }
}