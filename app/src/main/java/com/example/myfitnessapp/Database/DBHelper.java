package com.example.myfitnessapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.myfitnessapp.PersonalData.personalDataForm;

import androidx.annotation.Nullable;

import com.example.myfitnessapp.Api.NutritionixResponse;

import java.util.HashMap;
import java.util.Map;

public class DBHelper extends SQLiteOpenHelper {

    public static String db_name="db";
    public static String table_name = "nutritionTable";
    public static String personaData_Table="personaldataTable";
    public static String column_NutrientID ="nutrientID";
    public static String column_NutrientName ="nutrientName";
    public static String column_NutrientValue="value";
    public static String column_Age="age";
    public static String column_Weight="weight";
    public static String column_Height="Height";
    public static String column_gender="gender";
    public static String column_Goal="Goal";
    public static String column_activitylevel="activitylevel";



    public DBHelper(@Nullable Context context) {
        super(context, db_name, null, 1);
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createTableForPersonalData(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ table_name);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + personaData_Table);
        onCreate(sqLiteDatabase);
    }

    public void createTableForFood(String tableName){
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        String query="CREATE TABLE IF NOT EXISTS " + tableName + " ("
                + column_NutrientID + " INTEGER PRIMARY KEY , "
                + column_NutrientName + " TEXT, "
                + column_NutrientValue + " REAL)";
        sqLiteDatabase.execSQL(query);
    }

    public void createTableForPersonalData(SQLiteDatabase sqLiteDatabase){
        String query = "CREATE TABLE IF NOT EXISTS " + personaData_Table + " ("
                + column_Age + " TEXT, "
                + column_Weight + " REAL, "
                + column_Height + " REAL, "
                + column_gender + " TEXT, "
                + column_Goal + " TEXT, "
                + column_activitylevel + " TEXT)";
        sqLiteDatabase.execSQL(query);

    }

    public void adduserDataToTable(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(column_Age, personalDataForm.age);
        values.put(column_Height, personalDataForm.height);
        values.put(column_Weight, personalDataForm.weight);
        values.put(column_gender, personalDataForm.selectedGender);
        values.put(column_Goal, personalDataForm.selectedGoal);
        values.put(column_activitylevel, personalDataForm.selectedActivity);
        sqLiteDatabase.insert(personaData_Table, null, values);
    }
    public void addNutrientsToTable(String tableName, int nutrientID ,NutritionixResponse.Food.Nutrient nutrient){
        String nutrientName=NutrientMapping.getNutrientName(nutrientID);
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues values= new ContentValues();
        values.put(column_NutrientID,nutrient.getAttrId());
        values.put(column_NutrientName,nutrientName);
        values.put(column_NutrientValue,String.format("%.3f", nutrient.getValue()));
       // sqLiteDatabase.insert(tableName,null,values);

      sqLiteDatabase.insertWithOnConflict(tableName, null, values, SQLiteDatabase.CONFLICT_REPLACE);

    }

    public Map<String, Float> getCombinedNutrientValues() {
        Map<String, Float> nutrientValues = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'AND name != 'personaldataTable'", null);
        if (cursor.moveToFirst()) {
            do {
                String tableName = cursor.getString(0);
                if (!tableName.equals("android_metadata") && !tableName.equals("sqlite_sequence")) {
                    Cursor tableCursor = db.rawQuery("SELECT " + column_NutrientName + ", " + column_NutrientValue + " FROM " + tableName, null);
                    if (tableCursor.moveToFirst()) {
                        do {
                            String nutrientName = tableCursor.getString(tableCursor.getColumnIndex(column_NutrientName));
                            float nutrientValue = tableCursor.getFloat(tableCursor.getColumnIndex(column_NutrientValue));
                            if (nutrientValues.containsKey(nutrientName)) {
                                nutrientValues.put(nutrientName, nutrientValues.get(nutrientName) + nutrientValue);
                            } else {
                                nutrientValues.put(nutrientName, nutrientValue);
                            }
                        } while (tableCursor.moveToNext());
                    }
                    tableCursor.close();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return nutrientValues;
    }
}
