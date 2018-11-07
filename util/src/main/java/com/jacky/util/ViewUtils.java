package com.jacky.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jacky.log.Logger;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * Created by Administrator on 2017-08-23.
 */

public final class ViewUtils {

    public static <T extends View> T findView(@NonNull Activity activity, @IdRes int id) {
        return (T) activity.findViewById(id);
    }

    public static <T extends View> T findView(@NonNull View group, @IdRes int id) {
        return (T) group.findViewById(id);
    }

    public static <T extends View> T findView(@NonNull Dialog dialog, @IdRes int id) {
        return (T) dialog.findViewById(id);
    }

    public static void setViewVisible(@NonNull View... views) {
        for(View view : views) {
            view.setVisibility(View.VISIBLE);
        }
    }

    public static void setViewGone(@NonNull View... views) {
        for(View view : views) {
            view.setVisibility(View.GONE);
        }
    }

    public static void setViewParentVisibility(@NonNull View view, int visible) {
        View p = (View) view.getParent();
        p.setVisibility(visible);
    }

    public static void startActivity(@NonNull Context context, Class<? extends Activity> clazz) {
        context.startActivity(new Intent(context, clazz));
    }

    public static void finishActivity(@NonNull Activity activity, Class<? extends Activity> clazz) {
        activity.startActivity(new Intent(activity, clazz));
        activity.finish();
    }

    public static void startActivityForResult(@NonNull Activity context, Class<? extends Activity> clazz, int code) {
        context.startActivityForResult(new Intent(context, clazz), code);
    }

    public static void startActivityForResult(@NonNull Fragment fragment, Class<? extends Activity> clazz, int code) {
        fragment.startActivityForResult(new Intent(fragment.getContext(), clazz), code);
    }

    /**
     * 判断文本框里面的数据是否为空
     * @param view
     * @return
     */
    public static boolean isTextEmpty(@NonNull TextView view) {
        return view.getText().length() <= 0;
    }


    @IntDef({ModeColorFilter.none, ModeColorFilter.backgroud, ModeColorFilter.image_drawable})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ModeColorFilter {
        int none = 0;
        int backgroud = 1;
        int image_drawable = 2;
    }

    private static float[] BT_SELECTED = new float[]{
            1, 0, 0, 0, -50,
            0, 1, 0, 0, -50,
            0, 0, 1, 0, -50,
            0, 0, 0, 1, 0};
    private static ColorMatrixColorFilter cM_Selected = new ColorMatrixColorFilter(BT_SELECTED);

    public static void addClickColorFilter(View v) {
        addClickColorFilter(v, ModeColorFilter.backgroud);
    }

    public static void addClickColorFilter(View v, @ModeColorFilter int mode) {
            if(v == null) return;
            Drawable drawable = null;
            if(mode == ModeColorFilter.backgroud ) {
                drawable = v.getBackground();
            } else if(mode == ModeColorFilter.image_drawable && v instanceof ImageView) {
                drawable = ((ImageView)v).getDrawable();
            }

            View.OnTouchListener touchListener = getTouchListener(drawable, null, mode);
            if (touchListener == null) return;
            v.setOnTouchListener(touchListener);
    }

    public static View.OnTouchListener getTouchListener(
            final Drawable drawable, final ColorMatrixColorFilter cM, final @ModeColorFilter int mode) {
        if (drawable == null || mode == ModeColorFilter.none) {
            return null;
        }
        View.OnTouchListener mTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                drawable.setColorFilter(cM == null ? cM_Selected : cM);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        setDrawablet(v, mode);
                        break;
                    case MotionEvent.ACTION_UP:
                        drawable.clearColorFilter();
                        setDrawablet(v, mode);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        drawable.clearColorFilter();
                        v.setFocusable(false);
                        v.setFocusableInTouchMode(false);
                        setDrawablet(v, mode);
                        break;
                }
                return false;
            }

            private void setDrawablet(View v, @ModeColorFilter int mode) {
                if (mode == ModeColorFilter.backgroud) {
                    v.setBackgroundDrawable(drawable);
                } else if (mode == ModeColorFilter.image_drawable) {
                    ((ImageView) v).setImageDrawable(drawable);
                }
            }
        };
        return mTouchListener;
    }

    /**
     * 一个Activity界面上有多个Fragment需要切换显示，一个Fragment显示的时候，其他Fragment隐藏
     * @param activity
     * @param fragment
     * @param layoutId
     */
    public static synchronized void switchFragment(@NonNull FragmentActivity activity,@NonNull Class<? extends Fragment> fragment, @IdRes int layoutId) {
        String tag = fragment.getSimpleName();
        FragmentManager manager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        Fragment baseFragment = manager.findFragmentByTag(tag);
        List<Fragment> list = manager.getFragments();
        if(list != null) {
            for (Fragment f : list) {
                if (f == null) continue;
                if (f == baseFragment) {
                    transaction.show(f);
                } else {
                    transaction.hide(f);
                }
            }
        }
        if (baseFragment == null) {
            try {
                baseFragment = fragment.newInstance();
                transaction.add(layoutId, baseFragment, tag);
            } catch (InstantiationException e) {
                Logger.e(e);
            } catch (IllegalAccessException e) {
                Logger.e(e);
            }
        }
        transaction.commitAllowingStateLoss();
    }

    public static <T extends Fragment> T findFragment(@NonNull FragmentActivity activity,@NonNull Class<T> fragment) {
        String tag = fragment.getSimpleName();
        FragmentManager manager = activity.getSupportFragmentManager();
        Fragment baseFragment = manager.findFragmentByTag(tag);
        return (T)baseFragment;
    }
}
