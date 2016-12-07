package com.ait.igenem.newDecisionFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.ait.igenem.CreateDecisionDataInterface;
import com.ait.igenem.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Emily on 12/2/16.
 */
public class BlobsFragment extends Fragment {

    public static final String TAG = "BlobsFragment";
    private CreateDecisionDataInterface createDecisionDataInterface;

    @BindView(R.id.btnBlobFragBack)
    Button btnBlobFragBack;

    @BindView(R.id.btnBlobFragSave)
    Button btnBlobFragSave;

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
        View root = inflater.inflate(R.layout.fragment_new_decision_blobs, null);
        ButterKnife.bind(this, root);

        btnBlobFragBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDecisionDataInterface.showFragmentByTag(InfoFragment.TAG);
            }
        });

        btnBlobFragSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "show DecisionActivity for this newly " +
                        "created Decision", Toast.LENGTH_SHORT).show();
                //newDecision is actually created in previous fragment...
                createDecisionDataInterface.showNewDecisionActivity(createDecisionDataInterface.getNewDecision());
            }
        });

        return root;
    }
}
