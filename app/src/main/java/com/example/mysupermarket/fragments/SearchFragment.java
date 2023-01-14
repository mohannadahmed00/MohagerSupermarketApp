package com.example.mysupermarket.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.mysupermarket.Interfaces.ProductsView;
import com.example.mysupermarket.R;
import com.example.mysupermarket.adapters.ItemsArrayAdapter;
import com.example.mysupermarket.models.Item;
import com.example.mysupermarket.presenters.ProductsPresenter;

import java.util.ArrayList;


public class SearchFragment extends Fragment implements View.OnClickListener, ProductsView {
    View view;
    TextView tvRecentSearchFragmentSearch, tvNoSearchFragmentSearch;
    EditText etSearchFragmentSearch;
    ImageView ivClearFragmentSearch;
    ImageView ivDeleteFragmentSearch, ivSearchFragmentSearch;
    LinearLayout llRecentSearchFragmentSearch;
    GridView gvSearchFragmentSearch;
    ArrayList<Item> recentItems, resultItems;
    ItemsArrayAdapter itemsArrayAdapter;
    ProductsPresenter presenter;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_search, container, false);
        init();
        presenter.getRecent();
        search();

        ivClearFragmentSearch.setOnClickListener(this);
        ivDeleteFragmentSearch.setOnClickListener(this);
        ivSearchFragmentSearch.setOnClickListener(this);
        gvSearchFragmentSearch.setOnItemClickListener((parent, view, position, id) -> {
            etSearchFragmentSearch.setText(recentItems.get(position).getName());
            presenter.getProducts(recentItems.get(position).getName());
        });


        return view;
    }


    private void init() {
        presenter = new ProductsPresenter(getContext(),this, R.layout.fragment_search);
        recentItems = new ArrayList<>();
        resultItems = new ArrayList<>();
        ivSearchFragmentSearch = view.findViewById(R.id.iv_search_fragment_search);
        llRecentSearchFragmentSearch = view.findViewById(R.id.ll_recent_search_fragment_search);
        tvRecentSearchFragmentSearch = view.findViewById(R.id.tv_recent_search_fragment_search);
        tvNoSearchFragmentSearch = view.findViewById(R.id.tv_no_search_fragment_search);
        etSearchFragmentSearch = view.findViewById(R.id.et_search_fragment_search);
        ivClearFragmentSearch = view.findViewById(R.id.iv_clear_fragment_search);
        ivDeleteFragmentSearch = view.findViewById(R.id.iv_delete_fragment_search);
        gvSearchFragmentSearch = view.findViewById(R.id.gv_search_fragment_search);

    }


    private void search() {

        etSearchFragmentSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    ivClearFragmentSearch.setVisibility(View.INVISIBLE);//x button
                } else {
                    ivClearFragmentSearch.setVisibility(View.VISIBLE);
                }


                presenter.getRecent();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    public void onClick(View view) {
        if (view.getId() == R.id.iv_clear_fragment_search) {
            etSearchFragmentSearch.setText("");
            resultItems.clear();
        } else if (view.getId() == R.id.iv_delete_fragment_search) {
            presenter.deleteAllRecent();
            recentItems.clear();
            llRecentSearchFragmentSearch.setVisibility(View.GONE);
            itemsArrayAdapter.notifyDataSetChanged();
        } else if (view.getId() == R.id.iv_search_fragment_search) {
            presenter.uploadRecent(etSearchFragmentSearch.getText().toString());
            resultItems.clear();
            for (String text : etSearchFragmentSearch.getText().toString().trim().split(" ")) {
                if (text.length() > 2) {
                    presenter.getProducts(text);
                }
            }
        }
    }

    //Find the Best Search Algorithm


    @Override
    public void onGetProductsName(ArrayList<Item> productsName) {
        if (!productsName.isEmpty()) {
            llRecentSearchFragmentSearch.setVisibility(View.GONE);
            resultItems = addJustNewItems(productsName);
            gvSearchFragmentSearch.setVisibility(View.VISIBLE);
            tvNoSearchFragmentSearch.setVisibility(View.GONE);
            itemsArrayAdapter = new ItemsArrayAdapter(requireContext(), resultItems, R.layout.fragment_search, 1);
            gvSearchFragmentSearch.setAdapter(itemsArrayAdapter);
            itemsArrayAdapter.notifyDataSetChanged();
        } else {
            llRecentSearchFragmentSearch.setVisibility(View.GONE);
            gvSearchFragmentSearch.setVisibility(View.GONE);
            tvNoSearchFragmentSearch.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onGetRecent(ArrayList<Item> recentSearches) {
        if (!recentSearches.isEmpty()) {
            recentItems = recentSearches;
            gvSearchFragmentSearch.setVisibility(View.VISIBLE);
            tvNoSearchFragmentSearch.setVisibility(View.GONE);
            llRecentSearchFragmentSearch.setVisibility(View.VISIBLE);
            itemsArrayAdapter = new ItemsArrayAdapter(requireContext(), recentItems, R.layout.fragment_search, 0);
            gvSearchFragmentSearch.setAdapter(itemsArrayAdapter);
            itemsArrayAdapter.notifyDataSetChanged();
        } else {
            llRecentSearchFragmentSearch.setVisibility(View.GONE);
        }

    }

    @Override
    public void onGetCartItems(Context context,ArrayList<Item> cartItems) {

    }

    @Override
    public void isAdded(Boolean status,String count) {

    }

    ArrayList<Item> addJustNewItems(ArrayList<Item> newItems) {

        for (Item item : newItems) {
            if (!isExist(item, resultItems)) {
                resultItems.add(item);
            }
        }

        return resultItems;
    }

    boolean isExist(Item item, ArrayList<Item> arrayList) {
        ///
        boolean x = false;
        for (Item i : arrayList) {
            if (i.getId().equals(item.getId())) {
                x = true;
                break;
            }

        }
        return x;
    }

}
