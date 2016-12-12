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

import com.ait.igenem.adapter.BlobRecyclerAdapter;
import com.ait.igenem.adapter.BlobTouchHelperCallback;
import com.ait.igenem.model.Blob;
import com.ait.igenem.model.Decision;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateDecisionBlobsActivity extends AppCompatActivity {

    @BindView(R.id.btnBlobActivityBack)
    Button btnBlobActivityBack;

    @BindView(R.id.btnBlobActivitySave)
    Button btnBlobActivitySave;

    @BindView(R.id.btnNewBlob)
    Button btnNewBlob;

    //Setup RecyclerView
    @BindView(R.id.recyclerBlob)
    RecyclerView recyclerBlob;
    BlobRecyclerAdapter blobRecyclerAdapter;

    private Decision newDecision;
    private String dKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_decision_blobs);
        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        setFont();
        setupBackBtnListener();
        setupSaveBtnListener();
        setupNewBlobListener();
        setupRecyclerView();
    }

    private void setupNewBlobListener() {
        btnNewBlob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blobRecyclerAdapter.showBlob();
                recyclerBlob.scrollToPosition(0);
            }
        });
    }

    private void setupRecyclerView() {
        recyclerBlob.setHasFixedSize(true);
        final LinearLayoutManager mLayoutManager =
                new LinearLayoutManager(this);
        recyclerBlob.setLayoutManager(mLayoutManager);

        blobRecyclerAdapter = new BlobRecyclerAdapter(this);

        ItemTouchHelper.Callback callback = new BlobTouchHelperCallback(blobRecyclerAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerBlob);
        recyclerBlob.setItemViewCacheSize(100);
        recyclerBlob.setAdapter(blobRecyclerAdapter);
    }

    private void setupSaveBtnListener() {
        btnBlobActivitySave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewDecision();
                saveBlobsToFirebase(newDecision, dKey);
                updateDecisonScore(newDecision, dKey);
                showDecisionActivity(newDecision, dKey);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });
    }

    private void createNewDecision() {
        newDecision = (Decision) getIntent().getSerializableExtra(
                CreateDecisionActivity.KEY_NEW_DECISION);
        dKey = FirebaseDatabase.getInstance().getReference().child
                (getString(R.string.decisions)).push().getKey();

        FirebaseDatabase.getInstance().getReference().child(
                getString(R.string.decisions)).
                child(dKey).setValue(newDecision);
    }

    private void updateDecisonScore(Decision newDecision, String dKey) {
        FirebaseDatabase.getInstance().getReference().child
                (getString(R.string.decisions)).child(dKey).child
                (getString(R.string.proscore)).setValue(newDecision.getProScore());
        FirebaseDatabase.getInstance().getReference().child
                (getString(R.string.decisions)).child(dKey).child
                (getString(R.string.totalscore)).setValue(newDecision.getTotalScore());
    }

    private void saveBlobsToFirebase(Decision newDecision, String dKey) {
        List<Blob> saveBlobs = blobRecyclerAdapter.getBlobList();
        String blobKey;
        if (saveBlobs != null) {
            for (Blob b : saveBlobs) {
                if (b != null && !b.getName().equals("")) {
                    blobKey = FirebaseDatabase.getInstance().getReference().child
                            (getString(R.string.decisions)).child(dKey).child
                            (getString(R.string.blobs)).push().getKey();
                    FirebaseDatabase.getInstance().getReference().child
                            (getString(R.string.decisions)).child(dKey).child
                            (getString(R.string.blobs)).child(blobKey).setValue(b);
                    newDecision.updateScoreNewBlob(b.getRadius(), b.isPro());
                }
            }
        }
    }

    private void showDecisionActivity(Decision newDecision, String key) {
        Intent showDecision = new Intent();
        showDecision.setClass(CreateDecisionBlobsActivity.this, DecisionActivity.class);
        showDecision.putExtra(ProfileActivity.KEY_D, newDecision);
        showDecision.putExtra(ProfileActivity.KEY_D_KEY, key);
        showDecision.putExtra(ProfileActivity.KEY_PREVIOUS, getString(R.string.create_decision_activity));
        startActivity(showDecision);
    }

    private void setupBackBtnListener() {
        btnBlobActivityBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getAssets(), "VarelaRound-Regular.ttf");
        btnBlobActivityBack.setTypeface(font);
        btnBlobActivitySave.setTypeface(font);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

}