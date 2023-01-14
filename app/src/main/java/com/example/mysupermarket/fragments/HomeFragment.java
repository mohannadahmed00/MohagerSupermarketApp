package com.example.mysupermarket.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.mysupermarket.R;
import com.example.mysupermarket.adapters.ItemsArrayAdapter;
import com.example.mysupermarket.adapters.OffersPagerAdapter;
import com.example.mysupermarket.models.Item;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class HomeFragment extends Fragment {
    int currentPage=0;
    View view;
    ViewPager vpOffersFragmentHome;
    GridView gvCatFragmentHome;
    Timer timer;// This will create a new Thread
    ItemsArrayAdapter itemsArrayAdapter;
    OffersPagerAdapter offersPagerAdapter;
    ArrayList<Item> items;
    ArrayList<String> imgURLs;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        init();
        adaptGridView(gvCatFragmentHome,itemsArrayAdapter);
        adaptViewPager(vpOffersFragmentHome,offersPagerAdapter);
        return view;

    }

    private void init() {
        //Offer Presenter
        imgURLs=new ArrayList<>();
        imgURLs.add("https://scontent.fcai20-3.fna.fbcdn.net/v/t1.6435-9/217489672_10159193907385642_3339544743795063745_n.jpg?_nc_cat=107&ccb=1-3&_nc_sid=730e14&_nc_eui2=AeFyUQp5Cu0_WNHcGIXmZQHL1_piFnZnV83X-mIWdmdXzQErfIv8OConFUeX-hs29B-qwJtjh6j-xMucaDtVWPAf&_nc_ohc=YsNbE8KZlccAX8Q9Gav&_nc_oc=AQmRiBUMTRudp9fsunpnh4ZnvzQfCeJw463JqtyF3KfolbBDfsCS2WEVRV2hss0BihQ&tn=RyeBbnYXkBTVKjQP&_nc_ht=scontent.fcai20-3.fna&oh=206b5d542c6d1a780d42174675fe2200&oe=6125AA94");
        imgURLs.add("https://scontent.fcai20-3.fna.fbcdn.net/v/t1.6435-9/218088588_10159192254520642_8234691200742656302_n.jpg?_nc_cat=108&ccb=1-3&_nc_sid=730e14&_nc_eui2=AeFLPopOfebApQ1e28cmanqwTyrfSPC9Vv5PKt9I8L1W_jnQIedwj3ppHBCarRiPwUBZGLQ2YpDAgaZ8JdL2nYzO&_nc_ohc=k6gYkhOO4fAAX9IRqPI&_nc_oc=AQkLI1bsFGZqXIoyOwbr5W2aKa9sV-JK6EyuLDPyTwUSG80Bar3zeoB20lynvcOyB8U&_nc_ht=scontent.fcai20-3.fna&oh=fabd4788d2a39c5bbec46809c7927549&oe=6123EF61");
        imgURLs.add("https://scontent.fcai20-3.fna.fbcdn.net/v/t1.6435-9/219982194_10159212475730642_8989873134345079631_n.jpg?_nc_cat=110&ccb=1-3&_nc_sid=730e14&_nc_eui2=AeELZiytQaUiiXS6MyH8M9EWyWWpHVMglsTJZakdUyCWxNiusFzVc61nA7Njo11lXJXtM7UqnyNaVw8a0gKxFQYE&_nc_ohc=67CkBVgHcbEAX-a2Ch_&_nc_ht=scontent.fcai20-3.fna&oh=9cd068c4a2324b75f36bf897c4145de2&oe=61266F9B");
        imgURLs.add("https://scontent.fcai20-3.fna.fbcdn.net/v/t1.6435-9/221832368_10159215190140642_3782510388295167268_n.png?_nc_cat=102&ccb=1-3&_nc_sid=730e14&_nc_eui2=AeHwRLJZNxA9QH41kprcCSGQN7Kh1-BznAI3sqHX4HOcAu57m-u5671d8_QNrIw0-Lkqlw36TaxZJT6ni6vzgqYW&_nc_ohc=fGh63DcxKLMAX8ep3yR&_nc_ht=scontent.fcai20-3.fna&oh=b5e999f5b67aacdbf03a66e268dfc152&oe=6126A0E0");
        imgURLs.add("https://scontent.fcai20-3.fna.fbcdn.net/v/t1.6435-9/223484350_10159221035290642_6361236492576310949_n.jpg?_nc_cat=110&ccb=1-3&_nc_sid=730e14&_nc_eui2=AeFhSchb4XDYTvyTOy6M_tKcZNbdT0oowY5k1t1PSijBjvSJpMJRMpv-YLTikr3M7HzMKoUdz-R9ZHuaY1ErWuJg&_nc_ohc=XR8ASt4Hc8IAX9Xyd7A&_nc_ht=scontent.fcai20-3.fna&oh=54dd5bf4a604a98bcd77dec24e8f58ea&oe=6125B099");
        imgURLs.add("https://scontent.fcai20-3.fna.fbcdn.net/v/t1.6435-9/220583857_10159207236795642_1915110570639116152_n.jpg?_nc_cat=110&ccb=1-3&_nc_sid=730e14&_nc_eui2=AeGZGPAtTVgpFYBfqBHS5mSI4rZMbHcdGknitkxsdx0aSaxC6khMMH8VFnItsGK_ejTBd9jCmkcuC6Kf0wQW_wcc&_nc_ohc=h1rsr9VsYMcAX8ghFTm&tn=RyeBbnYXkBTVKjQP&_nc_ht=scontent.fcai20-3.fna&oh=016a06b63340ec09622db95ad373278f&oe=6127382A");
        imgURLs.add("https://scontent.fcai20-3.fna.fbcdn.net/v/t1.6435-9/215945646_10159193907175642_546467806823979058_n.jpg?_nc_cat=106&ccb=1-3&_nc_sid=730e14&_nc_eui2=AeF1uBlaGCh8kOOuuRrmRGkPC0YmOxnojYELRiY7GeiNgZGIb-kQY9--7zGr2-W128TMomdPQIKYdvsmgStA7TKT&_nc_ohc=mYq3DKKj9tMAX_e1ArB&_nc_ht=scontent.fcai20-3.fna&oh=e4f497bc040d72909e088c950377bea1&oe=61266668");


        items  = new ArrayList<>();
        items.add(new Item(getString(R.string.dairy), R.drawable.dairies));
        items.add(new Item(getString(R.string.drinks), R.drawable.drinks));
        items.add(new Item(getString(R.string.grocery), R.drawable.groceries));
        items.add(new Item(getString(R.string.oils), R.drawable.oils));
        items.add(new Item(getString(R.string.preserves), R.drawable.preserve));
        items.add(new Item(getString(R.string.sauces), R.drawable.sauces));
        items.add(new Item(getString(R.string.sweets), R.drawable.sweets));


        gvCatFragmentHome = view.findViewById(R.id.gv_cat_fragment_home);
        itemsArrayAdapter = new ItemsArrayAdapter(requireContext(), items, R.layout.fragment_home);

        vpOffersFragmentHome =view.findViewById(R.id.vp_offers_fragment_home);
        offersPagerAdapter=new OffersPagerAdapter(getContext(),imgURLs);
    }

    void adaptViewPager(ViewPager viewPager, PagerAdapter adapter){
        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        //thread to changes images automatically
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        final Handler handler = new Handler();
        final Runnable Update = () -> {
            if (currentPage == adapter.getCount()) {
                currentPage = 0;
            }
            viewPager.setCurrentItem(currentPage++, true);
        };
        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 500, 3000);
    }

    void adaptGridView(GridView gridView, ItemsArrayAdapter adapter){
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


}