package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference = db.collection("Notes");
    private static final String TAG = "MainActivity";
    //vars
    RecyclerView recyclerView ;
     static ArrayList<String> titleList = new ArrayList<>();
     static ArrayList<String> thoughtList = new ArrayList<>();
     static ArrayList<String> dateList = new ArrayList<>();
     static RecyclerViewAdapter adapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.deleteAll :

                Log.d("deleteAll", "onOptionsItemSelected: ");
                db.collection("Notes")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    int i =0;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        db.collection("Notes").document(titleList.get(i)).delete();
                                        i++;
                                    }
                                    titleList.clear();
                                    thoughtList.clear();
                                    dateList.clear();
                                    recyclerView.removeAllViewsInLayout();
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });




                return false;
            default:return  false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button newButton = findViewById(R.id.newButton);
        recyclerView = findViewById(R.id.recyclerView);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), DataEntry.class);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        recyclerView.removeAllViewsInLayout();
        titleList.clear();
        thoughtList.clear();
        dateList.clear();
        db.collection("Notes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG,document.getId() + " => " + document.getData());
                                titleList.add((String)document.get("title"));
                                thoughtList.add((String)document.get("thought"));
                                dateList.add((String)document.get("date"));
                            }
                            initRecyclerView();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


    }

/*
    @Override
    protected void onRestart() {
        super.onRestart();
        recyclerView.removeAllViewsInLayout();
        titleList.clear();
        thoughtList.clear();
        dateList.clear();
        db.collection("Notes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG,document.getId() + " => " + document.getData());
                                titleList.add((String)document.get("title"));
                                thoughtList.add((String)document.get("thought"));
                                dateList.add((String)document.get("date"));
                            }
                            initRecyclerView();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        initRecyclerView();
    }
*/

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview");
        adapter = new RecyclerViewAdapter(titleList,thoughtList,dateList,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }




}
