package com.bsw.mydemo.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bsw.mydemo.MainActivity;
import com.bsw.mydemo.R;
import com.bsw.mydemo.event.NetBroadcastReceiver;

import java.util.ArrayList;
import java.util.List;
/**
 * @author 半寿翁
 */
public abstract class BaseActivity extends AppCompatActivity implements NetBroadcastReceiver.NetEvent, View.OnClickListener {
    public final int BACK_ID = R.id.base_back;
    public final int RIGHT_TEXT_ID = R.id.base_right_text;
    public final int RIGHT_ICON_ID1 = R.id.base_right_icon1;
    public final int RIGHT_ICON_ID2 = R.id.base_right_icon2;

    /**
     * 网络状态监听接受者
     */
    public static NetBroadcastReceiver.NetEvent event;
    /**
     * 用于传递的上下文信息
     */
    public Context context;
    public Activity activity;

    private ImageView baseBack;
    private ViewStub titleStub;

    /**
     * 当前打开Activity存储List
     */
    private static List<Activity> activities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activities.add(this);
        context = getApplicationContext();
        activity = this;
        event = this;

        //下面的代码可以写在BaseActivity里面
        highApiEffects();

        setContentView(R.layout.activity_base);
        initSDK();
        setBaseContentView(getLayoutId());
        findViews();
        getBundle(getIntent().getBundleExtra("bundle"));
        formatViews();
        formatData();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void highApiEffects() {
        getWindow().getDecorView().setFitsSystemWindows(true);
        //透明状态栏 @顶部
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏 @底部 这一句不要加，目的是防止沉浸式状态栏和部分底部自带虚拟按键的手机（比如华为）发生冲突，注释掉就好了
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }

    /**
     * 隐藏头布局
     */
    public void hideTitle() {
        getView(R.id.base_title_layout).setVisibility(View.GONE);
    }

    /**
     * 隐藏返回键
     */
    public void hideBack() {
        baseBack.setVisibility(View.GONE);
    }

    /**
     * 显示返回键
     */
    public void showBack() {
        baseBack.setVisibility(View.VISIBLE);
    }

    /**
     * 设置标题
     *
     * @param title 标题的文本
     */
    public void setTitle(String title) {
        setTitle(title, true);
    }

    /**
     * 设置标题
     *
     * @param titleId 标题的文本
     */
    public void setTitle(int titleId) {
        setTitle(titleId, true);
    }

    /**
     * 控件初始化
     */
    protected void initBaseView() {
        if (titleStub == null) {
            titleStub = getView(R.id.base_title_layout);
            titleStub.inflate();
        }
    }

    /**
     * 设置标题
     *
     * @param title    标题的文本
     * @param showBack 是否显示返回键
     */
    public void setTitle(String title, boolean showBack) {
        initBaseView();
        ((TextView) getView(R.id.base_title)).setText(title);
        if (showBack) {
            baseBack = getView(R.id.base_back);
            baseBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    /**
     * 设置标题
     *
     * @param titleId  标题的文本
     * @param showBack 是否显示返回键
     */
    public void setTitle(int titleId, boolean showBack) {
        initBaseView();
        ((TextView) getView(R.id.base_title)).setText(titleId);
        if (showBack) {
            baseBack = getView(R.id.base_back);
            baseBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    /**
     * 设置返回点击事件
     */
    public void setBaseBack() {
        initBaseView();
        baseBack = getView(R.id.base_back);
        baseBack.setOnClickListener(this);
    }

    /**
     * 最右侧图片功能键设置方法
     *
     * @param resId     图片id
     * @param alertText 语音辅助提示读取信息
     * @return 将当前ImageView返回方便进一步处理
     */
    public ImageView setBaseRightIcon1(int resId, String alertText) {
        initBaseView();

        ImageView baseRightIcon1 = getView(R.id.base_right_icon1);
        baseRightIcon1.setImageResource(resId);
        baseRightIcon1.setVisibility(View.VISIBLE);
        //语音辅助提示的时候读取的信息
        baseRightIcon1.setContentDescription(alertText);
        baseRightIcon1.setOnClickListener(this);
        return baseRightIcon1;
    }

    /**
     * 右数第二个图片功能键设置方法
     *
     * @param resId     图片id
     * @param alertText 语音辅助提示读取信息
     * @return 将当前ImageView返回方便进一步处理
     */
    public ImageView setBaseRightIcon2(int resId, String alertText) {
        initBaseView();

        ImageView baseRightIcon2 = getView(R.id.base_right_icon2);
        baseRightIcon2.setImageResource(resId);
        baseRightIcon2.setVisibility(View.VISIBLE);
        //语音辅助提示的时候读取的信息
        baseRightIcon2.setContentDescription(alertText);
        baseRightIcon2.setOnClickListener(this);
        return baseRightIcon2;
    }

    /**
     * 最右侧文本功能键设置方法
     *
     * @param text 文本信息
     * @return 将当前TextView返回方便进一步处理
     */
    public TextView setBaseRightText(String text) {
        initBaseView();

        TextView baseRightText = getView(R.id.base_right_text);
        baseRightText.setText(text);
        baseRightText.setVisibility(View.VISIBLE);
        baseRightText.setOnClickListener(this);
        return baseRightText;
    }

    /**
     * 最右侧文本功能键设置方法
     *
     * @param textId 文本信息id
     * @return 将当前TextView返回方便进一步处理
     */
    public TextView setBaseRightText(int textId) {
        initBaseView();

        TextView baseRightText = getView(R.id.base_right_text);
        baseRightText.setText(textId);
        baseRightText.setVisibility(View.VISIBLE);
        baseRightText.setOnClickListener(this);
        return baseRightText;
    }

    /**
     * 引用头部布局
     *
     * @param layoutId 布局id
     */
    private void setBaseContentView(int layoutId) {
        LinearLayout layout = getView(R.id.base_main_layout);

        //获取布局，并在BaseActivity基础上显示
        final View view = getLayoutInflater().inflate(layoutId, null);
        //关闭键盘
        hideKeyBoard();
        //给EditText的父控件设置焦点，防止键盘自动弹出
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layout.addView(view, params);
    }

    /**
     * 隐藏键盘
     */
    public void hideKeyBoard() {
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    protected String getName() {
        return getClass().getSimpleName();
    }

    /**
     * 跳转页面
     *
     * @param clz 所跳转的目的Activity类
     */
    public void jumpTo(Class<?> clz) {
        startActivity(new Intent(this, clz));
    }

    /**
     * 跳转页面
     *
     * @param clz    所跳转的目的Activity类
     * @param bundle 跳转所携带的信息
     */
    public void jumpTo(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(this, clz);
        if (bundle != null) {
            intent.putExtra("bundle", bundle);
        }
        startActivity(intent);
    }

    /**
     * 跳转页面
     *
     * @param clz         所跳转的Activity类
     * @param requestCode 请求码
     */
    public void jumpTo(Class<?> clz, int requestCode) {
        startActivityForResult(new Intent(this, clz), requestCode);
    }

    /**
     * 跳转页面
     *
     * @param clz         所跳转的Activity类
     * @param bundle      跳转所携带的信息
     * @param requestCode 请求码
     */
    public void jumpTo(Class<?> clz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, clz);
        if (bundle != null) {
            intent.putExtra("bundle", bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 消息提示框
     *
     * @param message 提示消息文本
     */
    public void toast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * 消息提示框
     *
     * @param messageId 提示消息文本ID
     */
    public void toast(int messageId) {
        Toast.makeText(context, messageId, Toast.LENGTH_LONG).show();
    }

    /**
     * 关闭所有Activity（除MainActivity以外）
     */
    public void finishActivity() {
        for (Activity activity : activities) {
            activity.finish();
        }
    }

    /**
     * 返回历史界面
     *
     * @param clz 指定的Activity对应的class
     */
    public void backTo(Class<?> clz) {
        if (clz.equals(MainActivity.class)) {
            finishActivity();
        } else {
            for (int i = activities.size() - 1; i >= 0; i--) {
                if (clz.equals(activities.get(i).getClass())) {
                    break;
                } else {
                    activities.get(i).finish();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activities.remove(this);
        event = null;
    }

    /**
     * 网络变化回调方法
     *
     * @param mobileNetState 当前的网络状态
     */
    @Override
    public void onNetChanged(int mobileNetState) {

    }

    /**
     * 检测当的网络（Wlan、3G/2G）状态
     *
     * @return true 表示网络可用
     */
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * SDK初始化
     */
    protected void initSDK() {
    }

    /**
     * 简化获取View
     *
     * @param viewId View的ID
     * @param <T>    将View转化为对应泛型，简化强转的步骤
     * @return ID对应的View
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId) {
        return (T) findViewById(viewId);
    }

    /**
     * 简化获取View
     *
     * @param view   父view
     * @param viewId View的ID
     * @param <T>    将View转化为对应泛型，简化强转的步骤
     * @return ID对应的View
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(View view, int viewId) {
        return (T) view.findViewById(viewId);
    }

    /**
     * 简化获取View
     *
     * @param layoutId 父布局Id
     * @param viewId   View的ID
     * @param <T>      将View转化为对应泛型，简化强转的步骤
     * @return ID对应的View
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int layoutId, int viewId) {
        return (T) LayoutInflater.from(context).inflate(layoutId, null).findViewById(viewId);
    }

    /**
     * 设置点击事件
     *
     * @param layouts 点击控件Id
     */
    protected void setClickView(int... layouts) {
        for (int layout : layouts) {
            getView(layout).setOnClickListener(this);
        }
    }

    /**
     * 获取布局ID
     *
     * @return 获取的布局ID
     */
    protected abstract int getLayoutId();

    /**
     * 获取所有View信息
     */
    protected abstract void findViews();

    /**
     * 初始化布局信息
     */
    protected abstract void formatViews();

    /**
     * 初始化数据信息
     */
    protected abstract void formatData();

    /**
     * 初始化Bundle
     */
    protected abstract void getBundle(Bundle bundle);
}
