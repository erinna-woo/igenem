package com.ait.igenem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DecisionActivity extends AppCompatActivity {

    @BindView(R.id.btnNewBlob)
    Button btnNewBlob;

    @BindView(R.id.editBlobLayout)
    LinearLayout editBlobLayout;

    //Blob list
    @BindView(R.id.layoutBlobs)
    LinearLayout layoutBlobs;

    //For creating a new blob
    @BindView(R.id.createBlobLayout)
    LinearLayout createBlobLayout;
    @BindView(R.id.btnOkNewBlob)
    Button btnOkNewBlob;
    @BindView(R.id.etBlobName)
    EditText etBlobName;
    @BindView(R.id.etBlobRadius)
    EditText etBlobRadius;
    @BindView(R.id.cbProCheckBox)
    CheckBox cbProCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decision);
        ButterKnife.bind(this);

        //TODO: from button --> double click blob
        setupNewBlobButton();
        setupOkayCreateBlobButton();
    }


    private void setupOkayCreateBlobButton() {
        btnOkNewBlob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View blobRow = getLayoutInflater().inflate(
                        R.layout.blob_row, null, false);
                TextView tvBlobName = (TextView) blobRow.findViewById(R.id.tvBlobName);
                TextView tvBlobScore = (TextView) blobRow.findViewById(R.id.tvBlobScore);
                TextView tvProCon = (TextView) blobRow.findViewById(R.id.tvProCon);
                tvBlobName.setText(etBlobName.getText().toString());
                if(cbProCheck.isChecked()){
                    tvBlobScore.setText(etBlobRadius.getText().toString());
                    tvProCon.setText("PRO");
                }
                else{
                    tvBlobScore.setText("-" + etBlobRadius.getText().toString());
                    tvProCon.setText("CON");
                }
                //onclick listener?
                layoutBlobs.addView(blobRow,0);
                resetCreateBlobLayout();
            }
        });
    }

    private void resetCreateBlobLayout() {
        createBlobLayout.setVisibility(View.GONE);
        etBlobName.setText("");
        etBlobRadius.setText("");
        cbProCheck.setChecked(false);
    }

    private void setupNewBlobButton() {
        btnNewBlob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createBlobLayout.setVisibility(View.VISIBLE);
            }
        });
    }
    //TODO
}

/*

btnEditBlob.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editBlobLayout.setVisibility(View.VISIBLE);
                    }
                });
                //SET TEXT*/
