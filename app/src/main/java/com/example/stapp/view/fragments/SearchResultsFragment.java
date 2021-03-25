package com.example.stapp.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stapp.adapters.StocksListAdapter;
import com.example.stapp.api.ResponseListener;
import com.example.stapp.models.ListItem;
import com.example.stapp.utils.DateUtil;
import com.example.stapp.R;
import com.example.stapp.utils.TinyDB;
import com.example.stapp.models.StocksDaily;

import java.util.ArrayList;

import static com.example.stapp.api.SearchRequests.getSearchResults;

public class SearchResultsFragment extends Fragment
{
    private RecyclerView rvSearchResults;
    private TinyDB tinyDB;
    private StocksListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_search_results, container, false);
        rvSearchResults = (RecyclerView) rootView.findViewById(R.id.rvSearchResults);
        SearchView svStocks = (SearchView) getActivity().findViewById(R.id.svStocks);
        svStocks.setBackgroundResource(R.drawable.search_rounded_focus);

        tinyDB = new TinyDB(getActivity());
        try
        {
            StocksDaily temp = tinyDB.getObject("searchedStocks", StocksDaily.class);
            if (!temp.getDate().equals(DateUtil.now())) throw new Exception();
        } catch (Exception e)
        {
            tinyDB.putObject("searchedStocks", new StocksDaily(
                    DateUtil.now(), tinyDB.getObject("mainStocks", StocksDaily.class).getStocksItems()
            ));
        }

        String searchQuery = svStocks.getQuery().toString();
        getSearchResults(getActivity(), searchQuery, new ResponseListener()
        {
            @Override
            public void onResponse(ArrayList<ListItem> stocksResponseItems)
            {
                initRecyclerView(stocksResponseItems);
            }

            @Override
            public void onError(String message)
            {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        try
        {
            ArrayList<ListItem> itemList = tinyDB.getListObject("clickedList", ListItem.class);
            if (itemList.size() != 0)
            {
                setRecyclerViewAdapter(itemList);
                tinyDB.putListObject("clickedList", new ArrayList<>());
            }
        } catch (NullPointerException e) { e.printStackTrace(); }
    }

    public void initRecyclerView(ArrayList<ListItem> responseList)
    {
        if (responseList.size() == 0) Toast.makeText(getActivity(), "No results", Toast.LENGTH_SHORT).show();
        else
        {
            LinearLayoutManager llManager = new LinearLayoutManager(getActivity());
            rvSearchResults.setLayoutManager(llManager);
            setRecyclerViewAdapter(responseList);
        }
    }

    public void setRecyclerViewAdapter(ArrayList<ListItem> itemList)
    {
        adapter = new StocksListAdapter(itemList, getActivity());
        rvSearchResults.setAdapter(adapter);
    }
}