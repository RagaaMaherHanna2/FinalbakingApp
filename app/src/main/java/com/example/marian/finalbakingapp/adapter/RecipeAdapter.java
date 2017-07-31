package com.example.marian.finalbakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marian.finalbakingapp.R;
import com.example.marian.finalbakingapp.activity.StepListActivity;
import com.example.marian.finalbakingapp.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.marian.finalbakingapp.activity.MainActivity.RECIPE;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {


    private Context context;
    private ArrayList<Recipe> recipes;

    public RecipeAdapter(final Context context, ArrayList<Recipe> recipes)
    {
        //get the context from the activity
        this.context = context;
        //get the passed arrayList
        this.recipes = recipes;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(final ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item, parent, false);

        return new RecipeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecipeViewHolder holder, final int position)
    {
        holder.onRelate(position);

    }

    @Override
    public int getItemCount()
    {
        if (recipes == null)
        {
            return 0;
        }
        return recipes.size();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        @BindView(R.id.tv_recipe_name) TextView mRecipeName;
        @BindView(R.id.iv_recipe_image) ImageView mRecipeImage;

        public RecipeViewHolder(final View itemView)
        {
            super(itemView);
            //TODO this how it's done inside the RecyclerView
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void onRelate(int position)
        {


            if (!recipes.isEmpty())
            {
                if (!recipes.get(position).getImageUrl().isEmpty())
                {
                    mRecipeImage.setVisibility(View.VISIBLE);
                    Picasso.with(context)
                            .load(recipes.get(position).getImageUrl())
                            .into(mRecipeImage);
                }
                else
                {
                    mRecipeImage.setImageResource(R.drawable.recipe_icon);

                    mRecipeName.setText(recipes.get(position).getName());
                }


            }
        }
                @Override
        public void onClick(View v)
        {
            //when he clicks on the recyclerView go to the ingredientStepActivity
            Intent intent = new Intent(context, StepListActivity.class);
            //pass the recipe that he touched to the activity
            intent.putExtra(RECIPE, recipes.get(getAdapterPosition()));

            context.startActivity(intent);
        }
    }
}
