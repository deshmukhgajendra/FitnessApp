package com.example.myfitnessapp.History;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfitnessapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class History extends AppCompatActivity {

    String query;
    FloatingActionButton floatingActionButton;
    TextView emptyTextView,resultTextView;
    RecyclerView historyRecyclerView;
    FirebaseFirestore db;
    String userID;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    // Get the currently signed-in user
    FirebaseUser currentUser = auth.getCurrentUser();
//    List<HistoryItem> nutrientList;
//    Map<String, Object> nutrients;
//    HistoryRecyclerAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        floatingActionButton=findViewById(R.id.floatingActionButton);
        emptyTextView=findViewById(R.id.emptyTextView);
        resultTextView=findViewById(R.id.resultTextView);
        db=FirebaseFirestore.getInstance();
        userID=currentUser.getUid();
//        nutrientList= new ArrayList<>();
//        adapter= new HistoryRecyclerAdapter(nutrientList);
//        historyRecyclerView.setAdapter(adapter);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popuplayout();
            }
        });

    }
   public void popuplayout(){
       LayoutInflater inflater= getLayoutInflater();
       View view =inflater.inflate(R.layout.historyquerylayout,null);

       DatePicker datePicker= view .findViewById(R.id.datePicker);
       Button doneButton = view.findViewById(R.id.doneButton);

       AlertDialog.Builder alert= new AlertDialog.Builder(this);
       alert.setView(view);
       AlertDialog dialog= alert.create();
       dialog.show();

       doneButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               int day = datePicker.getDayOfMonth();
               int month = datePicker.getMonth();
               int year = datePicker.getYear();


               String formattedDay = String.format("%02d", day);
               String formattedMonth = String.format("%02d", month + 1);

               query = formattedDay + "-" + formattedMonth + "-" + year;

               fetchDocumentsFromFirebase();
             //  Toast.makeText(getApplicationContext(), formattedDate, Toast.LENGTH_LONG).show();
               dialog.dismiss();

           }
       });

   }

   public void fetchDocumentsFromFirebase(){

        db.collection("users").document(userID).collection("history").document(query)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            DocumentSnapshot documentSnapshot = task.getResult();

                            if (documentSnapshot.exists()) {
                                Map<String, Object> dataMap = documentSnapshot.getData();


                                if (dataMap != null) {

                                    StringBuilder resultBuilder = new StringBuilder();

                                    for (Map.Entry<String, Object> entry : dataMap.entrySet()) {

                                        resultBuilder.append(entry.getKey()).append(": ").append(entry.getValue().toString()).append("\n");
                                    }


                                    resultTextView.setText(resultBuilder.toString());
                                    emptyTextView.setVisibility(View.GONE);
                                    //     HistoryItem historyItem = documentSnapshot.toObject(HistoryItem.class);
//                                if (historyItem != null) {
//                                    nutrientList.clear();  // Clear the old data
//                                    nutrientList.add(historyItem);  // Add the new data
//
//                                    // Notify the adapter of the data change
//                                    adapter.notifyDataSetChanged();
//                                } else {
//                                    Log.d("Firestore", "No such document");
//                                }
//                            }
//                                else {
//                                Log.d("Firestore", "No such document");
                                }

                            }
                        }
                    }

                });


                }
}