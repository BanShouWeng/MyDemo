package com.bsw.mydemo.widget.edittext;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

/**
 * @author 半寿翁
 * @date 2017/10/11
 */
public class HideHintEditText extends AppCompatEditText {
    private String hint;

    public HideHintEditText(Context context) {
        super(context);
        init();
    }

    public HideHintEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        CharSequence noEmojiString = emojiJudge(text);
        // text是SpannableStringBuilder，不能直接判断
        if (!text.toString().equals(noEmojiString.toString())) {
            setText(noEmojiString);
            // 如果
            setSelection(noEmojiString.length());
        }
    }

    private CharSequence emojiJudge(CharSequence text) {
        try {
            return Pattern.compile("[\ud800\udc00-\udbff\udfff]|[\u2600-\u27ff]|\r|\n|\\s",
                    Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE).matcher(new String(text.toString().getBytes("UTF-8"), "UTF-8")).replaceAll("");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return text;
        }
    }

    /**
     * 获得hint值
     *
     * @author admin 2016-9-5 下午4:32:19
     */
    private void init() {
        hint = getHint().toString();
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        if (focused) {
            setHint("");
        } else {
            setHint(hint);
        }
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }
}
