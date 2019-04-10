package com.bsw.mydemo;

import android.util.SparseIntArray;

import com.bsw.mydemo.utils.Const;

import org.junit.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void RxJava() {
        Observable.just("On", "Off", "On", "On")
                //在传递过程中对事件进行过滤操作
                .map(new Function<String, Boolean>() {
                    @Override
                    public Boolean apply(String s) throws Exception {
                        return s != null;
                    }
                })
                .subscribe(new Observer<Boolean>() {
                               @Override
                               public void onError(Throwable exception) {
                                   //出现错误会调用这个方法
                                   StringWriter stackTrace = new StringWriter();
                                   exception.printStackTrace(new PrintWriter(stackTrace));
                                   String logContent = stackTrace.toString();
                                   System.out.println(logContent);
                               }

                               @Override
                               public void onComplete() {
                                   //被观察者的onCompleted()事件会走到这里;
                                   System.out.println("结束观察...\n");
                               }

                               @Override
                               public void onSubscribe(Disposable d) {

                               }

                               @Override
                               public void onNext(Boolean s) {
                                   //处理传过来的onNext事件
                                   System.out.println("handle this---" + s);
                               }
                           }
                );
    }

    @Test
    public void and() {
        final int LEFT = 1;
        final int TOP = 2;
        final int RIGHT = 4;
        final int BOTTOM = 8;

        int a = LEFT | RIGHT;

        System.out.println("LEFT = " + (LEFT & a) + " *** TOP = " + (TOP & a) + " *** RIGHT = " + (RIGHT & a) + " *** BOTTOM = " + (BOTTOM & a));
    }
}