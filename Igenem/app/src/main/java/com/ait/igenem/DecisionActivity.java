package com.ait.igenem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.ait.igenem.adapter.BlobRecyclerAdapter;
import com.ait.igenem.model.Blob;
import com.ait.igenem.model.Decision;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DecisionActivity extends AppCompatActivity implements PassDataBlobInterface {

    @BindView(R.id.btnNewBlob)
    Button btnNewBlob;

    //Editing blob
    @BindView(R.id.editBlobLayout)
    LinearLayout editBlobLayout;
    @BindView(R.id.btnOkEditBlob)
    Button btnOkEditBlob;
    @BindView(R.id.btnCancelEdit)
    Button btnCancelEdit;

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

    //Setup RecyclerView
    @BindView(R.id.recyclerBlob)
    RecyclerView recyclerBlob;
    BlobRecyclerAdapter blobRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decision);
        ButterKnife.bind(this);

        //set the title as the decision name
        Decision decision = (Decision) this.getIntent().getSerializableExtra(ProfileActivity.KEY_D);
        this.setTitle(decision.getName());
        setupDecisionUI();
    }

    private void setupDecisionUI() {
        setupNewBlobButton();
        setupOkayCreateBlobButton();
        setupEditBlobListeners();
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        recyclerBlob.setHasFixedSize(true);
        final LinearLayoutManager mLayoutManager =
                new LinearLayoutManager(this);
        recyclerBlob.setLayoutManager(mLayoutManager);
        blobRecyclerAdapter = new BlobRecyclerAdapter();

        //callback, touchhelper?
        recyclerBlob.setAdapter(blobRecyclerAdapter);
    }

    private void setupEditBlobListeners() {
        btnOkEditBlob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: set diff values
                editBlobLayout.setVisibility(View.GONE);
            }
        });
        btnCancelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editBlobLayout.setVisibility(View.GONE);
            }
        });

    }

    private void setupOkayCreateBlobButton() {
        btnOkNewBlob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // do i need an onclick listener for the cb?
                Blob newBlob = new Blob(etBlobName.getText().toString(),
                        cbProCheck.isChecked(), Integer.parseInt(etBlobRadius.getText().toString()));
                blobRecyclerAdapter.addBlob(newBlob);
                resetCreateBlobLayout();
                recyclerBlob.scrollToPosition(0);
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

    @Override
    public void showEdit(Blob blobToEdit, int position) {
        editBlobLayout.setVisibility(View.VISIBLE);
        //TODO: not sure if we need the blob and position
    }
}
