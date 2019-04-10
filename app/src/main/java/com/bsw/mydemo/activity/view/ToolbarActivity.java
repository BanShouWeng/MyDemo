package com.bsw.mydemo.activity.view;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bsw.mydemo.R;
import com.bsw.mydemo.utils.Logger;

import java.lang.reflect.Method;

public class ToolbarActivity extends AppCompatActivity {

    private CoordinatorLayout parentCdl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar);

        parentCdl = findViewById(R.id.parent_cdl);
        Toolbar toolbar = findViewById(R.id.test_tlb);
//        // Logo
//        toolbar.setLogo(R.drawable.logo);
//
//        // 主标题s
        toolbar.setTitle("Title");
//
//        // 副标题
//        toolbar.setSubtitle("Sub Title");

        //设置toolbar
        setSupportActionBar(toolbar);

        //左边的小箭头（注意需要在setSupportActionBar(toolbar)之后才有效果）
        toolbar.setNavigationIcon(R.mipmap.back);

        //菜单点击事件（注意需要在setSupportActionBar(toolbar)之后才有效果）
        toolbar.setOnMenuItemClickListener(onMenuItemClick);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 绑定toobar跟menu
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.addSubMenu(0,0,0,R.string.search)
                .setIcon(R.mipmap.search);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

//    /**
//     * 如下设置，可以显示menu中的icon
//     * @param view
//     * @param menu
//     * @return
//     */
//    @SuppressLint("RestrictedApi")
//    @Override
//    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
//        if (menu != null) {
//            if (menu.getClass() == MenuBuilder.class) {
//                try {
//                    @SuppressLint("PrivateApi") Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
//                    m.setAccessible(true);
//                    m.invoke(menu, true);
//                } catch (Exception e) {
//                    Logger.i(getClass().getSimpleName(), getClass().getSimpleName() + "onMenuOpened...unable to set icons for overflow menu" + e);
//                }
//            }
//        }
//        return super.onPrepareOptionsPanel(view, menu);
//    }

    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            String msg = "";
            switch (menuItem.getItemId()) {
                case R.id.action_edit:
                    msg += "Click edit";
                    break;
                case R.id.action_share:
                    msg += "Click share";
                    break;
                case R.id.action_settings:
                    msg += "Click setting";
                    break;
            }

            if (! msg.equals("")) {
//                Snackbar.make(parentCdl, msg, Snackbar.LENGTH_LONG).show();
                Snackbar mSnackbar = Snackbar.make(parentCdl, msg, Snackbar.LENGTH_LONG);
//                View v = mSnackbar.getView();
//                ViewGroup.LayoutParams vl = v.getLayoutParams();
//                CoordinatorLayout.LayoutParams cl = new CoordinatorLayout.LayoutParams(vl.width, vl.height);
//                //设置字体为红色
//                ((TextView) v.findViewById(R.id.snackbar_text)).setTextColor(Color.RED);
//                //设置显示位置居中
//                cl.gravity = Gravity.CENTER;
//                v.setLayoutParams(cl);
//                //设置背景色为绿色
//                v.setBackgroundColor(Color.GREEN);
//                //自定义动画
////                v.setAnimation();
//                //设置icon
////                ImageView iconImage = new ImageView(ToolbarActivity.this);
////                iconImage.setImageResource(R.mipmap.ic_launcher);
//
////                TextView textView  = new TextView(ToolbarActivity.this);
////                textView.setText(R.string.album);
////
//////                //icon插入布局
////                Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) v;
////                snackbarLayout.addView(textView);
//                //设置按钮为蓝色
                final String finalMsg = msg;
                mSnackbar.setActionTextColor(Color.BLUE).setAction("点我", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Snackbar.make(parentCdl, finalMsg, Snackbar.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(),finalMsg,Toast.LENGTH_LONG).show();
                    }
                }).show();
            }
            return true;
        }
    };

}
