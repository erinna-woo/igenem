package com.ait.igenem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.ait.igenem.adapter.DecisionRecyclerAdapter;
import com.ait.igenem.model.Decision;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity implements PassDataDecisionInterface{

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

    @Override
    protected void onResume() {
        super.onResume();
        getDecisions();
    }

    @Override
    protected void onPause() {
        super.onPause();
        decisionRecyclerAdapter.clearDecisions();
    }

    private void getDecisions() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ref.child("decisions").orderByChild("ownerId").equalTo(userId).
                addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("DATA", "numChildren: "+dataSnapshot.getChildrenCount());
                for (DataSnapshot childSnap : dataSnapshot.getChildren()) {
                    Decision decision = childSnap.getValue(Decision.class);
                    decisionRecyclerAdapter.addDecision(decision);
                    Log.i("DATA", decision.getName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
        openDecisionActivity.putExtra(KEY_D, decision);
        startActivity(openDecisionActivity);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }
}
