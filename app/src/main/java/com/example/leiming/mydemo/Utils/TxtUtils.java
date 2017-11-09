package com.example.leiming.mydemo.Utils;

import android.widget.EditText;
import android.widget.TextView;

/**
 * @author leiming
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
