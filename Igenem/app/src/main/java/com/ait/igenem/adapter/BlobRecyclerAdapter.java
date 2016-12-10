package com.ait.igenem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.ait.igenem.CreateDecisionActivity;
import com.ait.igenem.R;
import com.ait.igenem.model.Blob;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Erinna on 12/9/16.
 */

public class BlobRecyclerAdapter extends RecyclerView.Adapter<BlobRecyclerAdapter.ViewHolder> {

    private List<Blob> blobList;
    private Context context;
    private Blob currBlob;


    public BlobRecyclerAdapter(Context context) {
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
        holder.editTextListener.updatePosition(holder.getAdapterPosition());
        setupProListener(holder);
        setupRadiusListener(holder);
        setupDeleteListener(holder);
    }

    private void setupProListener(final ViewHolder holder) {
        holder.swProCon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                currBlob = blobList.get(holder.getAdapterPosition());
                if (b) {
                    currBlob.setPro(true);
                    holder.tvProCon.setText("PRO");
                }
                else{
                    currBlob.setPro(false);
                    holder.tvProCon.setText("CON");
                }
            }
        });
//        holder.swProCon.setOn(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                currBlob = blobList.get(holder.getAdapterPosition());
//                currBlob.setPro(!(currBlob.isPro()));
//                //notifyItemChanged(holder.getAdapterPosition());
//
//            }
//        });
    }

    private void setupRadiusListener(final ViewHolder holder) {
        holder.sbRadius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (seekBar.isShown() && b) {
                    blobList.get(holder.getAdapterPosition()).setRadius(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //notifyItemChanged(holder.getAdapterPosition());
            }
        });
    }

    private void setupDeleteListener(final ViewHolder holder) {
        holder.btnDeleteblob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideBlob(holder.getAdapterPosition());
            }
        });
    }

    public void hideBlob(int position) {
        blobList.remove(position);
        notifyItemRemoved(position);
    }

    public List<Blob> getBlobList() {

        return blobList;
    }

    public void showBlob() {
        blobList.add(0, new Blob("", false, 0));
        notifyItemInserted(0);
    }

    @Override
    public int getItemCount() {
        return blobList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.etBlobName)
        EditText etBlobName;

        @BindView(R.id.tvProCon)
        TextView tvProCon;

        @BindView(R.id.swProCon)
        Switch swProCon;

        @BindView(R.id.sbRadius)
        SeekBar sbRadius;

        @BindView(R.id.btnDeleteBlob)
        Button btnDeleteblob;


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

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            blobList.get(position).setName(charSequence.toString());
            //     notifyItemChanged(position);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

}
