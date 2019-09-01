package com.zero.ci.base.adapter.listener;

import android.view.View;

import com.zero.ci.base.adapter.BaseRVAdapter;

/**
 * A convenience class to extend when you only want to OnItemChildLongClickListener for a subset
 * of all the SimpleClickListener. This implements all methods in the
 * {@link SimpleClickListener}
 **/
public abstract class OnItemChildLongClickListener extends SimpleClickListener {
    @Override
    public void onItemClick(BaseRVAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemLongClick(BaseRVAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildClick(BaseRVAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildLongClick(BaseRVAdapter adapter, View view, int position) {
        onSimpleItemChildLongClick(adapter, view, position);
    }

    public abstract void onSimpleItemChildLongClick(BaseRVAdapter adapter, View view, int position);
}
