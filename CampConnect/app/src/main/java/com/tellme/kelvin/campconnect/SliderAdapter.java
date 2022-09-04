package com.tellme.kelvin.campconnect;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    //arrays..
    public int[] slides_image1 = {
            R.drawable.icon01,
            R.drawable.icon02,
            R.drawable.icon03
    };
    public String[] slide_heading1 = {
            "NYSC CONNECT",
            "NYSC CONNECT",
            "NYSC CONNECT"
    };
    public String[] slide_desc1 = {
            "....Your Journey Starts Here in The Camp With Nysc Connect",
            "Stay Informed, Motivated, and Updated",
            "Corper Wee, Are You GOOD 2 GO ? Are You Motivated?"
    };

    @Override
    public int getCount() {
        return slide_heading1.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView sliderImageView = view.findViewById(R.id.slide_image1);
        TextView sliderHeading = view.findViewById(R.id.slide_heading1);
        TextView sliderDescription = view.findViewById(R.id.slide_desc1);

        sliderImageView.setImageResource(slides_image1[position]);
        sliderHeading.setText(slide_heading1[position]);
        sliderDescription.setText(slide_desc1[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout)object);
    }
}