package com.example.mysupermarket.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mysupermarket.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.et_first_name_activity_register)
    EditText etFirstNameActivityRegister;
    @BindView(R.id.et_last_name_activity_register)
    EditText etLastNameActivityRegister;
    @BindView(R.id.et_mail_activity_register)
    EditText etMailActivityRegister;
    @BindView(R.id.tv_age_activity_register)
    EditText tvAgeActivityRegister;
    @BindView(R.id.et_invitation_code_activity_register)
    EditText etInvitationCodeActivityRegister;
    @BindView(R.id.ch_terms_activity_register)
    TextView chTermsActivityRegister;
    @BindView(R.id.btn_register_activity_register)
    Button btnRegisterActivityRegister;
    @BindView(R.id.btn_in_register_activity_register)
    Button btnInRegisterActivityRegister;

    int year = 0, month = 0, day = 0;
    String phoneNumber;

    DatePickerDialog.OnDateSetListener dateSetListener;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        init();
    }

    void init() {
        phoneNumber=getIntent().getStringExtra("phone");
        db = FirebaseFirestore.getInstance();
        dateSetListener = (view, year, month, dayOfMonth) -> {
            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec",};
            String date = dayOfMonth + " - " + months[month] + " - " + year;
            tvAgeActivityRegister.setText(date);
        };
        etFirstNameActivityRegister.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etLastNameActivityRegister.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etMailActivityRegister.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tvAgeActivityRegister.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @OnClick({R.id.btn_register_activity_register, R.id.tv_age_activity_register})
    public void onClick(View view) {
        if (view.getId() == R.id.btn_register_activity_register) {

            Map<String, Object> user = new HashMap<>();
            user.put("first name", etFirstNameActivityRegister.getText().toString());
            user.put("last name", etLastNameActivityRegister.getText().toString());
            user.put("email", etMailActivityRegister.getText().toString());
            user.put("birthdate", tvAgeActivityRegister.getText().toString());

            db.collection("Users")
                    .document(phoneNumber)
                    .set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Intent intent = new Intent(RegisterActivity.this, SplashActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getBaseContext(), "Sign Up Failed", Toast.LENGTH_SHORT).show();
                        }
                    });



        } else if (view.getId() == R.id.tv_age_activity_register) {
            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this, dateSetListener, year, month, day);
            //datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            datePickerDialog.show();
        }
    }

    void checkFields() {
        if (etFirstNameActivityRegister.getText().toString().length() != 0 && etLastNameActivityRegister.getText().toString().length() != 0 && etMailActivityRegister.getText().toString().length() != 0 && tvAgeActivityRegister.getText().toString().length() != 0) {
            btnInRegisterActivityRegister.setVisibility(View.GONE);
            btnRegisterActivityRegister.setVisibility(View.VISIBLE);

        } else {
            btnInRegisterActivityRegister.setVisibility(View.VISIBLE);
            btnRegisterActivityRegister.setVisibility(View.GONE);
        }
    }
}