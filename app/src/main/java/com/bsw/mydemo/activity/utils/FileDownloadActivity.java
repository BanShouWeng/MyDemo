package com.bsw.mydemo.activity.utils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bsw.mydemo.R;
import com.bsw.mydemo.activity.view.ScanCodeActivity;
import com.bsw.mydemo.base.BaseBean;
import com.bsw.mydemo.base.BaseNetActivity;
import com.bsw.mydemo.utils.Logger;
import com.bsw.mydemo.utils.download.DownloadInfo;
import com.bsw.mydemo.utils.download.DownloadManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class FileDownloadActivity extends BaseNetActivity {
    private final int REQUEST_CODE = 102;
    private ProgressBar uploadProgress;

    private String inputText = "http://zxyun119.com/app/hzyzx.apk";//下载地址
    private EditText etUploadUrl;
    private DownloadInfo downloadInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.main_activity_btn_file_download);
        setBaseRightText(R.string.confirm);

        EventBus.getDefault().register(this);
    }

    @Override
    public void success(String action, BaseBean baseBean) {

    }

    @Override
    public void error(String action, Throwable e) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_file_download;
    }

    @Override
    protected void findViews() {
        etUploadUrl = getView(R.id.et_download_url);
        uploadProgress = getView(R.id.main_progress);
    }

    @Override
    protected void formatViews() {
        setClickView(R.id.to_scan_qrCode);
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
            case RIGHT_TEXT_ID:
                Logger.i(getName(), "inputText = " + inputText);
                upload();
                break;

            case R.id.to_scan_qrCode:
                Bundle bundle = new Bundle();
                bundle.putString("tag", "getCode");
                jumpTo(ScanCodeActivity.class, REQUEST_CODE, bundle);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void update(DownloadInfo info){
        if (info.getUrl() != inputText){
            return;
        }
        if (DownloadInfo.DOWNLOAD.equals(info.getDownloadStatus())){
            downloadInfo = info;
            if (info.getTotal() == 0){
                uploadProgress.setProgress(0);
            }else{
                float progress = info.getProgress() * uploadProgress.getMax() / info.getTotal();
                uploadProgress.setProgress((int) progress);
            }

        }else if (DownloadInfo.DOWNLOAD_OVER.equals(info.getDownloadStatus())){

            uploadProgress.setProgress(uploadProgress.getMax());

        }else if (DownloadInfo.DOWNLOAD_PAUSE.equals(info.getDownloadStatus())){

            Toast.makeText(this,"下载暂停",Toast.LENGTH_SHORT).show();

        }else if (DownloadInfo.DOWNLOAD_CANCEL.equals(info.getDownloadStatus())){

            uploadProgress.setProgress(0);
            Toast.makeText(this,"下载取消",Toast.LENGTH_SHORT).show();

        }else if (DownloadInfo.DOWNLOAD_ERROR.equals(info.getDownloadStatus())){

            Toast.makeText(this,"下载出错",Toast.LENGTH_SHORT).show();

        }
    }

    public void upload(){
        new Thread(new Runnable() {
            @Override
            public void run() {
//                        getFile(inputText, false);
                DownloadManager.getInstance().download(inputText);
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            inputText = data.getStringExtra("result");
            etUploadUrl.setText(inputText);
            upload();
        }
    }
}
