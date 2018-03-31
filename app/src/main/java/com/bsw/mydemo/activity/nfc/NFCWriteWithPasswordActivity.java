package com.bsw.mydemo.activity.nfc;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bsw.mydemo.R;
import com.bsw.mydemo.Utils.Logger;
import com.bsw.mydemo.base.BaseNfcActivity;
/**
 * @author 半寿翁
 */
public class NFCWriteWithPasswordActivity extends BaseNfcActivity {

    private String message = "fe847f40be464016af2ee3f6fa74ca4e@6de34e4daeaf45aba998d0394f0a993b@zxycloud.com";// 短信
    private String pwd = "1111";// 密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_nfcwrite_with_password;
    }

    @Override
    protected void findViews() {

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
    public void onNewIntent(Intent intent) {
        if (message == null)
            return;
        //1.获取Tag对象
        Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//        writeNFCTag(detectedTag);
        writeNFC(intent);
    }

    public void writeNFC(Intent intent) {
        int count = 0;
        if (intent == null) {
            return;
        }
        byte[] pwd_b = pwd.getBytes();
        Logger.i("tag", count++ + "");
        byte[] message_b = message.getBytes();
        Logger.i("tag", count++ + "");
        try {
//            IsoDep isoDep = IsoDep.get(tag);
//            isoDep.connect();
//            isoDep.setTimeout(5000);
//            isoDep.transceive(new byte[]{
//                    (byte) 0x1B,  /* CMD = WRITE */
//                    (byte) 0x2C,  /* PAGE = 44 */
//                    pwd_b[0], pwd_b[1], (byte) 0, (byte) 0
//            });
//            for (int i = 0; i < message_b.length / 4; i++) {
//                isoDep.transceive();
//            }
//            isoDep.close();
            MifareUltralight mifare = MifareUltralight.get((Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG));
            Logger.i("tag", count++ + "");
            mifare.connect();
            Logger.i("tag", count++ + "");
            mifare.setTimeout(5000);
            Logger.i("tag", count++ + "");
            if (mifare.isConnected()) {
                byte[] result1 = mifare.transceive(new byte[]{
                        (byte) 0x1B,  /* CMD = WRITE */
                        (byte) 0x2C,  /* PAGE = 44 */
                        pwd_b[0], pwd_b[1], (byte) 0, (byte) 0
                });
                Logger.i("tag", result1.toString());
                byte[] result2 = mifare.transceive(message_b);
                Logger.i("tag", result2.toString());

            }
            mifare.close();
            Logger.i("tag", "成功");
        } catch (Exception e) {
            Logger.i("tag", e.getMessage());
        }
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
        try {
            byte[] pwd_b = pwd.getBytes();

//            NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{new NdefRecord(NdefRecord.TNF_EXTERNAL_TYPE, "android.com:pkg".getBytes(), null,new byte[]{
//                    (byte) 0xA2,  /* CMD = WRITE */
//                    (byte) 0x2C,  /* PAGE = 44 */
//                    pwd_b[0], pwd_b[1], 0, 0
//            }), NdefRecord
//                    .createApplicationRecord(mPackageName)});
            NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{new NdefRecord(NdefRecord.TNF_EXTERNAL_TYPE, "android.com:pkg".getBytes(), null, new byte[]{
                    (byte) 0xA2,  /* CMD = WRITE */
                    (byte) 0x2C,  /* PAGE = 44 */
                    pwd_b[0], pwd_b[1], 0, 0
            })});
            //转换成字节获得大小
            int size = ndefMessage.toByteArray().length;
            //2.判断NFC标签的数据类型（通过Ndef.get方法）
            Ndef ndef = Ndef.get(tag);
            //判断是否为NDEF标签
            if (ndef != null) {
                ndef.connect();
                //判断是否支持可写
                if (!ndef.isWritable()) {
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

    }
}