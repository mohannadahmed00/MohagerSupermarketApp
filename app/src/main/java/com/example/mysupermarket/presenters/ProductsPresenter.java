package com.example.mysupermarket.presenters;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mysupermarket.Interfaces.ProductsView;
import com.example.mysupermarket.R;
import com.example.mysupermarket.models.Item;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ProductsPresenter {
    ProductsView productsView;
    ArrayList<Item> items;
    int fragmentLayout;
    FirebaseFirestore db;
    DatabaseReference db1;
    HashMap<Item.Unit, Object> pack, unit, sub;
    Item.Unit pack1, unit1, sub1;
    Context context;


    public ProductsPresenter(Context context, ProductsView productsView, int fragmentLayout) {
        this.context = context;
        this.productsView = productsView;
        this.fragmentLayout = fragmentLayout;
        db = FirebaseFirestore.getInstance();
        db1 = FirebaseDatabase.getInstance().getReference();
    }

    void getProductsInfo(String searchText) {

        items = new ArrayList<>();
        if (fragmentLayout == R.layout.fragment_items) {

            db.collection("Products").whereEqualTo("category", searchText).get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                            if (Objects.requireNonNull(snapshot.get("by")).toString().equals("Unit")) {
                                unit = ((HashMap<Item.Unit, Object>) snapshot.get("unit"));
                                unit1 = new Item.Unit();
                                unit1.setConsistNum(Double.parseDouble(unit.get("consistNum").toString()));
                                unit1.setConsistName(unit.get("consistName").toString());
                                unit1.setPrice(Double.parseDouble(unit.get("price").toString()));
                                unit1.setName(unit.get("name").toString());
                                unit1.setLeft(Integer.parseInt(unit.get("left").toString()));

                                if (snapshot.get("sub pack") != null) {
                                    sub = ((HashMap<Item.Unit, Object>) snapshot.get("sub pack"));
                                    sub1 = new Item.Unit();
                                    sub1.setConsistNum(Integer.parseInt(sub.get("consistNum").toString()));
                                    sub1.setConsistName(sub.get("consistName").toString());
                                    sub1.setPrice(Double.parseDouble(sub.get("price").toString()));
                                    sub1.setName(sub.get("name").toString());
                                    sub1.setLeft(Integer.parseInt(sub.get("left").toString()));
                                }

                                pack = ((HashMap<Item.Unit, Object>) snapshot.get("pack"));
                                pack1 = new Item.Unit();
                                pack1.setConsistNum(Integer.parseInt(pack.get("consistNum").toString()));
                                pack1.setConsistName(pack.get("consistName").toString());
                                pack1.setPrice(Double.parseDouble(pack.get("price").toString()));
                                pack1.setName(pack.get("name").toString());
                                pack1.setLeft(Integer.parseInt(pack.get("left").toString()));

                                items.add(new Item(snapshot.getId()
                                        , snapshot.get("product type") + " " + snapshot.get("product name")
                                        , (String) snapshot.get("img uri")
                                        , Objects.requireNonNull(snapshot.get("category")).toString()//no usage
                                        , Objects.requireNonNull(snapshot.get("by")).toString()
                                        , unit1
                                        , sub1
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


                        }
                        productsView.onGetProductsName(items);

                        //productsView.onGetProductsName(items);

                    });
        } else if (fragmentLayout == R.layout.fragment_search) {

            search1(searchText);
        }

    }

    public void getProducts(String searchText) {
        getProductsInfo(searchText);
    }

    public void removeFromCart(Item item, int position, boolean remove) {
        String price, id;
        if (item.getUnit() != null) {
            price = getSelectedUnit(item, position).getPrice();
        } else {
            price = item.getsPrice();
        }
        id = item.getId() + price;
        String phone = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();
        db.collection("Users")
                .document(Objects.requireNonNull(phone))
                .collection("cart")
                .document(id)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                if (remove) {
                    snapshot.getReference().delete();
                } else {
                    int count = Integer.parseInt(Objects.requireNonNull(snapshot.get("count")).toString());
                    if (count - 1 == 0) {
                        snapshot.getReference().delete();
                    } else {
                        snapshot.getReference().update("count", count - 1);
                    }
                }
            }
        });
    }

    /*public void listenCart() {
        String phone = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();
        db.collection("Users")
                .document(Objects.requireNonNull(phone))
                .collection("cart").orderBy("product name")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        ArrayList<Item> itemsss = new ArrayList<>();
                        for (DocumentSnapshot snapshot : Objects.requireNonNull(value).getDocuments()) {

                            //items.add(new Item())
                            itemsss.add(new Item(snapshot.get("product id").toString()
                                    , snapshot.get("product name").toString()
                                    , snapshot.get("description").toString()
                                    , (String) snapshot.get("product img")
                                    , Integer.parseInt(snapshot.get("count").toString())//no usage
                                    , Objects.requireNonNull(snapshot.get("price")).toString()
                                    , Integer.parseInt(snapshot.get("position").toString())
                            ));

                        }
                        productsView.onGetCartItems(context, itemsss);

                    }
                });
    }*/

    public void getCartItems() {

        String phone = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();
        db.collection("Users")
                .document(Objects.requireNonNull(phone))
                .collection("cart").orderBy("product name", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        ArrayList<Item> itemsss = new ArrayList<>();
                        for (DocumentSnapshot snapshot : Objects.requireNonNull(value).getDocuments()) {

                            //items.add(new Item())
                            itemsss.add(new Item(snapshot.get("product id").toString()
                                    , snapshot.get("product name").toString()
                                    , snapshot.get("description").toString()
                                    , (String) snapshot.get("product img")
                                    , Integer.parseInt(snapshot.get("count").toString())//no usage
                                    , Objects.requireNonNull(snapshot.get("price")).toString()
                                    , Integer.parseInt(snapshot.get("position").toString())
                            ));

                        }
                        productsView.onGetCartItems(context, itemsss);

                    }
                });
        db.collection("Users")
                .document(Objects.requireNonNull(phone))
                .collection("cart").orderBy("product name", Query.Direction.ASCENDING)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<Item> itemsss = new ArrayList<>();
                for (DocumentSnapshot snapshot : queryDocumentSnapshots) {

                    //items.add(new Item())
                    itemsss.add(new Item(snapshot.get("product id").toString()
                            , snapshot.get("product name").toString()
                            , snapshot.get("description").toString()
                            , (String) snapshot.get("product img")
                            , Integer.parseInt(snapshot.get("count").toString())//no usage
                            , Objects.requireNonNull(snapshot.get("price")).toString()
                            , Integer.parseInt(snapshot.get("position").toString())
                    ));

                }


                items = new ArrayList<>();
                for (Item i : itemsss) {
                    getItem(i, "check");
                }
            }
        });

    }

    public void lastCheck(Item origin, Item cartItem) {
        HashMap<String, Object> map = new HashMap<>();
        String phone = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();
        db.collection("Users")
                .document(Objects.requireNonNull(phone))
                .collection("cart")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<Item.Unit> units = new ArrayList<>();
                    units.add(origin.getUnit());
                    if (origin.getSubPack() != null) {
                        units.add(origin.getSubPack());
                    }
                    units.add(origin.getPack());

                    if (cartItem.getPosition() + 1 < units.size()) {//it's not pack


                        if (cartItem.getCount() <= units.get(cartItem.getPosition()).getLeft()) {
                            items.add(cartItem);
                            productsView.onGetCartItems(context, items);
                            //productsView.isAvail(true,origin,cartItem);
                        } else if (units.get(cartItem.getPosition() + 1).getLeft() != 0) {
                            items.add(cartItem);
                            productsView.onGetCartItems(context, items);
                            //productsView.isAvail(true,origin,cartItem);
                        } else if ((units.size() == 3 && cartItem.getPosition() == 0) && units.get(cartItem.getPosition() + 2).getLeft() != 0) {
                            items.add(cartItem);
                            productsView.onGetCartItems(context, items);
                            // productsView.isAvail(true,origin,cartItem);
                        } else {
                            //productsView.isAvail(false,origin,cartItem);
                        }

                    } else {//it's pack
                        if (cartItem.getCount() <= units.get(cartItem.getPosition()).getLeft()) {
                            items.add(cartItem);
                            productsView.onGetCartItems(context, items);
                        } else {

                        }
                        //productsView.isAvail(cartItem.getCount() <= units.get(cartItem.getPosition()).getLeft(),origin,cartItem);
                    }


                });

    }

    public void addToCart(Context context, Item origin, int count, int position, int layout) {
        Item.Unit unit = getSelectedUnit(origin, position);
        HashMap<String, Object> map = new HashMap<>();
        String phone = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();
        db.collection("Users")
                .document(Objects.requireNonNull(phone))
                .collection("cart")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    boolean isExist = false;

                    if (layout != R.layout.fragment_basket) {
                        for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()) {
                            String productID = Objects.requireNonNull(snap.get("product id")).toString();
                            String[] des = Objects.requireNonNull(snap.get("description")).toString().split(" ");
                            if (origin.getId().equals(productID) && unit.getName().equals(des[0])) {
                                isExist = true;
                                break;
                            }
                        }
                    } else {
                        isExist = true;
                    }
                    if (!isExist) {
                        String description = unit.getName() + " " + unit.getConsistNum() + unit.getConsistName();
                        map.put("product id", origin.getId());
                        map.put("product img", origin.getImgURI());
                        map.put("product name", origin.getName());
                        map.put("price", unit.getPrice());
                        map.put("count", count);
                        map.put("description", description);
                        map.put("position", position);

                        db.collection("Users")
                                .document(Objects.requireNonNull(Objects.requireNonNull(phone)))
                                .collection("cart")
                                .document(origin.getId() + unit.getPrice())
                                .set(map);
                    } else {//the product is exist
                        //in case called from basket


                        ArrayList<Item.Unit> units = new ArrayList<>();
                        units.add(origin.getUnit());
                        if (origin.getSubPack() != null) {
                            units.add(origin.getSubPack());
                        }
                        units.add(origin.getPack());


                        if (position + 1 < units.size()) {//it's not pack


                            if (count + 1 <= units.get(position).getLeft()) {
                                if (count + 1 >= Integer.parseInt(units.get(position + 1).getConsistNum())) {//num of units less than the next unit
                                    productsView.isAdded(false, String.valueOf(count + 1));
                                    Toast.makeText(context, "select a " + units.get(position + 1).getName() + " better than your choice of " + (count + 1) + " " + unit.getName(), Toast.LENGTH_SHORT).show();
                                } else {
                                    db.collection("Users")
                                            .document(Objects.requireNonNull(Objects.requireNonNull(phone)))
                                            .collection("cart")
                                            .document(origin.getId() + unit.getPrice())
                                            .update("count", count + 1).addOnSuccessListener(e -> {
                                        productsView.isAdded(true, String.valueOf(count + 1));
                                    });
                                }
                            } else if (units.get(position + 1).getLeft() != 0) {
                                if (count + 1 >= Integer.parseInt(units.get(position + 1).getConsistNum())) {//num of units less than the next unit
                                    productsView.isAdded(false, String.valueOf(count + 1));
                                    Toast.makeText(context, "select a " + units.get(position + 1).getName() + " better than your choice of " + (count + 1) + " " + unit.getName(), Toast.LENGTH_SHORT).show();
                                } else {
                                    db.collection("Users")
                                            .document(Objects.requireNonNull(Objects.requireNonNull(phone)))
                                            .collection("cart")
                                            .document(origin.getId() + unit.getPrice())
                                            .update("count", count + 1).addOnSuccessListener(e -> {
                                        productsView.isAdded(true, String.valueOf(count + 1));
                                    });
                                }
                            } else if ((units.size() == 3 && position == 0) && units.get(position + 2).getLeft() != 0) {
                                if (count + 1 >= Integer.parseInt(units.get(position + 1).getConsistNum())) {//num of units less than the next unit
                                    productsView.isAdded(false, String.valueOf(count + 1));
                                    Toast.makeText(context, "select a " + units.get(position + 1).getName() + " better than your choice of " + (count + 1) + " " + unit.getName(), Toast.LENGTH_SHORT).show();
                                } else {
                                    db.collection("Users")
                                            .document(Objects.requireNonNull(Objects.requireNonNull(phone)))
                                            .collection("cart")
                                            .document(origin.getId() + unit.getPrice())
                                            .update("count", count + 1).addOnSuccessListener(e -> {
                                        productsView.isAdded(true, String.valueOf(count + 1));
                                    });
                                }
                            } else {
                                productsView.isAdded(false, String.valueOf(count + 1));
                                Toast.makeText(context, "we'll supply this product soon", Toast.LENGTH_SHORT).show();
                            }

                        } else {//it's pack
                            if (count + 1 <= units.get(position).getLeft()) {
                                db.collection("Users")
                                        .document(Objects.requireNonNull(Objects.requireNonNull(phone)))
                                        .collection("cart")
                                        .document(origin.getId() + unit.getPrice())
                                        .update("count", count + 1).addOnSuccessListener(e -> {
                                    productsView.isAdded(true, String.valueOf(count + 1));
                                });
                            } else {
                                productsView.isAdded(false, String.valueOf(count + 1));
                                Toast.makeText(context, "we'll supply this product soon", Toast.LENGTH_SHORT).show();
                            }
                        }


                    }//


                });
    }

    public void getItem(Item cartItem, String status) {
        db.collection("Products").document(cartItem.getId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                if (Objects.requireNonNull(snapshot.get("by")).toString().equals("Unit")) {
                    HashMap<Item.Unit, Object> unit = ((HashMap<Item.Unit, Object>) snapshot.get("unit"));
                    unit1 = new Item.Unit();
                    unit1.setConsistNum(Double.parseDouble(unit.get("consistNum").toString()));
                    unit1.setConsistName(unit.get("consistName").toString());
                    unit1.setPrice(Double.parseDouble(unit.get("price").toString()));
                    unit1.setName(unit.get("name").toString());
                    unit1.setLeft(Integer.parseInt(unit.get("left").toString()));

                    if (snapshot.get("sub pack") != null) {
                        sub = ((HashMap<Item.Unit, Object>) snapshot.get("sub pack"));
                        sub1 = new Item.Unit();
                        sub1.setConsistNum(Integer.parseInt(sub.get("consistNum").toString()));
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

                    Item origin = new Item(snapshot.getId()
                            , snapshot.get("product type") + " " + snapshot.get("product name")
                            , (String) snapshot.get("img uri")
                            , Objects.requireNonNull(snapshot.get("category")).toString()//no usage
                            , Objects.requireNonNull(snapshot.get("by")).toString()
                            , unit1
                            , sub1
                            , pack1
                            , (boolean) snapshot.get("lock"));
                    if (status.equals("add")) {
                        //productsView.onGetItems(origin,cartItem);
                        addToCart(context, origin, cartItem.getCount(), cartItem.getPosition(), R.layout.fragment_basket);
                    } else {
                        lastCheck(origin, cartItem);
                    }

                    //
                }
            }
        });
    }

    public Item.Unit getSelectedUnit(Item item, int position) {
        ArrayList<Item.Unit> unitArrayList = new ArrayList<>();
        unitArrayList.add(item.getUnit());
        if (item.getSubPack() != null) {
            unitArrayList.add(item.getSubPack());
        }
        unitArrayList.add(item.getPack());
        Item.Unit unit = unitArrayList.get(position);
        return unit;
    }

    //search functions
    public void uploadRecent(String textSearch) {
        HashMap<String, Object> map = new HashMap<>();
        String phone = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();
        db.collection("Search History").document(Objects.requireNonNull(phone)).collection("recent searches").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    boolean isExist = false;
                    for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()) {
                        String result = Objects.requireNonNull(snap.get("text")).toString();
                        if (result.equals(textSearch)) {
                            isExist = true;
                            break;
                        }
                    }
                    if (!isExist) {
                        map.put("text", textSearch);
                        db.collection("Search History").document(phone).collection("recent searches").add(map);
                    }


                });
    }

    public void getRecent() {
        ArrayList<Item> recentSearches = new ArrayList<>();
        String phone = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();
        db.collection("Search History").document(Objects.requireNonNull(phone)).collection("recent searches").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()) {
                        recentSearches.add(new Item(Objects.requireNonNull(snap.get("text")).toString()));
                    }
                    productsView.onGetRecent(recentSearches);
                });
    }

    public void deleteAllRecent() {
        String phone = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhoneNumber();
        db.collection("Search History").document(Objects.requireNonNull(phone)).collection("recent searches").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()) {
                        db.collection("Search History").document(Objects.requireNonNull(phone)).collection("recent searches").document(snap.getId()).delete();
                    }
                });
    }

    void search1(String text) {
        int valueOfLastChar = text.charAt(text.length() - 1);
        String next = String.valueOf((char) (valueOfLastChar + 1));
        String searchText = text.substring(0, text.length() - 1) + next;
        db.collection("Products")
                .whereGreaterThanOrEqualTo("product type", text).whereLessThan("product type", searchText)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    items.clear();
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                        items.add(new Item(snapshot.getId(), snapshot.get("product type") + " " + snapshot.get("product name"), (String) snapshot.get("img uri"), (String) snapshot.get("description")));
                    }
                    if (items.isEmpty()) {
                        search2(text);
                    } else {
                        productsView.onGetProductsName(items);
                    }

                });

        //.whereArrayContains("key words", searchText)
    }

    void search2(String text) {
        int valueOfLastChar = text.charAt(text.length() - 1);
        String next = String.valueOf((char) (valueOfLastChar + 1));
        String searchText = text.substring(0, text.length() - 1) + next;
        db.collection("Products")
                .whereGreaterThanOrEqualTo("product name", text).whereLessThan("product name", searchText)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    //items.clear();
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                        items.add(new Item(snapshot.getId(), snapshot.get("product type") + " " + snapshot.get("product name"), (String) snapshot.get("img uri"), (String) snapshot.get("description")));
                    }
                    if (items.isEmpty()) {
                        search3(text);
                    } else {
                        productsView.onGetProductsName(items);
                    }

                });

        //.whereArrayContains("key words", searchText)
    }

    void search3(String text) {
        int valueOfLastChar = text.charAt(text.length() - 1);
        String next = String.valueOf((char) (valueOfLastChar + 1));
        String searchText = text.substring(0, text.length() - 1) + next;
        db.collection("Products")
                .whereGreaterThanOrEqualTo("category", text).whereLessThan("category", searchText)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    //items.clear();
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                        items.add(new Item(snapshot.getId(), snapshot.get("product type") + " " + snapshot.get("product name"), (String) snapshot.get("img uri"), (String) snapshot.get("description")));
                    }
                    productsView.onGetProductsName(items);
                });
        //.whereArrayContains("key words", searchText)
    }


}
/*public void addToCart(String productID, String des, String type, double price) {
        HashMap<String, Object> map = new HashMap<>();
        db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                .collection("Cart")
                .document(productID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (productID.equals(documentSnapshot.getId()) && des.equals(documentSnapshot.get("des"))) {
                    int count = Integer.parseInt(documentSnapshot.get("count").toString());
                    db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                            .collection("Cart")
                            .document(productID)
                            .update("count", count + 1, "total", (count + 1) * price);

                } else {
                    map.put("description", des);
                    map.put("price", price);
                    map.put("type", type);
                    map.put("count", 1);
                    map.put("total", price);
                    db.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber())
                            .collection("Cart")
                            .document(productID)
                            .set(map);
                }
            }
        });

    }*/
/*ArrayList<String> addToTop(String s, ArrayList<String> arrayList) {
        arrayList.remove(arrayList.size() - 1);
        for (int i = arrayList.size() - 1; i >= 0; i--) {
            if (i == 0) {
                arrayList.add(i, s);
            }
            arrayList.add(i, arrayList.get(i - 1));
        }
        return arrayList;
    }*/
/*db1.child("Products").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        if (Objects.requireNonNull(snap.child("by").getValue()).toString().equals("Unit")) {

                            unit = ((HashMap<Item.Unit, Object>) snapshot.child("unit").getValue());
                            unit1 = new Item.Unit();
                            unit1.setConsistNum(Integer.parseInt(unit.get("consistNum").toString()));
                            unit1.setConsistName(unit.get("consistName").toString());
                            unit1.setPrice(Double.parseDouble(unit.get("price").toString()));
                            unit1.setName(unit.get("name").toString());
                            unit1.setLeft(Integer.parseInt(unit.get("left").toString()));

                            if (snapshot.child("sub pack").getValue()!=null){
                                sub = ((HashMap<Item.Unit, Object>) snapshot.child("sub pack").getValue());
                                sub1 = new Item.Unit();
                                sub1.setConsistNum(Integer.parseInt(sub.get("consistNum").toString()));
                                sub1.setConsistName(sub.get("consistName").toString());
                                sub1.setPrice(Double.parseDouble(sub.get("price").toString()));
                                sub1.setName(sub.get("name").toString());
                                sub1.setLeft(Integer.parseInt(sub.get("left").toString()));
                            }

                            pack = ((HashMap<Item.Unit, Object>) snap.child("pack").getValue());
                            pack1 = new Item.Unit();
                            pack1.setConsistNum(Integer.parseInt(pack.get("consistNum").toString()));
                            pack1.setConsistName(pack.get("consistName").toString());
                            pack1.setPrice(Double.parseDouble(pack.get("price").toString()));
                            pack1.setName(pack.get("name").toString());
                            pack1.setLeft(Integer.parseInt(pack.get("left").toString()));

                            items.add(new Item(snap.getKey()
                                    , snap.child("product type").getValue() + " " + snap.child("product name").getValue()
                                    , (String) snap.child("img uri").getValue(), Objects.requireNonNull(snap.child("description").getValue()).toString()
                                    , Objects.requireNonNull(snap.child("by").getValue()).toString()
                                    ,unit1
                                    ,sub1
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


                    }
                    productsView.onGetProductsName(items);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });*/

/*{

                        ArrayList<Item.Unit> units = new ArrayList<>();
                        units.add(origin.getUnit());
                        if (origin.getSubPack() != null) {
                            units.add(origin.getSubPack());
                        }
                        units.add(origin.getPack());


                        if (position + 1 < units.size()) {//it's not pack


                            if (count + 1 <= units.get(position).getLeft()) {
                                if (count + 1 >= Integer.parseInt(units.get(position + 1).getConsistNum())) {//num of units less than the next unit
                                    Toast.makeText(context, "select a " + units.get(position + 1).getName() + " better than your choice of " + (count + 1) + " " + unit.getName(), Toast.LENGTH_SHORT).show();
                                } else {
                                    db.collection("Users")
                                            .document(Objects.requireNonNull(Objects.requireNonNull(phone)))
                                            .collection("cart")
                                            .document(origin.getId() + unit.getPrice())
                                            .update("count", count + 1);
                                }
                            } else if (units.get(position + 1).getLeft() != 0) {
                                if (count + 1 >= Integer.parseInt(units.get(position + 1).getConsistNum())) {//num of units less than the next unit
                                    Toast.makeText(context, "select a " + units.get(position + 1).getName() + " better than your choice of " + (count + 1) + " " + unit.getName(), Toast.LENGTH_SHORT).show();
                                } else {
                                    db.collection("Users")
                                            .document(Objects.requireNonNull(Objects.requireNonNull(phone)))
                                            .collection("cart")
                                            .document(origin.getId() + unit.getPrice())
                                            .update("count", count + 1);
                                }
                            } else if ((units.size() == 3 && position == 0) && units.get(position + 2).getLeft() != 0) {
                                if (count + 1 >= Integer.parseInt(units.get(position + 1).getConsistNum())) {//num of units less than the next unit
                                    Toast.makeText(context, "select a " + units.get(position + 1).getName() + " better than your choice of " + (count + 1) + " " + unit.getName(), Toast.LENGTH_SHORT).show();
                                } else {
                                    db.collection("Users")
                                            .document(Objects.requireNonNull(Objects.requireNonNull(phone)))
                                            .collection("cart")
                                            .document(origin.getId() + unit.getPrice())
                                            .update("count", count + 1);
                                }
                            } else {
                                Toast.makeText(context, "we'll supply this product soon", Toast.LENGTH_SHORT).show();
                                //productsView.isAvail(false,count,item);
                            }

                        } else {//it's pack
                            if (count + 1 <= units.get(position).getLeft()) {
                                db.collection("Users")
                                        .document(Objects.requireNonNull(Objects.requireNonNull(phone)))
                                        .collection("cart")
                                        .document(origin.getId() + unit.getPrice())
                                        .update("count", count + 1);
                            } else {
                                Toast.makeText(context, "we'll supply this product soon", Toast.LENGTH_SHORT).show();
                                //productsView.isAvail(false,count,item);
                            }
                        }


                    } */