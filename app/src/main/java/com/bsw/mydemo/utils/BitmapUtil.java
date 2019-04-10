package com.bsw.mydemo.utils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@SuppressLint({"NewApi", "DefaultLocale"})
public class BitmapUtil {

    // 保存图片
    public static String saveBitmapAsJpeg(Bitmap bitmap, Context context) {
        if (bitmap == null) {
            return null;
        }
        boolean success = false;
        File dest = null;
        try {

            File file = getAvailableDir(context);
            dest = new File(file, UUID.randomUUID().toString() + ".jpg");
            dest.createNewFile();
            FileOutputStream out = new FileOutputStream(dest);
            try {
                success = bitmap.compress(CompressFormat.JPEG, 100, out);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (success) {
                File thumb = new File(dest.getAbsolutePath());
                thumb.createNewFile();
                out = new FileOutputStream(thumb);
                success = success
                        && Bitmap.createScaledBitmap(bitmap, 200,
                        200 * bitmap.getHeight() / bitmap.getWidth(),
                        false).compress(CompressFormat.JPEG, 100, out);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success ? dest.getAbsolutePath() : null;
    }

    //默认加载
    public static String getAbsolutePath(Context mContext, String path) {
//        File file = new File(path);
//        if (!file.exists()) {
//            String pa = ImageDBService.getInstance(mContext).findValue(path);
//            if (pa != null) {
//                file = new File(pa);
//                if (file != null) {
//                    path = pa;
//                }
//            }
//        }
//        if (file.exists()) {
//            path = "file://" + path;
//        } else {
//            path = BuildConfig.STATIC_PIC_PATH + path;
//        }
        return path;
    }

    // 保存图片
    public static String saveBitmapToLocal(Bitmap bitmap, Context context) {
        if (bitmap == null) {
            return null;
        }
        boolean success = false;
        File dest = null;
        try {
            File file = getAvailableDir(context);
            dest = new File(file, UUID.randomUUID().toString() + ".jpg");

            if (!dest.exists()) {
                dest.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(dest);

            success = bitmap.compress(CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success ? dest.getAbsolutePath() : null;
    }

    public static File getAvailableDir(Context context) {
        File file = null;
        String path = null;
        if (file == null) {
            path = context.getExternalCacheDir() + "/ypyt" + "/";
            file = new File(path);
            if (file != null && !file.exists()) {
                file.mkdirs();
            }
        }
        if (file == null) {
            path = Environment.getExternalStorageDirectory() + "/ypyt" + "/";
            file = new File(path);
            if (file != null && !file.exists()) {
                file.mkdirs();
            }
        }

        if (file == null) {
            path = context.getCacheDir() + "/ypyt" + "/";
            file = new File(path);
            if (file != null && !file.exists()) {
                file.mkdirs();
            }
        }

        return file;
    }

    /**
     * 根据路径删除指定的目录或文件，无论存在与否
     *
     * @param sPath 要删除的目录或文件
     * @return 删除成功返回 true，否则返回 false。
     */
    public static boolean deleteFolder(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 判断目录或文件是否存在
        if (!file.exists()) {  // 不存在返回 false
            return flag;
        } else {
            // 判断是否为文件
            if (file.isFile()) {  // 为文件时调用删除文件方法
                return deleteFile(sPath);
            } else {  // 为目录时调用删除目录方法
                return deleteDirectory(sPath);
            }
        }
    }

    /**
     * 删除单个文件
     *
     * @param sPath 被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     *
     * @param sPath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String sPath) {
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        //删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        if (files == null) {
            return true;
        }
        for (int i = 0; i < files.length; i++) {
            //删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } //删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据本地路径获取bitmap
     */
    public static Bitmap getBitmapByUrl(String url) {
        int degree = readImgDegree(url);
        Bitmap bmp = BitmapUtil.decodeSampledBitmapFromPath(url);
        bmp = rotaingImageView(degree, bmp);
        return bmp;
    }

    @SuppressWarnings("null")
    public static byte[] decodeBitmap(String path) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高
        BitmapFactory.decodeFile(path, opts);
//		opts.inSampleSize = computeSampleSize(opts, -1, 1024 * 800);
        opts.inJustDecodeBounds = false;// 这里一定要将其设置回false，因为之前我们将其设置成了true
        opts.inPurgeable = true;
        opts.inInputShareable = true;
        opts.inDither = false;
        opts.inPurgeable = true;
        opts.inTempStorage = new byte[16 * 1024];
        FileInputStream is = null;
        Bitmap bmp = null;
        Bitmap bmp2 = null;
        ByteArrayOutputStream baos = null;
        try {
            is = new FileInputStream(path);
            bmp = BitmapFactory.decodeFileDescriptor(is.getFD(), null, opts);
            double scale = getScaling(opts.outWidth * opts.outHeight,
                    1024 * 600);
            bmp2 = Bitmap.createScaledBitmap(bmp,
                    (int) (opts.outWidth * scale),
                    (int) (opts.outHeight * scale), true);
            bmp.recycle();
            baos = new ByteArrayOutputStream();
            bmp2.compress(CompressFormat.JPEG, 100, baos);
            return baos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
                if (baos != null)
                    baos.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (baos != null) {
            return baos.toByteArray();
        } else {
            return null;
        }

    }

    private static double getScaling(int src, int des) {
        /**
         * 48 目标尺寸÷原尺寸 sqrt开方，得出宽高百分比 49
         */
        double scale = Math.sqrt((double) des / (double) src);
        return scale;
    }

    /**
     * 读取图片旋转角度
     *
     * @param path
     * @return
     */
    public static int readImgDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片
     *
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * @Method : 图像压缩
     * @Description :
     * @auth : RamboCai
     */

    // Path&&URl
    public static Bitmap decodeSampledBitmapFromPath(String path, int reqWidth,
                                                     int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    // Path&&URl
    public static Bitmap decodeSampledBitmapFromPath(String path) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, 540,
                960);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    // 将Uri转为Path
    public static String uri2StringPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }

    // Resource
    public static Bitmap decodeSampledBitmapFromResource(Resources res,
                                                         int resId, int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    // 缩放比例计算
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 保存bitmap，返回路径
     *
     * @return
     */
    public static String savaBitmap(Bitmap bitmap) {
        String mPhotoPath = Environment.getExternalStorageDirectory() + "/crop"
                + "/" + System.currentTimeMillis() + ".jpg";

        try {
            File mcropFile = new File(mPhotoPath);
            if (!mcropFile.getParentFile().exists()) {
                mcropFile.getParentFile().mkdirs();
            }
            if (!mcropFile.exists()) {
                mcropFile.createNewFile();
            }

            FileOutputStream out = new FileOutputStream(mcropFile);
            bitmap.compress(CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            return mPhotoPath;

        } catch (IOException e) {
            // TODO Auto-generated catch <span id="3_nwp"
            // style="width: auto; height: auto; float: none;"><a id="3_nwl"
            // href="http://cpro.baidu.com/cpro/ui/uijs.php?rs=1&u=http%3A%2F%2Fwww%2Edaxueit%2Ecom%2Farticle%2F5182%2Ehtml&p=baidu&c=news&n=10&t=tpclicked3_hc&q=00007110_cpr&k=block&k0=android&kdi0=8&k1=%B1%E0%B3%CC%BC%BC%CA%F5&kdi1=8&k2=alpha&kdi2=8&k3=%B1%E0%B3%CC&kdi3=8&k4=only&kdi4=8&k5=block&kdi5=8&sid=39ea44ec4f838052&ch=0&tu=u1704338&jk=7fd53b4ff3fdca4a&cf=29&fv=14&stid=9&urlid=0&luki=6&seller_id=1&di=128"
            // target="_blank" mpid="3" style="text-decoration: none;"><span
            // style="color:#0000ff;font-size:14px;width:auto;height:auto;float:none;">block</span></a></span>
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] bmpToByteArray(final Bitmap bmp,
                                        final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}