package com.example.marian.finalbakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.marian.finalbakingapp.R;
import com.example.marian.finalbakingapp.dummy.DummyContent;
import com.example.marian.finalbakingapp.model.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Marian on 7/29/2017.
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {

    private final Recipe mRecipe;
    private Context mContext;
    private final OnRecipeListener monRecipeListener;

    public StepAdapter(Recipe item, Context context, OnRecipeListener onRecipeListener)
    {
        mContext = context;
        mRecipe = item;
        monRecipeListener = onRecipeListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.step_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {

        holder.mTextView.setText(String.valueOf(mRecipe.getSteps()
                .get(position).getShortDescription()));
    }

    @Override
    public int getItemCount() {
        if (mRecipe == null)
            return 0;
        return mRecipe.getSteps().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        @BindView(R.id.tv_ingredient_step)
        TextView mTextView;
        public DummyContent.DummyItem mItem;

        public ViewHolder(View view)
        {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View v)
        {
            int clickedRecipe = getAdapterPosition();
            monRecipeListener.onRecipeSelected(clickedRecipe);

        }
    }

    public interface OnRecipeListener
    {
     void onRecipeSelected(int position);
    }


}