package com.example.stapp.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stapp.R;
import com.example.stapp.adapters.StocksListAdapter;
import com.example.stapp.models.ListItem;
import com.example.stapp.models.StocksDaily;
import com.example.stapp.utils.TinyDB;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment
{
    private TinyDB tinyDB;
    private RecyclerView rvFavorite;
    private StocksListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_favorite_stocks, container, false);
        rvFavorite = (RecyclerView) rootView.findViewById(R.id.rvFavorite);

        tinyDB = new TinyDB(getActivity());

        StocksDaily mainStocks = tinyDB.getObject("mainStocks", StocksDaily.class);
        List<ListItem> stocksList = mainStocks.getStocksItems();

        List<String> favorites = tinyDB.getListString("favorites");
        for (int i = 0; i < stocksList.size(); i++)
        {
            stocksList.get(i).setFavorite(favorites.contains(stocksList.get(i).getSymbol()));
        }

        List<ListItem> favList = new ArrayList<>();
        for (ListItem item : stocksList) if (item.isFavorite()) favList.add(item);
        try
        {
            StocksDaily searchedStocksContainer = tinyDB.getObject("searchedStocks", StocksDaily.class);
            List<ListItem> searchedStocks = searchedStocksContainer.getStocksItems();
            for (ListItem item : searchedStocks)
            {
                if (favorites.contains(item.getSymbol()) && !mainStocks.getStocksItemsSymbols().contains(item.getSymbol()))
                {
                    item.setFavorite(true);
                    favList.add(item);
                }
            }
        } catch (NullPointerException e)
        {
            e.printStackTrace();
        }

        LinearLayoutManager llManager = new LinearLayoutManager(getActivity());
        rvFavorite.setLayoutManager(llManager);
        setRecyclerViewAdapter(new ArrayList<>(favList));

        return rootView;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        try
        {
            List<ListItem> itemList = tinyDB.getListObject("clickedList", ListItem.class);
            if (itemList.size() != 0)
            {
                setRecyclerViewAdapter(itemList);
                tinyDB.putListObject("clickedList", new ArrayList<>());
            }
        } catch (NullPointerException e)
        {
            e.printStackTrace();
        }
    }

    public void setRecyclerViewAdapter(List<ListItem> itemList)
    {
        adapter = new StocksListAdapter(itemList, getActivity());
        rvFavorite.setAdapter(adapter);
    }
}