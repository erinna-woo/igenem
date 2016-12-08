package com.ait.igenem.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class BlobRecyclerAdapter extends RecyclerView.Adapter<BlobRecyclerAdapter.ViewHolder>{

    private List<Blob> blobList;
    private Blob currBlob;
    private PassDataBlobInterface passDataInterface;


    public BlobRecyclerAdapter(PassDataBlobInterface passDataInterface){
        blobList = new ArrayList<Blob>();
        this.passDataInterface = passDataInterface;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.blob_row, parent, false);
        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvBlobName.setText("Name: " + blobList.get(position).getName());
        if(blobList.get(position).isPro()){
            holder.tvBlobScore.setText("Score: " + blobList.get(position).getRadius());
            holder.tvProCon.setText("PRO");
            holder.layoutBlobRow.setBackgroundColor(Color.rgb(173, 244, 203));
        }
        else{
            holder.tvBlobScore.setText("Score: -"  + blobList.get(position).getRadius());
            holder.tvProCon.setText("CON");
            holder.layoutBlobRow.setBackgroundColor(Color.rgb(244, 173, 173));
        }
        setEditButtonListener(holder, position);
    }

    public void setEditButtonListener(ViewHolder holder, final int position){
        holder.btnEditBlob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passDataInterface.showEdit(blobList.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return blobList.size();
    }

    public void addBlob(Blob newBlob) {
        blobList.add(0, newBlob);
        //save in sugar ORM??
        notifyItemInserted(0);
        //scroll to position 0?
    }

    public Blob getBlob(int position) {
        return blobList.get(position);
    }

    public void updateBlob(Blob updateBlob) {
        int index = blobList.indexOf(updateBlob);
        if (index != -1) {
            blobList.set(index, updateBlob);
            notifyItemChanged(index);
        }
    }

    public void removeBlob(Blob removeBlob) {
        int index = blobList.indexOf(removeBlob);
        if (index != -1) {
            blobList.remove(index);
            notifyItemRemoved(index);
        }
    }

    public void clearBlobs() {
        blobList.clear();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

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

        public ViewHolder(View itemView) {

            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
