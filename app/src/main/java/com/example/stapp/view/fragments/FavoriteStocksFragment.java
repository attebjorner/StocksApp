package com.example.stapp.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stapp.models.ListItem;
import com.example.stapp.adapters.MainListAdapter;
import com.example.stapp.R;
import com.example.stapp.utils.TinyDB;
import com.example.stapp.models.StocksDailyContainer;

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

        TinyDB tinyDB = new TinyDB(getActivity());

        StocksDailyContainer mainStocks = tinyDB.getObject("mainStocks", StocksDailyContainer.class);
        ArrayList<ListItem> stocksList = mainStocks.getStocksItems();

        ArrayList<String> favorites = tinyDB.getListString("favorites");
        for (int i=0; i<stocksList.size(); i++)
        {
            if (favorites.contains(stocksList.get(i).getSymbol())) stocksList.get(i).setFavorite(true);
        }

        ArrayList<ListItem> favList = new ArrayList<>();
        for (ListItem item : stocksList) if (item.isFavorite()) favList.add(item);
        try
        {
            StocksDailyContainer searchedStocksContainer = tinyDB.getObject("searchedStocks", StocksDailyContainer.class);
            ArrayList<ListItem> searchedStocks = searchedStocksContainer.getStocksItems();
            for (ListItem item : searchedStocks)
            {
                if (favorites.contains(item.getSymbol()) && !mainStocks.getStocksItemsSymbols().contains(item.getSymbol()))
                {
                    item.setFavorite(true);
                    favList.add(item);
                }
            }
        } catch (NullPointerException e) { e.printStackTrace(); }


        LinearLayoutManager llManager = new LinearLayoutManager(getActivity());
        rvFavorite.setLayoutManager(llManager);
        MainListAdapter adapter = new MainListAdapter(favList, getActivity());
        rvFavorite.setAdapter(adapter);

        return rootView;
    }
}