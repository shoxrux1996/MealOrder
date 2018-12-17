package com.example.shoxrux.mealorder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shoxrux on 12/13/18.
 */

public class Tab2Menu extends Fragment {
    private RecyclerView mRecyclerView;
    private MenuRecyclerViewAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2menu, container, false);
        mRecyclerView = rootView.findViewById(R.id.menu_recycle_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(rootView.getContext(), 2));

        mAdapter = new MenuRecyclerViewAdapter(rootView.getContext());
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }
}
