package com.ait.igenem.newDecisionFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ait.igenem.CreateDecisionDataInterface;
import com.ait.igenem.R;
import com.ait.igenem.model.Decision;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Emily on 12/2/16.
 */
public class InfoFragment extends Fragment {

    public static final String TAG = "InfoFragment";
    private CreateDecisionDataInterface createDecisionDataInterface = null;


    @BindView(R.id.etNewDecisionName)
    EditText etNewDecisionName;

    @BindView(R.id.btnInfoFragCancel)
    Button btnInfoFragCancel;

    @BindView(R.id.btnInfoFragNext)
    Button btnInfoFragNext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof CreateDecisionDataInterface) {
            createDecisionDataInterface = (CreateDecisionDataInterface) context;
        } else {
            throw new RuntimeException("this Activity is not implementing the " +
                    "CreateDecisionDataInterface");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_new_decision_info, null);
        ButterKnife.bind(this, root);

        btnInfoFragCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        btnInfoFragNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etNewDecisionName.getText().toString().equals("")) {
                    etNewDecisionName.setError("Please enter a decision name");
                }
                else {
                    String name = etNewDecisionName.getText().toString();
                    String color = "#000000";   //this value will be inputted by user

                    Decision newDecision = new Decision(name, color, createDecisionDataInterface.getUserName());
                    createDecisionDataInterface.addDecisionToFirebase(newDecision);

                    createDecisionDataInterface.showFragmentByTag(BlobsFragment.TAG);
                }
            }
        });

        return root;
    }
}
