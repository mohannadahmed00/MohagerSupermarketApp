package com.example.mysupermarket.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mysupermarket.R;
import com.example.mysupermarket.models.Item;

import java.util.ArrayList;

public class MySpinnerAdapter extends BaseAdapter {


    Context context;
    Item item;
    ArrayList<Item.Unit> unitArrayList;


    public MySpinnerAdapter(@NonNull Context context, Item item) {
        this.context = context;
        this.item = item;
        this.unitArrayList = new ArrayList<>();
        unitArrayList.add(item.getUnit());
        if (item.getSubPack() != null) {
            unitArrayList.add(item.getSubPack());
        }
        unitArrayList.add(item.getPack());


    }


    @Override
    public int getCount() {
        return unitArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, parent);
    }

    public View getCustomView(int position, ViewGroup parent) {
        //add if condition if unit.left==0 return null
        View layout = LayoutInflater.from(context).inflate(R.layout.my_text_spinner, parent, false);
        TextView tvDes = layout.findViewById(R.id.tv_des_spinner);
        TextView tvPrice = layout.findViewById(R.id.tv_price_spinner);
        String des, price;
        Item.Unit unit = unitArrayList.get(position);

        des = unit.getName() + " " + unit.getConsistNum() + unit.getConsistName();
        price = unit.getPrice() + " LE";

        tvDes.setText(des);
        tvPrice.setText(price);
        return layout;
    }
    public Item.Unit getSelectedUnit(int position){
        return unitArrayList.get(position);
    }



}

/*@Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view=LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_dropdown_item,parent,false);
        return super.getDropDownView(position, view, parent);
    }*/
