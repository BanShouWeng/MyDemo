package com.bsw.mydemo.Utils;

import android.util.Log;

import com.bsw.mydemo.BuildConfig;

import java.io.PrintWriter;
import java.io.StringWriter;


/**
 * @author 半寿翁
 */
public class Logger {
    //设为false关闭日志
    private static int showLength = 3999;

    /**
     * 打印捕获的错误日志
     *
     * @param exception 异常
     * @param tag       打印log的标记
     */
    public static <T extends Exception> void i(String tag, T exception) {
        if (! BuildConfig.DEBUG) {
            return;
        }
        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        String logContent = stackTrace.toString();
        if (logContent.length() > showLength) {
            String show = logContent.substring(0, showLength);
            Log.i(tag, show);
            /*剩余的字符串如果大于规定显示的长度，截取剩余字符串进行递归，否则打印结果*/
            if ((logContent.length() - showLength) > showLength) {
                String partLog = logContent.substring(showLength, logContent.length());
                i(tag, partLog);
            } else {
                String printLog = logContent.substring(showLength, logContent.length());
                Log.i(tag, printLog);
            }
        } else {
            Log.i(tag, logContent);
        }
    }

    public static void i(String tag, String logContent) {
        if (! BuildConfig.DEBUG) {
            return;
        }
        if (logContent.length() > showLength) {
            String show = logContent.substring(0, showLength);
            Log.i(tag, show);
            /*剩余的字符串如果大于规定显示的长度，截取剩余字符串进行递归，否则打印结果*/
            if ((logContent.length() - showLength) > showLength) {
                String partLog = logContent.substring(showLength, logContent.length());
                i(tag, partLog);
            } else {
                String printLog = logContent.substring(showLength, logContent.length());
                Log.i(tag, printLog);
            }
        } else {
            Log.i(tag, logContent);
        }
    }

    public static void v(String tag, String logContent) {
        if (! BuildConfig.DEBUG) {
            return;
        }
        if (logContent.length() > showLength) {
            String show = logContent.substring(0, showLength);
            Log.v(tag, show);
            /*剩余的字符串如果大于规定显示的长度，截取剩余字符串进行递归，否则打印结果*/
            if ((logContent.length() - showLength) > showLength) {
                String partLog = logContent.substring(showLength, logContent.length());
                v(tag, partLog);
            } else {
                String printLog = logContent.substring(showLength, logContent.length());
                Log.v(tag, printLog);
            }
        } else {
            Log.v(tag, logContent);
        }
    }

    public static void d(String tag, String logContent) {
        if (! BuildConfig.DEBUG) {
            return;
        }
        if (logContent.length() > showLength) {
            String show = logContent.substring(0, showLength);
            Log.d(tag, show);
            /*剩余的字符串如果大于规定显示的长度，截取剩余字符串进行递归，否则打印结果*/
            if ((logContent.length() - showLength) > showLength) {
                String partLog = logContent.substring(showLength, logContent.length());
                d(tag, partLog);
            } else {
                String printLog = logContent.substring(showLength, logContent.length());
                Log.d(tag, printLog);
            }
        } else {
            Log.d(tag, logContent);
        }
    }

    public static void w(String tag, String logContent) {
        if (! BuildConfig.DEBUG) {
            return;
        }
        if (logContent.length() > showLength) {
            String show = logContent.substring(0, showLength);
            Log.w(tag, show);
            /*剩余的字符串如果大于规定显示的长度，截取剩余字符串进行递归，否则打印结果*/
            if ((logContent.length() - showLength) > showLength) {
                String partLog = logContent.substring(showLength, logContent.length());
                w(tag, partLog);
            } else {
                String printLog = logContent.substring(showLength, logContent.length());
                Log.w(tag, printLog);
            }
        } else {
            Log.w(tag, logContent);
        }
    }

    public static void e(String tag, String logContent) {
        if (! BuildConfig.DEBUG) {
            return;
        }
        if (logContent.length() > showLength) {
            String show = logContent.substring(0, showLength);
            Log.e(tag, show);
            /*剩余的字符串如果大于规定显示的长度，截取剩余字符串进行递归，否则打印结果*/
            if ((logContent.length() - showLength) > showLength) {
                String partLog = logContent.substring(showLength, logContent.length());
                e(tag, partLog);
            } else {
                String printLog = logContent.substring(showLength, logContent.length());
                Log.e(tag, printLog);
            }
        } else {
            Log.e(tag, logContent);
        }
    }

    /**
     * 打印捕获的错误日志
     *
     * @param exception 异常
     * @param tag       打印log的标记
     */
    public static <T extends Exception> void e(String tag, T exception) {
        if (! BuildConfig.DEBUG) {
            return;
        }
        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        String logContent = stackTrace.toString();
        if (logContent.length() > showLength) {
            String show = logContent.substring(0, showLength);
            Log.e(tag, show);
            /*剩余的字符串如果大于规定显示的长度，截取剩余字符串进行递归，否则打印结果*/
            if ((logContent.length() - showLength) > showLength) {
                String partLog = logContent.substring(showLength, logContent.length());
                e(tag, partLog);
            } else {
                String printLog = logContent.substring(showLength, logContent.length());
                Log.e(tag, printLog);
            }
        } else {
            Log.e(tag, logContent);
        }
    }

    /**
     * 打印捕获的错误日志
     *
     * @param exception 异常
     * @param tag       打印log的标记
     */
    public static <T extends Exception> void e(String tag, String content, T exception) {
        if (! BuildConfig.DEBUG) {
            return;
        }
        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        String logContent = stackTrace.toString();
        logContent = content + logContent;
        if (logContent.length() > showLength) {
            String show = logContent.substring(0, showLength);
            Log.e(tag, show);
            /*剩余的字符串如果大于规定显示的长度，截取剩余字符串进行递归，否则打印结果*/
            if ((logContent.length() - showLength) > showLength) {
                String partLog = logContent.substring(showLength, logContent.length());
                e(tag, partLog);
            } else {
                String printLog = logContent.substring(showLength, logContent.length());
                Log.e(tag, printLog);
            }
        } else {
            Log.e(tag, logContent);
        }
    }

    /**
     * 打印捕获的错误日志
     *
     * @param exception 异常
     * @param tag       打印log的标记
     */
    public static <T extends Throwable> void e(String tag, T exception) {
        if (! BuildConfig.DEBUG) {
            return;
        }
        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        String logContent = stackTrace.toString();
        if (logContent.length() > showLength) {
            String show = logContent.substring(0, showLength);
            Log.e(tag, show);
            /*剩余的字符串如果大于规定显示的长度，截取剩余字符串进行递归，否则打印结果*/
            if ((logContent.length() - showLength) > showLength) {
                String partLog = logContent.substring(showLength, logContent.length());
                e(tag, partLog);
            } else {
                String printLog = logContent.substring(showLength, logContent.length());
                Log.e(tag, printLog);
            }
        } else {
            Log.e(tag, logContent);
        }
    }
}
