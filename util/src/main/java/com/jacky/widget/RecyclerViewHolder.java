package com.jacky.widget;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jacky.log.Logger;

import java.lang.ref.WeakReference;

/**
 * Created by jacky on 2016/10/30.
 */

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<WeakReference<View>> mViews;
    private long lastTime;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
    }

    public <T extends View> T getView(int id) {
        WeakReference<View> v = mViews.get(id);
        View tt = v == null ? null : v.get();
        if(tt == null) {
            tt = itemView.findViewById(id);
            mViews.put(id, new WeakReference<>(tt));
        }
        return (T) tt;
    }

    public RecyclerViewHolder setOnItemClickListener(@NonNull final AdapterView.OnItemClickListener listener) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long t = System.currentTimeMillis();
                if(t - lastTime > 1000) {
                    lastTime = t;
                    listener.onItemClick(null, v, getPosition(), v.getId());
                }
            }
        });
        return this;
    }

    public RecyclerViewHolder setOnItemLongClickListener(final AdapterView.OnItemLongClickListener listener) {
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onItemLongClick(null, v, getPosition(), v.getId());
                return true;
            }
        });
        return this;
    }

    public TextView getTextView(int id) {
        return getView(id);
    }

    public ImageView getImageView(int id) {
        return getView(id);
    }

    public Button getButton(int id) {
        return getView(id);
    }

    /**
     * 滚动到最后一条记录的时候，自动加载更多
     */
    public static abstract class OnLoadMoreListener extends RecyclerView.OnScrollListener {

        private LinearLayoutManager layoutManager;
        private int itemCount, lastPosition, lastItemCount;

        public abstract void onLoadMore();

        @Override
        public final void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                itemCount = layoutManager.getItemCount();
                lastPosition = layoutManager.findLastCompletelyVisibleItemPosition();
            } else {
                Logger.w("OnLoadMoreListener", "The OnLoadMoreListener only support LinearLayoutManager");
                return;
            }
            if(itemCount < 10) return; //小于10条 就不加载更多了
            if (lastItemCount != itemCount && lastPosition == itemCount - 1) {
                lastItemCount = itemCount;
                this.onLoadMore();
            }
        }
    }
}
