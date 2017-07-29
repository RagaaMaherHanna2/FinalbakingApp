package com.example.marian.finalbakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.marian.finalbakingapp.R;
import com.example.marian.finalbakingapp.activity.StepDetailActivity;
import com.example.marian.finalbakingapp.dummy.DummyContent;
import com.example.marian.finalbakingapp.fragment.stepDetailFragment;
import com.example.marian.finalbakingapp.model.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.marian.finalbakingapp.activity.StepListActivity.mTwoPane;

/**
 * Created by Marian on 7/29/2017.
 */

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {

    private final Recipe mRecipe;
    private Context mContext ;
    public StepAdapter(Recipe item, Context context) {
        mContext=context;
        mRecipe = item;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.step_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mTextView.setText(String.valueOf(mRecipe.getSteps().get(position).getShortDescription()));
        holder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(stepDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                    stepDetailFragment fragment = new stepDetailFragment();
                    fragment.setArguments(arguments);
                   fragment.getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.step_detail_container, fragment)
                            .commit();
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, StepDetailActivity.class);
                    intent.putExtra(stepDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        if (mRecipe == null)
            return 0;
        return mRecipe.getSteps().size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
       @BindView(R.id.tv_ingredient_step) TextView mTextView;
        public DummyContent.DummyItem mItem;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }


    }
}