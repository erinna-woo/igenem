package com.ait.igenem;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jawnnypoo.physicslayout.PhysicsFrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.physicsLayout)
    PhysicsFrameLayout physicsLayout;

    @BindView(R.id.tvHomeGreeting)
    TextView tvHomeGreeting;

    @BindView(R.id.btnProfile)
    Button btnProfile;

    @BindView(R.id.btnNewDecision)
    Button btnNewDecision;

    @BindView(R.id.btnAnimate)
    Button btnAnimate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setFont();

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openProfile = new Intent();
                openProfile.setClass(HomeActivity.this, ProfileActivity.class);
                startActivity(openProfile);
                overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
            }
        });

        btnNewDecision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createDecision = new Intent();
                createDecision.setClass(HomeActivity.this, CreateDecisionActivity.class);
                startActivity(createDecision);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

        btnAnimate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                physicsLayout.getPhysics().giveRandomImpulse();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getAssets(), "VarelaRound-Regular.ttf");

        tvHomeGreeting.setTypeface(font);
    }

}
