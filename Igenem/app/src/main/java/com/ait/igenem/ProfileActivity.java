package com.ait.igenem;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.TextView;

import com.ait.igenem.adapter.DecisionRecyclerAdapter;
import com.ait.igenem.adapter.DecisionTouchHelperCallback;
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

    // User profile
    public static final String KEY_D = "KEY_D";
    public static final String KEY_D_KEY = "KEY_D_KEY";
    public static final String KEY_PREVIOUS = "KEY_PREVIOUS";

    @BindView(R.id.recyclerDecision)
    RecyclerView recyclerDecision;
    DecisionRecyclerAdapter decisionRecyclerAdapter;

    @BindView(R.id.tvUsername)
    TextView tvUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        setupRecyclerView();

        tvUsername.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getAssets(), "VarelaRound-Regular.ttf");

        tvUsername.setTypeface(font);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent goHome = new Intent();
        goHome.setClass(ProfileActivity.this, HomeActivity.class);
        goHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(goHome);
    }

    private void getDecisions() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ref.child("decisions").orderByChild("ownerId").equalTo(userId).
                addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childSnap : dataSnapshot.getChildren()) {
                    Decision decision = childSnap.getValue(Decision.class);
                    decisionRecyclerAdapter.addDecision(decision, childSnap.getKey());
                    recyclerDecision.scrollToPosition(0);
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

        ItemTouchHelper.Callback callback = new DecisionTouchHelperCallback(decisionRecyclerAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerDecision);
        recyclerDecision.setAdapter(decisionRecyclerAdapter);
    }

    @Override
    public void showDecisionActivity(Decision decision, String key) {
        Intent openDecisionActivity = new Intent();
        openDecisionActivity.setClass(ProfileActivity.this, DecisionActivity.class);
        openDecisionActivity.putExtra(KEY_D, decision);
        openDecisionActivity.putExtra(KEY_D_KEY, key);
        openDecisionActivity.putExtra(KEY_PREVIOUS, "ProfileActivity");
        startActivity(openDecisionActivity);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }
}
