package com.bk.recipe;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Recipe.class}, version = 8, exportSchema = false)
public abstract class RecipeDatabase extends RoomDatabase {


    public static final String DATABASE_NAME = "recipedb";
    static RecipeDatabase INSTANCE;

    public abstract RecipeDao recipeDao();

    public static RecipeDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    RecipeDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
