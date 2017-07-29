package com.example.marian.finalbakingapp.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.marian.finalbakingapp.R;
import com.example.marian.finalbakingapp.adapter.StepAdapter;
import com.example.marian.finalbakingapp.model.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.marian.finalbakingapp.activity.MainActivity.RECIPE;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link StepDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class StepListActivity extends AppCompatActivity {
    private Recipe mRecipe;
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    public static boolean mTwoPane;
    @BindView(R.id.step_list) RecyclerView recyclerView;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fab) FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_list);

        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        toolbar.setTitle(getTitle());

        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Hello action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //get the recpes from the activity
        //TODO (1) get the Recipes

        mRecipe = getIntent().getParcelableExtra(RECIPE);

        //pass the Step
        recyclerView.setAdapter(new StepAdapter(mRecipe,this));
       // assert recyclerView != null;
       // setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.step_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }



}
