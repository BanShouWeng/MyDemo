package com.bsw.mydemo.myTest;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bsw.mydemo.utils.ScreenUtil;

/**
 * 布局测试VIew
 *
 * @author leiming
 * @date 2020-5-20
 */
public class TestView extends RelativeLayout {

    Context context;
    private TextView switchButton;
    private RelativeLayout relativeLayout;

    public TestView(Context context) {
        this(context, null);
    }

    public TestView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TestView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        relativeLayout = new RelativeLayout(context);
        relativeLayout.setBackgroundColor(Color.parseColor("#00FFEE"));
        switchButton = new TextView(context);
        switchButton.setText("登录");
        switchButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20f);
        switchButton.setTextColor(Color.WHITE);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, ScreenUtil.dp2px(context, 40));
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        switchButton.setLayoutParams(lp);
        switchButton.setBackgroundColor(Color.BLUE);
        switchButton.setGravity(Gravity.CENTER);
        relativeLayout.addView(switchButton);
        addView(relativeLayout);
//        addView(switchButton);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        relativeLayout.layout(0, 0, getWidth(), getHeight());
    }
}
