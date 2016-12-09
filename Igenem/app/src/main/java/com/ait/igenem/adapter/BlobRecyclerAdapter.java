package com.ait.igenem.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ait.igenem.PassDataBlobInterface;
import com.ait.igenem.R;
import com.ait.igenem.model.Blob;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Erinna on 12/9/16.
 */

public class BlobRecyclerAdapter extends RecyclerView.Adapter<BlobRecyclerAdapter.ViewHolder>{

    private List<Blob> blobList;

    public BlobRecyclerAdapter(){
        blobList = new ArrayList<Blob>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.blob_row, parent, false);
        return new BlobRecyclerAdapter.ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //holder.key = dynamicBlobKeys.get(holder.getAdapterPosition());
        holder.etBlobName.setText(blobList.get(holder.getAdapterPosition()).getName());
        //does this work?
        holder.sbRadius.setProgress(blobList.get(holder.getAdapterPosition()).getRadius());
    }

    public void showBlob(){
        blobList.add(0, new Blob());
        notifyItemInserted(0);
    }

    @Override
    public int getItemCount() {
        return blobList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.etBlobName)
        EditText etBlobName;

        @BindView(R.id.cbProCon)
        CheckBox cbProCon;

        @BindView(R.id.sbRadius)
        SeekBar sbRadius;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
