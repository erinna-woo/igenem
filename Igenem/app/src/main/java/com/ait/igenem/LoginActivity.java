package com.ait.igenem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    //temporary to open the decision activity. delete later
    @BindView(R.id.btnOpenDecision)
    Button btnOpenDecision;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        btnOpenDecision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openDecision = new Intent();
                openDecision.setClass(LoginActivity.this, DecisionActivity.class);
                startActivity(openDecision);
            }
        });
    }
}
