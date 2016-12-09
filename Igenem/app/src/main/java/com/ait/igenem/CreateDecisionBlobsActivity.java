package com.ait.igenem;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ait.igenem.model.Decision;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateDecisionBlobsActivity extends AppCompatActivity {

    @BindView(R.id.btnBlobActivityBack)
    Button btnBlobActivityBack;

    @BindView(R.id.btnBlobActivitySave)
    Button btnBlobActivitySave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_decision_blobs);

        ButterKnife.bind(this);

        setFont();
        setupBackBtnListener();
        setupSaveBtnListener();
    }

    private void setupSaveBtnListener() {
        btnBlobActivitySave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Decision newDecision = (Decision) getIntent().getSerializableExtra(
                        CreateDecisionActivity.KEY_NEW_DECISION);
                String key = getIntent().getStringExtra(CreateDecisionActivity.KEY_DECISION_KEY);

                showDecisionActivity(newDecision, key);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });
    }

    private void showDecisionActivity(Decision newDecision, String key) {
        Intent showDecision = new Intent();
        showDecision.setClass(CreateDecisionBlobsActivity.this, DecisionActivity.class);
        showDecision.putExtra(ProfileActivity.KEY_D, newDecision);
        showDecision.putExtra(ProfileActivity.KEY_D_KEY, key);
        showDecision.putExtra(ProfileActivity.KEY_PREVIOUS, "CreateDecision");
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