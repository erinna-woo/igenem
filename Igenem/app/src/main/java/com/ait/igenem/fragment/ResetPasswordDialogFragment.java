package com.ait.igenem.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ait.igenem.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Erinna on 12/10/16.
 */

public class ResetPasswordDialogFragment extends DialogFragment {

    public interface PassDataResetPasswordDialogInterface {
        public void sendResetEmail();
    }

    @BindView(R.id.tvForgotPw)
    TextView tvForgotPw;

    @BindView(R.id.btnNo)
    Button btnNo;

    @BindView(R.id.btnYes)
    Button btnYes;

    private PassDataResetPasswordDialogInterface passDataResetPasswordDialogInterface;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PassDataResetPasswordDialogInterface) {
            passDataResetPasswordDialogInterface = (PassDataResetPasswordDialogInterface) context;
        } else {
            throw new RuntimeException(
                    getString(R.string.exception_not_implementing));
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(),R.style.ResetPasswordDialogFragment)).create();
        alertDialog.setTitle(R.string.alert_dialog_title);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View rootView = inflater.inflate(R.layout.fragment_reset_password, null);
        alertDialog.setView(rootView);

        ButterKnife.bind(this, rootView);
        setupUI(alertDialog);

        return alertDialog;
    }

    private void setupUI(AlertDialog alertDialog) {
        setFont();
        setupYesButton(alertDialog);
        setupNoButton(alertDialog);
    }



    private void setupNoButton(final AlertDialog ad) {
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ad.dismiss();
            }
        });
    }

    private void setupYesButton(final AlertDialog ad) {
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passDataResetPasswordDialogInterface.sendResetEmail();
                ad.dismiss();
            }
        });
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "VarelaRound-Regular.ttf");
        tvForgotPw.setTypeface(font);
        btnNo.setTypeface(font);
        btnYes.setTypeface(font);
    }
}


