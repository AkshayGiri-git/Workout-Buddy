package com.example.workoutbuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    public SliderAdapter(Context context){
        this.context = context;
    }
    public int [] slides_images = {
            R.drawable.eat,
            R.drawable.sleep,
            R.drawable.code
    };
    public String [] slide_headings = {
            "Eat",
            "Sleep",
            "Code"
    };
    public String[] slide_desc = {
            "Eating is very good .Eating is very good .Eating is very good .Eating is very good .Eating is very good .Eating is very good .Eating is very good .",
            "Sleeping is very good .Sleeping is very good .Sleeping is very good .Sleeping is very good .Sleeping is very good .Sleeping is very good .Sleeping is very good ",
            "Coding is very good .Coding is very good .Coding is very good .Coding is very good .Coding is very good .Coding is very good .Coding is very good ."
    };

    @Override
    public int getCount() {
        return slides_images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.activity_slides,container,false);
        ImageView image = (ImageView) view.findViewById(R.id.imageView);
        TextView textView = (TextView) view.findViewById(R.id.textView);
        TextView textView1 = (TextView) view.findViewById(R.id.textView2);
        image.setImageResource(slides_images[position]);
        textView.setText(slide_headings[position]);
        textView1.setText(slide_desc[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}
