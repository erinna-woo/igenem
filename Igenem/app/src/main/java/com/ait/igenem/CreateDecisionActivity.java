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
import com.flask.colorpicker.ColorPickerView;
import com.google.firebase.auth.FirebaseAuth;

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

    @BindView(R.id.btnCancel)
    Button btnCancel;

    @BindView(R.id.btnNext)
    Button btnNext;

    @BindView(R.id.color_picker_view)
    ColorPickerView colorPickerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_decision);

        ButterKnife.bind(this);
        setupUI();
    }

    private void setupUI() {
        setFont();
        setupCancelBtnListener();
        setupNextListener();
    }

    private void setupCancelBtnListener() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setupNextListener() {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etNewDecisionName.getText().toString().equals("")) {
                    etNewDecisionName.setError(getString(R.string.enterDecisionName));
                } else {
                    String name = etNewDecisionName.getText().toString();
                    int colorInt = colorPickerView.getSelectedColor();

                    //TODO: error if they don't select a color? White is default tho.
                    Decision newDecision =
                            new Decision(name, colorInt, getUserName(), getUserId());

                    openCreateDecisionBlobsActivity(newDecision);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                }
            }
        });
    }

    private void openCreateDecisionBlobsActivity(Decision newDecision) {
        Intent addBlobs = new Intent();
        addBlobs.setClass(CreateDecisionActivity.this, CreateDecisionBlobsActivity.class);
        addBlobs.putExtra(KEY_NEW_DECISION, newDecision);
        startActivity(addBlobs);
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getAssets(), "VarelaRound-Regular.ttf");

        tvCreateDecisionTitle.setTypeface(font);
        etNewDecisionName.setTypeface(font);
        btnCancel.setTypeface(font);
        btnNext.setTypeface(font);
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