package com.example.stapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.stapp.R;
import com.example.stapp.TinyDB;
import com.example.stapp.adapters.SearchHistoryAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class SearchHistoryFragment extends Fragment
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_empty_search, container, false);
        RecyclerView rvSearched = (RecyclerView) rootView.findViewById(R.id.rvSearched);
        SearchView svStocks = (SearchView) getActivity().findViewById(R.id.svStocks);

        TinyDB tinyDB = new TinyDB(getActivity());
        ArrayList<String> searchedHistory = tinyDB.getListString("searchedHistory");
        Collections.reverse(searchedHistory);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.HORIZONTAL);
        rvSearched.setLayoutManager(layoutManager);
        SearchHistoryAdapter adapter = new SearchHistoryAdapter(searchedHistory, svStocks);
        rvSearched.setAdapter(adapter);

        return rootView;
    }
}