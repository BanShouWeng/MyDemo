package com.bsw.mydemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bsw.mydemo.R;
import com.bsw.mydemo.base.BaseActivity;
import com.bsw.mydemo.widget.MarkerImg.MarkerImgLayout;
import com.bsw.mydemo.widget.MarkerImg.PointSimple;

import java.util.ArrayList;

public class ImgMarkerActivity extends BaseActivity {

    private MarkerImgLayout marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ArrayList<PointSimple> pointSimples = new ArrayList<>();
//        PointSimple pointSimple1 = new PointSimple();
//        pointSimple1.width_scale = 0.1f;
//        pointSimple1.height_scale = 0.1f;
//
//        PointSimple pointSimple2 = new PointSimple();
//        pointSimple2.width_scale = 0.9f;
//        pointSimple2.height_scale = 0.9f;
//
//
//        PointSimple pointSimple3 = new PointSimple();
//        pointSimple3.width_scale = 0.5f;
//        pointSimple3.height_scale = 0.5f;
//
//        pointSimples.add(pointSimple1);
//        pointSimples.add(pointSimple2);
//        pointSimples.add(pointSimple3);
//        marker.setPoints(pointSimples);
//
//        int w = View.MeasureSpec.makeMeasureSpec(0,
//                View.MeasureSpec.UNSPECIFIED);
//        int h = View.MeasureSpec.makeMeasureSpec(0,
//                View.MeasureSpec.UNSPECIFIED);
//
//        marker.measure(w, h);
//
//        marker.setImgBg(marker.getMeasuredWidth(), marker.getMeasuredHeight(), R.drawable.marker_bg);

        marker.setImageBg(R.drawable.floor_bg);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_img_marker;
    }

    @Override
    protected void findViews() {
        marker = getView(R.id.marker_bg);
        setOnClickListener(R.id.zoom_img);
    }

    @Override
    protected void formatViews() {

    }

    @Override
    protected void formatData() {

    }

    @Override
    protected void getBundle(Bundle bundle) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zoom_img:
                jumpTo(DragAndZoomImgActivity.class);
                break;
        }
    }
}
