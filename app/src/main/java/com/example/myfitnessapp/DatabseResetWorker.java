package com.example.myfitnessapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.example.myfitnessapp.Database.DBHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class DatabseResetWorker extends Worker {

    private static final String TAG = "DatabaseResetWorker";
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    DBHelper helper= new DBHelper(getApplicationContext());

    public DatabseResetWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG, "DatabaseResetWorker is running");


        Context context = getApplicationContext();


        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        storeHistoryToFirebase(helper);
        try {

            Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
            if (cursor.moveToFirst()) {
                do {
                    String tableName = cursor.getString(0);
                    if (!tableName.equals("android_metadata") && !tableName.equals("sqlite_sequence")) {

                        db.execSQL("DROP TABLE IF EXISTS " + tableName);
                        Log.d(TAG, "Dropped table: " + tableName);
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        } finally {

            dbHelper.close();
        }

        return Result.success();
    }

    public void storeHistoryToFirebase(DBHelper helper) {
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String userId = mAuth.getCurrentUser().getUid();
        Map<String, Float> combinedNutrientValues = helper.getCombinedNutrientValues();

        db.collection("users")
                .document(userId)
                .collection("history")
                .document(currentDate)  // Document ID is the date
                .set(combinedNutrientValues)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "History data stored successfully for " + currentDate);
                        } else {
                            Log.e(TAG, "Failed to store history data for " + currentDate, task.getException());
                        }
                    }
                });
    }

}
