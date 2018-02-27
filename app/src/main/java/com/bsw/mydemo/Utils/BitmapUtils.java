package com.bsw.mydemo.Utils;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

/**
 * @author 半寿翁
 * @date 2018/2/27
 */
public class BitmapUtils {
    public static byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}
