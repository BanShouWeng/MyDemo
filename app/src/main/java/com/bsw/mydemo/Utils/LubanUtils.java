package com.bsw.mydemo.Utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.bsw.mydemo.widget.luban.CompressionPredicate;
import com.bsw.mydemo.widget.luban.Luban;
import com.bsw.mydemo.widget.luban.OnCompressListener;

import java.io.File;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author leiming
 * @date 2018/6/13.
 */

public class LubanUtils {
    private static CompositeDisposable mDisposable;

    public static <T> void asynchronousCompression(Context context, final List<T> photos, final LubanImageListener lubanImageListener) {
        Luban.with(context)
                .load(photos)
                .ignoreBy(100)
                .setTargetDir(getPath())
                .filter(new CompressionPredicate() {
                    @Override
                    public boolean apply(String path) {
                        return ! (TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                    }
                })
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        // 压缩开始前调用，可以在方法内启动 loading UI
                    }

                    @Override
                    public void onSuccess(File file) {
                        lubanImageListener.lubanSuccess(file);
                        //  压缩成功后调用，返回压缩后的图片文件
                    }

                    @Override
                    public void onError(Throwable e) {
                        lubanImageListener.lubanError(e);
                        //  当压缩过程出现问题时调用
                    }
                }).launch();
    }

    public static <T> void synchronousCompression(final Context context, final List<T> photos, final LubanImageListener lubanImageListener) {
        if (Const.isEmpty(mDisposable)) {
            mDisposable = new CompositeDisposable();
        }
        try {
            mDisposable.add(Flowable.just(photos)
                    .observeOn(Schedulers.io())
                    .map(new Function<List<T>, List<File>>() {
                        @Override
                        public List<File> apply(@NonNull List<T> list) throws Exception {
                            return Luban.with(context)
                                    .load(list)
                                    .get();
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<File>>() {
                        @Override
                        public void accept(@NonNull List<File> list) {
                            lubanImageListener.lubanSuccess(list);
                        }
                    }));
        } catch (Throwable throwable) {
            lubanImageListener.lubanError(throwable);
        }
    }

    private static String getPath() {
        String path = Environment.getExternalStorageDirectory() + "/Luban/image/";
        File file = new File(path);
        if (file.mkdirs()) {
            return path;
        }
        return path;
    }

    interface LubanImageListener {
        void lubanSuccess(File files);

        void lubanSuccess(List<File> files);

        void lubanError(Throwable e);
    }
}
