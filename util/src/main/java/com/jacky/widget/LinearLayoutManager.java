package com.jacky.widget;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;

import com.jacky.log.Logger;

/**
 * Created by lixinquan on 2020/7/6.
 */
public class LinearLayoutManager extends androidx.recyclerview.widget.LinearLayoutManager {

    public LinearLayoutManager(Context context) {
        super(context);
    }

    public LinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public LinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            //todo... 这里捕获之前的数组越界问题
            super.onLayoutChildren( recycler, state );
        } catch (IndexOutOfBoundsException e) {
            Logger.e(e);
        }
    }
}
