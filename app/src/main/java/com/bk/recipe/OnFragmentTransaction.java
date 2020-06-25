package com.bk.recipe;

enum Dest{
    ADD_RECIPE,RECYCLER_VIEW,RECIPE_DETAILS,EDIT_RECIPE,RECYCLER_VIEW_FAVORITE;

}

public interface OnFragmentTransaction {
    void beginTransaction(Dest des,Integer id,Recipe recipe);
}
