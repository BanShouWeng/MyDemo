package com.example.leiming.mydemo.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.leiming.mydemo.R;

import java.util.Set;

public class BluetoothActivity extends AppCompatActivity implements View.OnClickListener {

    private BluetoothAdapter mBluetoothAdapter = null;
    private int REQUEST_ENABLE = 0x01;
    private String TAG = "blue_tooth";

    private ListView listView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        listView = (ListView) findViewById(R.id.bluetooth_list);
        textView = (TextView) findViewById(R.id.bluetooth_name);
        findViewById(R.id.bluetooth_begin).setOnClickListener(this);
        findViewById(R.id.bluetooth_stop).setOnClickListener(this);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            //弹出对话框提示用户是后打开
            Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enabler, REQUEST_ENABLE);
            //不做提示，直接打开，不建议用下面的方法，有的手机会有问题。
            // mBluetoothAdapter.enable();
        } else {
            getMyInfo();
        }
    }

    private void getMyInfo() {
        //获取本机蓝牙名称
        String name = mBluetoothAdapter.getName();
//获取本机蓝牙地址
        String address = mBluetoothAdapter.getAddress();
        Log.d(TAG, "bluetooth name =" + name + " address =" + address);
//获取已配对蓝牙设备
        Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
        Log.i(TAG, "bonded device size =" + devices.size());
        for (BluetoothDevice bonddevice : devices) {
            Log.i(TAG, "bonded device name =" + bonddevice.getName() + " address" + bonddevice.getAddress());
        }

        initReceiver();
    }

    private void initReceiver() {
        IntentFilter filter = new IntentFilter();
//发现设备
        filter.addAction(BluetoothDevice.ACTION_FOUND);
//设备连接状态改变
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
//蓝牙设备状态改变
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mBluetoothReceiver, filter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getMyInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bluetooth_begin:
                mBluetoothAdapter.startDiscovery();
                break;

            case R.id.bluetooth_stop:
                mBluetoothAdapter.cancelDiscovery();
                break;
        }
    }

    private BroadcastReceiver mBluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i(TAG, "mBluetoothReceiver action =" + action);
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {//每扫描到一个设备，系统都会发送此广播。
                //获取蓝牙设备
                BluetoothDevice scanDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (scanDevice == null || scanDevice.getName() == null) return;
                Log.i(TAG, "name=" + scanDevice.getName() + "address=" + scanDevice.getAddress());
                //蓝牙设备名称
                String name = scanDevice.getName();
                textView.setText(name);
                mBluetoothAdapter.cancelDiscovery();
//                if (name != null && name.equals(BLUETOOTH_NAME)) {
                //取消扫描
//                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Log.i(TAG, "没有蓝牙设备");
            }
        }
    };
}
