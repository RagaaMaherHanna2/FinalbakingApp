package com.example.marian.finalbakingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.marian.finalbakingapp.R;
import com.example.marian.finalbakingapp.adapter.StepAdapter;
import com.example.marian.finalbakingapp.fragment.stepDetailFragment;
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
public class StepListActivity extends AppCompatActivity implements StepAdapter.OnRecipeListener
{
    private Recipe recipe;
    public static final String STEPS = "steps";
    public static final String PANES = "panes";
    public static final String POSITION = "position";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    public static boolean mTwoPane;



    @BindView(R.id.fab)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_list);

        ButterKnife.bind(this);



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Hello action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //get the recipe from the activity
        //TODO (1) get the Recipes
        recipe = getIntent().getParcelableExtra(RECIPE);

        //pass the Step
        //recyclerView.setAdapter(new StepAdapter(mRecipe, this));

        if (findViewById(R.id.tablet_linear_layout) != null)
        {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }


    @Override
    public void onRecipeSelected(int position) {
        Bundle arguments = new Bundle();

        if (mTwoPane) {
            arguments.putInt(stepDetailFragment.ARG_ITEM_ID, position);
            arguments.putBoolean(PANES, mTwoPane);
            stepDetailFragment fragment = new stepDetailFragment();
            arguments.putParcelableArrayList(STEPS, recipe.getSteps());

            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.tablet_linear_layout, fragment)
                    .commit();
        } else if (mTwoPane && position == 0) {
            stepDetailFragment fragment = new stepDetailFragment();
            arguments.putInt(stepDetailFragment.ARG_ITEM_ID, position);
            arguments.putBoolean(PANES, mTwoPane);
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.tablet_linear_layout, fragment).commit();
        }
        else
            {
            arguments.putInt(stepDetailFragment.ARG_ITEM_ID, position);
            arguments.putBoolean(PANES, mTwoPane);
            arguments.putParcelableArrayList(STEPS, recipe.getSteps());
            Intent intent = new Intent(StepListActivity.this, StepDetailActivity.class);
            intent.putExtras(arguments);
            startActivity(intent);
        }

    }
}





