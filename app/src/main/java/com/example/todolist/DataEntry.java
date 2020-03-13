package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.provider.Settings.NameValueTable.VALUE;
import static java.sql.Types.TIMESTAMP;

public class DataEntry extends AppCompatActivity {

    Button saveButton;
    EditText titleText,thoughtText;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "DataEntry";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_entry);

        saveButton = findViewById(R.id.saveButton);
        titleText = findViewById(R.id.Title);
        thoughtText = findViewById(R.id.idea);
        try{
            Bundle extras = getIntent().getExtras();
            String title = extras.get("title").toString();
            String thought = extras.get("thought").toString();
            String date = extras.get("date").toString();
            titleText.setText(title);
            thoughtText.setText(thought);
            updateIt(title);

        }catch(Exception e){
        saveIt();
    }
    }

    public void updateIt(final String title){
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(titleText.getText() == null || thoughtText.getText() == null){
                    Toast.makeText(DataEntry.this,"Please Enter all Data",Toast.LENGTH_SHORT)
                            .show();

                }else{
                    DocumentReference journalRef = db.collection("Notes")
                            .document(title);
                    Map<String,Object> map = new HashMap<>();
                    titleText.setEnabled(false);
                    map.put("thought",thoughtText.getText().toString());
                    map.put("date",Calendar.getInstance().getTime().toString());
                    journalRef.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(DataEntry.this, "Updated", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("POPDOWN", "onFailure: ");
                        }
                    });
                }
                Intent intent = new Intent(DataEntry.this,MainActivity.class);
                startActivity(intent);

            }
        });
    }

    public void saveIt(){
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(titleText.getText() == null || thoughtText.getText() == null){
                    Toast.makeText(DataEntry.this,"Please Enter all Data",Toast.LENGTH_SHORT)
                            .show();
                }else{
                    DocumentReference journalRef = db.collection("Notes")
                            .document(titleText.getText().toString());
                    String title = titleText.getText().toString();
                    String thought = thoughtText.getText().toString();
                    Map<String,String> map = new HashMap<>();
                    map.put("title",title);
                    map.put("thought",thought);
                    map.put("date", Calendar.getInstance().getTime().toString());
                    journalRef
                            .set(map)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(DataEntry.this,"Success",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(DataEntry.this,MainActivity.class);
                                    startActivity(intent);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: " + e.toString());
                        }
                    });
                }
            }
        });
    }

}
