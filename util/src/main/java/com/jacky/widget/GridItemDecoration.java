package com.jacky.widget;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * Created by Administrator on 2016/10/16.
 */
public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDivider;
    private int mSpaceHeight, mSpaceWidth;
    private int mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom;

    private int mShouldWidth, mRealWidth;

    public GridItemDecoration(int width, int height) {
        this(new ColorDrawable(), width, height);
    }

    public GridItemDecoration(Drawable drawable, int width, int height) {
        mDivider = drawable;
        this.mSpaceHeight = height;
        this.mSpaceWidth = width;
    }

    public void setPadding(int left,int top,int right, int bottom) {
        mPaddingLeft = left;
        mPaddingTop = top;
        mPaddingRight = right;
        mPaddingBottom = bottom;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int spanCount = getSpanCount(parent);
        drawHorizontal(c, parent, spanCount);
        drawVertical(c, parent, spanCount);
    }

    private int getSpanCount(RecyclerView parent) {
        // 列数
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        return spanCount;
    }

    private void drawHorizontal(Canvas c, RecyclerView parent, int spanCount) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
               final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            int left = child.getLeft() - params.leftMargin;
            if(isFirstColum(parent, i, spanCount, childCount)) {
                left -= mPaddingLeft;
            }
            int right = child.getRight() + params.rightMargin;
            if(isLastColum(parent, i, spanCount, childCount)) {
                right += mPaddingRight;
            } else {
                right += mSpaceWidth;
            }
            final int top = child.getBottom() + params.bottomMargin;
            int bottom;
            if(isLastRaw(parent, i, spanCount, childCount)) {
                bottom = top + mPaddingBottom;
            } else {
                bottom = top + mSpaceHeight;
            }
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);

            if(mPaddingTop > 0&& isFirstRaw(parent, i, spanCount, childCount)) {
                mDivider.setBounds(left, 0, right, child.getTop() - params.topMargin);
                mDivider.draw(c);
            }
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent, int spanCount) {
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);

            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getTop() - params.topMargin;
            final int bottom = child.getBottom() + params.bottomMargin;
            final int left = child.getRight() + params.rightMargin;
            int right = left;

            if(isLastColum(parent, i, spanCount, childCount)) {
                right += mPaddingRight;
            } else {
                right += mSpaceWidth;
            }

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);

            if(isFirstColum(parent, i, spanCount, childCount)) {
                mDivider.setBounds(0, top, child.getLeft() - params.leftMargin, bottom);
                mDivider.draw(c);
            }
        }
    }

    private boolean isLastColum(RecyclerView parent, int pos, int spanCount,
                                int childCount) {
//        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
//        if (layoutManager instanceof GridLayoutManager) {
            if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
            {
                return true;
            }
//        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
//            int orientation = ((StaggeredGridLayoutManager) layoutManager)
//                    .getOrientation();
//            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
//                if ((pos + 1) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
//                {
//                    return true;
//                }
//            } else {
//                childCount = childCount - childCount % spanCount;
//                if (pos >= childCount)// 如果是最后一列，则不需要绘制右边
//                    return true;
//            }
//        }
        return false;
    }

    private boolean isLastRaw(RecyclerView parent, int pos, int spanCount, int childCount) {
//        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
//        if (layoutManager instanceof GridLayoutManager) {
            if(childCount % spanCount == 0) {
                childCount = (childCount / spanCount - 1) * spanCount;
            } else {
                childCount = (childCount / spanCount) * spanCount;
            }
            return (pos >= childCount);
//        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
//            int orientation = ((StaggeredGridLayoutManager) layoutManager)
//                    .getOrientation();
//            // StaggeredGridLayoutManager 且纵向滚动
//            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
//                childCount = childCount - childCount % spanCount;
//                // 如果是最后一行，则不需要绘制底部
//                if (pos >= childCount)
//                    return true;
//            } else
//            // StaggeredGridLayoutManager 且横向滚动
//            {
//                // 如果是最后一行，则不需要绘制底部
//                if ((pos + 1) % spanCount == 0) {
//                    return true;
//                }
//            }
//        }
//        return false;
    }

    private boolean isFirstRaw(RecyclerView parent, int pos, int spanCount, int childCount) {
        if (pos < spanCount)// 如果是第一行
            return true;
        return false;
    }

    private boolean isFirstColum(RecyclerView parent, int pos, int spanCount, int childCount) {
        if (pos % spanCount == 0)// 如果是第一列
            return true;
        return false;
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        int spanCount = getSpanCount(parent);
        int childCount = parent.getAdapter().getItemCount();
        if(mShouldWidth == 0) {
            int w = parent.getWidth()- parent.getPaddingLeft() - parent.getPaddingRight();
            mShouldWidth = w / spanCount;
            mRealWidth = (w - mPaddingLeft - mPaddingRight - (spanCount - 1) * mSpaceWidth)/spanCount;
        }

        int left = 0,right = 0, top = 0, bottom = 0;
        if(isFirstRaw(parent, itemPosition, spanCount, childCount)) { //第一行
            top = mPaddingTop;
        }
        if(isLastRaw(parent, itemPosition, spanCount, childCount)) { //最后一行
            bottom = mPaddingBottom;
        } else {
            bottom = mSpaceHeight;
        }
        if(isFirstColum(parent, itemPosition, spanCount, childCount)) {  //第一列
            left = mPaddingLeft;
        } else {
            int p = (itemPosition % spanCount);
            /*
            实际的位置 减去 默认情况下的位置 就是差距
            mPaddingLeft + (mRealWidth + mSpaceWidth) * p  就是实际该VIEW所在的位置
            mShouldWidth * p 是在没有padding，没有间距的情况下默认的位置
             */
            left = (mRealWidth + mSpaceWidth - mShouldWidth) * p + mPaddingLeft;
        }
        if(isLastColum(parent, itemPosition, spanCount, childCount)) { //最后一列
            right = mPaddingRight;
        } else {
            int p = (itemPosition % spanCount) + 1;
            /*
            在默认的right 减去 实际的right， 就是他们之间的差距
            mShouldWidth * p 是在没有padding，没有间距的情况下默认的位置
            mPaddingLeft + mRealWidth * p + mSpaceWidth * (p - 1)  就是实际该VIEW所在的位置
             */
            right = p * (mShouldWidth -  mRealWidth - mSpaceWidth) + mSpaceWidth - mPaddingLeft;
        }
        outRect.set(left, top, right, bottom);
    }
}