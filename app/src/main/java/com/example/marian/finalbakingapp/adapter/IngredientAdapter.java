package com.example.marian.finalbakingapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.marian.finalbakingapp.R;
import com.example.marian.finalbakingapp.model.Ingredient;
import com.example.marian.finalbakingapp.model.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Marian on 7/30/2017.
 */

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>
{
    private ArrayList<Ingredient> mIngredient;
    Recipe recipe;
    public IngredientAdapter(ArrayList<Ingredient> ingredients)
    {
        this.mIngredient = ingredients;
    }

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_item, parent, false);
        return new IngredientViewHolder(view);
    }



    @Override
    public void onBindViewHolder(IngredientViewHolder holder, int position)
    {
        holder.tvIngredient.setText(mIngredient.get(position).getAll());
        holder.tvQuantity.setText(Integer.toString(mIngredient.get(position).getQuantity()));
        holder.tvMeasure.setText(mIngredient.get(position).getMeasure());

    }

    @Override
    public int getItemCount()
    {
        if (recipe == null)
            return 0;
        return mIngredient.size();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.ingredients)
        TextView tvIngredient;
        @BindView(R.id.measure)
        TextView tvMeasure;
        @BindView(R.id.quantity)
        TextView tvQuantity;


        public IngredientViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
