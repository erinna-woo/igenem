package com.ait.igenem;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ait.igenem.adapter.DynamicBlobRecyclerAdapter;
import com.ait.igenem.model.Blob;
import com.ait.igenem.model.Decision;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
//TODO: if you don't hit OK and just click "edit" for another blob. will only be saved locally, not in firebase

public class DecisionActivity extends AppCompatActivity implements PassDataDynamicBlobInterface {

    @BindView(R.id.linearLayoutDecision)
    LinearLayout linearLayoutDecision;

    @BindView(R.id.btnNewBlob)
    Button btnNewBlob;
    @BindView(R.id.tvDecisionName)
    TextView tvDecisionName;
    @BindView(R.id.tvPercentPro)
    TextView tvPercentPro;

    //Editing blob
    @BindView(R.id.editBlobLayout)
    LinearLayout editBlobLayout;
    @BindView(R.id.btnOkEditBlob)
    Button btnOkEditBlob;
    @BindView(R.id.btnCancelEdit)
    Button btnCancelEdit;

    @BindView(R.id.btnPlus)
    Button btnPlus;
    @BindView(R.id.btnMinus)
    Button btnMinus;
    @BindView(R.id.btnDeleteBlob)
    Button btnDeleteBlob;

    //For creating a new blob
    @BindView(R.id.createBlobLayout)
    LinearLayout createBlobLayout;
    @BindView(R.id.btnDOkNewBlob)
    Button btnOkNewBlob;
    @BindView(R.id.etDBlobName)
    EditText etBlobName;
    @BindView(R.id.etDBlobRadius)
    EditText etBlobRadius;
    @BindView(R.id.cbDProCheckBox)
    CheckBox cbProCheck;

    //deleting decision
    @BindView(R.id.btnDeleteDecision)
    Button btnDeleteDecision;

    //Setup RecyclerView
    @BindView(R.id.recyclerDynamicBlob)
    RecyclerView recyclerDynamicBlob;
    DynamicBlobRecyclerAdapter dynamicBlobRecyclerAdapter;

    private Decision decision;
    private String decisionKey;
    private String previousActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decision);
        ButterKnife.bind(this);

        previousActivity = getIntent().getStringExtra(ProfileActivity.KEY_PREVIOUS);

        decision = (Decision) this.getIntent().getSerializableExtra(ProfileActivity.KEY_D);
        decisionKey = this.getIntent().getStringExtra(ProfileActivity.KEY_D_KEY);

        updateBackgroundColor();

        setupDecisionUI();
        setupFirebaseListener();

    }

    private void updateBackgroundColor() {
        int decisionColor = decision.getColor();
        Float percentColor = decision.getPercentPro();
        float[] hsv = new float[3];
        Color.colorToHSV(decisionColor, hsv);
        hsv[1] = hsv[1] * percentColor;
        linearLayoutDecision.setBackgroundColor(Color.HSVToColor(hsv));
        recyclerDynamicBlob.setBackgroundColor(Color.HSVToColor(hsv));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (previousActivity.equals("ProfileActivity")) {
            Intent goProfileIntent = new Intent();
            goProfileIntent.setClass(DecisionActivity.this, ProfileActivity.class);
            startActivity(goProfileIntent);
        } else if (previousActivity.equals("CreateDecision")) {
            Intent goHomeIntent = new Intent();
            goHomeIntent.setClass(DecisionActivity.this, HomeActivity.class);
            //Clear entire back stack so when you click back from HomeActivity, the app exits.
            goHomeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(goHomeIntent);
        }

    }

    private void setupFirebaseListener() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("decisions").child(decisionKey).child("blobs").orderByKey().
                addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Blob newBlob = dataSnapshot.getValue(Blob.class);
                        dynamicBlobRecyclerAdapter.addBlob(newBlob, dataSnapshot.getKey());
                        recyclerDynamicBlob.scrollToPosition(0);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Blob changedBlob = dataSnapshot.getValue(Blob.class);
                        dynamicBlobRecyclerAdapter.updateBlob(changedBlob, dataSnapshot.getKey());
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        dynamicBlobRecyclerAdapter.removeBlobByKey(dataSnapshot.getKey());
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void setupDecisionUI() {
        tvDecisionName.setText(decision.getName());
        tvPercentPro.setText(String.valueOf(decision.getPercentPro()));
        setupNewBlobButton();
        setupDeleteDecisionButton();
        setupOkayCreateBlobButton();
        setupEditBlobListeners();
        setupRecyclerView();
    }

    private void setupDeleteDecisionButton() {
        btnDeleteDecision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //delete from firebase
                FirebaseDatabase.getInstance().getReference().child("decisions").
                        child(decisionKey).removeValue();

                //Go back to profile page
                Intent showProfileIntent = new Intent();
                showProfileIntent.setClass(DecisionActivity.this, ProfileActivity.class);
                startActivity(showProfileIntent);
                finish();
            }
        });
    }

    private void setupRecyclerView() {
        recyclerDynamicBlob.setHasFixedSize(true);
        final LinearLayoutManager mLayoutManager =
                new LinearLayoutManager(this);
        recyclerDynamicBlob.setLayoutManager(mLayoutManager);
        dynamicBlobRecyclerAdapter = new DynamicBlobRecyclerAdapter((PassDataDynamicBlobInterface) this);

        //callback, touchhelper?
        recyclerDynamicBlob.setAdapter(dynamicBlobRecyclerAdapter);
    }

    private void setupEditBlobListeners() {
        btnOkEditBlob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editBlobLayout.setVisibility(View.GONE);
            }
        });
        btnCancelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editBlobLayout.setVisibility(View.GONE);
            }
        });

    }

    private void setupOkayCreateBlobButton() {
        btnOkNewBlob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etBlobName.getText().toString().equals("")) {
                    etBlobName.setError(getString(R.string.enterBlobName));
                }
                //TODO: delete later. won't be setting a radius.
                if (etBlobRadius.getText().toString().equals("")) {
                    etBlobRadius.setError(getString(R.string.enterBlobRadius));
                } else {
                    // Add newBlob to Firebase, obtain key.
                    String key = FirebaseDatabase.getInstance().getReference().child("decisions").
                            child(decisionKey).child("blobs").push().getKey();

                    Blob newBlob = new Blob(etBlobName.getText().toString(),
                            cbProCheck.isChecked(), Integer.parseInt(etBlobRadius.getText().toString()));
                    FirebaseDatabase.getInstance().getReference().child("decisions").
                            child(decisionKey).child("blobs").child(key).setValue(newBlob);

                    updateDecisionScoreNewBlob();
                    updateBackgroundColor();

                    resetCreateBlobLayout();
                    recyclerDynamicBlob.scrollToPosition(0);
                }
            }
        });
    }

    private void updateDecisionScoreNewBlob() {
        decision.updateScoreNewBlob(Integer.parseInt(etBlobRadius.getText().toString()),cbProCheck.isChecked());
        tvPercentPro.setText(String.valueOf(decision.getPercentPro()));
        updateScoreFirebase();
    }

    private void updateScoreFirebase() {
        FirebaseDatabase.getInstance().getReference().child("decisions").
                child(decisionKey).child("proScore").setValue(decision.getProScore());
        FirebaseDatabase.getInstance().getReference().child("decisions").
                child(decisionKey).child("totalScore").setValue(decision.getTotalScore());
    }

    private void resetCreateBlobLayout() {
        createBlobLayout.setVisibility(View.GONE);
        etBlobName.setText("");
        etBlobRadius.setText("");
        cbProCheck.setChecked(false);
    }

    private void setupNewBlobButton() {
        btnNewBlob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editBlobLayout.setVisibility(View.GONE);
                createBlobLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void showEdit(Blob blobToEdit, int position) {
        createBlobLayout.setVisibility(View.GONE);
        editBlobLayout.setVisibility(View.VISIBLE);
        //TODO: not sure if we need the blob and position

        final int positionToEdit = position;
        setupPlusListener(positionToEdit);
        setupMinusListener(positionToEdit);
        setupDeleteListener(positionToEdit);
        setupOkEditListener(positionToEdit);
    }

    private void setupOkEditListener(final int positionToEdit) {
        btnOkEditBlob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = dynamicBlobRecyclerAdapter.getBlobKey(positionToEdit);
                Blob blob = dynamicBlobRecyclerAdapter.getBlob(positionToEdit);
                updateBlobFirebase(blob, key);
                updateScoreFirebase();
                editBlobLayout.setVisibility(View.GONE);
            }
        });
    }

    private void setupDeleteListener(final int positionToEdit) {
        btnDeleteBlob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = dynamicBlobRecyclerAdapter.getBlobKey(positionToEdit);
                FirebaseDatabase.getInstance().getReference().child("decisions").
                        child(decisionKey).child("blobs").child(key).removeValue();
                updateDecisionScoreDeleteBlob(positionToEdit);
                editBlobLayout.setVisibility(View.GONE);
            }
        });
    }

    private void updateDecisionScoreDeleteBlob(int positionToEdit) {
        Blob delBlob = dynamicBlobRecyclerAdapter.getBlob(positionToEdit);
        decision.updateDecisionScoreDeleteBlob(delBlob.getRadius(), delBlob.isPro());
        tvPercentPro.setText(String.valueOf(decision.getPercentPro()));
        updateScoreFirebase();
    }

    private void setupMinusListener(final int positionToEdit) {
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decreaseRadius(positionToEdit);
            }
        });
    }

    private void setupPlusListener(final int positionToEdit) {
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increaseRadius(positionToEdit);
            }
        });
    }

    private void updateBlobFirebase(Blob updating, String key) {
        FirebaseDatabase.getInstance().getReference().child("decisions").
                child(decisionKey).child("blobs").child(key).setValue(updating);
    }

    public void increaseRadius(int positionToEdit) {
        String key = dynamicBlobRecyclerAdapter.getBlobKey(positionToEdit);
        Blob updating = dynamicBlobRecyclerAdapter.getBlob(positionToEdit);
        updating.increaseRadius();
        decision.increase(updating.isPro());
        //this could be excessive and maybe combined into a field cry ugh
        tvPercentPro.setText(String.valueOf(decision.getPercentPro()));
        dynamicBlobRecyclerAdapter.updateBlob(updating, key);
    }

    public void decreaseRadius(int positionToEdit) {
        String key = dynamicBlobRecyclerAdapter.getBlobKey(positionToEdit);
        Blob updating = dynamicBlobRecyclerAdapter.getBlob(positionToEdit);
        updating.decreaseRadius();
        decision.decrease(updating.isPro());
        //this could be excessive and maybe combined into a field cry ugh
        tvPercentPro.setText(String.valueOf(decision.getPercentPro()));
        dynamicBlobRecyclerAdapter.updateBlob(updating, key);
    }

}