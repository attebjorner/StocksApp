package com.example.stapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stapp.R;
import com.example.stapp.TinyDB;
import com.example.stapp.adapters.EmptySearchAdapter;

import java.util.LinkedList;

public class EmptySearchFragment extends Fragment
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

//        TinyDB tinyDB = new TinyDB(getActivity());
//        LinkedList<String> searchedFor = tinyDB.getObject("searched", LinkedList.class);
        LinkedList<String> searchedList = new LinkedList<>();
        searchedList.add("hui");
        searchedList.add("pizda");
        searchedList.add("manda");
        searchedList.add("escho hui");
        searchedList.add("pise4ka");
        searchedList.add("hui");
        searchedList.add("pizda");
        searchedList.add("manda");
        searchedList.add("escho hui");
        searchedList.add("pise4ka");

        EmptySearchAdapter emptySearchAdapter = new EmptySearchAdapter(searchedList);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.HORIZONTAL);
        rvSearched.setLayoutManager(layoutManager);
        rvSearched.setAdapter(emptySearchAdapter);

        return rootView;
    }
}