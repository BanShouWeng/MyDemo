package com.bsw.mydemo.activity.nfc;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bsw.mydemo.R;
import com.bsw.mydemo.utils.TxtUtils;
import com.bsw.mydemo.activity.view.ScanCodeActivity;
import com.bsw.mydemo.base.BaseNfcActivity;

/**
 * @author 半寿翁
 */
public class NFCWriteActivity extends BaseNfcActivity {

    private final int REQUEST_CODE = 102;

    private String inputText = "";//短信
    private EditText etNfcContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.NFC_write);
        setBaseRightText(R.string.confirm);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_nfcwrite;
    }

    @Override
    protected void findViews() {
        etNfcContent = getView(R.id.et_nfc_content);
    }

    @Override
    protected void formatViews() {
        etNfcContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        setClickView(R.id.to_scan_qrCode);
    }

    @Override
    protected void formatData() {

    }

    @Override
    protected void getBundle(Bundle bundle) {

    }

    @Override
    public void onNewIntent(Intent intent) {
        if (inputText == null)
            return;
        //1.获取Tag对象
        Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        writeNFCTag(detectedTag);
    }

    /**
     * 往标签写数据的方法
     *
     * @param tag
     */
    public void writeNFCTag(Tag tag) {
        if (tag == null) {
            return;
        }
        if (TextUtils.isEmpty(inputText)) {
            inputText = TxtUtils.getText(etNfcContent);
        }
        if (TextUtils.isEmpty(inputText)){
            toast(R.string.cannot_be_null);
            return;
        }
        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[] {NdefRecord
                .createApplicationRecord(inputText)});
        //转换成字节获得大小
        int size = ndefMessage.toByteArray().length;
        try {
            //2.判断NFC标签的数据类型（通过Ndef.get方法）
            Ndef ndef = Ndef.get(tag);
            //判断是否为NDEF标签
            if (ndef != null) {
                ndef.connect();
                //判断是否支持可写
                if (! ndef.isWritable()) {
                    return;
                }
                //判断标签的容量是否够用
                if (ndef.getMaxSize() < size) {
                    return;
                }
                //3.写入数据
                ndef.writeNdefMessage(ndefMessage);
                Toast.makeText(this, "写入成功", Toast.LENGTH_SHORT).show();
            } else { //当我们买回来的NFC标签是没有格式化的，或者没有分区的执行此步
                //Ndef格式类
                NdefFormatable format = NdefFormatable.get(tag);
                //判断是否获得了NdefFormatable对象，有一些标签是只读的或者不允许格式化的
                if (format != null) {
                    //连接
                    format.connect();
                    //格式化并将信息写入标签
                    format.format(ndefMessage);
                    Toast.makeText(this, "写入成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "写入失败", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case RIGHT_TEXT_ID:
                inputText = TxtUtils.getText(etNfcContent);
                break;

            case R.id.to_scan_qrCode:
                Bundle bundle = new Bundle();
                bundle.putString("tag", "getCode");
                jumpTo(ScanCodeActivity.class, REQUEST_CODE, bundle);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            inputText = data.getStringExtra("result");
            etNfcContent.setText(inputText);
        }
    }
}
