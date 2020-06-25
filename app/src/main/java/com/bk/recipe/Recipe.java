package com.bk.recipe;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Recipe implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int recipeID;
    private String name;
    private String ingredients;
    private String instructions;
    private String category;
    boolean isFavoite;


    private String uri;


    public Recipe(String name, String ingredients, String instructions, String category, String uri) {

        this.name = name;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.category = category;
        this.uri = uri;

    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }


    @Ignore
    public Recipe(String name, String category) {
        this.name = name;
        this.category = category;

    }

    @Ignore
    public Recipe() {
    }

    public int getRecipeID() {
        return recipeID;
    }

    public String getName() {
        return name;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getCategory() {
        return category;
    }


    public void setRecipeID(int recipeID) {
        this.recipeID = recipeID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public void setCategory(String category) {
        this.category = category;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.recipeID);
        dest.writeString(this.name);
        dest.writeString(this.ingredients);
        dest.writeString(this.instructions);
        dest.writeString(this.category);
        dest.writeString(this.uri);
    }

    protected Recipe(Parcel in) {
        this.recipeID = in.readInt();
        this.name = in.readString();
        this.ingredients = in.readString();
        this.instructions = in.readString();
        this.category = in.readString();
        this.uri = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public boolean isFavorite() {
        return isFavoite;
    }

    public void setFavirte(boolean favorite) {
        isFavoite = favorite;
    }
}
