package com.example.marian.finalbakingapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.marian.finalbakingapp.R;
import com.example.marian.finalbakingapp.adapter.IngredientAdapter;
import com.example.marian.finalbakingapp.adapter.StepAdapter;
import com.example.marian.finalbakingapp.model.Ingredient;
import com.example.marian.finalbakingapp.model.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.marian.finalbakingapp.activity.MainActivity.RECIPE;

/**
 * Created by Marian on 7/30/2017.
 */

public class StepsFragment extends Fragment
{

    @BindView(R.id.rv_ingredients)
    RecyclerView rvIngredients;
    @BindView(R.id.rv_steps)
    RecyclerView rvSteps;

    private Recipe recipe;
    private IngredientAdapter ingredientAdapter;
    private ArrayList<Ingredient> ingredients;
    private StepAdapter stepAdapter;
    private StepAdapter.OnRecipeListener recipeSelected;


    public StepsFragment()
    {

    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putParcelable(RECIPE, recipe);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (savedInstanceState != null)
        {
            recipe = savedInstanceState.getParcelable(RECIPE);
        }

        final View rootView = inflater.inflate(R.layout.step_fragment, container, false);
        ButterKnife.bind(this, rootView);

        //get the recipe
        recipe = getActivity().getIntent().getParcelableExtra(RECIPE);

        //get the ingredients of this recipe
        ingredients = recipe.getIngredients();

        //set Adapters for ingredients and steps
        stepAdapter = new StepAdapter(recipe,getContext(),recipeSelected);
        rvSteps.setAdapter(stepAdapter);
        ingredientAdapter = new IngredientAdapter(ingredients);
        rvIngredients.setAdapter(ingredientAdapter);

        return rootView;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        try
        {
            recipeSelected = (StepAdapter.OnRecipeListener) context;

        }
        catch (ClassCastException e)
        {
            Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();

        }
    }
}
