package com.example.we_play;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.we_play.GridView.RecyclerViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class food_page extends AppCompatActivity {

    RecyclerView foodview;
    String big_city;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<String> img_link = new ArrayList<>();
    ArrayList<String> title = new ArrayList<>();
    ArrayList<String> location = new ArrayList<>();
    ArrayList<String> rating = new ArrayList<>();
    TextView textView;
    ImageButton img_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_page);


        getdata();

        textView = findViewById(R.id.title_food);
        foodview = findViewById(R.id.food_recycle);
        img_btn = findViewById(R.id.img_btn);

        textView.setText(big_city);

        img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        CollectionReference collectionReference = db.collection("관광정보").document(big_city).collection("로컬 맛집");

        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for(DocumentSnapshot documentSnapshot : task.getResult()){
                        title.add(documentSnapshot.getId());
                        String img_url = documentSnapshot.get("thumbnail_url").toString();
                        String img_url2 = "http://"+ img_url.substring(8);
                        img_link.add(img_url2);
                        location.add(documentSnapshot.get("위치정보").toString());
                        try {
                            rating.add(documentSnapshot.get("rating").toString());
                        }catch (Exception e){
                            rating.add("5");
                        }

                    }
                }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("aa","test1");
            }
        }).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                RecyclerViewAdapter mAdapter = new RecyclerViewAdapter(img_link,title,location,getApplicationContext(),rating,big_city,"로컬 맛집");
                foodview.setAdapter(mAdapter);
                foodview.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                mAdapter.notifyDataSetChanged();
            }
        });
    }


    public void getdata(){
        Intent intent = getIntent();
        big_city = intent.getStringExtra("도시");
    }
}