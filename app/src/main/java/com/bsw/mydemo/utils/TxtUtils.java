package com.bsw.mydemo.utils;

import android.widget.EditText;
import android.widget.TextView;

/**
 * @author 半寿翁
 * @date 2017/10/11
 */
public class TxtUtils {
    public static String getText(EditText editText) {
        return editText.getText().toString().trim();
    }

    public static String getText(TextView textText) {
        return textText.getText().toString().trim();
    }
}
