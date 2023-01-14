package com.example.mysupermarket.fragments;

import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import androidx.fragment.app.Fragment;
import com.example.mysupermarket.R;
import com.example.mysupermarket.adapters.ItemsArrayAdapter;
import com.example.mysupermarket.models.Item;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class CategoryFragment extends Fragment {
    String category;

    public CategoryFragment(String category) {
        this.category = category;
    }

    View view;
    GridView gvItemFragment;
    ItemsArrayAdapter itemsArrayAdapter;
    DatabaseReference db1;
    FirebaseFirestore db;
    HashMap<Item.Unit, Object> pack,unit,sub;
    Item.Unit pack1,unit1,sub1;
    ArrayList<Item> items;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_items, container, false);
        init();
        return view;
    }

    void init() {
        db=FirebaseFirestore.getInstance();
        db1 = FirebaseDatabase.getInstance().getReference();
        items = new ArrayList<>();
        gvItemFragment = view.findViewById(R.id.gv_item_fragment_items);
        itemsArrayAdapter = new ItemsArrayAdapter(getContext(), items, R.layout.fragment_items);
        gvItemFragment.setAdapter(itemsArrayAdapter);

        //problem is here
        db.collection("Products").whereEqualTo("category", category).addSnapshotListener((value, error) -> {
            items.clear();
            for (DocumentSnapshot snapshot : value.getDocuments()) {
                if (Objects.requireNonNull(snapshot.get("by")).toString().equals("Unit")) {
                    unit = ((HashMap<Item.Unit, Object>) snapshot.get("unit"));
                    unit1 = new Item.Unit();
                    unit1.setConsistNum(Double.parseDouble(unit.get("consistNum").toString()));
                    unit1.setConsistName(unit.get("consistName").toString());
                    unit1.setPrice(Double.parseDouble(unit.get("price").toString()));
                    unit1.setName(unit.get("name").toString());
                    unit1.setLeft(Integer.parseInt(unit.get("left").toString()));

                    if (snapshot.get("sub pack")!=null){
                        sub = ((HashMap<Item.Unit, Object>) snapshot.get("sub pack"));
                        sub1 = new Item.Unit();
                        sub1.setConsistNum(Double.parseDouble(sub.get("consistNum").toString()));
                        sub1.setConsistName(sub.get("consistName").toString());
                        sub1.setPrice(Double.parseDouble(sub.get("price").toString()));
                        sub1.setName(sub.get("name").toString());
                        sub1.setLeft(Integer.parseInt(sub.get("left").toString()));
                    }

                    pack = ((HashMap<Item.Unit, Object>) snapshot.get("pack"));
                    pack1 = new Item.Unit();
                    pack1.setConsistNum(Double.parseDouble(pack.get("consistNum").toString()));
                    pack1.setConsistName(pack.get("consistName").toString());
                    pack1.setPrice(Double.parseDouble(pack.get("price").toString()));
                    pack1.setName(pack.get("name").toString());
                    pack1.setLeft(Integer.parseInt(pack.get("left").toString()));

                    items.add(new Item(snapshot.getId()
                            , snapshot.get("product type") + " " + snapshot.get("product name")
                            , (String) snapshot.get("img uri")
                            , Objects.requireNonNull(snapshot.get("category")).toString()//no usage
                            , Objects.requireNonNull(snapshot.get("by")).toString()
                            ,unit1
                            ,sub1
                            , pack1
                            , (boolean) snapshot.get("lock")));
                } else {
                    items.add(new Item(snapshot.getId()
                            , snapshot.get("product type") + " " + snapshot.get("product name")
                            , (String) snapshot.get("img uri"), Objects.requireNonNull(snapshot.get("description")).toString()
                            , Objects.requireNonNull(snapshot.get("by")).toString()
                            , Double.parseDouble(snapshot.get("weight in stock").toString())
                            , (boolean) snapshot.get("lock")));
                }
                itemsArrayAdapter.notifyDataSetChanged();


            }
        });
    }

}

/*void reload(){
        requireActivity().finish();
        requireActivity().startActivity(requireActivity().getIntent());
    }*/
/*@Override
    public void onGetProductsName(ArrayList<Item> productsName) {
        itemsArrayAdapter = new ItemsArrayAdapter(this.getContext(), productsName, R.layout.fragment_items);
        gvItemFragment.setAdapter(itemsArrayAdapter);
        itemsArrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetRecent(ArrayList<Item> recentSearches) {

    }*/
/*presenter = new ProductsPresenter(this, R.layout.fragment_items);
        presenter.getProducts(category);
        gvItemFragment = view.findViewById(R.id.gv_item_fragment_items);*/
/*db1.child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                items.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    if (Objects.requireNonNull(snap.child("by").getValue()).toString().equals("Unit")) {

                        pack = ((HashMap<Item.Unit, Object>) snap.child("pack").getValue());
                        pack1 = new Item.Unit();
                        pack1.setNum(Integer.parseInt(pack.get("num").toString()));
                        pack1.setuPrice(Double.parseDouble(pack.get("unitPrice").toString()));
                        pack1.setuName(pack.get("unitName").toString());
                        pack1.setLeft(Integer.parseInt(pack.get("left").toString()));

                        items.add(new Item(snap.getKey()
                                , snap.child("product type").getValue() + " " + snap.child("product name").getValue()
                                , (String) snap.child("img uri").getValue(), Objects.requireNonNull(snap.child("description").getValue()).toString()
                                , Objects.requireNonNull(snap.child("by").getValue()).toString()
                                , pack1
                                , (boolean) snap.child("lock").getValue()));

                    } else {
                        items.add(new Item(snap.getKey()
                                , snap.child("product type").getValue() + " " + snap.child("product name").getValue()
                                , (String) snap.child("img uri").getValue(), Objects.requireNonNull(snap.child("description").getValue()).toString()
                                , Objects.requireNonNull(snap.child("by").getValue()).toString()
                                , Double.parseDouble(Objects.requireNonNull(snap.child("weight in stock").getValue()).toString())
                                , (boolean) snap.child("lock").getValue()));

                    }
                    itemsArrayAdapter.notifyDataSetChanged();


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
/*db.collection("Products").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

            }
        });*/
/*db.collection("Products").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                db.collection("Products").whereEqualTo("category", category).get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            items.clear();
                            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                                if (Objects.requireNonNull(snapshot.get("by")).toString().equals("Unit")) {
                                    pack = ((HashMap<Item.Unit, Object>) snapshot.get("pack"));
                                    pack1 = new Item.Unit();
                                    pack1.setNum(Integer.parseInt(pack.get("num").toString()));
                                    pack1.setuPrice(Double.parseDouble(pack.get("unitPrice").toString()));
                                    pack1.setuName(pack.get("unitName").toString());
                                    pack1.setLeft(Integer.parseInt(pack.get("left").toString()));

                                    items.add(new Item(snapshot.getId()
                                            , snapshot.get("product type") + " " + snapshot.get("product name")
                                            , (String) snapshot.get("img uri"), Objects.requireNonNull(snapshot.get("description")).toString()
                                            , Objects.requireNonNull(snapshot.get("by")).toString()
                                            , pack1
                                            , (boolean) snapshot.get("lock")));
                                } else {
                                    items.add(new Item(snapshot.getId()
                                            , snapshot.get("product type") + " " + snapshot.get("product name")
                                            , (String) snapshot.get("img uri"), Objects.requireNonNull(snapshot.get("description")).toString()
                                            , Objects.requireNonNull(snapshot.get("by")).toString()
                                            , Double.parseDouble(snapshot.get("weight in stock").toString())
                                            , (boolean) snapshot.get("lock")));
                                }
                                itemsArrayAdapter.notifyDataSetChanged();


                            }



                            //productsView.onGetProductsName(items);

                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(requireContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });*/