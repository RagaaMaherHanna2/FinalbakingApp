package com.example.marian.finalbakingapp.activity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.marian.finalbakingapp.R;
import com.example.marian.finalbakingapp.adapter.StepAdapter;
import com.example.marian.finalbakingapp.fragment.stepDetailFragment;
import com.example.marian.finalbakingapp.model.Ingredient;
import com.example.marian.finalbakingapp.model.Recipe;

import java.util.ArrayList;

import static com.example.marian.finalbakingapp.activity.MainActivity.RECIPE;
import static com.example.marian.finalbakingapp.database.Contract.RECIPE_CONTENT_URI;
import static com.example.marian.finalbakingapp.database.Contract.RecipeEntry;
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



//    @BindView(R.id.fab)
//    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_list);

//        ButterKnife.bind(this);

//
//
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Hello action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        //get the recipe from the activity
        //TODO (1) get the Recipes
        recipe = getIntent().getParcelableExtra(RECIPE);

        //pass the Step
        //recyclerView.setAdapter(new StepAdapter(mRecipe, this));

        mTwoPane = findViewById(R.id.tablet_linear_layout) != null;

    }


    @Override
    public void onRecipeSelected(int position)
    {
        Bundle arguments = new Bundle();

        if (mTwoPane)
        {
            stepDetailFragment fragment = new stepDetailFragment();

            arguments.putInt(stepDetailFragment.ARG_ITEM_ID, position);
            arguments.putBoolean(PANES, mTwoPane);
            arguments.putParcelableArrayList(STEPS, recipe.getSteps());

            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_frame, fragment)
                    .commit();

        }
        else if (mTwoPane && position == 0)
        {
            stepDetailFragment fragment = new stepDetailFragment();

            arguments.putInt(stepDetailFragment.ARG_ITEM_ID, position);
            arguments.putBoolean(PANES, mTwoPane);

            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_frame, fragment).commit();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_widget:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                {
                    if (isFavorite())
                    {
                        deleteFromWidget();
                        item.setIcon(R.drawable.ic_favorite_border_black_24dp);
                        Toast.makeText(this, String.format(getString(R.string.remove_from_widget), recipe.getName()), Toast.LENGTH_LONG).show();
                    }
                    else
                        {
                        addToWidget();
                        item.setIcon(R.drawable.ic_favorite_black_24dp);
                        Toast.makeText(this, String.format(getString(R.string.add_to_widget), recipe.getName()), Toast.LENGTH_LONG).show();
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        MenuItem menuItem = menu.findItem(R.id.action_widget);
        if (isFavorite()) {
            menuItem.setIcon(R.drawable.ic_favorite_black_24dp);
        }
        else {
            menuItem.setIcon(R.drawable.ic_favorite_border_black_24dp);
        }
        return true;
    }


    synchronized private void deleteFromWidget()
    {
        getContentResolver().delete(RECIPE_CONTENT_URI, null, null);
    }

    synchronized private void addToWidget()
    {
        getContentResolver().delete(RECIPE_CONTENT_URI, null, null);
        getDetails(recipe.getIngredients());
    }


    private void getDetails(ArrayList<Ingredient> ingredients)
    {

        for (Ingredient ingredient : ingredients)
        {
            ContentValues values = new ContentValues();
            values.put(RecipeEntry.COLUMN_RECIPE_ID, recipe.getId());
            values.put(RecipeEntry.COLUMN_RECIPE_NAME, recipe.getName());
            values.put(RecipeEntry.COLUMN_INGREDIENT_NAME, ingredient.getIngredient());
            values.put(RecipeEntry.COLUMN_INGREDIENT_QUANTITY, ingredient.getQuantity());
            values.put(RecipeEntry.COLUMN_INGREDIENT_MEASURE, ingredient.getMeasure());
            getContentResolver().insert(RECIPE_CONTENT_URI, values);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private boolean isFavorite() {
        String[] projection = {RecipeEntry.COLUMN_RECIPE_ID};
        String selection = RecipeEntry.COLUMN_RECIPE_ID + " = " + recipe.getId();
        @SuppressLint("Recycle") Cursor cursor = getContentResolver().query(RECIPE_CONTENT_URI,
                projection,
                selection,
                null,
                null,
                null);

        return (cursor != null ? cursor.getCount() : 0) > 0;
    }

}





