package com.example.marian.finalbakingapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.marian.finalbakingapp.R;
import com.example.marian.finalbakingapp.fragment.stepDetailFragment;
import com.example.marian.finalbakingapp.model.Step;

import java.util.ArrayList;

import static com.example.marian.finalbakingapp.activity.StepListActivity.STEPS;

/**
 * An activity representing a single step detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link StepListActivity}.
 */
public class StepDetailActivity extends AppCompatActivity {

    int position;
    boolean TowPane;
    ArrayList<Step> steps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_detail);


        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        
        if (savedInstanceState == null) {
            position = getIntent().getIntExtra(StepListActivity.POSITION, 0);
            TowPane = getIntent().getBooleanExtra(StepListActivity.PANES, false);
            steps = getIntent().getParcelableArrayListExtra(STEPS);

            stepDetailFragment fragment = new stepDetailFragment();

            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();

            arguments.putInt(StepListActivity.POSITION, position);
            arguments.putBoolean(StepListActivity.PANES, TowPane);
            arguments.putParcelableArrayList(STEPS, steps);

            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.step_detail, fragment)
                    .commit();
        }
    }

}
