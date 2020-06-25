package com.bk.recipe;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ReciprViewHolder> implements Filterable {
    private final Context context;
    private ArrayList<Recipe> recipes;
    private ArrayList<Recipe> recipesFull;
    public OnFragmentTransaction onFragmentTransactionListener;
    private FloatingActionButton fab;
    OnRecipeClickListener onRecipeClickListener;

    public RecipeAdapter(Context context, ArrayList<Recipe> recipes, OnFragmentTransaction listener, OnRecipeClickListener onRecipeClickListener) {
        this.context = context;
        this.recipes = recipes;
        recipesFull = new ArrayList<>(recipes);
        this.onFragmentTransactionListener = listener;
        this.onRecipeClickListener = onRecipeClickListener;
    }

    @NonNull
    @Override
    public ReciprViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item, parent, false);
        ReciprViewHolder evh = new ReciprViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ReciprViewHolder holder, int position) {


        holder.bindItem(position);

    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    @Override
    public Filter getFilter() {
        return recipesFilter;
    }

    private Filter recipesFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Recipe> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(recipesFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Recipe item : recipesFull) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }else if (item.getCategory().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            recipes.clear();
            recipes.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


    /**
     * View Holder
     */
    public class ReciprViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView recipeName;
        public TextView category;
        public int id;
        ImageView more;
        ImageView favorite;
        private Recipe currentItem;


        public ReciprViewHolder(@NonNull View itemView) {
            super(itemView);
            this.recipeName = itemView.findViewById(R.id.recipeName);
            this.category = itemView.findViewById(R.id.category);
            this.more = itemView.findViewById(R.id.more);
            favorite = itemView.findViewById(R.id.favorite);
            favorite.setOnClickListener(this);
            itemView.setOnClickListener(this);
            this.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopupMenu(v);
                }
            });
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.favorite:
                    currentItem.setFavirte(!currentItem.isFavorite());
                    favorite.setImageDrawable(context.getResources().getDrawable(currentItem.isFavoite ? R.drawable.ic_star_yellow : R.drawable.ic_star_gray));
                    RecipeDatabase.getInstance(context).recipeDao().updateRecipe(currentItem);
                    break;
                default:
                    onFragmentTransactionListener.beginTransaction(Dest.RECIPE_DETAILS, id, null);
                    break;

            }

        }


        private void showPopupMenu(View view) {
            PopupMenu popup = new PopupMenu(view.getContext(), view);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.edit_recipe, popup.getMenu());
            popup.show();
            popup.setOnMenuItemClickListener(new MyMenuItemClickListener(recipes.get(getAdapterPosition())));
            popup.show();

        }





        public void bindItem(int position) {
            currentItem = recipes.get(position);


            recipeName.setText(currentItem.getName());
            category.setText(currentItem.getCategory());
            id = currentItem.getRecipeID();

            favorite.setImageDrawable(context.getResources().getDrawable(currentItem.isFavoite ? R.drawable.ic_star_yellow : R.drawable.ic_star_gray));


        }

        class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
            private Recipe recipe;

            public MyMenuItemClickListener(Recipe recipe) {
                this.recipe = recipe;
            }

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_edit_recipe:
//                        if (recipeListener != null)
//                            recipeListener.onEditRecipe(recipe);
                        onRecipeClickListener.onRecipeClickEdit(getAdapterPosition());
                        break;
                    case R.id.action_delete_recipe:
                        if (onFragmentTransactionListener != null)
//                            listfeagment = Listfeagment.newInstance(categoryRecipe);
                            onRecipeClickListener.onRecipeClick(getAdapterPosition());
//                        return true;
                    default:
                }
                return false;
            }
        }











    }





}
