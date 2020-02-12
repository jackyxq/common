package com.jacky.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by lixinquan on 2018/8/1.
 */

public class SwipeItemLayout extends FrameLayout {

    private static final int MAX_OFFSET = 15;
    private View leftView, mainView, rightView;
    private int leftMaxOffset, rightMaxOffset;
    private boolean canSwipe;

    private View.OnClickListener listener;

    public SwipeItemLayout(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public SwipeItemLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SwipeItemLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        canSwipe = true;
    }

    public void setCanSwipe(boolean canSwipe) {
        this.canSwipe = canSwipe;
//        cancelView.setVisibility(canSwipe ? VISIBLE : GONE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        for(int i = 0, size = getChildCount();i < size;i++ ) {
            View view = getChildAt(i);
            LayoutParams layout = (LayoutParams) view.getLayoutParams();
            if(layout.gravity == LayoutParams.UNSPECIFIED_GRAVITY) {
                mainView = view;
                mainView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(listener != null) listener.onClick(SwipeItemLayout.this);
                    }
                }); //防止被盖住的左右按钮被点击
            }else if(layout.gravity == Gravity.LEFT) {
                leftView = view;
                leftMaxOffset = view.getMeasuredWidth();
                leftView.setClickable(false);
            } else if(layout.gravity == Gravity.RIGHT) {
                rightView = view;
                rightMaxOffset = view.getMeasuredWidth();
                rightView.setClickable(false);
            }
//            Logger.d(view.getMeasuredWidth(), layout.gravity,view.isClickable());
        }
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        listener = l;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //解决在复用的时候，可以点击的问题
        setViewClickable(leftView, false);
        setViewClickable(rightView, false);
    }

    private float downX, downY;
//    private long downTime;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(canSwipe) {
            int action = ev.getAction();
//            Logger.d("action", action);
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    downX = ev.getX();
                    downY = ev.getY();
//                    downTime = System.currentTimeMillis();
                    break;
                case MotionEvent.ACTION_CANCEL:
//                    reset();
                    break;
                case MotionEvent.ACTION_MOVE :
                case MotionEvent.ACTION_UP :
                    float mx = ev.getX();
                    int distance = (int) (mx - downX);
                    boolean change = false;
                    if(distance > MAX_OFFSET && leftMaxOffset > 0) { //右滑
                        change = true;
                        if(distance > leftMaxOffset) {
                            distance = leftMaxOffset;
                        } else if(action == MotionEvent.ACTION_UP) {
                            distance = distance > leftMaxOffset* 0.3f ? leftMaxOffset : 0;
                        }
                    } else if(-distance > MAX_OFFSET && rightMaxOffset > 0) { //左滑
                        change = true;
                        distance = 0 - distance; //负数变成正数
                        if(distance > rightMaxOffset) {
                            distance = rightMaxOffset;
                        } else if(action == MotionEvent.ACTION_UP) {
                            distance = distance > rightMaxOffset * 0.3f ? rightMaxOffset : 0;
                        }
                        distance = 0- distance; //正数变负数
                    }
                    if(action == MotionEvent.ACTION_MOVE) getParent().requestDisallowInterceptTouchEvent(change);
                    if(change) {
                        changeLayoutParams(distance);

                        setViewClickable(leftView, distance == leftMaxOffset);
                        setViewClickable(rightView, distance == 0-rightMaxOffset);

                        MotionEvent ev2 = ev.obtain(ev);
                        ev2.setAction(MotionEvent.ACTION_CANCEL); //防止子View有触摸效果及点击事件
                        super.dispatchTouchEvent(ev2);
                        return true;
                    }
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private void changeLayoutParams(int d) {
        LayoutParams p = (LayoutParams) mainView.getLayoutParams();
        p.leftMargin = d;
        p.rightMargin = -d;
        mainView.setLayoutParams(p);
    }

//    private boolean isClick(MotionEvent ev) {
//        if(System.currentTimeMillis() - downTime > ViewConfiguration.getTapTimeout()) return false;
//        if(Math.abs(downX - ev.getX()) < MAX_OFFSET && Math.abs(downY - ev.getY()) < MAX_OFFSET) return true;
//        return false;
//    }

    private boolean isViewTouch(View view, float x, float y) {
        if(view == null) return false;
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
//        Logger.i(rect, x,  y);
        if(rect.contains((int)x, (int)y)) return true;
        return false;
    }

    private void reset(float x, float y) {
        if(isViewTouch(leftView, x, y)) return;
        if(isViewTouch(rightView, x, y)) return;
        reset();
    }

    private void reset() {
        changeLayoutParams(0);

        setViewClickable(leftView, false);
        setViewClickable(rightView, false);
    }

    private void setViewClickable(View view, boolean click) {
        if(view == null) return;
        view.setClickable(click);
    }

    /**
     * 全部归位
     */
    public static void resetAllSlide(ViewGroup view, MotionEvent ev) {
        if(ev != null) {
            if (ev.getAction() != MotionEvent.ACTION_DOWN) {
                return;
            }
        }
        for(int i = 0, size = view.getChildCount();i < size;i++) {
            View child = view.getChildAt(i);
            if(!(child instanceof SwipeItemLayout)) continue;

            SwipeItemLayout slide = (SwipeItemLayout) child;
            if(ev == null) {
                slide.reset();
            } else {
                slide.reset(ev.getRawX(), ev.getRawY());
            }
        }
    }
}
