package com.tran.jakphotodemo;


import android.content.ContentResolver;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tran.jakphotodemo.adapter.PhotosAdapter;
import com.tran.jakphotodemo.models.Photo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainFragment extends Fragment {

    @Bind(R.id.recycler_view) public RecyclerView recyclerView;

    private List<Photo> mDataset;
    private PhotosAdapter mAdapter;
    private ContentResolver mResolver;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blank, container, false);
        ButterKnife.bind(this, view);
        setRecyclerView();
        return view;
    }

    private void setRecyclerView() {

        if(mDataset == null) mDataset = Collections.emptyList();
        mAdapter = new PhotosAdapter(getContext(), mDataset);
        recyclerView.setAdapter(mAdapter);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2, GridLayoutManager.VERTICAL, false));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1, GridLayoutManager.VERTICAL, false));
        }
    }

}
