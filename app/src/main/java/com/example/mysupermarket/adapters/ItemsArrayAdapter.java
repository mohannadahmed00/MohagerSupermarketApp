package com.example.mysupermarket.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.mysupermarket.Interfaces.ProductsView;
import com.example.mysupermarket.R;
import com.example.mysupermarket.activities.CategoryActivity;
import com.example.mysupermarket.models.Item;
import com.example.mysupermarket.presenters.ProductsPresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemsArrayAdapter extends ArrayAdapter<Item> implements ProductsView {

    ViewHolder viewHolder;
    LinearLayout.LayoutParams params;
    int fragmentName, status, myCount, myPosition;
    Context context;
    MySpinnerAdapter itemUnitsAdapter;
    ArrayList<String> itemUnits;
    ArrayList<Item.Unit> units;
    FirebaseFirestore db;
    boolean isAdded;
    View myView;
    ProductsPresenter presenter;
    FirebaseUser firebaseUser;
    boolean checkFlag;

    public ItemsArrayAdapter(Context context, ArrayList<Item> list, int fragmentName) {
        super(context, 0, list);
        this.context = context;
        this.fragmentName = fragmentName;
    }

    public ItemsArrayAdapter(Context context, ArrayList<Item> list, int fragmentName, int status) {
        super(context
                , 0
                , list);
        this.context = context;
        this.fragmentName = fragmentName;
        this.status = status;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Item item = getItem(position);
        if (convertView == null) {
            if (fragmentName == R.layout.fragment_basket) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.h_item, parent, false);
            } else {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.v_item, parent, false);
            }
        }
        viewHolder = new ViewHolder(convertView);
        if (fragmentName != R.layout.fragment_basket) {
            viewHolder.tvNameItemV.setText(item.getName());
            //till upload categories and its images to db
            if (item.getImgURI() == null) {
                viewHolder.ivItemV.setImageResource(item.getImg());
            } else {
                Glide.with(convertView).load(item.getImgURI()).into(viewHolder.ivItemV);
            }
        }
        if (fragmentName == R.layout.fragment_home) {
            //viewHolder.tvNameItemV.setText(item.getName());
            params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            );
            params.setMargins(50, 50, 50, 50);
            viewHolder.llItemV.setLayoutParams(params);
            //llItemV.setBackground(AppCompatResources.getDrawable(getContext(),R.drawable.background_white));
            viewHolder.tvNameItemV.setGravity(Gravity.CENTER);
            viewHolder.ivItemV.setVisibility(View.VISIBLE);
            viewHolder.btnAddItemV.setVisibility(View.GONE);
            viewHolder.llItemV.setOnClickListener(e -> {
                Intent intent = new Intent(context, CategoryActivity.class);
                intent.putExtra("position", position);
                context.startActivity(intent);
            });

        } else if (fragmentName == R.layout.fragment_search) {

            if (status == 0) {
                //0 means i need vertical item view to recent search items
                params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                );
                params.setMargins(10, 10, 10, 10);
                viewHolder.llItemV.setLayoutParams(params);
                viewHolder.tvNameItemV.setGravity(Gravity.START);
                viewHolder.ivItemV.setVisibility(View.GONE);
                viewHolder.btnAddItemV.setVisibility(View.GONE);
            } else if (status == 1) {
                //1 means i need vertical item view to search items
                params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                );
                params.setMargins(50, 50, 50, 50);
                viewHolder.llItemV.setLayoutParams(params);
                //llItemV.setBackground(AppCompatResources.getDrawable(getContext(),R.drawable.background_white_no_ripp));
                viewHolder.tvNameItemV.setGravity(Gravity.START);
                viewHolder.ivItemV.setVisibility(View.VISIBLE);
                viewHolder.btnAddItemV.setVisibility(View.VISIBLE);
            }
        } else if (fragmentName == R.layout.fragment_basket) {

            presenter = new ProductsPresenter(getContext(), this, 0);
            //to check its available or not
            /*checkFlag = true;
            myCount = item.getCount();
            myPosition = item.getPosition();
            presenter.getItem(item);*/



            //the item here is a (cartItem)
            viewHolder.tvNameItemH.setText(item.getName());
            viewHolder.tvDescItemH.setText(item.getDes() + ", " + item.getsPrice() + "LE");
            viewHolder.tvCountItemH.setText(String.valueOf(item.getCount()));
            viewHolder.tvPriceItemH.setText("total: " + item.removeZero(Double.parseDouble(item.getsPrice()) * item.getCount()) + "LE");
            Glide.with(convertView).load(item.getImgURI()).into(viewHolder.ivItemH);


            viewHolder.ivPlusItemH.setOnClickListener(e -> {
                viewHolder.ivPlusItemH.setEnabled(false);
                presenter.getItem(item,"add");
                viewHolder.ivPlusItemH.setEnabled(true);
            });
            viewHolder.ivMinusItemH.setOnClickListener(e -> {
                viewHolder.ivMinusItemH.setEnabled(false);
                presenter.removeFromCart(item, 0, false);//any another number like 0 means just decrease one item
                viewHolder.ivMinusItemH.setEnabled(true);
            });
            viewHolder.ivDeleteItemH.setOnClickListener(e -> {
                viewHolder.ivDeleteItemH.setEnabled(false);
                presenter.removeFromCart(item, -1, true);//-1 mean delete directly
                viewHolder.ivDeleteItemH.setEnabled(true);
            });
        } else if (fragmentName == R.layout.fragment_items) {
            presenter = new ProductsPresenter(getContext(), this, 0);
            TextView tvCount = convertView.findViewById(R.id.tv_count_item_v);
            LinearLayout btnCount = convertView.findViewById(R.id.ll_num_item_v);
            Button btnOut = convertView.findViewById(R.id.btn_out_add_item_v);
            Button btnAdd = convertView.findViewById(R.id.btn_add_item_v);
            Spinner spinner = convertView.findViewById(R.id.s_item_v);
            if (item.getBy().equals("Unit")) {

                viewHolder.sItemV.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.my_spinner_style_gray));
                viewHolder.sItemV.setVisibility(View.VISIBLE);
                viewHolder.etWeightItemV.setVisibility(View.GONE);
                itemUnitsAdapter = new MySpinnerAdapter(getContext(), item);
                viewHolder.sItemV.setAdapter(itemUnitsAdapter);

                ///////////i work here

                /*if (item.getPack().getLeft() != 0) {
                    viewHolder.btnOutAddItemV.setVisibility(View.GONE);
                    viewHolder.btnAddItemV.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.btnOutAddItemV.setVisibility(View.VISIBLE);
                    viewHolder.btnAddItemV.setVisibility(View.GONE);
                }*/
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Item.Unit sUnit = presenter.getSelectedUnit(item, spinner.getSelectedItemPosition());
                        /*if (sUnit.getLeft()!=0){
                            btnOut.setVisibility(View.GONE);
                            btnAdd.setVisibility(View.VISIBLE);
                        }else {
                            if (item.getPack().getLeft()==0){
                                btnOut.setVisibility(View.VISIBLE);
                                btnAdd.setVisibility(View.GONE);
                            }else {
                                btnOut.setVisibility(View.GONE);
                                btnAdd.setVisibility(View.VISIBLE);
                            }

                        }*/
                        if (sUnit.getLeft() == 0) {
                            if (item.getPack().getLeft() == 0){
                                btnOut.setVisibility(View.VISIBLE);
                                btnCount.setVisibility(View.GONE);
                                btnAdd.setVisibility(View.GONE);
                            }else {
                                btnOut.setVisibility(View.GONE);
                                btnCount.setVisibility(View.GONE);
                                btnAdd.setVisibility(View.VISIBLE);
                            }
                        } else {
                            btnOut.setVisibility(View.GONE);
                            btnCount.setVisibility(View.GONE);
                            btnAdd.setVisibility(View.VISIBLE);
                        }
                        //Item.Unit sUnit = getSelectedUnit(item, spinner.getSelectedItemPosition());
                        FirebaseFirestore.getInstance().collection("Users")
                                .document(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                                .collection("cart").addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                for (DocumentSnapshot snapshot : value.getDocuments()) {
                                    String id = item.getId() + sUnit.getPrice();
                                    ////our work///////////

                                    if (snapshot.getId().equals(id)) {//yes in cart
                                        String  c=snapshot.get("count").toString();
                                        if (Integer.parseInt(c)<=sUnit.getLeft() || item.getPack().getLeft()!=0){
                                            tvCount.setText(c);
                                            btnCount.setVisibility(View.VISIBLE);
                                            btnAdd.setVisibility(View.GONE);
                                            btnOut.setVisibility(View.GONE);
                                        }/*else {
                                            btnOut.setVisibility(View.VISIBLE);
                                            btnCount.setVisibility(View.GONE);
                                            btnAdd.setVisibility(View.GONE);
                                        }*/

                                        /*if (Integer.parseInt(snapshot.get("count").toString()) != 0) {//it will be removed in future
                                            tvCount.setText(snapshot.get("count").toString());
                                            btnCount.setVisibility(View.VISIBLE);
                                            btnAdd.setVisibility(View.GONE);
                                        } else {
                                            tvCount.setText(snapshot.get("count").toString());
                                            btnCount.setVisibility(View.GONE);
                                            btnAdd.setVisibility(View.VISIBLE);
                                        }*/
                                        break;
                                    }
                                    else if (sUnit.getLeft() == 0) {
                                        if (item.getPack().getLeft() == 0){
                                            btnOut.setVisibility(View.VISIBLE);
                                            btnCount.setVisibility(View.GONE);
                                            btnAdd.setVisibility(View.GONE);
                                        }else {
                                            btnOut.setVisibility(View.GONE);
                                            btnCount.setVisibility(View.GONE);
                                            btnAdd.setVisibility(View.VISIBLE);
                                        }
                                    } else {
                                        btnOut.setVisibility(View.GONE);
                                        btnCount.setVisibility(View.GONE);
                                        btnAdd.setVisibility(View.VISIBLE);
                                    }

                                    /*else if (sUnit.getLeft() == 0) {
                                        if (item.getPack().getLeft() == 0){
                                            btnOut.setVisibility(View.VISIBLE);
                                            btnCount.setVisibility(View.GONE);
                                            btnAdd.setVisibility(View.GONE);
                                        }else {
                                            btnOut.setVisibility(View.GONE);
                                            btnCount.setVisibility(View.GONE);
                                            btnAdd.setVisibility(View.VISIBLE);
                                        }
                                    } else {
                                        btnOut.setVisibility(View.GONE);
                                        btnCount.setVisibility(View.GONE);
                                        btnAdd.setVisibility(View.VISIBLE);
                                    }*/
                                }

                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                //viewHolder.llNumItemV.setVisibility(View.GONE);

            } else {
                viewHolder.sItemV.setVisibility(View.GONE);
                viewHolder.etWeightItemV.setVisibility(View.VISIBLE);
                if (Double.parseDouble(item.getWeight()) != 0) {
                    viewHolder.btnOutAddItemV.setVisibility(View.GONE);
                    viewHolder.btnAddItemV.setVisibility(View.VISIBLE);

                } else {
                    viewHolder.btnOutAddItemV.setVisibility(View.VISIBLE);
                    viewHolder.btnAddItemV.setVisibility(View.GONE);
                }
                //viewHolder.llNumItemV.setVisibility(View.GONE);
            }
            viewHolder.btnAddItemV.setOnClickListener(e -> {

                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if (firebaseUser != null) {
                    viewHolder.btnAddItemV.setEnabled(false);
                    presenter.addToCart(getContext(), item, 1, spinner.getSelectedItemPosition(), R.layout.fragment_items);
                    viewHolder.btnAddItemV.setEnabled(true);
                }
            });
            viewHolder.ivPlusItemV.setOnClickListener(e -> {
                viewHolder.ivPlusItemV.setEnabled(false);
                presenter.addToCart(getContext(), item, Integer.parseInt(tvCount.getText().toString()), spinner.getSelectedItemPosition(), R.layout.fragment_items);
                viewHolder.ivPlusItemV.setEnabled(true);
            });
            viewHolder.ivMinusItemV.setOnClickListener(e -> {
                viewHolder.ivMinusItemV.setEnabled(false);
                int x = Integer.parseInt(tvCount.getText().toString());
                if ((x - 1) == 0) {
                    btnAdd.setEnabled(true);
                    btnCount.setVisibility(View.GONE);
                    btnAdd.setVisibility(View.VISIBLE);
                }
                presenter.removeFromCart(item, spinner.getSelectedItemPosition(), false);
                viewHolder.ivMinusItemV.setEnabled(true);
            });

        }
        return convertView;
    }

    @Override
    public void onGetProductsName(ArrayList<Item> productsName) {

    }

    @Override
    public void onGetRecent(ArrayList<Item> recentSearches) {

    }

    @Override
    public void onGetCartItems(Context context, ArrayList<Item> cartItem) {

    }

    @Override
    public void isAdded(Boolean status,String count) {

    }


    static class ViewHolder {
        @Nullable
        @BindView(R.id.tv_name_item_h)
        TextView tvNameItemH;
        @Nullable
        @BindView(R.id.iv_item_h)
        ImageView ivItemH;
        @Nullable
        @BindView(R.id.tv_desc_item_h)
        TextView tvDescItemH;
        @Nullable
        @BindView(R.id.tv_price_item_h)
        TextView tvPriceItemH;
        @Nullable
        @BindView(R.id.iv_plus_item_h)
        ImageView ivPlusItemH;
        @Nullable
        @BindView(R.id.tv_count_item_h)
        TextView tvCountItemH;
        @Nullable
        @BindView(R.id.iv_minus_item_h)
        ImageView ivMinusItemH;
        @Nullable
        @BindView(R.id.iv_delete_item_h)
        ImageView ivDeleteItemH;


        @Nullable
        @BindView(R.id.et_weight_item_v)
        EditText etWeightItemV;
        @Nullable
        @BindView(R.id.s_item_v)
        Spinner sItemV;
        @Nullable
        @BindView(R.id.btn_out_add_item_v)
        Button btnOutAddItemV;
        @Nullable
        @BindView(R.id.iv_item_v)
        ImageView ivItemV;
        @Nullable
        @BindView(R.id.tv_name_item_v)
        TextView tvNameItemV;
        @Nullable
        @BindView(R.id.btn_add_item_v)
        Button btnAddItemV;
        @Nullable
        @BindView(R.id.iv_minus_item_v)
        ImageView ivMinusItemV;
        @Nullable
        @BindView(R.id.tv_count_item_v)
        TextView tvCountItemV;
        @Nullable
        @BindView(R.id.iv_plus_item_v)
        ImageView ivPlusItemV;
        @Nullable
        @BindView(R.id.ll_num_item_v)
        LinearLayout llNumItemV;
        @Nullable
        @BindView(R.id.ll_item_v)
        LinearLayout llItemV;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }


    }
}

/*Spinner spinner=convertView.findViewById(R.id.s_item_v);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int mposition, long id) {
                        if (mposition==0){
                            String u=String.valueOf(getItem(position).getUnit().getUnitPrice());
                            viewHolder.tvDescItemV.setText(u);
                        }else {
                            String p=String.valueOf(getItem(position).getPack().getUnitPrice());
                            viewHolder.tvDescItemV.setText(p);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });*/
/*if (!item.isLock()) {
                    FirebaseFirestore.getInstance().collection("Products").document(item.getId()).update("lock", true);
                    //my process with item



                    FirebaseFirestore.getInstance().collection("Products").document(item.getId()).update("lock", false);
                } else {
                    //can't take any action with item now
                }*/
