package com.example.mysupermarket.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mysupermarket.Interfaces.UserView;
import com.example.mysupermarket.R;
import com.example.mysupermarket.activities.SplashActivity;
import com.example.mysupermarket.activities.VerificationActivity;
import com.example.mysupermarket.presenters.UserPresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class ProfileFragment extends Fragment implements View.OnClickListener, UserView {
    View view;
    Button btnFragmentLogin;
    TextView tvNameFragmentProfile;
    TextView tvEmailFragmentProfile;
    TextView tvBirthdateFragmentProfile;
    ImageView ivEditFragmentProfile;
    Button btnOfferFragmentProfile;
    Button btnAddressFragmentProfile;
    Button btnOrdersFragmentProfile;
    TextView tvLogoutFragmentProfile;
    UserPresenter userPresenter;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            view = inflater.inflate(R.layout.fragment_profile, container, false);
        } else {
            view = inflater.inflate(R.layout.fragment_login, container, false);
        }
        init();
        return view;
    }

    private void init() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {

            tvNameFragmentProfile = view.findViewById(R.id.tv_name_fragment_profile);
            tvEmailFragmentProfile = view.findViewById(R.id.tv_email_fragment_profile);
            tvBirthdateFragmentProfile = view.findViewById(R.id.tv_birthdate_fragment_profile);
            ivEditFragmentProfile = view.findViewById(R.id.iv_edit_fragment_profile);
            btnOfferFragmentProfile = view.findViewById(R.id.btn_offer_fragment_profile);
            btnOrdersFragmentProfile = view.findViewById(R.id.btn_orders_fragment_profile);
            btnAddressFragmentProfile = view.findViewById(R.id.btn_address_fragment_profile);
            tvLogoutFragmentProfile = view.findViewById(R.id.tv_logout_fragment_profile);

            ivEditFragmentProfile.setOnClickListener(this);
            btnOfferFragmentProfile.setOnClickListener(this);
            btnOrdersFragmentProfile.setOnClickListener(this);
            btnAddressFragmentProfile.setOnClickListener(this);
            tvLogoutFragmentProfile.setOnClickListener(this);


            userPresenter = new UserPresenter(this);

            userPresenter.getUserName();
            userPresenter.getUserEmail();
            userPresenter.getUserBirthDate();


        } else {
            btnFragmentLogin = view.findViewById(R.id.btn_fragment_login);
            btnFragmentLogin.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_fragment_login) {
            startActivity(new Intent(getActivity(), VerificationActivity.class));
        } else if (v.getId() == R.id.iv_edit_fragment_profile) {
            Toast.makeText(getContext(), "edit", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.btn_offer_fragment_profile) {
            Toast.makeText(getContext(), "offers", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.btn_orders_fragment_profile) {
            Toast.makeText(getContext(), "orders", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.btn_address_fragment_profile) {
            Toast.makeText(getContext(), "addresses", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.tv_logout_fragment_profile) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getContext(), SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onGetUserName(String fullName) {
        tvNameFragmentProfile.setText(fullName);
    }

    @Override
    public void onGetUserEmail(String email) {
        tvEmailFragmentProfile.setText(email);
    }

    @Override
    public void onGetUserBirthDate(String birthDate) {
        tvBirthdateFragmentProfile.setText(birthDate);
    }
}