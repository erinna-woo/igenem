package com.ait.igenem;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
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

public class DecisionActivity extends AppCompatActivity {


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
    @BindView(R.id.btnDCancelNewBlob)
    Button btnDCancelNewBlob;
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
    RelativeLayout createBlobView;

    private Decision decision;
    private String decisionKey;
    private String previousActivity;
    private List<Blob> dynamicBlobList;
    private List<String> dynamicBlobKeys;

    private float mouseX;
    private float mouseY;

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

        createBlobView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Toast.makeText(DecisionActivity.this, "MAKE A NEW BLOB", Toast.LENGTH_LONG).show();
                createBlobLayout.setVisibility(View.VISIBLE);
                mouseX = motionEvent.getX();
                mouseY = motionEvent.getY();

                return false;
            }
        });
    }

    private void addBlobToScreen(final Blob blob) {
        final View blobView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.blob_view, null, false);
        final ImageView ivBlob = (ImageView) blobView.findViewById(R.id.ivBlob);
        TextView tvBlobName = (TextView) blobView.findViewById(R.id.tvBlobName);

        Blob currBlob = blob;
        if (currBlob.isPro()) {
            ivBlob.setImageResource(R.drawable.circle_white);
        } else {
            ivBlob.setImageResource(R.drawable.circle_black);
        }
        tvBlobName.setText(currBlob.getName());
        tvBlobName.setTextColor(Color.BLACK);


        // TODO: how terrible is this going to look on the phone because i'm not sure it's dp?
        ViewGroup.LayoutParams layoutParams = ivBlob.getLayoutParams();
        layoutParams.width = currBlob.getRadius() * 5;
        layoutParams.height = currBlob.getRadius() * 5;
        ivBlob.setLayoutParams(layoutParams);

        int xPos = 0;
        int yPos = 0;

        if (mouseX != -99 && mouseY != -99) {
            xPos = (int) mouseX;
            yPos = (int) mouseY;
        }
        else {
            // TODO: get values for width and height
            xPos = (int) (Math.random() * 200);
            yPos = (int) (Math.random() * 200);
        }

        blobView.setX(xPos);
        blobView.setY(yPos);

        mouseX = -99;
        mouseY = -99;

        blobView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DecisionActivity.this, "CLICKED A BLOB", Toast.LENGTH_LONG).show();
                //if this blob was just added, it should be at index 0 in both blob/key lists
                editBlobLayout.setVisibility(View.VISIBLE);
                setupDeleteListener(0);
                setupPlusListener(0, blobView);
                setupMinusListener(0, blobView);
                setupOkEditListener(0);
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

    private void setupDecisionUI() {
        tvDecisionName.setText(decision.getName());

        updatePercentPro();
        setupNewBlobButton();
        setupDeleteDecisionButton();
        setupOkayCreateBlobButton();
        setupCancelCreateBlobButton();
        setupEditBlobListeners();
        setupProConListener();
    }

    private void setupProConListener() {
        swDProCon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    //tv.setPro(true);
                    tvDProCon.setText(R.string.tv_pro);
                } else {
                    /// currBlob.setPro(false);
                    tvDProCon.setText(getString(R.string.tv_con));
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

    private void setupCancelCreateBlobButton() {
        btnDCancelNewBlob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createBlobLayout.setVisibility(View.GONE);
            }
        });
    }

    private void updateDecisionScoreNewBlob() {
        tvPercentPro.setText(String.valueOf(decision.getPercentPro()));
        decision.updateScoreNewBlob(sbDRadius.getProgress(), swDProCon.isChecked());
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

    private void updateBlobOnScreen(int position, View blobView) {
        String key = getBlobKey(position);
        Blob blob = getBlob(position);
        ImageView ivBlob = (ImageView) blobView.findViewById(R.id.ivBlob);
        ivBlob.setImageResource(R.drawable.circle_aqua);
        Log.i("LAYOUT_PARAMS", String.valueOf(blob.getRadius() * 5));
//        ViewGroup.LayoutParams layoutParams = ivBlob.getLayoutParams();
//        layoutParams.width = blob.getRadius()*5;
//        layoutParams.height = blob.getRadius()*5;
//        ivBlob.setLayoutParams(layoutParams);
    }


    private void updateDecisionScoreDeleteBlob(int positionToEdit) {
        Blob delBlob = getBlob(positionToEdit);
        decision.updateDecisionScoreDeleteBlob(delBlob.getRadius(), delBlob.isPro());
        tvPercentPro.setText(String.valueOf(decision.getPercentPro()));
        updateScoreFirebase();
    }

    private void setupMinusListener(final int positionToEdit, final View blobView) {
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decreaseRadius(positionToEdit, blobView);

            }
        });
    }

    private void setupPlusListener(final int positionToEdit, final View blobView) {
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increaseRadius(positionToEdit, blobView);
            }
        });
    }


    private void updateBlobFirebase(Blob updating, String key) {
        FirebaseDatabase.getInstance().getReference().child("decisions").
                child(decisionKey).child("blobs").child(key).setValue(updating);
    }

    public void increaseRadius(int positionToEdit, View blobView) {
        String key = getBlobKey(positionToEdit);
        Blob updating = getBlob(positionToEdit);
        updating.increaseRadius();
        decision.increase(updating.isPro());
        //this could be excessive and maybe combined into a field cry ugh
        updatePercentPro();
        updateBlob(updating, key);

        ImageView ivBlob = (ImageView) blobView.findViewById(R.id.ivBlob);

        ViewGroup.LayoutParams layoutParams = ivBlob.getLayoutParams();
        layoutParams.width = updating.getRadius() * 5;
        layoutParams.height = updating.getRadius() * 5;
        ivBlob.setLayoutParams(layoutParams);
    }

    public void decreaseRadius(int positionToEdit, View blobView) {
        String key = getBlobKey(positionToEdit);
        Blob updating = getBlob(positionToEdit);
        updating.decreaseRadius();
        decision.decrease(updating.isPro());
        //this could be excessive and maybe combined into a field cry ugh
        updatePercentPro();
        updateBlob(updating, key);

        ImageView ivBlob = (ImageView) blobView.findViewById(R.id.ivBlob);

        ViewGroup.LayoutParams layoutParams = ivBlob.getLayoutParams();
        layoutParams.width = updating.getRadius() * 5;
        layoutParams.height = updating.getRadius() * 5;
        ivBlob.setLayoutParams(layoutParams);
    }

}