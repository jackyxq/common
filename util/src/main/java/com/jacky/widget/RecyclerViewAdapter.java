package com.jacky.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by jacky on 2017-07-21.
 */

public abstract class RecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerViewHolder>
        implements AbsListView.OnItemClickListener {

    protected List<T> mData;

    public List<T> getData() {
        if(mData == null) return Collections.emptyList();
        return mData;
    }

    public void setData(Collection<T> data, boolean append) {
        if(append) appendData(data);
        else setData(data);
    }

    public void setData(T[] data, boolean append) {
        if(append) appendData(data);
        else setData(data);
    }

    public void setData(T... data) {
        mData = new ArrayList<>();
        if(data != null) {
            for(T t : data)  mData.add(t);
        }
        notifyDataSetChanged();
    }

    public void setData(Collection<T> data) {
        mData = new ArrayList<>();
        if(data != null) {
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void appendData(T... data) {
        if(data == null) return;
        if(mData == null) mData = new ArrayList<>();
        for(T t : data) {
            mData.add(t);
        }
        notifyDataSetChanged();
    }

    public void appendData(Collection<T> data) {
        if(data == null) return;
        if(mData == null) mData = new ArrayList<>();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void insertData(int position, T... data) {
        if(data == null) return;
        if(mData == null) mData = new ArrayList<>();
        for(T t : data) {
            mData.add(position++, t);
        }
        notifyDataSetChanged();
    }

    public void insertData(int position, Collection<T> data) {
        if(data == null) return;
        if(mData == null) mData = new ArrayList<>();
        mData.addAll(position, data);
        notifyDataSetChanged();
    }

    public void removeData(int position) {
        mData.remove(position);
        notifyDataSetChanged();
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public T getItem(int position) {
        return mData == null || mData.size() == 0 ? null : mData.get(position);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = onCreateView(viewGroup, viewType);
        return new RecyclerViewHolder(view).setOnItemClickListener(this);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder recyclerViewHolder, int position) {
        onBindViewHolder(recyclerViewHolder, getItem(position), position);
    }

    public abstract View onCreateView(ViewGroup group, int viewType);
    public abstract void onBindViewHolder(RecyclerViewHolder holder, T item, int position);

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {}
}
