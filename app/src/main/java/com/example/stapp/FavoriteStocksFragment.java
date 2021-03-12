package com.example.stapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stapp.api.StocksContainer;

import java.util.ArrayList;

public class FavoriteStocksFragment extends Fragment
{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_favorite_stocks, container, false);
        RecyclerView rvFavorite = (RecyclerView) rootView.findViewById(R.id.rvFavorite);
        SearchView svStocks = (SearchView) getActivity().findViewById(R.id.svStocks);
        final int[] lastFirstItem = new int[1];

        TinyDB tinyDB = new TinyDB(getActivity());

        StocksContainer mainStocks;
        mainStocks = tinyDB.getObject("mainStocks", StocksContainer.class);
        ArrayList<ListItem> stocksList = mainStocks.getMainStocks();

        ArrayList<String> favorites;
        favorites = tinyDB.getListString("favorites");

        for (int i=0; i<stocksList.size(); i++)
        {
            if (favorites.contains(stocksList.get(i).getSymbol())) stocksList.get(i).setFavorite(true);
        }

        ArrayList<ListItem> favList = new ArrayList<>();
        for (ListItem item : stocksList)
        {
            if (item.isFavorite()) favList.add(item);
        }

        LinearLayoutManager llManager = new LinearLayoutManager(getActivity());
        rvFavorite.setLayoutManager(llManager);
        MainListAdapter adapter = new MainListAdapter(favList, getActivity());
        rvFavorite.setAdapter(adapter);

        return rootView;
    }
}