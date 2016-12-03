package com.ait.igenem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ait.igenem.adapter.DecisionRecyclerAdapter;
import com.ait.igenem.model.Decision;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity implements PassDataDecisionInterface{

    // user profile
    public static final String KEY_D_NAME = "KEY_D_NAME";

    @BindView(R.id.recyclerDecision)
    RecyclerView recyclerDecision;
    DecisionRecyclerAdapter decisionRecyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        recyclerDecision.setHasFixedSize(true);
        final LinearLayoutManager mLayoutManager =
                new LinearLayoutManager(this);
        recyclerDecision.setLayoutManager(mLayoutManager);
        decisionRecyclerAdapter = new DecisionRecyclerAdapter((PassDataDecisionInterface)this);

        //call back? touch helper?
        recyclerDecision.setAdapter(decisionRecyclerAdapter);
    }

    @Override
    public void showDecisionActivity(Decision decision) {
        Intent openDecisionActivity = new Intent();
        openDecisionActivity.setClass(ProfileActivity.this, DecisionActivity.class);
        openDecisionActivity.putExtra(KEY_D_NAME, decision.getName());
        startActivity(openDecisionActivity);
    }
}
