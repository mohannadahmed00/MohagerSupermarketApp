package com.example.mysupermarket.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.mysupermarket.R;
import com.example.mysupermarket.activities.CategoryActivity;
import com.example.mysupermarket.activities.UploadActivity;


public class MoreFragment extends Fragment implements View.OnClickListener {
    View view;
    Button btnFaqFragmentMore, btnMailFragmentMore, btnPhoneFragmentMore, btnLanguageFragmentMore, btnProtectFragmentMore, btnTermsFragmentMore, btnLicencesFragmentMore, btnRateFragmentMore, btnShareFragmentMore;


    public MoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_more, container, false);
        init();
        return view;
    }

    private void init() {
        btnFaqFragmentMore = view.findViewById(R.id.btn_faq_fragment_more);
        btnMailFragmentMore = view.findViewById(R.id.btn_mail_fragment_more);
        btnPhoneFragmentMore = view.findViewById(R.id.btn_phone_fragment_more);
        btnLanguageFragmentMore = view.findViewById(R.id.btn_language_fragment_more);
        btnProtectFragmentMore = view.findViewById(R.id.btn_protect_fragment_more);
        btnTermsFragmentMore = view.findViewById(R.id.btn_terms_fragment_more);
        btnLicencesFragmentMore = view.findViewById(R.id.btn_licences_fragment_more);
        btnRateFragmentMore = view.findViewById(R.id.btn_Rate_fragment_more);
        btnShareFragmentMore = view.findViewById(R.id.btn_share_fragment_more);


        btnFaqFragmentMore.setOnClickListener(this);
        btnMailFragmentMore.setOnClickListener(this);
        btnPhoneFragmentMore.setOnClickListener(this);
        btnLanguageFragmentMore.setOnClickListener(this);
        btnProtectFragmentMore.setOnClickListener(this);
        btnTermsFragmentMore.setOnClickListener(this);
        btnLicencesFragmentMore.setOnClickListener(this);
        btnRateFragmentMore.setOnClickListener(this);
        btnShareFragmentMore.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId()== R.id.btn_faq_fragment_more){
            Toast.makeText(getContext(), "faq", Toast.LENGTH_SHORT).show();
        }else if (v.getId()== R.id.btn_mail_fragment_more){
            Toast.makeText(getContext(), "mail", Toast.LENGTH_SHORT).show();
        }else if (v.getId()== R.id.btn_phone_fragment_more){
            Toast.makeText(getContext(), "phone", Toast.LENGTH_SHORT).show();
        }else if (v.getId()== R.id.btn_language_fragment_more){
            Toast.makeText(getContext(), "language", Toast.LENGTH_SHORT).show();
        }else if (v.getId()== R.id.btn_protect_fragment_more){
            Toast.makeText(getContext(), "protection", Toast.LENGTH_SHORT).show();
        }else if (v.getId()== R.id.btn_terms_fragment_more){
            Toast.makeText(getContext(), "terms", Toast.LENGTH_SHORT).show();
        }else if (v.getId()== R.id.btn_licences_fragment_more){
            startActivity(new Intent(getActivity(), UploadActivity.class));
            Toast.makeText(getContext(), "licences", Toast.LENGTH_SHORT).show();
        }else if (v.getId()== R.id.btn_Rate_fragment_more){
            Toast.makeText(getContext(), "rate", Toast.LENGTH_SHORT).show();
        }else if (v.getId()== R.id.btn_share_fragment_more){
            Toast.makeText(getContext(), "share", Toast.LENGTH_SHORT).show();
        }
    }
}