package com.bk.recipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements OnFragmentTransaction{

 private MainFragment mainFragment;

    private RecyclerViewFragment recyclerViewFragment;
    private AddRecipe addRecipe;

    RecipeDetails recipeDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainFragment = new MainFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.FL_Activity_Main, mainFragment).commit();
        RecipeDatabase.getInstance(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.FL_Activity_Main);

        if (currentFragment instanceof  MainFragment) {
            Log.e( "onBackPressed: ", "DDDD");
        }

    }

    @Override
    public void beginTransaction(Dest des,Integer id,Recipe recipe) {
        switch (des){
            case ADD_RECIPE:
                addRecipe = new AddRecipe();
                getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.FL_Activity_Main, addRecipe).commit();
                break;
            case RECYCLER_VIEW:
                recyclerViewFragment = new RecyclerViewFragment();
                getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.FL_Activity_Main, recyclerViewFragment).commit();
                recyclerViewFragment.getCachedRecipeFromDataBase();
                break;
            case RECIPE_DETAILS:

                recipeDetails = RecipeDetails.newInsttance(id);
                getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.FL_Activity_Main,recipeDetails).commit();
                break;
            case EDIT_RECIPE:
                addRecipe = AddRecipe.newInstance(recipe);
                getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.FL_Activity_Main, addRecipe).commit();
                break;
            case RECYCLER_VIEW_FAVORITE:
                recyclerViewFragment = new RecyclerViewFragment();
                getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.FL_Activity_Main, recyclerViewFragment).commit();
                recyclerViewFragment.getCachedRecipeFromDataBaseFavorites();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerViewFragment.mAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }




}
