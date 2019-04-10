package com.bsw.mydemo.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bsw.mydemo.widget.BswRecyclerView.SwipeItemLayout;
import com.bsw.mydemo.R;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        FrameLayout testBg = findViewById(R.id.test_bg);
        SwipeItemLayout swipeItemLayout = new SwipeItemLayout(this);
//        View view = swipeItemLayout.getChildAt(0);
        View rightView = LayoutInflater.from(this).inflate(R.layout.user_right_layout, swipeItemLayout, false);
        FrameLayout.LayoutParams lpR = new FrameLayout.LayoutParams(rightView.getLayoutParams());
        lpR.gravity = Gravity.RIGHT;
        rightView.setLayoutParams(lpR);
        swipeItemLayout.addView(rightView);
        View leftView = LayoutInflater.from(this).inflate(R.layout.user_left_layout, swipeItemLayout, false);
        FrameLayout.LayoutParams lpL = new FrameLayout.LayoutParams(leftView.getLayoutParams());
        lpL.gravity = Gravity.LEFT;
        leftView.setLayoutParams(lpL);
        swipeItemLayout.addView(leftView);
        View mainView = LayoutInflater.from(this).inflate(R.layout.user_item_layout, swipeItemLayout, false);
        ((TextView) mainView.findViewById(R.id.user_name)).setText("这是姓名");
        ((TextView) mainView.findViewById(R.id.user_age)).setText("这是年纪");
        swipeItemLayout.addView(mainView);
        testBg.addView(swipeItemLayout);
    }
}
