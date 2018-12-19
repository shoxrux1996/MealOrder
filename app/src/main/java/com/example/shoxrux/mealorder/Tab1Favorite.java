package com.example.shoxrux.mealorder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by shoxrux on 12/13/18.
 */

public class Tab1Favorite extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1favorite, container, false);
        RecyclerView mRecyclerView = rootView.findViewById(R.id.favorite_recycle_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        FavoriteRecyclerViewAdapter mAdapter = new FavoriteRecyclerViewAdapter(rootView.getContext());
        mRecyclerView.setAdapter(mAdapter);

        return rootView;

    }


}
