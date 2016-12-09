package com.ait.igenem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ait.igenem.adapter.BlobRecyclerAdapter;
import com.ait.igenem.model.Blob;
import com.ait.igenem.model.Decision;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DecisionActivity extends AppCompatActivity implements PassDataBlobInterface {

    @BindView(R.id.btnNewBlob)
    Button btnNewBlob;

    //Editing blob
    @BindView(R.id.editBlobLayout)
    LinearLayout editBlobLayout;
    @BindView(R.id.btnOkEditBlob)
    Button btnOkEditBlob;
    @BindView(R.id.btnCancelEdit)
    Button btnCancelEdit;

    @BindView(R.id.btnPlusPro)
    Button btnPlusPro;
    @BindView(R.id.btnMinusCon)
    Button btnMinusCon;
    @BindView(R.id.btnDeleteBlob)
    Button btnDeleteBlob;

    //For creating a new blob
    @BindView(R.id.createBlobLayout)
    LinearLayout createBlobLayout;
    @BindView(R.id.btnOkNewBlob)
    Button btnOkNewBlob;
    @BindView(R.id.etBlobName)
    EditText etBlobName;
    @BindView(R.id.etBlobRadius)
    EditText etBlobRadius;
    @BindView(R.id.cbProCheckBox)
    CheckBox cbProCheck;

    //deleting decision
    @BindView(R.id.btnDeleteDecision)
    Button btnDeleteDecision;

    //Setup RecyclerView
    @BindView(R.id.recyclerBlob)
    RecyclerView recyclerBlob;
    BlobRecyclerAdapter blobRecyclerAdapter;

    private Decision decision;
    private String decisionKey;
    private String previousActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decision);
        ButterKnife.bind(this);

        previousActivity = getIntent().getStringExtra(ProfileActivity.KEY_PREVIOUS);

        //set the title as the decision name
        decision = (Decision) this.getIntent().getSerializableExtra(ProfileActivity.KEY_D);
        decisionKey = this.getIntent().getStringExtra(ProfileActivity.KEY_D_KEY);
        this.setTitle(decision.getName());
        setupDecisionUI();
        setupFirebaseListener();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (previousActivity.equals("ProfileActivity")) {
            Intent goProfileIntent = new Intent();
            goProfileIntent.setClass(DecisionActivity.this, ProfileActivity.class);
            startActivity(goProfileIntent);
        }
        else if (previousActivity.equals("CreateDecision")) {
            Intent goHomeIntent = new Intent();
            goHomeIntent.setClass(DecisionActivity.this, HomeActivity.class);
            //clear entire back stack so when you click back from home, exit
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
                blobRecyclerAdapter.addBlob(newBlob, dataSnapshot.getKey());
                recyclerBlob.scrollToPosition(0);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Blob changedBlob = dataSnapshot.getValue(Blob.class);
                blobRecyclerAdapter.updateBlob(changedBlob, dataSnapshot.getKey());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                blobRecyclerAdapter.removeBlobByKey(dataSnapshot.getKey());
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

                //go back to profile page
                Intent showProfileIntent = new Intent();
                showProfileIntent.setClass(DecisionActivity.this, ProfileActivity.class);
                startActivity(showProfileIntent);
                finish();
            }
        });
    }

    private void setupRecyclerView() {
        recyclerBlob.setHasFixedSize(true);
        final LinearLayoutManager mLayoutManager =
                new LinearLayoutManager(this);
        recyclerBlob.setLayoutManager(mLayoutManager);
        blobRecyclerAdapter = new BlobRecyclerAdapter((PassDataBlobInterface) this);

        //callback, touchhelper?
        recyclerBlob.setAdapter(blobRecyclerAdapter);
    }

    private void setupEditBlobListeners() {
        btnOkEditBlob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: set diff values
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
                // do i need an onclick listener for the cb?

                // add newBlob to Firebase
                String key = FirebaseDatabase.getInstance().getReference().child("decisions").
                        child(decisionKey).child("blobs").push().getKey();

                Blob newBlob = new Blob(etBlobName.getText().toString(),
                        cbProCheck.isChecked(), Integer.parseInt(etBlobRadius.getText().toString()));

                FirebaseDatabase.getInstance().getReference().child("decisions").
                        child(decisionKey).child("blobs").child(key).setValue(newBlob);

                resetCreateBlobLayout();
                recyclerBlob.scrollToPosition(0);
            }
        });
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
                createBlobLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void showEdit(Blob blobToEdit, int position) {
        editBlobLayout.setVisibility(View.VISIBLE);
        //TODO: not sure if we need the blob and position
        //idk if this really goes here but i'm too lazy to do it in a better way
//        Log.i("EDITING", String.valueOf(position));
        final int positionToEdit = position;
        btnPlusPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increaseRadius(positionToEdit);
            }
        });

        btnMinusCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decreaseRadius(positionToEdit);
            }
        });

        btnDeleteBlob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = blobRecyclerAdapter.getBlobKey(positionToEdit);
                FirebaseDatabase.getInstance().getReference().child("decisions").
                        child(decisionKey).child("blobs").child(key).removeValue();
                editBlobLayout.setVisibility(View.GONE);
            }
        });

        btnOkEditBlob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = blobRecyclerAdapter.getBlobKey(positionToEdit);
                Blob blob = blobRecyclerAdapter.getBlob(positionToEdit);
                updateBlobFirebase(blob, key);
                editBlobLayout.setVisibility(View.GONE);
            }
        });

    }

    private void updateBlobFirebase(Blob updating, String key) {
        FirebaseDatabase.getInstance().getReference().child("decisions").
                child(decisionKey).child("blobs").child(key).setValue(updating);
    }

    public void increaseRadius(int positionToEdit) {
        String key = blobRecyclerAdapter.getBlobKey(positionToEdit);
        Blob updating = blobRecyclerAdapter.getBlob(positionToEdit);
        updating.increaseRadius();
        blobRecyclerAdapter.updateBlob(updating, key);
    }

    public void decreaseRadius(int positionToEdit) {
        String key = blobRecyclerAdapter.getBlobKey(positionToEdit);
        Blob updating = blobRecyclerAdapter.getBlob(positionToEdit);
        updating.decreaseRadius();
        blobRecyclerAdapter.updateBlob(updating, key);
    }


}
