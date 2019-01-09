package com.jacky.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;

import com.jacky.log.Logger;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Created by jacky on 2018/10/29.
 */
public final class AppUtil {

    /**
     * dp转换成px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    /**
     * px转换成dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 无论成功与否都会回调{@link FragmentActivity#onRequestPermissionsResult(int, String[], int[])}方法
     * @param context
     * @param premission {@link android.Manifest.permission}
     * @param requestCode
     */
    public static void requestPermission(FragmentActivity context, int requestCode, String... premission) {
        if(premission == null) return;
        for(String pre : premission) {
            if (ContextCompat.checkSelfPermission(context, pre) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(context, premission,requestCode);
                return;
            }
        }
        int[] result = new int[premission.length];
        Arrays.fill(result, PackageManager.PERMISSION_GRANTED);
        context.onRequestPermissionsResult(requestCode, premission, result);
    }

    /**
     * 无论成功与否都会回调{@link Fragment#onRequestPermissionsResult(int, String[], int[])}方法
     * @param fragment
     * @param premission {@link android.Manifest.permission}
     * @param requestCode
     */
    public static void requestPermission(Fragment fragment, int requestCode, String... premission) {
        if(premission == null) return;
        Context context = fragment.getContext();
        for(String pre : premission) {
            if (ContextCompat.checkSelfPermission(context, pre) != PackageManager.PERMISSION_GRANTED) {
                fragment.requestPermissions(premission,requestCode);
                return;
            }
        }
        int[] result = new int[premission.length];
        Arrays.fill(result, PackageManager.PERMISSION_GRANTED);
        fragment.onRequestPermissionsResult(requestCode, premission, result);
    }

    /**
     *  检测权限申请是否可以
     * @param permission
     * @param grantResults
     * @return
     */
    public static boolean isPermissionOK(String[] permission, int[] grantResults) {
        for(int i : grantResults) {
            if (i == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 兼容android 7.0的权限处理
     * @param context
     * @param intent
     * @param file
     * @return
     */
    public static Uri fromFile(Context context, Intent intent, File file) {
        Uri data;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            data = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
            if(intent != null) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        } else {
            data = Uri.fromFile(file);
        }
        return data;
    }

    /**
     * 根据uri获取文件路径
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{
                    MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 通过反射机制为对象设值
     * @param object
     * @param fieldName
     * @param value
     */
    public static void setFieldValue(final Object object, final String fieldName, final Object value) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, value);
        } catch (Exception e) {
            Logger.e(e);
        }
    }

    /**
     * 通过反射机制获取对象里面的值
     * @param object
     * @param fieldName
     * @return
     */
    public static Object getFieldValue(Object object, String fieldName) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(object);
        } catch (Exception e) {
            Logger.e(e);
        }
        return null;
    }
}
