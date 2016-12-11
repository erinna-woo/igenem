package com.ait.igenem;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    //cstom view
    @BindView(R.id.createBlobView)
    RelativeLayout createBlobView;

    private LinearLayout.LayoutParams layoutParams;

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

        layoutParams = (LinearLayout.LayoutParams) createBlobView.getLayoutParams();

        updateBackgroundColor();

        setupDecisionUI();
        setupFirebaseListener();

        createBlobView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Toast.makeText(DecisionActivity.this, "MAKE A NEW BLOB", Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }

    private void addBlobToScreen(final Blob blob) {
        final View blobView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.blob_view, null, false);
        ImageView ivBlob = (ImageView) blobView.findViewById(R.id.ivBlob);
        TextView tvBlobName = (TextView) blobView.findViewById(R.id.tvBlobName);

        Blob currBlob = blob;
        if (currBlob.isPro()) {
            ivBlob.setImageResource(R.drawable.circle_white);
            tvBlobName.setTextColor(Color.BLACK);
        } else {
            ivBlob.setImageResource(R.drawable.circle_black);
            tvBlobName.setTextColor(Color.WHITE);
        }
        tvBlobName.setText(currBlob.getName());

        ivBlob.setScaleX((float) currBlob.getRadius() / 100);
        ivBlob.setScaleY((float) currBlob.getRadius() / 100);

        int xPos = (int) (Math.random()*700);
        int yPos = (int) (Math.random()*900);

        blobView.setX(xPos);
        blobView.setY(yPos);

        blobView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DecisionActivity.this, "CLICKED A BLOB", Toast.LENGTH_LONG).show();
            }
        });

//        blobView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                Log.i("DRAGGING", "longPress");
//
//                return false;
//            }
//        });

        createBlobView.addView(blobView);
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
                        //setBlobPos(newBlob);
                        //drawAllBlobs();
                        addBlobToScreen(newBlob);
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
        return (int) (Math.random() * (createBlobView.getHeight() - r))
                + r;
    }

    private int getBlobXPos(int r) {
        return (int) (Math.random() * (createBlobView.getWidth() - r))
                + r;
    }

    private void drawAllBlobs() {
        Canvas canvas = new Canvas();
        createBlobView.setTag(R.string.blob, dynamicBlobList);
        createBlobView.draw(canvas);
        createBlobView.invalidate();
    }

    private void setupDecisionUI() {
        tvDecisionName.setText(decision.getName());
        tvPercentPro.setText(String.valueOf(decision.getPercentPro()));
        setupNewBlobButton();
        setupDeleteDecisionButton();
        setupOkayCreateBlobButton();
        setupEditBlobListeners();
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
        btnOkNewBlob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etBlobName.getText().toString().equals("")) {
                    etBlobName.setError(getString(R.string.enterBlobName));
                }
                //TODO: delete later. won't be setting a radius.
                if (etBlobRadius.getText().toString().equals("") ||
                        Integer.valueOf(etBlobRadius.getText().toString()) > 100) {
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
                }
            }
        });
    }

    private void updateDecisionScoreNewBlob() {
        decision.updateScoreNewBlob(Integer.parseInt(etBlobRadius.getText().toString()), cbProCheck.isChecked());
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