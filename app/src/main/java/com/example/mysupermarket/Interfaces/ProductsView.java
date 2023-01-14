package com.example.mysupermarket.Interfaces;

import android.content.Context;

import com.example.mysupermarket.models.Item;

import java.util.ArrayList;

public interface ProductsView {
    void onGetProductsName(ArrayList<Item> productsName);
    void onGetRecent(ArrayList<Item> recentSearches);
    void onGetCartItems(Context context,ArrayList<Item> cartItem);
    void isAdded(Boolean status,String count);
    //void onGetItems(Item origin,Item cartItem);
}
