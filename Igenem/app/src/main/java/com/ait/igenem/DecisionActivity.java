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
import android.view.View.OnTouchListener;
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

public class DecisionActivity extends AppCompatActivity {

    @BindView(R.id.linearLayoutDecision)
    LinearLayout linearLayoutDecision;

    @BindView(R.id.btnGoHome)
    Button btnGoHome;
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

    //custom view
    @BindView(R.id.blobsLayout)
    RelativeLayout blobsLayout;

    private Decision decision;
    private String decisionKey;
    private String previousActivity;
    private List<Blob> dynamicBlobList;
    private List<String> dynamicBlobKeys;

    private float mouseX;
    private float mouseY;

    private int blobsLayoutWidth;
    private int blobsLayoutHeight;

    private boolean clicked;
    private boolean minusPressed = false;
    private boolean plusPressed = false;

    private MinusThread mt;
    private PlusThread pt;

    private Blob uneditedBlob;
    private int uneditedRadius;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decision);
        ButterKnife.bind(this);

        getExtras();

        dynamicBlobList = new ArrayList<Blob>();
        dynamicBlobKeys = new ArrayList<String>();
        clicked = false;

        setupDecisionUI();
        setupFirebaseListener();
        setFont();

        blobsLayout.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!clicked) {
                    clicked = true;
                    createBlobLayout.setVisibility(View.VISIBLE);
                    mouseX = motionEvent.getX();
                    mouseY = motionEvent.getY();
                }
                return false;
            }
        });
    }

    private void getExtras() {
        previousActivity = getIntent().getStringExtra(ProfileActivity.KEY_PREVIOUS);
        decision = (Decision) this.getIntent().getSerializableExtra(ProfileActivity.KEY_D);
        decisionKey = this.getIntent().getStringExtra(ProfileActivity.KEY_D_KEY);
    }

    private void setupDecisionUI() {
        tvDecisionName.setText(decision.getName());
        updatePercentPro();
        setupHomeButton();
        setupDeleteDecisionButton();
        setupOkayCreateBlobButton();
        setupCancelCreateBlobButton();
        setupProConListener();
    }

    private void updatePercentPro() {
        if (decision.getTotalScore() == 0) {
            tvPercentPro.setText("");
        } else {
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
        updateBackgroundColor();
    }

    private void updateBackgroundColor() {
        float[] hsv = new float[3];
        Color.colorToHSV(decision.getColor(), hsv);
        hsv[1] = hsv[1] * decision.getPercentPro();
        linearLayoutDecision.setBackgroundColor(Color.HSVToColor(hsv));
        blobsLayout.setBackgroundColor(Color.HSVToColor(hsv));
    }

    private void setupHomeButton() {
        btnGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToHome();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
            }
        });
    }

    private void goToHome() {
        Intent goHomeIntent = new Intent();
        goHomeIntent.setClass(DecisionActivity.this, HomeActivity.class);
        goHomeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(goHomeIntent);
    }


    private void setupDeleteListener(final String key, final Blob blob, final View blobView) {
        btnDeleteBlob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child(getString(R.string.decisions)).
                        child(decisionKey).child(getString(R.string.blobs)).child(key).removeValue();
                blobsLayout.removeView(blobView);
                updateDecisionScoreDeleteBlob(blob);
                updatePercentPro();
                editBlobLayout.setVisibility(View.GONE);
                clicked = false;
            }
        });
    }

    private void updateDecisionScoreDeleteBlob(Blob delBlob) {
        decision.updateDecisionScoreDeleteBlob(delBlob.getRadius(), delBlob.isPro());
        updatePercentPro();
        updateScoreFirebase();
    }

    private void setupOkayCreateBlobButton() {
        btnDOkNewBlob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etDBlobName.getText().toString().equals("")) {
                    etDBlobName.setError(getString(R.string.enterBlobName));
                } else {
                    addBlobFirebase();
                    updateDecisionScoreNewBlob();
                    updatePercentPro();
                    resetCreateBlobLayout();
                    clicked = false;
                }
            }
        });
    }

    private void addBlobFirebase() {
        String key = FirebaseDatabase.getInstance().getReference().child(getString(R.string.decisions)).
                child(decisionKey).child(getString(R.string.blobs)).push().getKey();
        Blob newBlob = new Blob(etDBlobName.getText().toString(),
                swDProCon.isChecked(), sbDRadius.getProgress());
        FirebaseDatabase.getInstance().getReference().child(getString(R.string.decisions)).
                child(decisionKey).child(getString(R.string.blobs)).child(key).setValue(newBlob);
    }

    private void updateDecisionScoreNewBlob() {
        tvPercentPro.setText(String.valueOf(decision.getPercentPro()));
        decision.updateScoreNewBlob(sbDRadius.getProgress(), swDProCon.isChecked());
        updatePercentPro();
        updateScoreFirebase();
    }

    private void resetCreateBlobLayout() {
        createBlobLayout.setVisibility(View.GONE);
        etDBlobName.setText("");
        sbDRadius.setProgress(0);
        swDProCon.setChecked(true);
    }

    private void setupCancelCreateBlobButton() {
        btnDCancelNewBlob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createBlobLayout.setVisibility(View.GONE);
                clicked = false;
            }
        });
    }

    private void setupProConListener() {
        swDProCon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    tvDProCon.setText(R.string.tv_pro);
                } else {
                    tvDProCon.setText(getString(R.string.tv_con));
                }
            }
        });
    }

    private void setupFirebaseListener() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child(getString(R.string.decisions)).child(decisionKey).child(getString(R.string.blobs)).orderByKey().
                addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Blob newBlob = dataSnapshot.getValue(Blob.class);
                        addBlob(newBlob, dataSnapshot.getKey());
                        addBlobToScreen(newBlob, dataSnapshot.getKey());
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

    public void addBlob(Blob newBlob, String key) {
        dynamicBlobList.add(0, newBlob);
        dynamicBlobKeys.add(0, key);
    }

    private void addBlobToScreen(final Blob blob, final String key) {
        final View blobView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.blob_view, null, false);
        final ImageView ivBlob = (ImageView) blobView.findViewById(R.id.ivBlob);
        TextView tvBlobName = (TextView) blobView.findViewById(R.id.tvBlobName);

        setupBlobView(ivBlob, tvBlobName, blob);
        updateBlobViewSize(blob, blobView);
        placeBlobRandomly(blobView);

        blobView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!clicked) {
                    clicked = true;
                    editBlobLayout.setVisibility(View.VISIBLE);
                    uneditedBlob = blob;
                    uneditedRadius = blob.getRadius();
                    setupListeners(key, blob, blobView);
                }
            }
        });
        // TODO: DRAGGING
        blobsLayout.addView(blobView);
    }

    private void setupBlobView(ImageView ivBlob, TextView tvBlobName, Blob currBlob) {
        if (currBlob.isPro()) {
            ivBlob.setImageResource(R.drawable.circle_white);
            tvBlobName.setTextColor(Color.BLACK);
        } else {
            ivBlob.setImageResource(R.drawable.circle_black);
            tvBlobName.setTextColor(Color.WHITE);
        }
        tvBlobName.setText(currBlob.getName());
    }


    private void updateBlobViewSize(Blob updating, View blobView) {
        ImageView ivBlob = (ImageView) blobView.findViewById(R.id.ivBlob);
        ViewGroup.LayoutParams layoutParams = ivBlob.getLayoutParams();
        layoutParams.width = updating.getRadius() * 5;
        layoutParams.height = updating.getRadius() * 5;
        ivBlob.setLayoutParams(layoutParams);
    }

    private void placeBlobRandomly(View blobView) {
        int xPos = 0;
        int yPos = 0;

        if (mouseX != -99 && mouseY != -99) {
            xPos = (int) mouseX;
            yPos = (int) mouseY;
        } else {
            xPos = (int) (Math.random() * 600);
            yPos = (int) (Math.random() * 800);
        }
        blobView.setX(xPos);
        blobView.setY(yPos);
        mouseX = -99;
        mouseY = -99;
    }

    private void setupListeners(String newKey, Blob newBlob, View blobView) {
        setupDeleteListener(newKey, newBlob, blobView);
        setupPlusListener(newBlob, blobView);
        setupMinusListener(newBlob, blobView);
        setupEditBlobListeners(newKey, newBlob, blobView);
    }

    private void setupDeleteDecisionButton() {
        btnDeleteDecision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child(getString(R.string.decisions)).
                        child(decisionKey).removeValue();
                goToProfile();
                finish();
            }
        });
    }

    private void setupPlusListener(final Blob blob, final View blobView) {
        btnPlus.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        plusPressed = true;
                        pt = new PlusThread(blob, blobView);
                        pt.start();
                        break;
                    case MotionEvent.ACTION_UP:
                        pt.interrupt();
                        plusPressed = false;
                }
                return false;
            }
        });
    }

    private void setupMinusListener(final Blob blob, final View blobView) {
        btnMinus.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        minusPressed = true;
                        mt = new MinusThread(blob, blobView);
                        mt.start();
                        break;
                    case MotionEvent.ACTION_UP:
                        mt.interrupt();
                        minusPressed = false;
                }
                return false;
            }

        });
    }

    private void setupEditBlobListeners(final String key, final Blob updating, final View blobView) {
        btnOkEditBlob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editBlobLayout.setVisibility(View.GONE);
                updateBlobFirebase(updating, key);
                updateScoreFirebase();
                clicked = false;
            }
        });
        btnCancelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editBlobLayout.setVisibility(View.GONE);
                updateDecisionScoreEditedBlob(updating.getRadius(), uneditedRadius, updating.isPro());
                updating.setRadius(uneditedRadius);
                updateBlob(updating, key);
                updateBlobViewSize(updating, blobView);
                clicked = false;
            }
        });
    }

    private void updateBlobFirebase(Blob updating, String key) {
        FirebaseDatabase.getInstance().getReference().child(getString(R.string.decisions)).
                child(decisionKey).child(getString(R.string.blobs)).child(key).setValue(updating);
    }

    private void updateScoreFirebase() {
        FirebaseDatabase.getInstance().getReference().child(getString(R.string.decisions)).
                child(decisionKey).child(getString(R.string.proscore)).
                setValue(decision.getProScore());
        FirebaseDatabase.getInstance().getReference().child(getString(R.string.decisions)).
                child(decisionKey).child(getString(R.string.totalscore)).
                setValue(decision.getTotalScore());
    }

    private void updateDecisionScoreEditedBlob(int oldRadius, int newRadius, boolean isPro) {
        decision.updateDecisionScoreEditBlob(oldRadius, newRadius, isPro);
        updatePercentPro();
    }

    public void updateBlob(Blob updateBlob, String key) {
        int index = dynamicBlobKeys.indexOf(key);
        if (index != -1) {
            dynamicBlobList.set(index, updateBlob);
        }
    }

    public void removeBlobByKey(String key) {
        int index = dynamicBlobKeys.indexOf(key);
        if (index != -1) {
            dynamicBlobList.remove(index);
            dynamicBlobKeys.remove(index);
        }
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getAssets(), "VarelaRound-Regular.ttf");
        btnGoHome.setTypeface(font);
        btnDeleteDecision.setTypeface(font);
        tvDecisionName.setTypeface(font);
        tvPercentPro.setTypeface(font);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (previousActivity.equals(getString(R.string.profileactivity))) {
            goToProfile();
        } else if (previousActivity.equals(getString(R.string.create_decision_activity))) {
            goToHome();
        }
    }

    private void goToProfile() {
        Intent goProfileIntent = new Intent();
        goProfileIntent.setClass(DecisionActivity.this, ProfileActivity.class);
        startActivity(goProfileIntent);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        getRelativeLayoutInfo();
    }

    private void getRelativeLayoutInfo() {
        blobsLayoutWidth = blobsLayout.getWidth();
        blobsLayoutHeight = blobsLayout.getHeight();
    }

    public void increaseRadius(Blob updating, View blobView) {
        updating.increaseRadius();
        decision.increase(updating.isPro());
        updatePercentPro();
        updateBlobViewSize(updating, blobView);
    }

    public void decreaseRadius(Blob updating, View blobView) {
        updating.decreaseRadius();
        decision.decrease(updating.isPro());
        updatePercentPro();
        updateBlobViewSize(updating, blobView);
    }

    private class MinusThread extends Thread {

        private Blob b;
        private View bv;

        public MinusThread(Blob b, View bv) {
            this.b = b;
            this.bv = bv;
        }

        public void run() {
            while (minusPressed) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (b.getRadius() > 1) {
                            decreaseRadius(b, bv);
                        }
                    }
                });
                try {
                    sleep(150);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class PlusThread extends Thread {

        private Blob b;
        private View bv;

        public PlusThread(Blob b, View bv) {
            this.b = b;
            this.bv = bv;
        }

        public void run() {
            while (plusPressed) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (b.getRadius() < 100) {
                            increaseRadius(b, bv);
                        }
                    }
                });
                try {
                    sleep(150);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}