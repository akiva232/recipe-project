package com.bk.recipe;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RoomWarnings;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RecipeDao {

    @Insert
     void addRecipe(Recipe recipe);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("select * from Recipe")
     List<Recipe> getRecipes();
    @Query("select * from Recipe where isFavoite like 1")
    List<Recipe> getFavoritesRecipes();

    @Query("select * from Recipe where recipeID like :id")
    Recipe getRecipeDetails(int id);

    @Query("delete from Recipe where recipeID like :id")
    void deleteRecipe(int id);

    @Update
    void updateRecipe(Recipe recipe);
}
