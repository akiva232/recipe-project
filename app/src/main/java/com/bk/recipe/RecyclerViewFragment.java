package com.bk.recipe;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewFragment extends Fragment implements OnRecipeClickListener {
    private ArrayList<Recipe> mRecipes;


    private RecyclerView mRecyclerView;
    RecipeAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton fab;
    private OnFragmentTransaction onFragmentTransactionListener;

    int adapterPosition;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recyclerview, container, false);


        //createExampleList();
        buildRecyclerView(view);
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFragmentTransactionListener.beginTransaction(Dest.ADD_RECIPE, null, null);
            }
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentTransaction) {
            this.onFragmentTransactionListener = (OnFragmentTransaction) context;
        } else {
            throw new RuntimeException("Error");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // ((MainActivity)getActivity()).getAllCategory().setVisibility(View.VISIBLE);
    }


     void getCachedRecipeFromDataBase() {
        mRecipes = new ArrayList<>();
        List<Recipe> cachedRecipes = RecipeDatabase.getInstance(getContext()).recipeDao().getRecipes();
        mRecipes.addAll(cachedRecipes);
    }
    void getCachedRecipeFromDataBaseFavorites(){
        mRecipes = new ArrayList<>();
        List<Recipe> cachedRecipes = RecipeDatabase.getInstance(getContext()).recipeDao().getFavoritesRecipes();
        mRecipes.addAll(cachedRecipes);
    }


    public void createExampleList() {
        mRecipes = new ArrayList<>();
        mRecipes.add(new Recipe("Soup", "Soups"));
        mRecipes.add(new Recipe("Soup", "Soups"));
        mRecipes.add(new Recipe("Soup", "Soups"));
        mRecipes.add(new Recipe("Soup", "Soups"));
        mRecipes.add(new Recipe("Soup", "Soups"));
        mRecipes.add(new Recipe("Soup", "Soups"));
        mRecipes.add(new Recipe("Soup", "Soups"));
        mRecipes.add(new Recipe("Soup", "Soups"));
        mRecipes.add(new Recipe("Soup", "Soups"));
        mRecipes.add(new Recipe("Soup", "Soups"));
        mRecipes.add(new Recipe("Soup", "Soups"));
        mRecipes.add(new Recipe("Soup", "Soups"));
        mRecipes.add(new Recipe("Soup", "Soups"));
    }

    public void buildRecyclerView(View view) {
        mRecyclerView = view.findViewById(R.id.recipeRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new RecipeAdapter(getContext(), mRecipes, (OnFragmentTransaction) getContext(), this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }


    //from Yedidia
    @Override
    public void onRecipeClick(int itemPosition) {
        FireMissilesDialogFragment fireMissilesDialogFragment = new FireMissilesDialogFragment(this);
        fireMissilesDialogFragment.show(getFragmentManager(), "dialog");
        adapterPosition = itemPosition;
    }

    @Override
    public void onRecipeClickEdit(int itemPosition) {
        onFragmentTransactionListener.beginTransaction(Dest.EDIT_RECIPE, null, mRecipes.get(itemPosition));
    }

    public void onClickDialog() {
        int recipeId = mRecipes.get(adapterPosition).getRecipeID();
        RecipeDatabase.getInstance(getContext()).recipeDao().deleteRecipe(recipeId);
//        mRecipes.remove(mRecipes.get(itemPosition));
        mRecipes.remove(adapterPosition);
        mAdapter.notifyDataSetChanged();
    }

    public static class FireMissilesDialogFragment extends DialogFragment {
        RecyclerViewFragment recyclerViewFragment;

        public FireMissilesDialogFragment(RecyclerViewFragment recyclerViewFragment) {
            this.recyclerViewFragment = recyclerViewFragment;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.dialog_fire_missiles)
                    .setPositiveButton(R.string.fire, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            recyclerViewFragment.onClickDialog();
                            // FIRE ZE MISSILES!
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }


    //code for change menu background
    //setMenuBackground();
    protected void setMenuBackground(){
        // Log.d(TAG, "Enterting setMenuBackGround");
        getLayoutInflater().setFactory( new LayoutInflater.Factory() {
            public View onCreateView(String name, Context context, AttributeSet attrs) {
                if ( name.equalsIgnoreCase( "com.android.internal.view.menu.IconMenuItemView" ) ) {
                    try { // Ask our inflater to create the view
                        LayoutInflater f = getLayoutInflater();
                        final View view = f.createView( name, null, attrs );
                        /* The background gets refreshed each time a new item is added the options menu.
                         * So each time Android applies the default background we need to set our own
                         * background. This is done using a thread giving the background change as runnable
                         * object */
                        new Handler().post(new Runnable() {
                            public void run () {
                                // sets the background color
                                view.setBackgroundResource( R.drawable.background);
                                // sets the text color
                                ((TextView) view).setTextColor(Color.RED);
                                // sets the text size
                                ((TextView) view).setTextSize(18);
                            }
                        } );
                        return view;
                    }
                    catch ( InflateException e ) {}
                    catch ( ClassNotFoundException e ) {}
                }
                return null;
            }});
    }
}
