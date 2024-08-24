package com.example.myfitnessapp.recyclerView;

public class NutrientItem {

    String name;
    float value;

    public NutrientItem(String name,float value){
        this.name=name;
        this.value=value;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
    public float getValue(){
        return value;
    }
    public void setValue(float value){
        this.value=value;
    }
}
