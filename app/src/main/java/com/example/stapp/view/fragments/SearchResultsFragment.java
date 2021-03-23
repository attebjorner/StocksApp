package com.example.stapp.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stapp.utils.DateUtil;
import com.example.stapp.R;
import com.example.stapp.utils.TinyDB;
import com.example.stapp.models.StocksDaily;

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
        svStocks.setBackgroundResource(R.drawable.search_rounded_focus);

        //create it here so that in api it will already exists
        TinyDB tinyDB = new TinyDB(getActivity());
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