package com.example.mysupermarket.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.mysupermarket.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VerificationActivity extends AppCompatActivity {

    @BindView(R.id.tv_phone_activity_verification)
    TextView tvPhoneActivityVerification;
    @BindView(R.id.ll_phone_activity_verification)
    LinearLayout llPhoneActivityVerification;
    @BindView(R.id.et_phone_activity_verification)
    EditText etPhoneActivityVerification;
    @BindView(R.id.tv_send_code_activity_verification)
    TextView tvSendCodeActivityVerification;
    @BindView(R.id.tv_seconds_activity_verification)
    TextView tvSecondsActivityVerification;
    @BindView(R.id.ll_send_code_activity_verification)
    LinearLayout llSendCodeActivityVerification;
    @BindView(R.id.btn_phone_activity_verification)
    Button btnPhoneActivityVerification;
    @BindView(R.id.btn_inactive_activity_verification)
    Button btnInactiveActivityVerification;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private String phoneNumber;
    private String verificationCode;
    private Integer seconds;
    Timer timer;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeStatBarColor(VerificationActivity.this);
        setContentView(R.layout.activity_verification);
        ButterKnife.bind(this);
        startFirebaseLogin();

        init();
    }


    void init() {
        db = FirebaseFirestore.getInstance();
        etPhoneActivityVerification.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String etHint = etPhoneActivityVerification.getHint().toString();
                if (etHint.equals(getResources().getString(R.string.enter_your_phone))) {
                    if (isCorrectPhone(s.toString())) {
                        btnPhoneActivityVerification.setVisibility(View.VISIBLE);
                        btnInactiveActivityVerification.setVisibility(View.GONE);
                    } else {
                        btnPhoneActivityVerification.setVisibility(View.GONE);
                        btnInactiveActivityVerification.setVisibility(View.VISIBLE);
                    }
                } else if (etHint.equals(getResources().getString(R.string.enter_your_code))) {
                    if (s.toString().length() == 6) {
                        btnPhoneActivityVerification.setVisibility(View.VISIBLE);
                        btnInactiveActivityVerification.setVisibility(View.GONE);
                    } else {
                        btnPhoneActivityVerification.setVisibility(View.GONE);
                        btnInactiveActivityVerification.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void startFirebaseLogin() {
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                etPhoneActivityVerification.setText(phoneAuthCredential.getSmsCode());
                //Toast.makeText(VerificationActivity.this, "verification completed: "+phoneAuthCredential, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(VerificationActivity.this, "verification failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                seconds = 60;
                final Handler handler = new Handler();
                final Runnable Update = () -> {
                    if (seconds == 0) {
                        tvSecondsActivityVerification.setVisibility(View.GONE);
                        tvSendCodeActivityVerification.setEnabled(true);
                        tvSendCodeActivityVerification.setTextColor(getResources().getColor(R.color.teal_200));
                        timer.cancel();
                    } else {
                        tvSecondsActivityVerification.setVisibility(View.VISIBLE);
                        tvSendCodeActivityVerification.setEnabled(false);
                        tvSendCodeActivityVerification.setTextColor(getResources().getColor(R.color.gray));
                        tvSecondsActivityVerification.setText(String.valueOf(seconds--).concat(" " + getString(R.string.seconds_left)));
                    }

                };
                timer = new Timer(); // This will create a new Thread
                timer.schedule(new TimerTask() { // task to be scheduled
                    @Override
                    public void run() {
                        handler.post(Update);
                    }
                }, 0, 1000);


                btnPhoneActivityVerification.setText(R.string.continue_verify);
                btnPhoneActivityVerification.setVisibility(View.GONE);
                btnInactiveActivityVerification.setText(R.string.continue_verify);
                btnInactiveActivityVerification.setVisibility(View.VISIBLE);

                tvPhoneActivityVerification.setText(phoneNumber);
                llPhoneActivityVerification.setVisibility(View.VISIBLE);
                llSendCodeActivityVerification.setVisibility(View.VISIBLE);

                etPhoneActivityVerification.setHint(R.string.enter_your_code);
                etPhoneActivityVerification.setText("");
                verificationCode = s;
                //Toast.makeText(VerificationActivity.this, "Code sent", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void sendCode(String phone) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                        .setPhoneNumber(phone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallback)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private void signInWithPhone(PhoneAuthCredential credential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        db.collection("Users").document(phoneNumber).get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (documentSnapshot.exists()) {
                                            Intent intent = new Intent(VerificationActivity.this, SplashActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        } else {
                                            Intent intent = new Intent(VerificationActivity.this, RegisterActivity.class);
                                            intent.putExtra("phone", phoneNumber);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getBaseContext(), "can't get data", Toast.LENGTH_SHORT).show();

                                    }
                                });

                    } else {
                        Toast.makeText(VerificationActivity.this, "Incorrect OTP", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    boolean isCorrectPhone(String phone) {
        boolean isCorrect = false;
        int len = phone.length();
        if (len == 11 && (phone.startsWith("011") || phone.startsWith("010") || phone.startsWith("012") || phone.startsWith("015"))) {
            isCorrect = true;
        }
        return isCorrect;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void changeStatBarColor(AppCompatActivity activity) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(activity, R.color.trans));
        //???????????????????
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        //???????????????????
    }



    @OnClick({R.id.tv_send_code_activity_verification, R.id.btn_phone_activity_verification})
    public void onClick(View view) {
        if (view.getId() == R.id.tv_send_code_activity_verification) {
            Toast.makeText(this, "send again", Toast.LENGTH_SHORT).show();
            sendCode(phoneNumber);//7639
        } else if (view.getId() == R.id.btn_phone_activity_verification) {
            String btnText = btnPhoneActivityVerification.getText().toString();
            if (btnText.equals(getResources().getString(R.string.send_code))) {
                phoneNumber = "+2".concat(etPhoneActivityVerification.getText().toString());
                sendCode(phoneNumber);//7639
            } else if (btnText.equals(getResources().getString(R.string.continue_verify))) {
                String otp = etPhoneActivityVerification.getText().toString();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otp);
                signInWithPhone(credential);
            }
        }

    }


}