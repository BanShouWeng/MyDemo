package com.bsw.mydemo.utils;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.IntDef;
import android.support.annotation.StringRes;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.SparseArray;
import android.widget.TextView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文本格式化类
 *
 * @author 半寿翁
 * @date 2018/12/21.
 */
public class StringFormatUtils {
    /*-------------------------显示文本加密------------------------*/

    /**
     * 保密措施
     *
     * @param isPhone 是否是手机号
     * @param str     保密内容
     */
    public static String securityString(String str, boolean isPhone) {
        if (!TextUtils.isEmpty(str)) {
            if (isPhone) {
                if (str.length() > 7) {
                    String a = str.substring(0, 3);
                    String c = str.substring(7);
                    str = a.concat("****").concat(c);
                }
            } else {
                String b = str.substring(str.length() - 1);
                for (int i = 0; i < str.length() - 1; i++) {
                    b = "*".concat(b);
                }
                str = b;
            }
        }
        return str;
    }

    /*-------------------------用于展示后台返回数据（若展示数据为空，则返回“-”，或设置默认项）------------------------*/

    /**
     * 判断传入文本是否为空，若为空，则显示"-"
     *
     * @param text 待判断文本
     * @return 返回字符串
     */
    public static String getString(String text) {
        return getString(text, "-");
    }

    /**
     * 判断传入文本是否为空，若为空，则显示默认字符串
     *
     * @param text 待判断文本
     * @return 返回字符串
     */
    public static String getString(String text, String defaultString) {
        return TextUtils.isEmpty(text) ? defaultString : text;
    }

    /**
     * 将int类型数据转换为String
     *
     * @param text int文本
     * @return String
     */
    public static String getString(int text) {
        return getString(String.format(Locale.CHINA, "%d", text));
    }

    /**
     * 将long类型数据转换为String
     *
     * @param text long文本
     * @return String
     */
    public static String getString(long text) {
        return getString(String.format(Locale.CHINA, "%d", text));
    }

    /**
     * 将double类型数据转换为String
     *
     * @param text double文本
     * @return String
     */
    public static String getString(double text) {
        return getString(String.format(Locale.CHINA, "%s", text));
    }

    /**
     * 将double类型数据转换为String
     *
     * @param text double文本
     * @return String
     */
    public static String getString(float text) {
        return getString(String.format(Locale.CHINA, "%s", text));
    }

    /*-------------------------用于根据业务逻辑显示或获取用户输入数据/APP显示的文本/封装的String------------------------*/

    /**
     * 获取stringId指向的文本
     *
     * @param context    上下文
     * @param resourceId 文本Id
     * @return 文本对应的字符串
     */
    public static String getString(Context context, int resourceId) {
        return context.getResources().getString(resourceId);
    }

    /**
     * 获取stringId指向的文本
     *
     * @param context    上下文
     * @param resourceId 文本Id
     * @return 文本对应的字符串
     */
    public static String getFormatString(Context context, int resourceId, Object... spanValues) {
        return String.format(context.getResources().getString(resourceId), spanValues);
    }

    /**
     * 字符串拼接
     *
     * @param interval 间隔符号
     * @param strings  待拼接字符串数组
     * @return 拼接后的字符串
     */
    public static String append(String interval, String... strings) {
        if (Const.judgeListNull(strings) == 0) {
            return "";
        }
        boolean hasInterval = !isEmpty(interval);
        StringBuilder builder = new StringBuilder();
        builder.append(strings[0]);
        for (int i = 1; i < strings.length; i++) {
            if (hasInterval) {
                builder.append(interval);
            }
            builder.append(strings[i]);
        }
        return String.valueOf(builder);
    }

    /**
     * 字符串拼接
     *
     * @param strings 待拼接字符串数组
     * @return 拼接后的字符串
     */
    public static String append(String... strings) {
        return append(null, strings);
    }

    /**
     * 是否为空
     *
     * @param str 待判断字符串
     * @return 是否为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * 过滤空格、回车
     *
     * @param str 待处理字符串
     * @return 过滤后的结果
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    /*-------------------------控件文本提取------------------------*/

    /**
     * 获取String类型
     *
     * @param textText 文本控件，EditText/Button都是继承TextView，不加额外封装
     * @return 返回字符串
     */
    public static String getString(TextView textText) {
        return textText.getText().toString().trim();
    }

    /**
     * 获取Integer类型
     *
     * @param textText 文本控件，EditText/Button都是继承TextView，不加额外封装
     * @return 返回数字
     */
    public static Integer getInt(TextView textText) throws NumberFormatException {
        return Integer.valueOf(textText.getText().toString().trim());
    }

    /**
     * 获取Float类型
     *
     * @param textText 文本控件，EditText/Button都是继承TextView，不加额外封装
     * @return 返回Float
     */
    public static Float getFloat(TextView textText) throws NumberFormatException {
        return Float.valueOf(textText.getText().toString().trim());
    }

    /**
     * 获取Double类型
     *
     * @param textText 文本控件，EditText/Button都是继承TextView，不加额外封装
     * @return 返回Double
     */
    public static Double getDouble(TextView textText) throws NumberFormatException {
        return Double.valueOf(textText.getText().toString().trim());
    }

    /**
     * 获取Long类型
     *
     * @param textText 文本控件，EditText/Button都是继承TextView，不加额外封装
     * @return 返回Long
     */
    public static Long getLong(TextView textText) throws NumberFormatException {
        return Long.valueOf(textText.getText().toString().trim());
    }

    /**
     * 获取Boolean类型
     *
     * @param textText 文本控件，EditText/Button都是继承TextView，不加额外封装
     * @return 返回Boolean
     */
    public static Boolean getBoolean(TextView textText) throws ClassFormatError {
        return Boolean.valueOf(textText.getText().toString().trim());
    }

    /*-------------------------多字符串长度格式化------------------------*/
    /**
     * 右对齐
     */
    public static final int LENGTH_FORMAT_RIGHT = 0x10;
    /**
     * 两端对齐
     */
    public static final int LENGTH_FORMAT_BOTH_ENDS = 0x11;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LENGTH_FORMAT_RIGHT, LENGTH_FORMAT_BOTH_ENDS})
    @interface LengthFormatType {
    }

    public synchronized static Map<String, String> formatStringLength(@LengthFormatType int type, String... strings) {
        return formatStringLength(type, false, strings);
    }

    public synchronized static Map<String, String> formatStringLength(@LengthFormatType int type, boolean isEnglishSplit, String... strings) {
        List<Integer> lengths = new ArrayList<>();
        Map<String, String> stringMap = new HashMap<>();
        int maxLength = 0;
        int currentLength;
        for (String string : strings) {
            currentLength = getStringLength(string);
            if (currentLength > maxLength) {
                maxLength = currentLength;
            }
            lengths.add(currentLength);
        }
        if (maxLength > 0) {
            for (int i = 0; i < strings.length; i++) {
                String fillString = strings[i];
                stringMap.put(fillString, fillSpace((maxLength - lengths.get(i)) / getStringLength("\u00A0"), fillString, type, isEnglishSplit));
            }
        }
        return stringMap;
    }

    /**
     * 格式化字符串，右对齐/两端对齐
     *
     * @param context   上下文
     * @param stringRes 字符串Res
     * @return 对齐后的字符串map
     */
    public synchronized static SparseArray<String> formatStringLength(Context context, @LengthFormatType int type, @StringRes int... stringRes) {
        return formatStringLength(context, type, false, stringRes);
    }

    public synchronized static SparseArray<String> formatStringLength(Context context, @LengthFormatType int type, boolean isEnglishSplit, @StringRes int... stringRes) {
        List<Integer> lengths = new ArrayList<>();
        SparseArray<String> stringMap = new SparseArray<>();
        int maxLength = 0;

        String currentString;
        for (int string : stringRes) {
            currentString = getString(context, string);
//            int currentLength = getStringShowLength(currentString);
            int currentLength = getStringLength(currentString);
            if (currentLength > maxLength) {
                maxLength = currentLength;
            }
            lengths.add(currentLength);
        }
        if (maxLength > 0) {
            for (int i = 0; i < stringRes.length; i++) {
                int fillString = stringRes[i];
                stringMap.put(fillString, fillSpace((maxLength - lengths.get(i)) / getStringLength("\u00A0"), getString(context, fillString), type, isEnglishSplit));
            }
        }
        return stringMap;
    }

    /**
     * 添加空格
     *
     * @param count 长度
     * @param s     被填充字符串
     * @param type  类型
     * @return 填充后的结果
     */
    private synchronized static String fillSpace(int count, String s, int type, boolean isEnglishSplit) {
        if (StringFormatUtils.isEmpty(s)) {
            return s;
        }
        if (count > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            switch (type) {
                case LENGTH_FORMAT_BOTH_ENDS:
                    char[] chars = s.toCharArray();
                    // 如果英语单词中间也做拆分
                    if (isEnglishSplit) {
                        int charSize = chars.length;
                        if (charSize == 1) {// 如果只有一位的情况下
                            for (int i = 0; i < count; i++) {
                                stringBuilder.append("\u00A0");
                                if (i == count / 2) {
                                    stringBuilder.append(chars[0]);
                                }
                            }
                        } else {            // 如果多位的情况下
                            // 商
                            int quotient = count / (charSize - 1);
                            // 余数
                            int remainder = count % (charSize - 1);
                            stringBuilder.append(chars[0]);
                            for (int i = 1; i < charSize; i++) {
                                for (int j = 0; j < quotient; j++)
                                    stringBuilder.append("\u00A0");
                                if (remainder > 0) {
                                    stringBuilder.append("\u00A0");
                                    remainder--;
                                }
                                stringBuilder.append(chars[i]);
                            }
                        }
                    } else {
                        // 字符串列表，由于英文/数字不拆分，因此需将被填充字符串拆分为需补充占位符的部分（拆分项）
                        List<String> strings = new ArrayList<>();
                        // 字符串缓存，用于存储需拼接的英文/数字
                        StringBuilder stringTemp = new StringBuilder();
                        boolean isCharSpan = false;
                        for (char c : chars) {
                            /*
                             * 判断是否是大小写字母或数字，如果是，则在字符串缓存中拼接，若不是，则直接添加到列表中
                             * isUpperCase  大写字母匹配
                             * isLowerCase  小写字母匹配
                             * isDigit      数字匹配
                             * "'"          ' 是英文缩写常用字符，因此也与字符做相同处理
                             */
                            if (Character.isUpperCase(c)
                                    || Character.isLowerCase(c)
                                    || Character.isDigit(c)
                                    || String.valueOf(c).equals("'")) {
                                stringTemp.append(c);
                                isCharSpan = true;
                                continue;
                            }
                            isCharSpan = false;
                            /*
                             * 若有缓存中的字母/数字，则取缓存字符串；若没有则取对应的char
                             */
                            if (stringTemp.length() > 0) {
                                if (isSpaceChar(c)) {
                                    // 如果是空格，则只需要存储缓存的字符串，空格自动忽略
                                    strings.add(stringTemp.toString());
                                    // 清空缓存
                                    stringTemp.setLength(0);
                                } else if (CharacterJudge.isPunctuation(c)) {
                                    // 如果是标点符号，则需要在存储缓存的字符串后拼接该标点，其后再读取这个标点
                                    stringTemp.append(c);
                                    strings.add(stringTemp.toString());
                                    stringTemp.setLength(0);
                                } else {
                                    // 如果不满足上面判断，则添加缓存字符串后，再添加一条当前char，如：“项目deadline是今晚”，拆分后位{"项","目","deadline","是","今","晚"}
                                    strings.add(stringTemp.toString());
                                    stringTemp.setLength(0);
                                    strings.add(String.valueOf(c));
                                }
                            } else {
                                strings.add(String.valueOf(c));
                            }
                        }
                        if (isCharSpan) {
                            strings.add(stringTemp.toString());
                        }

                        int stringSize = strings.size();
                        if (stringSize == 1) {
                            for (int i = 0; i < count; i++) {
                                stringBuilder.append("\u00A0");
                                if (i == count / 2) {
                                    stringBuilder.append(strings.get(0));
                                }
                            }
                        } else {
                            // 商
                            int quotient = count / (stringSize - 1);
                            // 余数
                            int remainder = count % (stringSize - 1);
                            stringBuilder.append(strings.get(0));
                            for (int i = 1; i < stringSize; i++) {
                                for (int j = 0; j < quotient; j++)
                                    stringBuilder.append("\u00A0");
                                if (remainder > 0) {
                                    stringBuilder.append("\u00A0");
                                    remainder--;
                                }
                                stringBuilder.append(strings.get(i));
                            }
                        }
                    }
                    break;

                case LENGTH_FORMAT_RIGHT:
                    for (int i = 0; i < count; i++) {
                        stringBuilder.append("\u00A0");
                    }
                    stringBuilder.append(s);
                    break;
            }
            return stringBuilder.toString();
        }
        return s;
    }

    /**
     * 校验一个字符是否是空格
     * \u00A0 ：不间断空格\u00A0,主要用在office中,让一个单词在结尾处不会换行显示,快捷键ctrl+shift+space;
     * \u0020 ：半角空格(英文符号)\u0020,代码中常用的;
     * \u3000 ：全角空格(中文符号)\u3000,中文文章中使用;
     *
     * @param c 被校验的字符
     * @return true代表是空格
     */
    public synchronized static boolean isSpaceChar(char c) {
        String[] spaces = {"\u00A0", "\u0020", "\u3000"};
        String s = String.valueOf(c);
        return spaces[0].equals(s)
                || spaces[1].equals(s)
                || spaces[2].equals(s);
    }

    public synchronized static int getStringLength(String s) {
        return (int) new TextPaint().measureText(s);
    }

    /**
     * 判断字符串中是否包含中文
     *
     * @param str 待校验字符串
     * @return 是否为中文
     * @warn 不能校验是否为中文标点符号
     */
    private synchronized static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        return m.find();
    }
}