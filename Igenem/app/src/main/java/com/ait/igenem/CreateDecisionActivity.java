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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class CreateDecisionActivity extends AppCompatActivity
        implements CreateDecisionDataInterface{

    private Decision newDecision;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_decision);
        
        showFragmentByTag(InfoFragment.TAG);
    }

    @Override
    public void addDecisionToFirebase(Decision decision) {
        String key = FirebaseDatabase.getInstance().getReference().child("decisions").push().getKey();
        FirebaseDatabase.getInstance().getReference().child("decisions").child(key).setValue(decision);
        newDecision = decision;
    }

    @Override
    public void showNewDecisionActivity(Decision decision) {
        Intent showDecision = new Intent();
        showDecision.setClass(CreateDecisionActivity.this, DecisionActivity.class);
        showDecision.putExtra(ProfileActivity.KEY_D, decision);
        startActivity(showDecision);
        finish();
    }

    @Override
    public Decision getNewDecision() {
        return newDecision;
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

    @Override
    public String getUserName() {
        return FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    }
}
