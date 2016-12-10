//package com.ait.igenem.adapter;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.ait.igenem.PassDataDynamicBlobInterface;
//import com.ait.igenem.R;
//import com.ait.igenem.model.Blob;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//
///**
// * Created by Erinna on 11/25/16.
// */
//
//public class DynamicBlobRecyclerAdapter extends RecyclerView.Adapter<DynamicBlobRecyclerAdapter.ViewHolder> {
//
//    public Blob currBlob;
//    private PassDataDynamicBlobInterface passDataDynamicBlobInterface;
//    private Context context;
//
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        holder.key = dynamicBlobKeys.get(holder.getAdapterPosition());
//        holder.tvDBlobName.setText("Name: " + dyamicBlobList.get(holder.getAdapterPosition()).getName());
//        if (dyamicBlobList.get(position).isPro()) {
//            holder.tvDBlobScore.setText("Score: " + dyamicBlobList.get(holder.getAdapterPosition()).getRadius());
//            holder.tvDProCon.setText("PRO");
//        } else {
//            holder.tvDBlobScore.setText("Score: " + dyamicBlobList.get(holder.getAdapterPosition()).getRadius());
//            holder.tvDProCon.setText("CON");
//        }
//        setupEditBlobListener(holder, holder.key);
//    }
//
//    private void setupEditBlobListener(final ViewHolder holder, final String key) {
//        holder.btnEditBlob.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int index = dynamicBlobKeys.indexOf(key);
//                if (index != -1) {
//                    passDataDynamicBlobInterface.showEdit(dyamicBlobList.get(index), index);
//                }
//            }
//        });
//    }
//
//
//
//
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//
//        @BindView(R.id.tvDBlobName)
//        TextView tvDBlobName;
//
//        @BindView(R.id.tvDBlobScore)
//        TextView tvDBlobScore;
//
//        @BindView(R.id.tvDProCon)
//        TextView tvDProCon;
//
//        @BindView(R.id.btnDEditBlob)
//        Button btnEditBlob;
//
//        String key;
//
//        public ViewHolder(View itemView) {
//
//            super(itemView);
//            ButterKnife.bind(this, itemView);
//            key = "";
//        }
//    }
//}
