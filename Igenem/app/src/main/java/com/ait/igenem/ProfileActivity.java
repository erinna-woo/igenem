package com.ait.igenem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {

    // user profile

    @BindView(R.id.btnTempToDecision)
    Button btnTempToDecision;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        //TEMPORARY. will be replaced with onclick listener for each decision in the recyclerview
        btnTempToDecision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openDecisionActivity = new Intent();
                openDecisionActivity.setClass(ProfileActivity.this, DecisionActivity.class);
                startActivity(openDecisionActivity);
            }
        });
    }
}
