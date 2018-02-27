package com.bsw.mydemo.activity.nfc;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcB;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bsw.mydemo.R;
import com.bsw.mydemo.Utils.Logger;
import com.bsw.mydemo.base.BaseNfcActivity;

import java.io.IOException;

public class SetNFCPasswordActivity extends BaseNfcActivity {

    private Tag tag;
    private String TAG = "tag";
    private String pass = "1111";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_nfcpassword;
    }

    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    // isoDep空指针
////            IsoDep isoDep = IsoDep.get(tag);
////            isoDep.connect();
////            isoDep.setTimeout(5000);
////            isoDep.transceive(new byte[]{
////                    (byte) 0xA2,  /* CMD = WRITE */
////                    (byte) 0x2C,  /* PAGE = 44 */
////                    (byte) array[0], (byte) array[1], (byte) 0, (byte) 0
////            });
////            isoDep.transceive(new byte[]{
////                    (byte) 0xA2,  /* CMD = WRITE */
////                    (byte) 0x2B,  /* PAGE = 42 */
////                    array[0], array[1], array[2], array[3]
////            });
////            isoDep.close();
//
//                    // Transceive failed
//
//                    String password = "pass";
//                    byte[] array = StringToAsciiString(password).getBytes();
//                    MifareUltralight mifare = MifareUltralight.get(tag);
//                    mifare.connect();
//                    mifare.setTimeout(5000);
//                    if (mifare.isConnected()) {
//                        byte[] result1 = mifare.transceive(new byte[]{
//                                (byte) 0xA2,  /* CMD = WRITE */
//                                (byte) 0x2C,  /* PAGE = 44 */
//                                (byte) 0x98, (byte) 0x76, 0, 0
//                        });
//                        byte[] result2 = mifare.transceive(new byte[]{
//                                (byte) 0xA2,  /* CMD = WRITE */
//                                (byte) 0x2B,  /* PAGE = 42 */
//                                0x00, 0x00, 0x00, 0x00
//                        });
//                    }
//                    mifare.close();
//
////            setPWDByMifareClassic(intent);
//                    setPWDByNFCb(intent);
//
//                    toast("成功");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    toast(e.getMessage());
//                }
//            }
//        }).start();

        writeNFCTag(tag);
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
            byte[] passByte = pass.getBytes();
            NdefMessage ndefMessage = new NdefMessage(new byte[]{
                    (byte) 0xA2,  /* CMD = WRITE */
                    (byte) 0x2B,  /* PAGE = 42 */
                    passByte[0], passByte[1], passByte[2], passByte[3]
            });
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

    private void setPWDByMifareClassic(Intent intent) throws IOException {
        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        MifareClassic mif = MifareClassic.get(tagFromIntent);
        int s_len = mif.getSectorCount();
        Logger.i(TAG, "tag sector count: " + s_len);

        int b_len = mif.getBlockCount();
        Logger.i(TAG, "tag block count: " + b_len);
        mif.connect();
        if (mif.isConnected()) {
            for (int i = 0; i < s_len; i++) {
                boolean isAuthenticated = false;
                if (mif.authenticateSectorWithKeyA(i, MifareClassic.KEY_MIFARE_APPLICATION_DIRECTORY)) {
                    isAuthenticated = true;
                } else if (mif.authenticateSectorWithKeyA(i, MifareClassic.KEY_DEFAULT)) {
                    isAuthenticated = true;
                } else if (mif.authenticateSectorWithKeyA(i, MifareClassic.KEY_NFC_FORUM)) {
                    isAuthenticated = true;
                } else {
                    Logger.i(TAG, "Authorization denied ");
                }

                if (isAuthenticated) {
                    int block_index = mif.sectorToBlock(i);

                    byte[] result1 = mif.transceive(new byte[]{
                            (byte) 0xA2,  /* CMD = WRITE */
                            (byte) 0x2C,  /* PAGE = 44 */
                            (byte) 0x98, (byte) 0x76, 0, 0
                    });
                    byte[] result2 = mif.transceive(new byte[]{
                            (byte) 0xA2,  /* CMD = WRITE */
                            (byte) 0x2B,  /* PAGE = 42 */
                            0x00, 0x00, 0x00, 0x00
                    });
                }
            }
        }
        mif.close();
    }

    private void setPWDByNFCb(Intent intent) throws IOException {
        Tag tagFromIntent = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        NfcB mif = NfcB.get(tagFromIntent);
        mif.connect();
        if (mif.isConnected()) {

            byte[] result1 = mif.transceive(new byte[]{
                    (byte) 0xA2,  /* CMD = WRITE */
                    (byte) 0x2C,  /* PAGE = 44 */
                    (byte) 0x98, (byte) 0x76, 0, 0
            });
            byte[] result2 = mif.transceive(new byte[]{
                    (byte) 0xA2,  /* CMD = WRITE */
                    (byte) 0x2B,  /* PAGE = 42 */
                    0x00, 0x00, 0x00, 0x00
            });
        }
        mif.close();
    }

    @Override
    protected void findViews() {

    }

    public String StringToAsciiString(String content) {
        String result = "";
        int max = content.length();
        for (int i = 0; i < max; i++) {
            char c = content.charAt(i);
            String b = Integer.toHexString(c);
            result = result + b;
        }
        return result;
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
}
