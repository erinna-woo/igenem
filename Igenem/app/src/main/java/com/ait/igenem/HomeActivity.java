package com.ait.igenem;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.Space;
import android.widget.TextView;

import com.jawnnypoo.physicslayout.PhysicsRelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.physicsLayout)
    PhysicsRelativeLayout physicsLayout;

    @BindView(R.id.space)
    Space space;

    @BindView(R.id.tvHomeGreeting)
    TextView tvHomeGreeting;

    @BindView(R.id.btnProfile)
    Button btnProfile;

    @BindView(R.id.btnNewDecision)
    Button btnNewDecision;

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

        final Animation out = new AlphaAnimation(1.0f, 0.0f);
        out.setDuration(3000);

        out.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                physicsLayout.removeView(tvHomeGreeting);
                physicsLayout.removeView(space);
                physicsLayout.getPhysics().setGravityY(5);
                physicsLayout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        physicsLayout.getPhysics().setGravityY(0);
                        physicsLayout.getPhysics().giveRandomImpulse();
                        return false;
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        tvHomeGreeting.startAnimation(out);

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
