package com.example.myfitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfitnessapp.Api.NutritionixRequest;
import com.example.myfitnessapp.Api.NutritionixResponse;
import com.example.myfitnessapp.Api.apiset;
import com.example.myfitnessapp.Database.DBHelper;
import com.example.myfitnessapp.History.History;
import com.example.myfitnessapp.authentication.login;
import com.example.myfitnessapp.recyclerView.NutrientItem;
import com.example.myfitnessapp.recyclerView.RecyclerViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.color.DynamicColors;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.example.myfitnessapp.PersonalData.personalDataForm;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.bumptech.glide.Glide;

import org.w3c.dom.Document;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String BASE_URL = "https://trackapi.nutritionix.com/";
    private static final String TAG = MainActivity.class.getSimpleName();
    private apiset api;
    private NutritionixRequest request;
    private String query;
    private DBHelper helper;
    private String tableName;
    private CardView cardView;
    private ProgressBar progressBar;
    private TextView baseGoalEditText, foodCaloriesEditText,msgTextView;
    private RecyclerView recyclerView;
    private List<NutrientItem> nutrientList;
    private RecyclerViewAdapter adapter;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private int calories;
    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton floatingActionButton;
    private ConstraintLayout queryBox;
    private EditText queryEditText;
    private Button addFoodButton;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DynamicColors.applyToActivitiesIfAvailable(getApplication());
        setContentView(R.layout.activity_main);

        cardView = findViewById(R.id.cardView);
        progressBar = findViewById(R.id.progressbar);
        baseGoalEditText = findViewById(R.id.baseGoalEditText);
        foodCaloriesEditText = findViewById(R.id.foodCaloriesEditText);
        recyclerView = findViewById(R.id.recyclerView);
        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.navigationView);
        msgTextView=findViewById(R.id.msgTextView);
        toolbar = findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        queryBox = findViewById(R.id.queryBox);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        setSupportActionBar(toolbar);

        FirebaseUser user = mAuth.getCurrentUser();
         id=user.getUid();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.OpenDrawer, R.string.closeDrawer);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        // Initialize list and adapter
        nutrientList = new ArrayList<>();
        adapter = new RecyclerViewAdapter(nutrientList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Initialize DBHelper
        helper = new DBHelper(this);

        floatingActionButton.setOnClickListener(view -> popupView());

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.historyButton:
                   Intent i= new Intent(MainActivity.this, History.class);
                   startActivity(i);
                    return true;
                case R.id.workoutButton:
                    Toast.makeText(MainActivity.this, "Workout button selected", Toast.LENGTH_LONG).show();
                    return true;
                default:
                    return false;
            }
        });
        loadInitialData();
        updateNavigationHeader();
        sheduleDailyDatabaseReset();
    }

    private void loadInitialData() {
        Map<String, Float> combinedNutrientValues = helper.getCombinedNutrientValues();
        Float temp = combinedNutrientValues.get("Energy");
        // Log.d(TAG, "displayCombinedNutrientValues: "+temp);

        if (temp != null) {
            calories = Math.round(temp);
            Log.d(TAG, "value of calories : "+ calories);
        } else {
           // Log.e(TAG, "Energy value is null");
            calories = 0;
        }
        nutrientList.clear();
        for (Map.Entry<String, Float> entry : combinedNutrientValues.entrySet()) {
            nutrientList.add(new NutrientItem(entry.getKey(), entry.getValue()));
        }
        displayProgressBar();
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    public void popupView() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.querylayout, null);

        queryEditText = view.findViewById(R.id.queryEditText);
        addFoodButton = view.findViewById(R.id.addFoodButton);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(view);
        AlertDialog dialog = alert.create();
        dialog.show();

        addFoodButton.setOnClickListener(view1 -> {
            query = queryEditText.getText().toString();
            if (query.isEmpty()) {
                Toast.makeText(MainActivity.this, "Query cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }
            request = new NutritionixRequest(query);
            fetchData(request, query);
            dialog.dismiss();
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homeButton:
                Toast.makeText(getApplicationContext(), "Home button is pressed", Toast.LENGTH_LONG).show();
                break;
            case R.id.settingButton:
                Toast.makeText(getApplicationContext(), "Settings button is pressed", Toast.LENGTH_LONG).show();
                break;
            case R.id.shareButton:
                Toast.makeText(getApplicationContext(), "Share button is pressed", Toast.LENGTH_LONG).show();
                break;
            case R.id.aboutButton:
                Toast.makeText(getApplicationContext(), "About button is pressed", Toast.LENGTH_LONG).show();
                break;
            case R.id.logoutButton:
                FirebaseAuth.getInstance().signOut();
                FirebaseUser user = mAuth.getCurrentUser();
                if (user == null) {
                    Toast.makeText(MainActivity.this, "sign out sucessfull", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(MainActivity.this, login.class);
                    startActivity(i);
                    finish();
                }
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void fetchData(NutritionixRequest request, String query) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(apiset.class);

        Call<NutritionixResponse> call = api.getNutrients(request);
        call.enqueue(new Callback<NutritionixResponse>() {
            @Override
            public void onResponse(Call<NutritionixResponse> call, Response<NutritionixResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    NutritionixResponse nutritionixResponse = response.body();
                    tableName = "table_" + query.toLowerCase().replaceAll(" ", "_");

                    saveResponseToDatabase(nutritionixResponse, tableName);
                    displayCombinedNutrientValues();

                    for (NutritionixResponse.Food.Nutrient nutrient : nutritionixResponse.getFoods().get(0).getFullNutrients()) {

                    }
                } else {
                    Log.e(TAG, "Failed to fetch data: " + response.message());
                    Log.e(TAG, "Error code: " + response.code());
                    if (response.errorBody() != null) {
                        try {
                            Log.e(TAG, "Error body: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<NutritionixResponse> call, Throwable t) {
                Log.e(TAG, "Failed to communicate with server: " + t.getMessage());
            }
        });
    }

    public void saveResponseToDatabase(NutritionixResponse response, String query) {
        helper.createTableForFood(query);
        for (NutritionixResponse.Food food : response.getFoods()) {
            for (NutritionixResponse.Food.Nutrient nutrient : food.getFullNutrients()) {
                helper.addNutrientsToTable(query, nutrient.getAttrId(), nutrient);
            }
        }
    }

    public  void displayCombinedNutrientValues() {
        Map<String, Float> combinedNutrientValues = helper.getCombinedNutrientValues();
        Float temp = combinedNutrientValues.get("Energy");


        if (temp != null) {
            calories = Math.round(temp);
            Log.d(TAG, "value of calories : "+ calories);
        } else {
            Log.e(TAG, "Energy value is null");
            calories = 0;  // Set a default value or handle accordingly
        }

        nutrientList.clear();
        for (Map.Entry<String, Float> entry : combinedNutrientValues.entrySet()) {
            nutrientList.add(new NutrientItem(entry.getKey(), entry.getValue()));
        }
        displayProgressBar();
        msgTextView.setVisibility(View.GONE);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    public void displayProgressBar() {
        DocumentReference docRef = db.collection("users")
                .document(id)
                .collection("personalData")
                .document(id);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {


                        Long goalValueLong = document.getLong("goal");

                        if (goalValueLong != null) {

                            String goalString = String.valueOf(goalValueLong);
                            baseGoalEditText.setText(goalString);


                            progressBar.setMax(goalValueLong.intValue());
                            progressBar.setProgress(calories);
                        } else {
                            Log.d(TAG, "Goal value is null.");
                        }


                        foodCaloriesEditText.setText(String.valueOf(calories));

//                        Log.d(TAG, "onComplete: calories=" + calories);
//                        Log.d(TAG, "onComplete: goal=" + goalValueLong);
                    } else {
                        Log.d("Firestore", "No such document");
                    }
                } else {
                    Log.d("Firestore", "get failed with ", task.getException());
                }
            }
        });
    }

    public void updateNavigationHeader() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            db.collection("users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String name = document.getString("name");
                            String email = document.getString("email");
                            String profileImageUrl = document.getString("profileImageUrl");

                            View headerView = navigationView.getHeaderView(0);
                            TextView navUserName = headerView.findViewById(R.id.nameTextView);
                            TextView navUserEmail = headerView.findViewById(R.id.emailTextView);
                            ImageView navUserImage = headerView.findViewById(R.id.userImageView);

                            navUserName.setText(name);
                            navUserEmail.setText(email);
                            Glide.with(MainActivity.this).load(profileImageUrl).into(navUserImage);
                        }
                    }
                }
            });
        }
    }

    public void sheduleDailyDatabaseReset() {
        // getting the current time
        Calendar currentTime = Calendar.getInstance();
        Calendar targetTime = Calendar.getInstance();
        targetTime.set(Calendar.HOUR_OF_DAY, 0);
        targetTime.set(Calendar.MINUTE, 0);
        targetTime.set(Calendar.SECOND, 0);


        if (targetTime.before(currentTime)) {
            targetTime.add(Calendar.DAY_OF_MONTH, 1);
        }


        long initialDelay = targetTime.getTimeInMillis() - currentTime.getTimeInMillis();


        PeriodicWorkRequest dailyWorkRequest = new PeriodicWorkRequest.Builder(DatabseResetWorker.class, 1, TimeUnit.DAYS)
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork("DailyDatabaseReset", ExistingPeriodicWorkPolicy.REPLACE, dailyWorkRequest);
    }
}
