package com.example.myfitnessapp.Api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface apiset {

    @Headers({
            "x-app-id: 7012e6ae",
            "x-app-key:16efec930d2eecbe7c620a7457f3eb3e",
            "Content-Type: application/json"
    })
    @POST("v2/natural/nutrients")
    Call<NutritionixResponse> getNutrients(@Body NutritionixRequest request);
}
