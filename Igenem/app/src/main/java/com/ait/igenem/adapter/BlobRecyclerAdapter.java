package com.ait.igenem.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ait.igenem.CreateDecisionActivity;
import com.ait.igenem.DecisionActivity;
import com.ait.igenem.PassDataBlobInterface;
import com.ait.igenem.model.Blob;
import com.ait.igenem.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Erinna on 11/25/16.
 */

public class BlobRecyclerAdapter extends RecyclerView.Adapter<BlobRecyclerAdapter.ViewHolder> {

    private List<Blob> blobList;
    private List<String> blobKeys;
    private Blob currBlob;
    private PassDataBlobInterface passDataBlobInterface;


    public BlobRecyclerAdapter(PassDataBlobInterface passDataBlobInterface) {

        // EACH BLOB SHOULD HAVE SAME INDEX IN blobList and blobKeys
        blobList = new ArrayList<Blob>();
        blobKeys = new ArrayList<String>();
        this.passDataBlobInterface = passDataBlobInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.blob_row, parent, false);
        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.key = blobKeys.get(holder.getAdapterPosition());
        holder.tvBlobName.setText("Name: " + blobList.get(holder.getAdapterPosition()).getName());
        if (blobList.get(position).isPro()) {
            holder.tvBlobScore.setText("Score: " + blobList.get(holder.getAdapterPosition()).getRadius());
            holder.tvProCon.setText("PRO");
            holder.layoutBlobRow.setBackgroundColor(Color.rgb(173, 244, 203));
        } else {
            holder.tvBlobScore.setText("Score: " + blobList.get(holder.getAdapterPosition()).getRadius());
            holder.tvProCon.setText("CON");
            holder.layoutBlobRow.setBackgroundColor(Color.rgb(244, 173, 173));
        }
        setupEditBlobListener(holder, holder.key);
    }

    private void setupEditBlobListener(final ViewHolder holder, final String key) {
        holder.btnEditBlob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = blobKeys.indexOf(key);
                if (index != -1) {
                    passDataBlobInterface.showEdit(blobList.get(index), index);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return blobList.size();
    }

    public void addBlob(Blob newBlob, String key) {
        blobList.add(0, newBlob);
        blobKeys.add(0, key);
        notifyItemInserted(0);
    }

    public Blob getBlob(int position) {
        return blobList.get(position);
    }

    public String getBlobKey(int position) {
        return blobKeys.get(position);
    }

    public void updateBlob(Blob updateBlob, String key) {
        int index = blobKeys.indexOf(key);
        if (index != -1) {
            blobList.set(index, updateBlob);
            notifyItemChanged(index);
        }
    }

    public void removeBlobByPos(int position) {
        blobList.remove(position);
        blobKeys.remove(position);
        notifyItemRemoved(position);
    }

    public void removeBlobByKey(String key) {
        int index = blobKeys.indexOf(key);
        if (index != -1) {
            blobList.remove(index);
            blobKeys.remove(index);
            notifyItemRemoved(index);
        }
    }

    public void clearBlobs() {
        blobList.clear();
        blobKeys.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvBlobName)
        TextView tvBlobName;

        @BindView(R.id.tvBlobScore)
        TextView tvBlobScore;

        @BindView(R.id.tvProCon)
        TextView tvProCon;

        @BindView(R.id.btnEditBlob)
        Button btnEditBlob;

        //it's emily's job to delete this:
        @BindView(R.id.layoutBlobRow)
        LinearLayout layoutBlobRow;

        String key;

        public ViewHolder(View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);
            key = "";
        }
    }
}
