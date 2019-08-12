package com.bsw.mydemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.bsw.mydemo.MainActivity;
import com.bsw.mydemo.R;
import com.bsw.mydemo.base.BaseActivity;
import com.bsw.mydemo.bean.JumpBean;
import com.bsw.mydemo.widget.BswRecyclerView.BswRecyclerView;
import com.bsw.mydemo.widget.BswRecyclerView.ConvertViewCallBack;
import com.bsw.mydemo.widget.BswRecyclerView.RecyclerViewHolder;

import java.util.Arrays;

/**
 * 鸣谢http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2017/0921/8536.html
 *
 * @author 半寿翁
 */
public class SettingPageJumpActivity extends BaseActivity {

    private BswRecyclerView<SettingBean> settingRv;
    private SettingBean[] settingBeanList = {
            new SettingBean("系统设置页面", Settings.ACTION_SETTINGS)
            , new SettingBean("APN设置界面", Settings.ACTION_APN_SETTINGS)
            , new SettingBean("APN设置界面", Settings.ACTION_APN_SETTINGS)
            , new SettingBean("定位设置界面", Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            , new SettingBean("更多连接方式设置界面", Settings.ACTION_AIRPLANE_MODE_SETTINGS)
            , new SettingBean("双卡和移动网络设置界面", Settings.ACTION_DATA_ROAMING_SETTINGS)
            , new SettingBean("无障碍设置界面", Settings.ACTION_ACCESSIBILITY_SETTINGS)
            , new SettingBean("同步设置界面", Settings.ACTION_SYNC_SETTINGS)
            , new SettingBean("添加账户界面", Settings.ACTION_ADD_ACCOUNT)
            , new SettingBean("选取运营商的界面", Settings.ACTION_NETWORK_OPERATOR_SETTINGS)
            , new SettingBean("安全设置界面", Settings.ACTION_SECURITY_SETTINGS)
            , new SettingBean("备份重置设置界面", Settings.ACTION_PRIVACY_SETTINGS)
            , new SettingBean("VPN设置界面,可能不存在", Settings.ACTION_VPN_SETTINGS)
            , new SettingBean("无线网设置界面", Settings.ACTION_WIFI_SETTINGS)
            , new SettingBean("WIFI的IP设置", Settings.ACTION_WIFI_IP_SETTINGS)
            , new SettingBean("蓝牙设置", Settings.ACTION_BLUETOOTH_SETTINGS)
            , new SettingBean("投射设置", Settings.ACTION_CAST_SETTINGS)
            , new SettingBean("日期时间设置", Settings.ACTION_DATE_SETTINGS)
            , new SettingBean("声音设置", Settings.ACTION_SOUND_SETTINGS)
            , new SettingBean("显示设置", Settings.ACTION_DISPLAY_SETTINGS)
            , new SettingBean("语言设置", Settings.ACTION_LOCALE_SETTINGS)
            , new SettingBean("辅助应用和语音输入设置", Settings.ACTION_VOICE_INPUT_SETTINGS)
            , new SettingBean("语言和输入法设置", Settings.ACTION_INPUT_METHOD_SETTINGS)
            , new SettingBean("个人字典设置界面", Settings.ACTION_USER_DICTIONARY_SETTINGS)
            , new SettingBean("存储空间设置的界面", Settings.ACTION_INTERNAL_STORAGE_SETTINGS)
            , new SettingBean("搜索设置界面", Settings.ACTION_SEARCH_SETTINGS)
            , new SettingBean("开发者选项设置", Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)
            , new SettingBean("手机状态信息的界面", Settings.ACTION_DEVICE_INFO_SETTINGS)
            , new SettingBean("互动屏保设置的界面", Settings.ACTION_DREAM_SETTINGS)
            , new SettingBean("通知使用权设置的界面", Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
            , new SettingBean("勿扰权限设置的界面", Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
            , new SettingBean("字幕设置的界面", Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
            , new SettingBean("打印设置界面", Settings.ACTION_PRINT_SETTINGS)
            , new SettingBean("节电助手界面", Settings.ACTION_BATTERY_SAVER_SETTINGS)
            , new SettingBean("主屏幕设置界面", Settings.ACTION_HOME_SETTINGS)
            , new SettingBean("主屏幕设置界面", Settings.ACTION_HOME_SETTINGS)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.main_activity_btn_NFC);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting_page_jump;
    }

    @Override
    protected void findViews() {
        settingRv = getView(R.id.setting_rv);
        settingRv.initAdapter(R.layout.item_main_layout, convertViewCallBack)
                .setDecoration()
                .setLayoutManager();
        settingRv.setData(Arrays.asList(settingBeanList));
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

    }

    private ConvertViewCallBack<SettingBean> convertViewCallBack = new ConvertViewCallBack<SettingBean>() {
        @Override
        public SettingBean convert(RecyclerViewHolder holder, final SettingBean settingBean, int position, int layoutId) {
            holder.setText(R.id.jumpTo, settingBean.name)
                    .setClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(settingBean.settingAction));
                        }
                    });
            return settingBean;
        }
    };

    class SettingBean {
        public SettingBean(String name, String settingAction) {
            this.name = name;
            this.settingAction = settingAction;
        }

        String name;
        String settingAction;
    }
}
