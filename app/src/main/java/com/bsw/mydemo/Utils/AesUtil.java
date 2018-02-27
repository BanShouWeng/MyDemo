package com.bsw.mydemo.Utils;

import android.annotation.SuppressLint;
import android.util.Log;

import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author leiming
 * @date 2017/10/11
 */
public class AesUtil {

    private static final String AES_KEY = "zxycloud20160629";

    public static String AESEncode(String content) {
        try {
            SecretKey key = new SecretKeySpec(Arrays.copyOf(AES_KEY.getBytes("utf-8"), 16), "AES");

            @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance("AES/ECB/Pkcs5Padding");

            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] byteEnconde = content.getBytes("utf-8");

            return parseByte2HexStr(cipher.doFinal(byteEnconde));

        } catch (Exception e) {
            Log.e("AES", "AES加密失败");
            return null;
        }
    }

    public static String AESDecode(String encodeKey, byte[] content) {
        try {
            SecretKey key = new SecretKeySpec(Arrays.copyOf(encodeKey.getBytes("utf-8"), 16), "AES");

            @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance("AES/ECB/Pkcs5Padding");

            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] byteDecode = cipher.doFinal(content);

            return new String(byteDecode, "utf-8");

        } catch (Exception e) {
            Log.e("AES", "AES解密失败");
            return null;
        }
    }

    private static String parseByte2HexStr(byte buf[]) {
        StringBuilder sb = new StringBuilder();
        for (byte aBuf : buf) {
            String hex = Integer.toHexString(aBuf & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toLowerCase());
        }
        return sb.toString();
    }

    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }
}
