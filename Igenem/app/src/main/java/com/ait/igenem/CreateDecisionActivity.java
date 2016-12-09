package com.ait.igenem;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ait.igenem.model.Decision;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateDecisionActivity extends AppCompatActivity {

    public static final String KEY_NEW_DECISION = "KEY_NEW_DECISION";
    public static final String KEY_DECISION_KEY = "KEY_DECISION_KEY";
    private Decision newDecision;

    @BindView(R.id.tvCreateDecisionTitle)
    TextView tvCreateDecisionTitle;

    @BindView(R.id.etNewDecisionName)
    EditText etNewDecisionName;

    @BindView(R.id.btnInfoActivityCancel)
    Button btnInfoActivityCancel;

    @BindView(R.id.btnInfoActivityNext)
    Button btnInfoActivityNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_decision);

        ButterKnife.bind(this);

        setFont();

        btnInfoActivityCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnInfoActivityNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etNewDecisionName.getText().toString().equals("")) {
                    etNewDecisionName.setError("Please enter a decision name");
                } else {
                    String name = etNewDecisionName.getText().toString();
                    String color = "#000000";   //this value will be inputted by user

                    Decision newDecision =
                            new Decision(name, color, getUserName(), getUserId());
                    String key = FirebaseDatabase.getInstance().getReference().child("decisions").push().getKey();
                    FirebaseDatabase.getInstance().getReference().child("decisions").
                            child(key).setValue(newDecision);
                    //addDecisionToFirebase(newDecision);

                    Intent addBlobs = new Intent();
                    addBlobs.setClass(CreateDecisionActivity.this, CreateDecisionBlobsActivity.class);
                    addBlobs.putExtra(KEY_NEW_DECISION, newDecision);
                    addBlobs.putExtra(KEY_DECISION_KEY, key);
                    startActivity(addBlobs);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                }
            }
        });
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getAssets(), "VarelaRound-Regular.ttf");

        tvCreateDecisionTitle.setTypeface(font);
        etNewDecisionName.setTypeface(font);
        btnInfoActivityCancel.setTypeface(font);
        btnInfoActivityNext.setTypeface(font);
    }

    public void addDecisionToFirebase(Decision decision) {
        String key = FirebaseDatabase.getInstance().getReference().child("decisions").push().getKey();
        FirebaseDatabase.getInstance().getReference().child("decisions").
                child(key).setValue(decision);
        newDecision = decision;
    }

    public String getUserName() {
        return FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    }

    public String getUserId() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}
