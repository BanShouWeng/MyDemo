package com.bsw.mydemo;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.SparseIntArray;

import com.bsw.mydemo.utils.Logger;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.bsw.mydemo", appContext.getPackageName());
    }

    @Test
    public void sparseIntArray() {
        SparseIntArray array = new SparseIntArray();
        array.put(5, 6);
        int a = array.get(5);
        int b = array.get(1);
        Logger.i("SparseIntArray", a + "");
        Logger.i("SparseIntArray", b + "");
    }
}
