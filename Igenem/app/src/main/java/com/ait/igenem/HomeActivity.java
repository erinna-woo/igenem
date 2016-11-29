package com.ait.igenem;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    // Hello Peter, let's make a decision
    // profile button
    // new decision button
    //TODO: why is this a nav bar activity?

    @BindView(R.id.btnProfile)
    Button btnProfile;
    @BindView(R.id.btnNewDecision)
    Button btnNewDecision;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openProfile = new Intent();
                openProfile.setClass(HomeActivity.this, ProfileActivity.class);
                startActivity(openProfile);
            }
        });

        btnNewDecision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeActivity.this,
                        "This will lead to creating a new decision", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
