package com.zero.ci.base.adapter.listener;

import android.view.View;

import com.zero.ci.base.adapter.BaseRVAdapter;


public abstract class OnItemLongClickListener extends SimpleClickListener {
    @Override
    public void onItemClick(BaseRVAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemLongClick(BaseRVAdapter adapter, View view, int position) {
        onSimpleItemLongClick(adapter, view, position);
    }

    @Override
    public void onItemChildClick(BaseRVAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildLongClick(BaseRVAdapter adapter, View view, int position) {
    }

    public abstract void onSimpleItemLongClick(BaseRVAdapter adapter, View view, int position);
}
