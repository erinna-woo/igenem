package com.ait.igenem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ait.igenem.model.Decision;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateDecisionActivity extends AppCompatActivity {

    public static final String KEY_NEW_DECISION = "KEY_NEW_DECISION";
    private Decision newDecision;

    @BindView(R.id.etNewDecisionName)
    EditText etNewDecisionName;

    @BindView(R.id.btnInfoFragCancel)
    Button btnInfoFragCancel;

    @BindView(R.id.btnInfoFragNext)
    Button btnInfoFragNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_decision);

        ButterKnife.bind(this);

        btnInfoFragCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        btnInfoFragNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etNewDecisionName.getText().toString().equals("")) {
                    etNewDecisionName.setError("Please enter a decision name");
                } else {
                    String name = etNewDecisionName.getText().toString();
                    String color = "#000000";   //this value will be inputted by user

                    Decision newDecision =
                            new Decision(name, color, getUserName(), getUserId(), getKey());
                    addDecisionToFirebase(newDecision);

                    Intent addBlobs = new Intent();
                    addBlobs.setClass(CreateDecisionActivity.this, CreateDecisionBlobsActivity.class);
                    addBlobs.putExtra(KEY_NEW_DECISION, newDecision);
                    startActivity(addBlobs);
                }
            }
        });
    }

    public void addDecisionToFirebase(Decision decision) {
        FirebaseDatabase.getInstance().getReference().child("decisions").
                child(decision.getKey()).setValue(decision);
        newDecision = decision;
    }

    public String getUserName() {
        return FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    }

    public String getUserId() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public String getKey() {
        return FirebaseDatabase.getInstance().getReference().child("decisions").push().getKey();
    }
}
