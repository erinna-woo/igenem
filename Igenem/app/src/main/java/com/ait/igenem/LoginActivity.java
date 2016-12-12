package com.ait.igenem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ait.igenem.fragment.ResetPasswordDialogFragment;
import com.ait.igenem.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity
        implements ResetPasswordDialogFragment.PassDataResetPasswordDialogInterface {

    @BindView(R.id.tvLoginTitle)
    TextView tvLoginTitle;

    @BindView(R.id.etEmail)
    EditText etEmail;

    @BindView(R.id.etPassword)
    EditText etPassword;

    @BindView(R.id.btnLogin)
    Button btnLogin;

    @BindView(R.id.btnRegister)
    Button btnRegister;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setFont();
        setupPersistenceStorage();
        setupFirebase();
        checkLoggedIn();
    }

    private void setupFirebase() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.keepSynced(true);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void setupPersistenceStorage() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean(getString(R.string.firstTime), false)) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(getString(R.string.firstTime), true);
            editor.commit();
        }
    }

    private void checkLoggedIn() {
        if (firebaseAuth.getCurrentUser() != null) {
            Intent goHomeIntent = new Intent();
            goHomeIntent.setClass(LoginActivity.this, HomeActivity.class);
            goHomeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(goHomeIntent);
        }
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getAssets(), "VarelaRound-Regular.ttf");
        tvLoginTitle.setTypeface(font);
        etEmail.setTypeface(font);
        etPassword.setTypeface(font);
        btnLogin.setTypeface(font);
        btnRegister.setTypeface(font);
    }

    @OnClick(R.id.btnRegister)
    void registerClick() {
        if (!isFormValid())
            return;

        showProgressDialog();

        firebaseAuth.createUserWithEmailAndPassword(etEmail.getText().toString(),
                etPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgressDialog();
                        if (task.isSuccessful()) {
                            loginUser(task);
                            Toast.makeText(LoginActivity.this, R.string.userCreated,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, getString(R.string.errorCreatingUser)
                                            + task.getException().getLocalizedMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void loginUser(@NonNull Task<AuthResult> task) {
        FirebaseUser fbUser = task.getResult().getUser();
        fbUser.updateProfile(new UserProfileChangeRequest.Builder().
                setDisplayName(usernameFromEmail(fbUser.getEmail())).build());

        User user = new User(usernameFromEmail(fbUser.getEmail()), fbUser.getEmail());
        databaseReference.child(getString(R.string.users)).child(fbUser.getUid()).setValue(user);
    }

    @OnClick(R.id.btnLogin)
    void loginClick() {
        if (!isFormValid()) {
            return;
        }

        showProgressDialog();

        firebaseAuth.signInWithEmailAndPassword(
                etEmail.getText().toString(),
                etPassword.getText().toString()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                hideProgressDialog();
                if (task.isSuccessful()) {
                    openHomeActivity();
                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        openResetPasswordDialogFragment();
                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this,
                                task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }

                }
            }
        });

    }

    private void openHomeActivity() {
        Intent openHome = new Intent();
        openHome.setClass(LoginActivity.this, HomeActivity.class);
        startActivity(openHome);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        finish();
    }

    private void openResetPasswordDialogFragment() {
        ResetPasswordDialogFragment resetPasswordDialogFragment = new ResetPasswordDialogFragment();
        resetPasswordDialogFragment.show(getSupportFragmentManager(), getString(R.string.reset_pw_dialog));
    }

    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }

    private boolean isFormValid() {
        if (TextUtils.isEmpty(etEmail.getText().toString())) {
            etEmail.setError(getString(R.string.emailReq));
            return false;
        }

        if (TextUtils.isEmpty(etPassword.getText().toString())) {
            etPassword.setError(getString(R.string.PWReq));
            return false;
        }

        return true;
    }

    @Override
    public void sendResetEmail() {
        firebaseAuth.sendPasswordResetEmail(etEmail.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(LoginActivity.this, R.string.tv_email_sent,
                                Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, getString(R.string.tv_email_send_failed)
                        + e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
