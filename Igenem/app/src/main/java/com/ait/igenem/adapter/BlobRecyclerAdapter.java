package com.ait.igenem.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ait.igenem.PassDataToDetailsActivityInterface;
import com.ait.igenem.model.Blob;
import com.ait.igenem.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Erinna on 11/25/16.
 */

public class BlobRecyclerAdapter extends RecyclerView.Adapter<BlobRecyclerAdapter.ViewHolder>{

    private List<Blob> blobList;
    private Blob currBlob;
    private PassDataToDetailsActivityInterface passDataToDetailsActivityInterface;


    public BlobRecyclerAdapter(){
        blobList = new ArrayList<Blob>();
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
        }
        else{
            holder.tvBlobScore.setText("Score: -"  + blobList.get(position).getRadius());
            holder.tvProCon.setText("CON");
        }
        setEditButtonListener(holder, position);
    }

    public void setEditButtonListener(ViewHolder holder, final int position){
        holder.btnEditBlob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passDataToDetailsActivityInterface.showEdit(blobList.get(position), position);
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
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvBlobName;
        private TextView tvBlobScore;
        private TextView tvProCon;
        private Button btnEditBlob;

        public ViewHolder(View itemView) {
            super(itemView);
            tvBlobName = (TextView) itemView.findViewById(R.id.tvBlobName);
            tvBlobScore = (TextView) itemView.findViewById(R.id.tvBlobScore);
            tvProCon = (TextView) itemView.findViewById(R.id.tvProCon);
            btnEditBlob = (Button) itemView.findViewById(R.id.btnEditBlob);

        }
    }
}
