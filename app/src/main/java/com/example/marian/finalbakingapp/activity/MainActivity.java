package com.example.marian.finalbakingapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.marian.finalbakingapp.R;
import com.example.marian.finalbakingapp.adapter.RecipeAdapter;
import com.example.marian.finalbakingapp.api.ApiClient;
import com.example.marian.finalbakingapp.api.ApiInterface;
import com.example.marian.finalbakingapp.model.Recipe;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
{
    @BindView(R.id.rv_recipe)
    RecyclerView mRecyclerView;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    private ArrayList<Recipe> mRecipes;
    public static final String RECIPE = "recipe";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (savedInstanceState != null)
        {

            mRecipes = savedInstanceState.getParcelableArrayList(RECIPE);
            mProgressBar.setVisibility(View.INVISIBLE);
            RecipeAdapter recipeAdapter = new RecipeAdapter(MainActivity.this, mRecipes);
            mRecyclerView.setAdapter(recipeAdapter);
        }
        else

        {
            getRecipes();
        }
    }

    private void getRecipes()
    {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        final Type TYPE = new TypeToken<ArrayList<Recipe>>()
        {}.getType();

        Call<JsonArray> call = apiInterface.getRecipe();
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                mRecipes = new Gson().fromJson(response.body(), TYPE);

                mProgressBar.setVisibility(View.INVISIBLE);

                RecipeAdapter recipeAdapter = new RecipeAdapter(MainActivity.this, mRecipes);
                mRecyclerView.setAdapter(recipeAdapter);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                mProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, R.string.error_no_internet, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(RECIPE, mRecipes);
    }
}
