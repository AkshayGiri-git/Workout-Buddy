package com.example.workoutbuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;


import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager2;
    private LinearLayout linearLayout;
    private TextView[] dots;
    private SliderAdapter sliderAdapter;
    private Button next;
    private Button prev;
    private int currentpage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager2 = findViewById(R.id.slideViewPager);
        linearLayout = (LinearLayout) findViewById(R.id.docsLayout);
        sliderAdapter = new SliderAdapter(this);
        viewPager2.setAdapter(sliderAdapter);
        next = findViewById(R.id.next);
        prev = findViewById(R.id.previous);
        addDotsIndicator(0);
        viewPager2.addOnPageChangeListener(viewListener);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentpage == dots.length-1){
                    Intent intent = new Intent(MainActivity.this,MapsActivity.class);
                    startActivity(intent);
                }
                viewPager2.setCurrentItem(currentpage+1);

            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager2.setCurrentItem(currentpage-1);
            }
        });
    }

    public void addDotsIndicator(int position) {
        dots = new TextView[3];
        linearLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.colorTransparentwhite));
            linearLayout.addView(dots[i]);

        }
        if(dots.length > 0){
            dots[position].setTextColor(getResources().getColor(R.color.white));
        }
    }
        ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addDotsIndicator(position);
                currentpage = position;
                if(position == 0){
                    next.setEnabled(true);
                    prev.setEnabled(false);
                    prev.setVisibility(View.INVISIBLE);
                    next.setText("Next");
                    prev.setText("");
                } else if (position == dots.length-1) {
                    next.setEnabled(true);
                    prev.setEnabled(true);
                    prev.setVisibility(View.VISIBLE);
                    next.setText("Finish");
                    prev.setText("Back");
                } else {
                    next.setEnabled(true);
                    prev.setEnabled(true);
                    prev.setVisibility(View.VISIBLE);
                    next.setText("Next");
                    prev.setText("Back");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };



}
