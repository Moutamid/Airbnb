package com.moutamid.airbnb.activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.airbnb.MainActivity;
import com.moutamid.airbnb.R;
import com.moutamid.airbnb.constant.Constants;
import com.moutamid.airbnb.databinding.ActivityLoginBinding;
import com.moutamid.airbnb.models.UserModel;
import com.mukeshsolanki.OnOtpCompletionListener;
import com.mukeshsolanki.OtpView;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    private GoogleSignInClient mGoogleSignInClient;
    String verificationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.checkApp(this);
        Constants.initDialog(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        binding.google.setOnClickListener(v -> {
            googleSignIn();
        });

        binding.email.setOnClickListener(v -> {
            Stash.put(Constants.phone, false);
            startActivity(new Intent(this, SignupActivity.class));
            finish();
        });

        binding.login.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginEmailActivity.class));
            finish();
        });

        binding.continueBtn.setOnClickListener(v -> {
            if (binding.phone.getText().toString().isEmpty()){
                Toast.makeText(this, "Please add your phone number", Toast.LENGTH_SHORT).show();
                binding.phone.setError("Phone Number is required");
            } else {
                Constants.showDialog();
                String num = binding.cpp.getSelectedCountryCodeWithPlus() + binding.phone.getText().toString();
                Stash.put("num", num);
                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(Constants.auth())
                                .setPhoneNumber(num)       // Phone number to verify
                                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                .setActivity(this)                 // (optional) Activity for callback binding
//                                // If no activity is passed, reCAPTCHA verification can not be used.
                                .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                .build();
                Constants.auth().useAppLanguage();
                // Force reCAPTCHA flow
                // FirebaseAuth.getInstance().getFirebaseAuthSettings().forceRecaptchaFlowForTesting();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }

        });

    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // Turn off phone auth app verification.
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        Constants.auth().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = task.getResult().getUser();
                            Constants.dismissDialog();
                            Stash.put(Constants.phone, true);
                            // Update UI
                            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                            finish();
                        } else {
                            // Sign in failed, display a message and update the UI
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(LoginActivity.this, "The verification code entered was invalid", Toast.LENGTH_SHORT).show();
                            }
                            Constants.dismissDialog();
                        }
                    }
                });
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential c) {
            final String code = c.getSmsCode();
            if (code!=null) {
                verifyCode(code);
            }
        }
        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Constants.dismissDialog();
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
            } else if (e instanceof FirebaseAuthMissingActivityForRecaptchaException) {
                // reCAPTCHA verification attempted with null Activity
            }
            Toast.makeText(LoginActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            // Show a message and update the UI
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken token) {
            super.onCodeSent(s, token);
            verificationId = s;
            showVerifyDialog();
            Constants.dismissDialog();
        }
    };

    private void showVerifyDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.otp_layout);
        dialog.setCancelable(false);

        TextView number = dialog.findViewById(R.id.number);
        number.setText(Stash.getString("num"));

        OtpView otpView = dialog.findViewById(R.id.otp_view);
        otpView.requestFocus();
        final String[] ss = {""};

        otpView.setOtpCompletionListener(new OnOtpCompletionListener() {
            @Override
            public void onOtpCompleted(String otp) {
                ss[0] = otp;
            }
        });

        Button verify = dialog.findViewById(R.id.verify);
        verify.setOnClickListener(v -> {
            if (ss[0].isEmpty()){
                Toast.makeText(this, "Please Enter OTP", Toast.LENGTH_SHORT).show();
            } else {
                Constants.showDialog();
                verifyCode(ss[0]);
                dialog.dismiss();
            }
        });

        Button retry = dialog.findViewById(R.id.retry);
        retry.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.Dialog;
        dialog.getWindow().setGravity(Gravity.CENTER);
    }

    private void googleSignIn() {
        Constants.showDialog();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, Constants.RC_SIGN_IN);
    }

    private void updateUI(FirebaseUser user) {
        Constants.databaseReference().child(Constants.USER).child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    UserModel userDetails = new UserModel(
                            user.getUid(), user.getDisplayName(), "", user.getEmail(), "", "", user.getPhotoUrl().toString()
                    );

                    Constants.databaseReference().child(Constants.USER).child(user.getUid())
                            .setValue(userDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Constants.dismissDialog();
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        finish();
                                    } else {
                                        Constants.dismissDialog();
                                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Constants.dismissDialog();
                    finish();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Constants.dismissDialog();
                Toast.makeText(LoginActivity.this, "Database failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onCancelled: " + error.getMessage());
            }
        });
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        Constants.auth().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = Constants.auth().getCurrentUser();
                            updateUI(user);
                        } else {
                            Constants.dismissDialog();
                            Toast.makeText(LoginActivity.this, "Google sign in failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == Constants.RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("firebaseAuthWithGoogle", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Constants.dismissDialog();
                Toast.makeText(this, "Google sign in failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.w("firebaseAuthWithGoogle", "Google sign in failed", e);
            }
        }
    }

}