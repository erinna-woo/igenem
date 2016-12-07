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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Erinna on 12/1/16.
 */

public class DecisionRecyclerAdapter extends RecyclerView.Adapter<DecisionRecyclerAdapter.ViewHolder> {

    private List<Decision> decisionList;
    private PassDataDecisionInterface passDataDecisionInterface;

    public DecisionRecyclerAdapter(PassDataDecisionInterface passDataDecisionInterface) {
        this.passDataDecisionInterface = passDataDecisionInterface;
        decisionList = new ArrayList<Decision>();
        //temp decision
        decisionList.add(new Decision("Decision 1", "Blue", "annire.woo", "1"));
        decisionList.add(new Decision("Decision 2", "Blue", "annire.woo", "1"));
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
                passDataDecisionInterface.showDecisionActivity(decisionList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return decisionList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

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
