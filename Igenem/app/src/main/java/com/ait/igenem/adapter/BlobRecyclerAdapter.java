package com.ait.igenem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;

import com.ait.igenem.CreateDecisionActivity;
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
    private Context context;
    private Blob currBlob;


    public BlobRecyclerAdapter(Context context){
        this.context = context;
        blobList = new ArrayList<Blob>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.blob_row, parent, false);

        return new BlobRecyclerAdapter.ViewHolder(rowView, new EditTextListener());
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //holder.key = dynamicBlobKeys.get(holder.getAdapterPosition());
        //does this work?
        holder.cbProCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currBlob = blobList.get(holder.getAdapterPosition());
                currBlob.setPro(!(currBlob.isPro()));
                notifyItemChanged(holder.getAdapterPosition());

            }
        });
        holder.editTextListener.updatePosition(holder.getAdapterPosition());
        holder.sbRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                blobList.get(holder.getAdapterPosition()).setRadius(i);
                //notifyItemChanged(holder.getAdapterPosition());

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        holder.btnDeleteblob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideBlob(holder.getAdapterPosition());
            }
        });
        holder.btnSaveBlob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveBlob(holder.getAdapterPosition(), holder.etBlobName.getText().toString(), holder.sbRadius.getProgress(), holder.cbProCon.isChecked());
            }
        });
    }

    public void hideBlob(int position){
        blobList.remove(position);
        notifyItemRemoved(position);
    }

    public List<Blob> getBlobList(){

        return blobList;
    }

    public void showBlob(){
        blobList.add(0, new Blob());
        notifyItemInserted(0);
    }

    @Override
    public int getItemCount() {
        return blobList.size();
    }

    public void saveBlob(int position, String name, int radius, boolean isPro) {
        blobList.set(position, new Blob(name, isPro, radius));
        notifyItemChanged(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.etBlobName)
        EditText etBlobName;

        @BindView(R.id.cbProCon)
        CheckBox cbProCon;

        @BindView(R.id.sbRadius)
        SeekBar sbRadius;

        @BindView(R.id.btnDeleteBlob)
        Button btnDeleteblob;

        @BindView(R.id.btnSaveBlob)
        Button btnSaveBlob;

        public EditTextListener editTextListener;

        public ViewHolder(View itemView, EditTextListener editTextListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.editTextListener = editTextListener;
            etBlobName.addTextChangedListener(editTextListener);
        }
    }

    private class EditTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position){
            this.position = position;
        }


        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            blobList.get(position).setName(charSequence.toString());
            //notifyItemChanged(position);

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

}
