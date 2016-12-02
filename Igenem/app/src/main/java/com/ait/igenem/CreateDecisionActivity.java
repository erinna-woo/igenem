package com.ait.igenem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.ait.igenem.model.Decision;
import com.ait.igenem.newDecisionFragments.BlobsFragment;
import com.ait.igenem.newDecisionFragments.InfoFragment;

public class CreateDecisionActivity extends AppCompatActivity implements PassDataDecisionInterface {

    public static final String KEY_DECISION = "KEY_DECISION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_decision);
        
        showFragmentByTag(InfoFragment.TAG);
    }

    @Override
    public void showDecisionActivity(Decision decision) {
        Intent showDecision = new Intent();
        showDecision.setClass(CreateDecisionActivity.this, DecisionActivity.class);
        showDecision.putExtra(KEY_DECISION, decision);
        startActivity(showDecision);
    }

    @Override
    public void showFragmentByTag(String tag) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            if (tag.equals(InfoFragment.TAG)) {
                fragment = new InfoFragment();
                Toast.makeText(CreateDecisionActivity.this, "showing InfoFragment", Toast.LENGTH_SHORT).show();
            }
            else if (tag.equals(BlobsFragment.TAG)) {
                fragment = new BlobsFragment();
                Toast.makeText(CreateDecisionActivity.this, "showing BlobsFragment", Toast.LENGTH_SHORT).show();
            }
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
        transaction.replace(R.id.layoutContainer, fragment, tag);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
