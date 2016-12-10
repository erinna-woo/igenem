package com.ait.igenem.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ait.igenem.R;
import butterknife.BindView;

/**
 * Created by Erinna on 12/10/16.
 */


public class ResetPasswordDialogFragment extends DialogFragment {

    public interface PassDataResetPasswordDialogInterface {
        public void sendResetEmail();
    }

    @BindView(R.id.tvForgotPw)
    TextView tvForgotPw;

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
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                new ContextThemeWrapper(getActivity(), R.style.ResetPasswordDialogFragment));
        alertDialogBuilder.setTitle(R.string.alert_dialog_title);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View rootView = inflater.inflate(R.layout.fragment_reset_password, null);
        alertDialogBuilder.setView(rootView);

        setupYesButton(alertDialogBuilder);
        setupNoButton(alertDialogBuilder);


        return alertDialogBuilder.create();
    }

    private void setupNoButton(AlertDialog.Builder alertDialogBuilder) {
        alertDialogBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
    }

    private void setupYesButton(AlertDialog.Builder alertDialogBuilder) {
        alertDialogBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                passDataResetPasswordDialogInterface.sendResetEmail();
                dialogInterface.dismiss();
            }
        });
    }
}


