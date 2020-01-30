package com.example.coffeeforeglu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrderListActivity extends AppCompatActivity {

    String TAG = "fireStore";
    RecyclerView recyclerView;
    ProgressBar progressBar;
    FirebaseFirestore firestoreInstance;
    List<DataModel> modelArrayList;
    OrderListAdapter adapter;
    FloatingActionButton buyCoffee;
    TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        init();
        setupRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        readDatabase();
    }

    private void init(){
        modelArrayList = new ArrayList<>();
        adapter = new OrderListAdapter();
        firestoreInstance = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recycleViewOrderList);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        buyCoffee = findViewById(R.id.buyCoffee);
        emptyView = findViewById(R.id.emptyView);

        buyCoffee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderListActivity.this, PlaceOrderActivity.class));
            }
        });

    }
    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void readDatabase(){
        if(firestoreInstance!=null){
            firestoreInstance.collection("purchase_list").orderBy("date", Query.Direction.ASCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot documentSnapshots) {
                    Log.d(TAG, "Successful: " + documentSnapshots.toString());
                    if (documentSnapshots.isEmpty()) {
                        Log.d(TAG, "onSuccess: LIST EMPTY");
                        progressBar.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                        return;
                    } else {
                        modelArrayList = documentSnapshots.toObjects(DataModel.class);

                        adapter.setItems(modelArrayList);
                        progressBar.setVisibility(View.GONE);
                        emptyView.setVisibility(View.GONE);
                        Log.d(TAG, "onSuccess: " + modelArrayList);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error", e);
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Internet connection required..",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}
