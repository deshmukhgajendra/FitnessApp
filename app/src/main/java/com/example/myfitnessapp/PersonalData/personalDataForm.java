package com.example.myfitnessapp.PersonalData;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import com.example.myfitnessapp.Database.DBHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfitnessapp.Database.DBHelper;
import com.example.myfitnessapp.MainActivity;
import com.example.myfitnessapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class personalDataForm extends AppCompatActivity {


    private TextInputEditText nameEditText, ageEditText, weightEditText, heightEditText;
    RadioGroup radioGroupGender,radioGroupGoal,radioGroupActivity;
    RadioButton radiobuttonGender,radiobuttonGoal,radiobuttonActivity;
    Button submitButton;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    public static String name;
    public static double age;
    public static double height;
    public static double weight;
    public static int goal;
    public  static String selectedActivity,selectedGoal,selectedGender;
    public double TDEE;
    double gender;
    double temp;
    double activityFactor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data_form);


        //DBHelper helper= new DBHelper(this);
        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        nameEditText=findViewById(R.id.nameEditText);
        ageEditText=findViewById(R.id.ageEditText);
        heightEditText=findViewById(R.id.heightEditText);
       weightEditText=findViewById(R.id.weightEditText);
        radioGroupGender=findViewById(R.id.radioGroupGender);
        radioGroupGoal=findViewById(R.id.radioGroupGoal);
        radioGroupActivity=findViewById(R.id.radioGroupActivity);
        submitButton=findViewById(R.id.submitButton);


        radioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                radiobuttonGender=findViewById(checkedId);
            }
        });

        radioGroupGoal.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                radiobuttonGoal=findViewById(checkedId);
            }
        });
        radioGroupActivity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                radiobuttonActivity=findViewById(checkedId);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInput()) {
                    calculateCalorie();
                    saveUserDataToFirestore();
                    Intent i = new Intent(personalDataForm.this, MainActivity.class);
                    startActivity(i);
                }
            }
        });


    }

    public void saveUserDataToFirestore(){

        String weight = weightEditText.getText().toString().trim();
        String height = heightEditText.getText().toString().trim();
        String age = ageEditText.getText().toString().trim();
        String Aimgoal = radiobuttonGoal.getText().toString();
        String gender = radiobuttonGender.getText().toString();
        String activityLevel = radiobuttonActivity.getText().toString();


        if (age.isEmpty() || height.isEmpty() || weight.isEmpty() || gender.isEmpty() || Aimgoal.isEmpty() || activityLevel.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }


        Map<String, Object> personalData = new HashMap<>();
        personalData.put("age", age);
        personalData.put("weight", weight);
        personalData.put("height", height);
        personalData.put("gender", gender);
        personalData.put("Aimgoal", Aimgoal);
        personalData.put("activityLevel", activityLevel);
        personalData.put("goal", goal);


        String userId = mAuth.getCurrentUser().getUid();


        DocumentReference docRef = db.collection("users")
                .document(userId)
                .collection("personalData")
                .document(userId);


        docRef.set(personalData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(personalDataForm.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("Firestore", "Error saving data: ", task.getException());
                    Toast.makeText(personalDataForm.this, "Failed to save data", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private boolean validateInput() {
        selectedGender = radiobuttonGender != null ? radiobuttonGender.getText().toString() : null;
        selectedGoal = radiobuttonGoal != null ? radiobuttonGoal.getText().toString() : null;
        selectedActivity = radiobuttonActivity != null ? radiobuttonActivity.getText().toString() : null;

        if (selectedGender == null || selectedGoal == null || selectedActivity == null) {
            Toast.makeText(getApplicationContext(), "Please select all options", Toast.LENGTH_LONG).show();
            return false;
        }

        try {
            age = Double.parseDouble(ageEditText.getText().toString());
            height = Double.parseDouble(heightEditText.getText().toString());
            weight = Double.parseDouble(weightEditText.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(getApplicationContext(), "Please enter valid numbers", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }


    public int calculateCalorie(){

        RadioGroup radioGroupActivity = findViewById(R.id.radioGroupActivity);
        int ActivityRadioButtonId = radioGroupActivity.getCheckedRadioButtonId();
        RadioButton ActivityRadioButton = findViewById(ActivityRadioButtonId);
        if (ActivityRadioButtonId != -1){

            selectedActivity = ActivityRadioButton.getText().toString();
        }else {
            Toast.makeText(getApplicationContext(),"select one option",Toast.LENGTH_LONG).show();
        }


        // radio button for check goal

        RadioGroup radioGroupGoal = findViewById(R.id.radioGroupGoal);
        int selectedRadioButtonId = radioGroupGoal.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
        if (selectedRadioButtonId != -1){

            selectedGoal = selectedRadioButton.getText().toString();
        }else {
            Toast.makeText(getApplicationContext(),"select one option",Toast.LENGTH_LONG).show();
        }


        // radio button for check gender

        RadioGroup radioGroupGender= findViewById(R.id.radioGroupGender);
        int genderRadioButtonId = radioGroupGender.getCheckedRadioButtonId();
        RadioButton genderRadioButton = findViewById(genderRadioButtonId);
        if (genderRadioButtonId != -1){
            selectedGender = genderRadioButton.getText().toString();
        }else {
            Toast.makeText(getApplicationContext(),"select one option",Toast.LENGTH_LONG).show();
        }


        if (selectedGender != null && selectedGender.equals("Male")){
            gender=+5.0;
        }else if (selectedGender != null && selectedGender.equals("Female")){
            gender=-161;
        }
        String w = weightEditText.getText().toString();
        weight = Double.parseDouble(w);
        String h = heightEditText.getText().toString();
        height = Double.parseDouble(h);
        String a = ageEditText.getText().toString();
        age = Double.parseDouble(a);

        if (selectedActivity != null) {
            if (selectedActivity.equals("Sedentary (little or no exercise)")) {
                activityFactor = 1.2;
            } else if (selectedActivity.equals("Lightly active (light exercise/sports 1-3 days/week)")) {
                activityFactor = 1.375;
            } else if (selectedActivity.equals("Moderately active (moderate exercise/sports 3-5 days/week)")) {
                activityFactor = 1.55;
            } else if (selectedActivity.equals("Very active (hard exercise/sports 6-7 days a week)")) {
                activityFactor = 1.725;
            } else if (selectedActivity.equals("Extremely active (very hard exercise/sports & physical job or 2x training)")) {
                activityFactor = 1.9;
            }
        }

        double BMR=0;

        BMR = 10.0 * weight + 6.25 * height - 5.0 * age + gender;

        TDEE = BMR * activityFactor;
        if (selectedGoal != null ){
            temp = (20.0 / 100.0) * TDEE;
            if (selectedGoal .equals("Weight Gain")){
                goal = (int) (TDEE + temp);
            } else if (selectedGoal . equals("Weight Loss")) {

                goal = (int)( TDEE - temp);
            }else {

                goal =(int) (TDEE);
            }
        }
        return goal;
   }


    }
