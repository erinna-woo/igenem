package com.ait.igenem;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ait.igenem.adapter.DecisionRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity{

    // user profile
    public static final String KEY_D = "KEY_D";

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
        decisionRecyclerAdapter = new DecisionRecyclerAdapter((CreateDecisionDataInterface)this);

        //call back? touch helper?
        recyclerDecision.setAdapter(decisionRecyclerAdapter);
    }

}
