package com.jacky.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jacky.util.AppUtil;
import com.jacky.util.R;

/**
 * Created by Administrator on 2017-09-15.
 */

public class LoopViewPager extends FrameLayout {

    public LoopViewPager(Context context) {
        super(context);
        initView(context, null);
    }

    public LoopViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public LoopViewPager(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private boolean mAutoScroll;
    private int mScrollTime;
    private ViewPager mViewPager;
    private LinearLayout mIndicators;
    private AutoScrollRunning mRunnable;
    private boolean isLoop;

    private void initView(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoopViewPager);
        boolean mShowDot = a.getBoolean(R.styleable.LoopViewPager_show_navgation_dot, true);
        mAutoScroll = a.getBoolean(R.styleable.LoopViewPager_autoscroll, false);
        mScrollTime = a.getInteger(R.styleable.LoopViewPager_scroll_time, 3000);
        int padding = a.getDimensionPixelSize(R.styleable.LoopViewPager_navgation_padding, 0);
        a.recycle();

        addView(mViewPager = new ViewPager(context){

        });
        LayoutParams p = new LayoutParams(-2, -2);
        p.bottomMargin = padding;
        p.gravity = Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM;
        addView(mIndicators = new LinearLayout(context), p);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                int size = mIndicators.getChildCount();
                int pos = isLoop ? (position - 1): position;

                if(pos >= size) pos = 0;
                else if(pos < 0) pos = size - 1;

                for(int i = 0;i < size;i++) {
                    mIndicators.getChildAt(i).setSelected(i == pos);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(state != ViewPager.SCROLL_STATE_IDLE) return;
                if(isLoop) {
                    //无限循环的情况下，需要将最后一个变换为1，将第一个变换为倒数第二个
                    int pos = mViewPager.getCurrentItem();
                    int size = mViewPager.getAdapter().getCount();
                    if(pos == 0) {
                        mViewPager.setCurrentItem(size - 2, false);
                    } else if(pos == size - 1) {
                        mViewPager.setCurrentItem(1, false);
                    }
                }
            }
        });
        if(!mShowDot) {
            mIndicators.setVisibility(GONE);
        }
    }

    public void setAdapter(PagerAdapter adapter) {
        mViewPager.setAdapter(adapter);
    }

//    public void addOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
//        mViewPager.addOnPageChangeListener(listener);
//    }

    public void notifyDataSetChanged() {
        mIndicators.removeAllViews();
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-2, -2);
        p.leftMargin = AppUtil.dip2px(getContext(), 5);

        PagerAdapter adapter = mViewPager.getAdapter();
        int size = adapter.getCount();
        if(adapter instanceof LoopPagerAdapter && size > 1) {
            isLoop = ((LoopPagerAdapter) adapter).isLoop;
        } else {
            isLoop = false;
        }
        int start = isLoop ? 1 : 0;
        int end = isLoop ? (size - 1): size;
        for(int i = start;i < end;i++ ) {
            ImageView view = new ImageView(getContext());
            view.setImageResource(R.drawable.loop_indiactor);
            if(i > start) view.setLayoutParams(p);
            view.setSelected(i == start);
            mIndicators.addView(view);
        }
        mViewPager.setCurrentItem(start, false);
    }

    @Override
    public void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if(mRunnable == null) return;
        if(visibility == View.VISIBLE) {
            mRunnable.start();
        } else {
            mRunnable.stop();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(mAutoScroll) {
            if(mRunnable == null) {
                mRunnable = new AutoScrollRunning();
            }
            mRunnable.start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(mRunnable != null) {
            mRunnable.stop();
        }
        mRunnable = null;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(mRunnable != null) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN : mRunnable.stop(); break;
                case MotionEvent.ACTION_UP : mRunnable.start(); break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private class AutoScrollRunning implements Runnable {

        volatile boolean running;
        public void start() {
            if(running) return;
            running = true;
            postDelayed(mRunnable, mScrollTime);
        }

        public void stop() {
            running = false;
            removeCallbacks(mRunnable);
        }

        @Override
        public void run() {
            if(running) {
                ledRun();
                postDelayed(mRunnable, mScrollTime);
            }
        }

        private void ledRun() {
            if(mViewPager.getAdapter() == null) return;
            int size = mViewPager.getAdapter().getCount();
            int index = mViewPager.getCurrentItem();
            if(index >= size) {
                index = 0;
            } else {
                index++;
            }
            mViewPager.setCurrentItem(index);
        }
    }

    private Drawable getDrawable() {
        StateListDrawable drawable = new StateListDrawable();

        ShapeDrawable drawable1 = new ShapeDrawable();
        drawable1.setShape(new OvalShape());
//        drawable1.set

        drawable.addState(new int []{android.R.attr.state_selected}, drawable1);
        return  drawable;
    }
}
