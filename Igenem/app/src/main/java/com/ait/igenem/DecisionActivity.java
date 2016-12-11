package com.ait.igenem;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.ait.igenem.model.Blob;
import com.ait.igenem.model.Decision;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

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
    Button btnDOkNewBlob;
    @BindView(R.id.etDBlobName)
    EditText etDBlobName;
    @BindView(R.id.swDProCon)
    Switch swDProCon;
    @BindView(R.id.sbDRadius)
    SeekBar sbDRadius;
    @BindView(R.id.tvDProCon)
    TextView tvDProCon;

    //deleting decision
    @BindView(R.id.btnDeleteDecision)
    Button btnDeleteDecision;

    //cstom view
    @BindView(R.id.createBlobView)
    View createBlobView;

    private Decision decision;
    private String decisionKey;
    private String previousActivity;
    private List<Blob> dynamicBlobList;
    private List<String> dynamicBlobKeys;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decision);
        ButterKnife.bind(this);

        previousActivity = getIntent().getStringExtra(ProfileActivity.KEY_PREVIOUS);

        decision = (Decision) this.getIntent().getSerializableExtra(ProfileActivity.KEY_D);
        decisionKey = this.getIntent().getStringExtra(ProfileActivity.KEY_D_KEY);

        dynamicBlobList = new ArrayList<Blob>();
        dynamicBlobKeys = new ArrayList<String>();

        updateBackgroundColor();

        setupDecisionUI();
        setupFirebaseListener();
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getAssets(), "VarelaRound-Regular.ttf");

        btnNewBlob.setTypeface(font);
        btnDeleteDecision.setTypeface(font);
        tvDecisionName.setTypeface(font);
        tvPercentPro.setTypeface(font);
    }

    public void addBlob(Blob newBlob, String key) {
        dynamicBlobList.add(0, newBlob);
        dynamicBlobKeys.add(0, key);
    }

    public Blob getBlob(int position) {
        return dynamicBlobList.get(position);
    }

    public String getBlobKey(int position) {
        return dynamicBlobKeys.get(position);
    }


    public void updateBlob(Blob updateBlob, String key) {
        int index = dynamicBlobKeys.indexOf(key);
        if (index != -1) {
            dynamicBlobList.set(index, updateBlob);
        }
    }

    public void removeBlobByPos(int position) {
        dynamicBlobList.remove(position);
        dynamicBlobKeys.remove(position);
    }


    public void removeBlobByKey(String key) {
        int index = dynamicBlobKeys.indexOf(key);
        if (index != -1) {
            dynamicBlobList.remove(index);
            dynamicBlobKeys.remove(index);
        }
    }

    private void updateBackgroundColor() {
        int decisionColor = decision.getColor();
        Float percentColor = decision.getPercentPro();
        float[] hsv = new float[3];
        Color.colorToHSV(decisionColor, hsv);
        hsv[1] = hsv[1] * percentColor;
        linearLayoutDecision.setBackgroundColor(Color.HSVToColor(hsv));
        createBlobView.setBackgroundColor(Color.HSVToColor(hsv));
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
            goHomeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
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
                        addBlob(newBlob, dataSnapshot.getKey());
                        setBlobPos(newBlob);
                        drawAllBlobs();
                        Log.d("DOWEHAVEIT", "onChildAdded: " + newBlob.getName());
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Blob changedBlob = dataSnapshot.getValue(Blob.class);
                        updateBlob(changedBlob, dataSnapshot.getKey());
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        removeBlobByKey(dataSnapshot.getKey());
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void setBlobPos(Blob newBlob) {
        int posx = getBlobXPos(newBlob.getRadius());
        int posy = getBlobYPos(newBlob.getRadius());

        int diffx;
        int diffy;
        int sumr;

//        for(int i = 0; i < dynamicBlobList.size(); i++){
//            Blob currblob = dynamicBlobList.get(i);
//
//            diffx = Math.abs(currblob.getPosx() - posx);
//            diffy = Math.abs(currblob.getPosy() - posy);
//            sumr = currblob.getRadius() + newBlob.getRadius();
//
//            while(diffx <= sumr ){
//                posx = getBlobXPos(newBlob.getRadius());
//                diffx = Math.abs(currblob.getPosx() - posx);
//            }
//            //will exit when diffx > sumr
//            while(diffy <= sumr ){
//                posy = getBlobYPos(newBlob.getRadius());
//                diffy= Math.abs(currblob.getPosy() - posy);
//            }
//            //will exit when diffy > sumr
//        }

        newBlob.setPosx(posx);
        newBlob.setPosy(posy);


    }

    private int getBlobYPos(int r) {
        return (int)(Math.random()* (createBlobView.getHeight()-r))
                +r;
    }

    private int getBlobXPos(int r) {
        return (int)(Math.random()* (createBlobView.getWidth()-r))
                +r;
    }

    private void drawAllBlobs(){
        Canvas canvas = new Canvas();
        createBlobView.setTag(R.string.blob,dynamicBlobList);
        createBlobView.draw(canvas);
        createBlobView.invalidate();
    }

    private void setupDecisionUI() {
        tvDecisionName.setText(decision.getName());

        updatePercentPro();
        setupNewBlobButton();
        setupDeleteDecisionButton();
        setupOkayCreateBlobButton();
        setupEditBlobListeners();
        setupProConListener();
    }

    private void setupProConListener() {
        swDProCon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    //tv.setPro(true);
                    tvDProCon.setText("PRO");
                }
                else{
                   /// currBlob.setPro(false);
                    tvDProCon.setText("CON");
                }
            }
        });
    }

    private void updatePercentPro() {
        double percentDouble = decision.getPercentPro() * 100;
        int percentInt = (int) Math.round(percentDouble);
        String proOrCon;

        if (percentInt > 49) {
            proOrCon = getString(R.string.tv_percent_pro);
        } else {
            percentInt = 100 - percentInt;
            proOrCon = getString(R.string.tv_percent_con);
        }

        tvPercentPro.setText(Integer.toString(percentInt) + proOrCon);
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
        btnDOkNewBlob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etDBlobName.getText().toString().equals("")) {
                    etDBlobName.setError(getString(R.string.enterBlobName));
                }

                    // Add newBlob to Firebase, obtain key.
                    String key = FirebaseDatabase.getInstance().getReference().child("decisions").
                            child(decisionKey).child("blobs").push().getKey();

                    Blob newBlob = new Blob(etDBlobName.getText().toString(),
                            swDProCon.isChecked(), sbDRadius.getProgress());
                    FirebaseDatabase.getInstance().getReference().child("decisions").
                            child(decisionKey).child("blobs").child(key).setValue(newBlob);

                    updateDecisionScoreNewBlob();
                    updateBackgroundColor();

                    resetCreateBlobLayout();

            }
        });
    }

    private void updateDecisionScoreNewBlob() {
        decision.updateScoreNewBlob(sbDRadius.getProgress(),swDProCon.isChecked());
        updatePercentPro();
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
        etDBlobName.setText("");
        sbDRadius.setProgress(0);
        swDProCon.setChecked(false);
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
                String key = getBlobKey(positionToEdit);
                Blob blob = getBlob(positionToEdit);
                updateBlobFirebase(blob, key);
                updateScoreFirebase();
                updateBackgroundColor();
                editBlobLayout.setVisibility(View.GONE);
            }
        });
    }

    private void setupDeleteListener(final int positionToEdit) {
        btnDeleteBlob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = getBlobKey(positionToEdit);
                FirebaseDatabase.getInstance().getReference().child("decisions").
                        child(decisionKey).child("blobs").child(key).removeValue();
                updateDecisionScoreDeleteBlob(positionToEdit);
                updateBackgroundColor();
                editBlobLayout.setVisibility(View.GONE);
            }
        });
    }

    private void updateDecisionScoreDeleteBlob(int positionToEdit) {
        Blob delBlob = getBlob(positionToEdit);
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
        String key = getBlobKey(positionToEdit);
        Blob updating = getBlob(positionToEdit);
        updating.increaseRadius();
        decision.increase(updating.isPro());
        //this could be excessive and maybe combined into a field cry ugh
        tvPercentPro.setText(String.valueOf(decision.getPercentPro()));
        updateBlob(updating, key);
    }

    public void decreaseRadius(int positionToEdit) {
        String key = getBlobKey(positionToEdit);
        Blob updating = getBlob(positionToEdit);
        updating.decreaseRadius();
        decision.decrease(updating.isPro());
        //this could be excessive and maybe combined into a field cry ugh
        tvPercentPro.setText(String.valueOf(decision.getPercentPro()));
        updateBlob(updating, key);
    }

}