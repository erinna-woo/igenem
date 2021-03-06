package com.ait.igenem.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by Erinna on 12/9/16.
 */

public class DecisionTouchHelperCallback extends ItemTouchHelper.Callback{

    private DecisionTouchHelperAdapter decisionTouchHelperAdapter;

    public DecisionTouchHelperCallback(DecisionTouchHelperAdapter decisionTouchHelperAdapter){
        this.decisionTouchHelperAdapter = decisionTouchHelperAdapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        decisionTouchHelperAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }
}
