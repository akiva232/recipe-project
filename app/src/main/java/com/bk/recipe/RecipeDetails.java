package com.bk.recipe;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class RecipeDetails extends Fragment {
    private ImageView imageView;
    private TextView name;
    private TextView ingredients;
    private TextView instructions;
    private static final String ID_K = "ID_K";
    int recipeId = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recipe_details, container, false);
    }

    public static RecipeDetails newInsttance(int id){
        Bundle args = new Bundle();
        args.putInt(ID_K,id);
        RecipeDetails fragment = new RecipeDetails();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey(ID_K));
        recipeId = getArguments().getInt(ID_K);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        imageView = view.findViewById(R.id.rd_image);
        name = view.findViewById(R.id.rd_name);
        ingredients = view.findViewById(R.id.rd_ingredients);
        instructions = view.findViewById(R.id.rd_instruction);
        if (recipeId >= 0)
            getCachedRecipeFromDataBase(recipeId);
    }

    public void getCachedRecipeFromDataBase(int id) {

        Recipe recipe = RecipeDatabase.getInstance(getContext()).recipeDao().getRecipeDetails(id);
        System.out.println(recipe.toString());
        if (recipe != null) {
            if (recipe.getUri() != null) {
                imageView.setImageURI(Uri.parse(recipe.getUri()));
            }
            name.setText(recipe.getName());
            ingredients.setText(recipe.getIngredients());
            instructions.setText(recipe.getInstructions());
        }

    }
}
