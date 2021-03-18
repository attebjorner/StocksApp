package com.example.stapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stapp.utils.CurrentDate;
import com.example.stapp.R;
import com.example.stapp.utils.TinyDB;
import com.example.stapp.models.StocksDailyContainer;

import static com.example.stapp.mainActivityApi.SearchResultsRequest.getSearchResults;

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
            if (!temp.getDate().equals(CurrentDate.now())) throw new Exception();
        } catch (Exception e)
        {
            tinyDB.putObject("searchedStocks", new StocksDailyContainer(
                    CurrentDate.now(), tinyDB.getObject("mainStocks", StocksDailyContainer.class).getStocksItems()
            ));
        }

        String searchQuery = svStocks.getQuery().toString();
        getSearchResults(getActivity(), rootView, searchQuery);

//        TODO: result list from finhub, symb lookup -> symb and name, (or name from twelve?)
//        quote -> cur price (c), change = c - pc, percChange = ((c - pc)*100)/ pc

//        LinearLayoutManager llManager = new LinearLayoutManager(getActivity());
//        rvSearchResults.setLayoutManager(llManager);
//        MainListAdapter adapter = new MainListAdapter(new ArrayList<>(), getActivity());
//        rvSearchResults.setAdapter(adapter);

        return rootView;
    }
}