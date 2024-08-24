package com.example.myfitnessapp.Api;

import java.util.List;

public class NutritionixResponse {

    private List<Food> foods;

    public List<Food> getFoods() {
        return foods;
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
    }

    public static class Food {

        private String food_name;
        private float nf_calories;
        private List<Nutrient> full_nutrients;

        public String getFoodName() {
            return food_name;
        }

        public void setFoodName(String food_name) {
            this.food_name = food_name;
        }

        public float getCalories() {
            return nf_calories;
        }

        public void setCalories(float nf_calories) {
            this.nf_calories = nf_calories;
        }

        public List<Nutrient> getFullNutrients() {
            return full_nutrients;
        }

        public void setFullNutrients(List<Nutrient> full_nutrients) {
            this.full_nutrients = full_nutrients;
        }


        public static class Nutrient {

            private int attr_id;
            private float value;

            public int getAttrId() {
                return attr_id;
            }

            public void setAttrId(int attr_id) {
                this.attr_id = attr_id;
            }

            public float getValue() {
                return value;
            }

            public void setValue(float value) {
                this.value = value;
            }
        }

    }
}
