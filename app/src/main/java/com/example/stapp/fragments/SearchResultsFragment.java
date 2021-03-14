package com.example.stapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.stapp.ListItem;
import com.example.stapp.R;
import com.example.stapp.TinyDB;
import com.example.stapp.adapters.MainListAdapter;
import com.example.stapp.api.StocksDailyContainer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.stapp.api.SearchResultsApi.getSearchResults;

public class SearchResultsFragment extends Fragment
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_search_results, container, false);
        RecyclerView rvSearchResults = (RecyclerView) rootView.findViewById(R.id.rvSearchResults);
        SearchView svStocks = (SearchView) getActivity().findViewById(R.id.svStocks);

        //create it here so that in api it will already exists
        TinyDB tinyDB = new TinyDB(getActivity());
        try
        {
            StocksDailyContainer temp = tinyDB.getObject("searchedStocks", StocksDailyContainer.class);
            if (!temp.getDate().equals(LocalDate.now().toString())) throw new Exception();
        } catch (Exception e)
        {
            tinyDB.putObject("searchedStocks", new StocksDailyContainer(
                    LocalDate.now(), tinyDB.getObject("mainStocks", StocksDailyContainer.class).getStocksItems()
            ));
        }

//        ArrayList<ListItem> searchResults = new ArrayList<>();
        String searchQuery = svStocks.getQuery().toString();
        Context cont = getActivity();
        new Timer().schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                System.out.println("TIMER TASK STARTDEF");
                getSearchResults(cont, rootView, searchQuery);
            }
        }, 3000);
//        getSearchResults(getActivity(), rootView, searchQuery);

//        TODO: result list from finhub, symb lookup -> symb and name, (or name from twelve?)
//        quote -> cur price (c), change = c - pc, percChange = ((c - pc)*100)/ pc

        LinearLayoutManager llManager = new LinearLayoutManager(getActivity());
        rvSearchResults.setLayoutManager(llManager);
        MainListAdapter adapter = new MainListAdapter(new ArrayList<>(), getActivity());
        rvSearchResults.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onStop()
    {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        if (requestQueue!= null) requestQueue.cancelAll("searchRequest");
        System.out.println("STOP FRAGMENT SUKA BLYAT");
        super.onStop();
    }
}