package com.jacky.widget;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Vibrator;
import androidx.annotation.RequiresPermission;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import java.util.Collections;

/**
 * Created by lixinquan on 2019/4/3.
 */
public class DragItemTouchCallback extends ItemTouchHelper.Callback {

    private RecyclerViewAdapter mAdapter;
    private boolean canDrag = true, canSwipe = false;
    private Vibrator vibrator;

    @RequiresPermission(Manifest.permission.VIBRATE)
    public DragItemTouchCallback(RecyclerView recyclerView, RecyclerViewAdapter adapter) {
        mAdapter = adapter;
        ItemTouchHelper helper = new ItemTouchHelper(this);
        helper.attachToRecyclerView(recyclerView);

        vibrator = (Vibrator) recyclerView.getContext().getSystemService(Context.VIBRATOR_SERVICE);
    }

    public DragItemTouchCallback setDrag(boolean drag) {
        canDrag = drag;
        return this;
    }

    public DragItemTouchCallback setSwipe(boolean swipe) {
        canSwipe = swipe;
        return this;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int dragFlags = 0;
        int swipeFlags = 0;
        if (layoutManager instanceof GridLayoutManager) {
            // 如果是Grid布局，则不能滑动，只能上下左右拖动
            dragFlags = !canDrag ? 0 :
                    (ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
            swipeFlags = 0;
        } else if (layoutManager instanceof LinearLayoutManager) {
            if (((LinearLayoutManager) layoutManager).getOrientation() == LinearLayoutManager.VERTICAL) {
                // 如果是纵向Linear布局，则能上下拖动，左右滑动
                dragFlags = canDrag ? (ItemTouchHelper.UP | ItemTouchHelper.DOWN) : 0;
                swipeFlags = canSwipe ? (ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) : 0;
            } else {
                // 如果是横向Linear布局，则能左右拖动，上下滑动
                swipeFlags = canSwipe ? (ItemTouchHelper.UP | ItemTouchHelper.DOWN) : 0;
                dragFlags = canDrag ? (ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) : 0;
            }
        }
        return makeMovementFlags(dragFlags, swipeFlags); //该方法指定可进行的操作
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        if(mAdapter.isEmpty()) return false;

        int i = viewHolder.getAdapterPosition();
        int j = target.getAdapterPosition();

        Collections.swap(mAdapter.getData(), i, j);
        mAdapter.notifyItemMoved(i, j);
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int pos = viewHolder.getAdapterPosition();
        mAdapter.getData().remove(pos);
        mAdapter.notifyItemRemoved(pos);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if(actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            vibrator.vibrate(50);
        }
        super.onSelectedChanged(viewHolder, actionState);
    }
}