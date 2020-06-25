package com.bk.recipe;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainFragment extends Fragment {
private TextView haeder;
    private Button allCategory;
    private Button favorite;
    private FloatingActionButton fab;
    private OnFragmentTransaction onFragmentTransactionListener;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentTransaction){
            this.onFragmentTransactionListener= (OnFragmentTransaction) context;
        } else {
            throw new RuntimeException("Error");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frgment_main,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        allCategory = view.findViewById(R.id.allCategoryButton);
        fab = view.findViewById(R.id.fab);
        haeder = view.findViewById(R.id.header);
        favorite = view.findViewById(R.id.favoriteButton);

        allCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFragmentTransactionListener.beginTransaction(Dest.RECYCLER_VIEW,null,null);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFragmentTransactionListener.beginTransaction(Dest.ADD_RECIPE,null,null);

            }
        });

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFragmentTransactionListener.beginTransaction(Dest.RECYCLER_VIEW_FAVORITE,null,null);
            }
        });
    }



}
