package com.ait.igenem;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import android.view.View;
import android.widget.Button;
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

    public static final String KEY_D = "KEY_D";
    public static final String KEY_D_KEY = "KEY_D_KEY";
    public static final String KEY_PREVIOUS = "KEY_PREVIOUS";

    @BindView(R.id.recyclerDecision)
    RecyclerView recyclerDecision;

    @BindView(R.id.tvUsername)
    TextView tvUsername;

    @BindView(R.id.btnHome)
    Button btnHome;

    @BindView(R.id.btnLogout)
    Button btnLogout;

    DecisionRecyclerAdapter decisionRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        tvUsername.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        setupRecyclerView();
        setupHomeListener();
        setupLogoutListener();
        setFont();
    }

    private void setupHomeListener() {
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goHome = new Intent();
                goHome.setClass(ProfileActivity.this, HomeActivity.class);
                goHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(goHome);
                finish();
            }
        });
    }

    private void setupLogoutListener() {
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent gotoLogin = new Intent();
                gotoLogin.setClass(ProfileActivity.this, LoginActivity.class);
                gotoLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(gotoLogin);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });
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
        ref.child(getString(R.string.decisions)).orderByChild(getString(R.string.ownerId)).equalTo(userId).
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
        openDecisionActivity.putExtra(KEY_PREVIOUS, getString(R.string.profileactivity));
        startActivity(openDecisionActivity);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }
}
