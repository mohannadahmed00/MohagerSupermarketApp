package com.example.mysupermarket.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mysupermarket.Interfaces.ProductsView;
import com.example.mysupermarket.R;
import com.example.mysupermarket.activities.VerificationActivity;
import com.example.mysupermarket.adapters.ItemsArrayAdapter;
import com.example.mysupermarket.models.Item;
import com.example.mysupermarket.presenters.ProductsPresenter;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Objects;


public class BasketFragment extends Fragment implements View.OnClickListener, ProductsView {
    View view;
    MaterialButton btnFragmentLogin;
    GridView gvItemsFragmentCart;
    Button btnTotalFragmentBasket;
    LinearLayout llNotEmptyFragmentCart;
    LinearLayout llEmptyFragmentBasket;
    LinearLayout llCheckoutFragmentBasket;
    ProductsPresenter presenter;


    ItemsArrayAdapter itemsArrayAdapter;
    ArrayList<Item> cartItems;


    public BasketFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            view = inflater.inflate(R.layout.fragment_basket, container, false);
            init();
            //presenter.listenCart();
            presenter.getCartItems();

            //getResultItems();
        } else {
            view = inflater.inflate(R.layout.fragment_login, container, false);
            init();
            btnFragmentLogin.setOnClickListener(this);
        }
        return view;
    }

    private void init() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            cartItems=new ArrayList<>();
            presenter=new ProductsPresenter(getContext(),this,R.layout.fragment_basket);
            gvItemsFragmentCart=view.findViewById(R.id.gv_items_fragment_basket);
            btnTotalFragmentBasket=view.findViewById(R.id.btn_total_fragment_basket);
            llEmptyFragmentBasket=view.findViewById(R.id.ll_empty_fragment_basket);
            llNotEmptyFragmentCart=view.findViewById(R.id.ll_not_empty_fragment_basket);
            llCheckoutFragmentBasket=view.findViewById(R.id.ll_checkout_fragment_basket);
            llCheckoutFragmentBasket.setOnClickListener(this);
        } else {
            btnFragmentLogin = view.findViewById(R.id.btn_fragment_login);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_fragment_login) {
            startActivity(new Intent(getActivity(), VerificationActivity.class));
        }
        else if (v.getId() == R.id.ll_checkout_fragment_basket) {
            Toast.makeText(getContext(), "total", Toast.LENGTH_SHORT).show();
        }

    }




    @Override
    public void onGetProductsName(ArrayList<Item> productsName) {

    }

    @Override
    public void onGetRecent(ArrayList<Item> recentSearches) {

    }

    @Override
    public void onGetCartItems(Context context,ArrayList<Item> cartItem) {

        itemsArrayAdapter = new ItemsArrayAdapter(context, cartItem, R.layout.fragment_basket,3);
        gvItemsFragmentCart.setAdapter(itemsArrayAdapter);
        if (!cartItem.isEmpty()){
            //cartItems=cartItem;
            double total=0;
            for (Item i : cartItem) {
                total= total + (i.getCount()*Double.parseDouble(i.getsPrice()));
            }
            btnTotalFragmentBasket.setText(Item.removeZero(total).concat(" LE"));
            llNotEmptyFragmentCart.setVisibility(View.VISIBLE);
            llEmptyFragmentBasket.setVisibility(View.GONE);


            itemsArrayAdapter.notifyDataSetChanged();

            /*cartItems.clear();
            for (Item i:cartItem){
                presenter.getItem(i,"check");
            }*/



        }else {
            llNotEmptyFragmentCart.setVisibility(View.GONE);
            llEmptyFragmentBasket.setVisibility(View.VISIBLE);
            llCheckoutFragmentBasket.setOnClickListener(this);
        }
    }

    @Override
    public void isAdded(Boolean status,String count) {

    }


}

/*void adaptGridView(GridView gridView, ItemsArrayAdapter adapter) {

        isEmpty();
    }

    void getResultItems(){
        //Items Presenter
        cartItems.add(new Item("Sauces", R.drawable.sauces));
        cartItems.add(new Item("Sweet", R.drawable.sweets));
        cartItems.add(new Item("Drinks", R.drawable.drinks));
        cartItems.add(new Item("Grocery", R.drawable.groceries));
        cartItems.add(new Item("Dairy", R.drawable.dairies));
        cartItems.add(new Item("Oils", R.drawable.oils));
        cartItems.add(new Item("Preserves", R.drawable.preserve));
        cartItems.add(new Item("Sauces", R.drawable.sauces));
        cartItems.add(new Item("Sweet", R.drawable.sweets));
        cartItems.add(new Item("Drinks", R.drawable.drinks));
        cartItems.add(new Item("Grocery", R.drawable.groceries));
        itemsArrayAdapter.notifyDataSetChanged();
        isEmpty();
    }*/
/*void isEmpty(){
        if (cartItems.size()==0){
            llNotEmptyFragmentCart.setVisibility(View.GONE);
            llEmptyFragmentBasket.setVisibility(View.VISIBLE);
        }else {
            llNotEmptyFragmentCart.setVisibility(View.VISIBLE);
            llEmptyFragmentBasket.setVisibility(View.GONE);
            llCheckoutFragmentBasket.setOnClickListener(this);
        }
    }*/