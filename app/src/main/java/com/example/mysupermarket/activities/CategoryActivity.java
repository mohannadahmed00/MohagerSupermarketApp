package com.example.mysupermarket.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.mysupermarket.R;
import com.example.mysupermarket.adapters.PagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    int position;
    ArrayList<String> sFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeStatBarColor(CategoryActivity.this, R.color.trans);
        setContentView(R.layout.activity_category);
        init();
    }

    void init() {
        position = getIntent().getIntExtra("position", 0);

        sFragments = new ArrayList<>();
        sFragments.add("Dairy");
        sFragments.add("Drink");
        sFragments.add("Grocery");
        sFragments.add("Oil");
        sFragments.add("Preserve");
        sFragments.add("Sauce");
        sFragments.add("Sweet");


        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), sFragments);
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(position);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void changeStatBarColor(AppCompatActivity activity, int color) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(activity, color));
        //???????????????????
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        //???????????????????
    }
}
