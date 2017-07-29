package com.example.marian.finalbakingapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;

import com.example.marian.finalbakingapp.R;
import com.example.marian.finalbakingapp.model.Recipe;

import java.util.ArrayList;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity {
@BindView(R.id.rv_recipe) RecyclerView mrecyclerView;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;
    private ArrayList<Recipe> mRecipes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}
