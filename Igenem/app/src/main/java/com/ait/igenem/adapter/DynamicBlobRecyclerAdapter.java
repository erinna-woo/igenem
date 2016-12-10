package com.ait.igenem.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ait.igenem.PassDataDynamicBlobInterface;
import com.ait.igenem.model.Blob;
import com.ait.igenem.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Erinna on 11/25/16.
 */

public class DynamicBlobRecyclerAdapter extends RecyclerView.Adapter<DynamicBlobRecyclerAdapter.ViewHolder> {

    private List<Blob> dyamicBlobList;
    private List<String> dynamicBlobKeys;
    private Blob currBlob;
    private PassDataDynamicBlobInterface passDataDynamicBlobInterface;

    public DynamicBlobRecyclerAdapter(PassDataDynamicBlobInterface passDataDynamicBlobInterface) {

        // EACH BLOB SHOULD HAVE SAME INDEX IN dyamicBlobList and dynamicBlobKeys
        dyamicBlobList = new ArrayList<Blob>();
        dynamicBlobKeys = new ArrayList<String>();
        this.passDataDynamicBlobInterface = passDataDynamicBlobInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.dynamic_blob_row, parent, false);
        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.key = dynamicBlobKeys.get(holder.getAdapterPosition());
        holder.tvDBlobName.setText("Name: " + dyamicBlobList.get(holder.getAdapterPosition()).getName());
        if (dyamicBlobList.get(position).isPro()) {
            holder.tvDBlobScore.setText("Score: " + dyamicBlobList.get(holder.getAdapterPosition()).getRadius());
            holder.tvDProCon.setText("PRO");
            holder.layoutDBlobRow.setBackgroundColor(Color.rgb(173, 244, 203));
        } else {
            holder.tvDBlobScore.setText("Score: " + dyamicBlobList.get(holder.getAdapterPosition()).getRadius());
            holder.tvDProCon.setText("CON");
            holder.layoutDBlobRow.setBackgroundColor(Color.rgb(244, 173, 173));
        }
        setupEditBlobListener(holder, holder.key);
    }

    private void setupEditBlobListener(final ViewHolder holder, final String key) {
        holder.btnEditBlob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = dynamicBlobKeys.indexOf(key);
                if (index != -1) {
                    passDataDynamicBlobInterface.showEdit(dyamicBlobList.get(index), index);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dyamicBlobList.size();
    }

    public void addBlob(Blob newBlob, String key) {
        dyamicBlobList.add(0, newBlob);
        dynamicBlobKeys.add(0, key);
        notifyItemInserted(0);
    }

    public Blob getBlob(int position) {
        return dyamicBlobList.get(position);
    }

    public String getBlobKey(int position) {
        return dynamicBlobKeys.get(position);
    }

    public void updateBlob(Blob updateBlob, String key) {
        int index = dynamicBlobKeys.indexOf(key);
        if (index != -1) {
            dyamicBlobList.set(index, updateBlob);
            notifyItemChanged(index);
        }
    }

    public void removeBlobByPos(int position) {
        dyamicBlobList.remove(position);
        dynamicBlobKeys.remove(position);
        notifyItemRemoved(position);
    }

    public void removeBlobByKey(String key) {
        int index = dynamicBlobKeys.indexOf(key);
        if (index != -1) {
            dyamicBlobList.remove(index);
            dynamicBlobKeys.remove(index);
            notifyItemRemoved(index);
        }
    }

    public void clearBlobs() {
        dyamicBlobList.clear();
        dynamicBlobKeys.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvDBlobName)
        TextView tvDBlobName;

        @BindView(R.id.tvDBlobScore)
        TextView tvDBlobScore;

        @BindView(R.id.tvDProCon)
        TextView tvDProCon;

        @BindView(R.id.btnDEditBlob)
        Button btnEditBlob;

        //it's emily's job to delete this:
        @BindView(R.id.layoutDBlobRow)
        LinearLayout layoutDBlobRow;

        String key;

        public ViewHolder(View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);
            key = "";
        }
    }
}
