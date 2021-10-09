package com.jacky.widget;

import androidx.viewpager.widget.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 无限循环PagerAdapter
 */

public abstract class LoopPagerAdapter<T> extends PagerAdapter {

    private List<T> mData;
    /*package*/boolean isLoop;

    public LoopPagerAdapter() {
        this(false);
    }

    public LoopPagerAdapter(boolean isLoop) {
        this.isLoop = isLoop;
    }

    private int mChildCount = 0;

    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        if ( mChildCount > 0) {
            mChildCount --;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

    public void setData(List<T> data) {
        mData = data;
        if(isLoop && data != null) {
            int size = data.size();
            if(size > 1) {
                mData = new ArrayList<>();
                mData.add(data.get(size - 1));
                mData.addAll(data);
                mData.add(data.get(0));
            }
        }
        notifyDataSetChanged();
    }

    public T getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    private int getOriCount() {
        if(mData == null) return 0;
        int size = mData.size();
        return size <= 1 ? size : (size - 2);
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = getItemView(container, position);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public abstract View getItemView(ViewGroup container, int position);
}
