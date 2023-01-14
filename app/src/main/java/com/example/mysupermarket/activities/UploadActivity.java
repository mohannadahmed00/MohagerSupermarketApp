package com.example.mysupermarket.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.mysupermarket.R;
import com.example.mysupermarket.models.Item;
import com.google.android.gms.tasks.Continuation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UploadActivity extends AppCompatActivity {


    FirebaseFirestore db;
    DatabaseReference db1;
    FirebaseUser firebaseUser;
    StorageReference storageRef;
    UploadTask uploadTask;
    String imgUri, by;
    ArrayList<String> categories, weights;
    ArrayAdapter<String> adapter, adapter2;
    private Uri filePath;


    @BindView(R.id.ll_unit_weight_activity_upload)
    LinearLayout llUnitWeightActivityUpload;
    @BindView(R.id.iv_add_item_activity_upload)
    ImageView ivAddItemActivityUpload;
    @BindView(R.id.rb_item_activity_upload)
    RadioButton rbItemActivityUpload;
    @BindView(R.id.rb_weight_activity_upload)
    RadioButton rbWeightActivityUpload;
    @BindView(R.id.s_category_activity_upload)
    Spinner sCategoryActivityUpload;
    @BindView(R.id.et_name_activity_upload)
    EditText etNameActivityUpload;
    @BindView(R.id.et_unit_name_activity_upload)
    EditText etUnitNameActivityUpload;
    @BindView(R.id.et_unit_weight_activity_upload)
    EditText etUnitWeightActivityUpload;
    @BindView(R.id.s_weights_unit_activity_upload)
    Spinner sWeightsUnitActivityUpload;
    @BindView(R.id.et_unit_num_activity_upload)
    EditText etUnitNumActivityUpload;
    @BindView(R.id.et_unit_price_activity_upload)
    EditText etUnitPriceActivityUpload;
    @BindView(R.id.et_sub_pack_num_activity_upload)
    EditText etSubPackNumActivityUpload;
    @BindView(R.id.et_sub_pack_price_activity_upload)
    EditText etSubPackPriceActivityUpload;
    @BindView(R.id.et_pack_num_activity_upload)
    EditText etPackNumActivityUpload;
    @BindView(R.id.et_pack_price_activity_upload)
    EditText etPackPriceActivityUpload;
    @BindView(R.id.et_weight_stock_activity_upload)
    EditText etWeightStockActivityUpload;
    @BindView(R.id.btn_activity_upload)
    Button btnActivityUpload;
    @BindView(R.id.btn_inactive_activity_upload)
    Button btnInactiveActivityUpload;
    @BindView(R.id.ll_unit_activity_upload)
    LinearLayout llUnitActivityUpload;
    @BindView(R.id.ll_weight_activity_upload)
    LinearLayout llWeightActivityUpload;
    @BindView(R.id.et_weight_price_activity_upload)
    EditText etWeightPriceActivityUpload;
    @BindView(R.id.et_unit_des_activity_upload)
    EditText etUnitDesActivityUpload;
    @BindView(R.id.et_type_activity_upload)
    EditText etTypeActivityUpload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeStatBarColor(UploadActivity.this, R.color.trans);
        setContentView(R.layout.activity_upload);
        ButterKnife.bind(this);
        init();

    }

    void init() {
        db = FirebaseFirestore.getInstance();
        db1 = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storageRef = FirebaseStorage.getInstance().getReference();
        categories = new ArrayList<>();
        categories.add("Dairy");
        categories.add("Drink");
        categories.add("Grocery");
        categories.add("Oil");
        categories.add("Preserve");
        categories.add("Sauce");
        categories.add("Sweet");


        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sCategoryActivityUpload.setAdapter(adapter);

        etTypeActivityUpload.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etNameActivityUpload.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @OnClick({R.id.iv_add_item_activity_upload, R.id.btn_activity_upload, R.id.rb_item_activity_upload, R.id.rb_weight_activity_upload})
    public void onClick(View view) {
        if (view.getId() == R.id.iv_add_item_activity_upload) {
            selectImg();
        } else if (view.getId() == R.id.btn_activity_upload) {
            Item item = new Item();
            Map<String, Object> itemMap = new HashMap<>();
            String category, productName, productType;
            category = sCategoryActivityUpload.getSelectedItem().toString();
            productName = etNameActivityUpload.getText().toString();
            productType = etTypeActivityUpload.getText().toString();


            if (rbItemActivityUpload.isChecked()) {
                Item.Unit unit, subPack, pack;
                String uName, sName, pName, weightUnit, des;
                int uNum, sNum, pNum;
                double uPrice, sPrice, pPrice, weight;
                List<String> keyWords;


                uName = etUnitNameActivityUpload.getText().toString();//bottle
                uPrice = Double.parseDouble(etUnitPriceActivityUpload.getText().toString());
                unit = new Item.Unit();
                unit.setName(uName);
                unit.setLeft(0);
                unit.setPrice(uPrice);
                if (etUnitWeightActivityUpload.getText().length() != 0) {
                    weight = Double.parseDouble(etUnitWeightActivityUpload.getText().toString());
                    weightUnit = sWeightsUnitActivityUpload.getSelectedItem().toString();
                    unit.setConsistNum(weight);
                    unit.setConsistName(weightUnit);
                } else {
                    String [] d = etUnitDesActivityUpload.getText().toString().split(" ");
                    unit.setConsistNum(Double.parseDouble(d[0]));
                    unit.setConsistName(d[1]);
                }




                pack = new Item.Unit();
                if (etSubPackNumActivityUpload.getText().length() != 0) {
                    sName = "sub-pack";
                    sNum=Integer.parseInt(etSubPackNumActivityUpload.getText().toString());
                    uNum = Integer.parseInt(etUnitNumActivityUpload.getText().toString());
                    sPrice = Double.parseDouble(etSubPackPriceActivityUpload.getText().toString());
                    subPack = new Item.Unit();
                    subPack.setName(sName);
                    subPack.setLeft(0);
                    subPack.setPrice(sPrice);

                    subPack.setConsistNum(uNum);
                    subPack.setConsistName(uName);

                    pack.setConsistNum(sNum);
                    pack.setConsistName(sName);

                    item.setSubPack(subPack);
                    itemMap.put("sub pack", item.getSubPack());
                }else {
                    uNum = Integer.parseInt(etUnitNumActivityUpload.getText().toString());
                    pack.setConsistNum(uNum);
                    pack.setConsistName(uName);
                }

                pName = "package";
                pNum = Integer.parseInt(etPackNumActivityUpload.getText().toString());
                pPrice = Double.parseDouble(etPackPriceActivityUpload.getText().toString());
                pack.setName(pName);
                pack.setLeft(pNum);
                pack.setPrice(pPrice);




                item.setCategory(category);
                item.setType(productType);
                item.setName(productName);
                item.setUnit(unit);
                item.setPack(pack);
                item.setBy(by);
                item.setLock(true);


                //split every word in three items category, name and type
                keyWords = new ArrayList<>();
                keyWords.add(item.getCategory());
                keyWords.add(item.getName());
                keyWords.add(item.getType());


                itemMap.put("category", item.getCategory());
                itemMap.put("product type", item.getType());
                itemMap.put("product name", item.getName());
                itemMap.put("unit", item.getUnit());
                itemMap.put("pack", item.getPack());
                itemMap.put("by", item.getBy());
                itemMap.put("lock", item.isLock());
                itemMap.put("key words", keyWords);


                uploadImg(itemMap);


            } else if (rbWeightActivityUpload.isChecked()) {
                double price, weightInStock;
                ArrayList<String> keyWords;
                price = Double.parseDouble(etWeightPriceActivityUpload.getText().toString());
                weightInStock = Double.parseDouble(etWeightStockActivityUpload.getText().toString());

                item = new Item();
                item.setType(productType);
                item.setCategory(category);
                item.setName(productName);
                item.setPrice(price);
                item.setWeight(weightInStock);
                item.setBy(by);
                item.setLock(true);
                item.setDes("nothing");

                keyWords = new ArrayList<>();
                keyWords.add(item.getCategory());
                keyWords.add(item.getName());
                keyWords.add(item.getType());

                itemMap = new HashMap<>();
                itemMap.put("category", item.getCategory());
                itemMap.put("product type", item.getType());
                itemMap.put("description", item.getDes());
                itemMap.put("product name", item.getName());
                itemMap.put("price", item.getPrice());
                itemMap.put("weight in stock", item.getWeight());
                itemMap.put("by", item.getBy());
                itemMap.put("lock", item.isLock());
                itemMap.put("key words", keyWords);


                uploadImg(itemMap);
            }

        } else if (view.getId() == R.id.rb_item_activity_upload) {
            by = rbItemActivityUpload.getHint().toString();
            checkFields();
            weights = new ArrayList<>();
            weights.add("ML");
            weights.add("L");
            weights.add("KG");
            weights.add("G");
            adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, weights);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sWeightsUnitActivityUpload.setAdapter(adapter2);

            etUnitNameActivityUpload.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    checkFields();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            etUnitNumActivityUpload.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    checkFields();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            etUnitPriceActivityUpload.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    checkFields();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            etUnitWeightActivityUpload.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() != 0) {
                        etUnitDesActivityUpload.setText("");
                        etSubPackNumActivityUpload.setText("");
                        etSubPackPriceActivityUpload.setText("");
                    }
                    checkFields();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            etUnitDesActivityUpload.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() != 0) {
                        etUnitWeightActivityUpload.setText("");
                    }
                    checkFields();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            etSubPackNumActivityUpload.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() != 0) {
                        etUnitWeightActivityUpload.setText("");
                    }
                    checkFields();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            etSubPackPriceActivityUpload.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() != 0) {
                        etUnitWeightActivityUpload.setText("");
                    }
                    checkFields();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            etPackNumActivityUpload.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    checkFields();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            etPackPriceActivityUpload.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    checkFields();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            llUnitActivityUpload.setVisibility(View.VISIBLE);
            llWeightActivityUpload.setVisibility(View.GONE);

        } else if (view.getId() == R.id.rb_weight_activity_upload) {
            by = rbWeightActivityUpload.getHint().toString();
            checkFields();

            etWeightStockActivityUpload.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    checkFields();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            etWeightPriceActivityUpload.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    checkFields();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            llUnitActivityUpload.setVisibility(View.GONE);
            llWeightActivityUpload.setVisibility(View.VISIBLE);
        }
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            // There are no request codes
            assert result.getData() != null;
            filePath = result.getData().getData();
            checkFields();
            ivAddItemActivityUpload.setImageURI(filePath);
        }
    });

    private void selectImg() {
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //startActivityForResult(intent, RESULT_LOAD_IMAGE);
        someActivityResultLauncher.launch(intent);
    }

    private void uploadImg(Map<String, Object> map) {

        if (filePath != null) {
            storageRef = storageRef.child("User Image").child(System.currentTimeMillis() + "." + getFileExe(filePath));
            uploadTask = storageRef.putFile(filePath);
            uploadTask.continueWithTask((Continuation) task -> {
                if (!task.isComplete()) {
                    throw Objects.requireNonNull(task.getException());
                }
                return storageRef.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                imgUri = Objects.requireNonNull(task.getResult()).toString();
                map.put("img uri", imgUri);
                db.collection("Products")
                        .add(map)
                        .addOnSuccessListener(i -> reload())
                        .addOnFailureListener(e -> Toast.makeText(getBaseContext(), "Upload Failed", Toast.LENGTH_SHORT).show());



            });
        } else {
            Toast.makeText(getBaseContext(), "please select product image", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void changeStatBarColor(AppCompatActivity activity, int color) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(activity, color));
        //???????????????????
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        //???????????????????
    }

    void checkFields() {
        if (filePath != null && etNameActivityUpload.getText().toString().length() != 0 && etTypeActivityUpload.getText().toString().length() != 0) {
            if (rbItemActivityUpload.isChecked() && (etUnitNameActivityUpload.getText().length() != 0 && etUnitNumActivityUpload.getText().length() != 0 && etPackNumActivityUpload.getText().length() != 0 && etPackPriceActivityUpload.getText().length() != 0) && ((etUnitWeightActivityUpload.getText().toString().isEmpty()) != (etUnitDesActivityUpload.getText().toString().isEmpty()))) {
                if ((etSubPackNumActivityUpload.getText().length() != 0) == (etSubPackPriceActivityUpload.getText().length() != 0)) {
                    btnInactiveActivityUpload.setVisibility(View.GONE);
                    btnActivityUpload.setVisibility(View.VISIBLE);
                } else {
                    btnInactiveActivityUpload.setVisibility(View.VISIBLE);
                    btnActivityUpload.setVisibility(View.GONE);
                }
            } else if (rbWeightActivityUpload.isChecked() && (etWeightStockActivityUpload.getText().length() != 0 && etWeightPriceActivityUpload.getText().length() != 0)) {
                btnInactiveActivityUpload.setVisibility(View.GONE);
                btnActivityUpload.setVisibility(View.VISIBLE);
            } else {
                btnInactiveActivityUpload.setVisibility(View.VISIBLE);
                btnActivityUpload.setVisibility(View.GONE);
            }
        } else {
            btnInactiveActivityUpload.setVisibility(View.VISIBLE);
            btnActivityUpload.setVisibility(View.GONE);
        }
    }

    private String getFileExe(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(UploadActivity.this.getContentResolver().getType(uri));
    }

    private void reload() {
        finish();
        startActivity(getIntent());
    }

}
/*db1.child("Products").push().setValue(map).addOnSuccessListener(unused -> reload())
                        .addOnFailureListener(e -> Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show());*/