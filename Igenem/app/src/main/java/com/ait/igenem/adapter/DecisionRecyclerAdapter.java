package com.ait.igenem.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ait.igenem.PassDataDecisionInterface;
import com.ait.igenem.R;
import com.ait.igenem.model.Decision;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Erinna on 12/1/16.
 */

public class DecisionRecyclerAdapter extends RecyclerView.Adapter<DecisionRecyclerAdapter.ViewHolder>
        implements DecisionTouchHelperAdapter {

    private List<Decision> decisionList;
    private List<String> decisionKeys;
    private PassDataDecisionInterface passDataDecisionInterface;

    public DecisionRecyclerAdapter(PassDataDecisionInterface passDataDecisionInterface) {
        this.passDataDecisionInterface = passDataDecisionInterface;
        decisionList = new ArrayList<>();
        decisionKeys = new ArrayList<>();
    }

    public void addDecision(Decision newDecision, String key) {
        decisionList.add(0, newDecision);
        decisionKeys.add(0, key);
        notifyItemInserted(0);
    }

    public void clearDecisions() {
        decisionList.clear();
        decisionKeys.clear();
        notifyDataSetChanged();
    }

    public void removeDecisionByPos(int position) {
        decisionList.remove(position);
        decisionKeys.remove(position);
        notifyItemRemoved(position);
    }

    public void removeDecisionByKey(String key) {
        int index = decisionKeys.indexOf(key);
        if (index != -1) {
            decisionList.remove(index);
            decisionKeys.remove(index);
            notifyItemRemoved(index);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.decision_row, parent, false);
        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tvDecisionName.setText(decisionList.get(position).getName());
        holder.layoutDecision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passDataDecisionInterface.showDecisionActivity(
                        decisionList.get(position), decisionKeys.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return decisionList.size();
    }

    @Override
    public void onItemDismiss(int position) {
        FirebaseDatabase.getInstance().getReference().child("decisions").
                child(decisionKeys.get(position)).removeValue();
        removeDecisionByPos(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvDecisionName)
        TextView tvDecisionName;

        @BindView(R.id.layoutDecision)
        LinearLayout layoutDecision;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
