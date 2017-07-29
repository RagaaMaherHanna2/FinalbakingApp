package com.example.marian.finalbakingapp.api;

import com.google.gson.JsonArray;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Marian on 7/29/2017.
 */

public interface ApiInterface {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<JsonArray> getRecipe();
}
