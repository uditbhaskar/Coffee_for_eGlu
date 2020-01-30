package com.example.coffeeforeglu;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class PlaceOrderActivity extends AppCompatActivity implements View.OnClickListener,
        RadioGroup.OnCheckedChangeListener {

    final double LATEE_PRICE = 50.0;
    final double ESPRESSO_PRICE = 75;
    final double CAPUCCINO_PRICE = 110;
    final double REGULAR_FACTOR = 1.1;
    final double MEDIUM_FACTOR = 2.5;
    final double LARGE_FACTOR = 3.1;
    String TAG = "fireStore";

    FirebaseFirestore firestoreInstance;

    EditText et_nameEditText;
    RadioGroup rg_coffeeRadioGroup;
    RadioButton rb_coffee_type;
    Button bn_regularSize;
    Button bn_mediumSize;
    Button bn_largeSize;
    TextView tv_totalAmount;
    Button bn_placeOrder;
    ProgressBar progressBar;
    private double coffeePrice = ESPRESSO_PRICE;
    private double priceFactor = 0.0;
    private double finalPrice = 0.0;
    private String selectedSize = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        firestoreInstance = FirebaseFirestore.getInstance();
        init();
        listeners();
        calculateFinalPrice();
    }

    private void init() {
        et_nameEditText = findViewById(R.id.nameEditText);
        rg_coffeeRadioGroup = findViewById(R.id.coffeeRadioGroup);
        bn_regularSize = findViewById(R.id.regularSize);
        bn_mediumSize = findViewById(R.id.mediumSize);
        bn_largeSize = findViewById(R.id.largeSize);
        tv_totalAmount = findViewById(R.id.totalAmount);
        bn_placeOrder = findViewById(R.id.placeOrder);
        progressBar = findViewById(R.id.progressBar);
    }

    private void listeners() {
        bn_mediumSize.setOnClickListener(this);
        bn_regularSize.setOnClickListener(this);
        bn_largeSize.setOnClickListener(this);
        rg_coffeeRadioGroup.setOnCheckedChangeListener(this);
        bn_placeOrder.setOnClickListener(this);
        bn_regularSize.performClick();
    }

    private RadioButton getCoffeeType() {
        int checkedId = rg_coffeeRadioGroup.getCheckedRadioButtonId();
        rb_coffee_type = findViewById(checkedId);
        if (rb_coffee_type != null) {
            return rb_coffee_type;
        }
        return null;
    }

    private void addToDatabase() {
        bn_placeOrder.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        long timeStamp = System.currentTimeMillis();
        String name = et_nameEditText.getText().toString().trim();
        String type = getCoffeeType().getText().toString().trim();
        String size = selectedSize;
        String total_price = tv_totalAmount.getText().toString().trim();

        if (firestoreInstance != null) {
            firestoreInstance.collection("purchase_list").add(new DataModel(timeStamp, name, type, size, total_price))
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(getApplicationContext(), "Order is Placed.", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            //Finishing Activity
                            progressBar.setVisibility(View.GONE);
                            bn_placeOrder.setVisibility(View.VISIBLE);

                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Unable to place Order.", Toast.LENGTH_SHORT).show();
                            Log.w(TAG, "Error adding document", e);
                            progressBar.setVisibility(View.GONE);
                            bn_placeOrder.setVisibility(View.VISIBLE);
                        }
                    });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.regularSize: {
                selectedSize = bn_regularSize.getText().toString();
                priceFactor = REGULAR_FACTOR;
                calculateFinalPrice();
                bn_regularSize.setBackgroundResource(R.drawable.button_clicked);
                bn_mediumSize.setBackgroundResource(R.drawable.button_unclicked);
                bn_largeSize.setBackgroundResource(R.drawable.button_unclicked);

                bn_regularSize.setTextColor(getResources().getColor(R.color.white));
                bn_mediumSize.setTextColor(getResources().getColor(R.color.black));
                bn_largeSize.setTextColor(getResources().getColor(R.color.black));
                break;
            }
            case R.id.mediumSize: {
                selectedSize = bn_mediumSize.getText().toString();
                priceFactor = MEDIUM_FACTOR;
                calculateFinalPrice();
                bn_regularSize.setBackgroundResource(R.drawable.button_unclicked);
                bn_mediumSize.setBackgroundResource(R.drawable.button_clicked);
                bn_largeSize.setBackgroundResource(R.drawable.button_unclicked);

                bn_mediumSize.setTextColor(getResources().getColor(R.color.white));
                bn_regularSize.setTextColor(getResources().getColor(R.color.black));
                bn_largeSize.setTextColor(getResources().getColor(R.color.black));
                break;
            }
            case R.id.largeSize: {
                selectedSize = bn_largeSize.getText().toString();
                priceFactor = LARGE_FACTOR;
                calculateFinalPrice();
                bn_regularSize.setBackgroundResource(R.drawable.button_unclicked);
                bn_mediumSize.setBackgroundResource(R.drawable.button_unclicked);
                bn_largeSize.setBackgroundResource(R.drawable.button_clicked);

                bn_largeSize.setTextColor(getResources().getColor(R.color.white));
                bn_regularSize.setTextColor(getResources().getColor(R.color.black));
                bn_mediumSize.setTextColor(getResources().getColor(R.color.black));

                break;
            }
            case R.id.placeOrder: {
                if (getCoffeeType() != null && !et_nameEditText.getText().toString().isEmpty() && selectedSize != null) {
                    addToDatabase();
                } else {
                    Toast.makeText(this, "Wrong Input", Toast.LENGTH_SHORT).show();
                }
                break;
            }

        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        if (checkedId == R.id.coffee_expresso) {
//75
            coffeePrice = ESPRESSO_PRICE;
            calculateFinalPrice();
        } else if (checkedId == R.id.coffee_capetuno) {
//110
            coffeePrice = CAPUCCINO_PRICE;
            calculateFinalPrice();
        } else if (checkedId == R.id.coffee_latte) {
//50
            coffeePrice = LATEE_PRICE;
            calculateFinalPrice();
        }
    }

    @SuppressLint("DefaultLocale")
    private void calculateFinalPrice() {
        finalPrice = coffeePrice * priceFactor;
        tv_totalAmount.setText(String.format("%.2f", finalPrice));
    }

}
