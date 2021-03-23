package com.example.stapp.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stapp.utils.DateUtil;
import com.example.stapp.models.ListItem;
import com.example.stapp.adapters.StocksListAdapter;
import com.example.stapp.R;
import com.example.stapp.utils.TinyDB;
import com.example.stapp.models.StocksDaily;

import java.util.ArrayList;

import static com.example.stapp.mainActivityApi.InitStocksRequest.*;

public class MainStocksFragment extends Fragment
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_stocks, container, false);
        RecyclerView rvStocks = (RecyclerView) rootView.findViewById(R.id.rvStocks);

        TinyDB tinyDB = new TinyDB(getActivity());
        StocksDaily mainStocks;
        try
        {
            mainStocks = tinyDB.getObject("mainStocks", StocksDaily.class);
            if (mainStocks.getStocksItems() == null) throw new NullPointerException();
        } catch (NullPointerException e)
        {
            mainStocks = new StocksDaily(DateUtil.now(), new ArrayList<>());
        }
        if (!mainStocks.getDate().equals(DateUtil.now()) || mainStocks.getStocksItems().isEmpty())
        {
            mainStocks.setStocksItems(getInitStocks(getActivity(), rootView));
        }
        ArrayList<ListItem> stocksList = mainStocks.getStocksItems();

        ArrayList<String> favorites;
        favorites = tinyDB.getListString("favorites");

        for (int i = 0; i < stocksList.size(); i++)
        {
            if (favorites.contains(stocksList.get(i).getSymbol())) stocksList.get(i).setFavorite(true);
        }

        LinearLayoutManager llManager = new LinearLayoutManager(getActivity());
        rvStocks.setLayoutManager(llManager);
        StocksListAdapter adapter = new StocksListAdapter(stocksList, getActivity());
        rvStocks.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onResume()
    {
        System.out.println("HUI HUI HUI HUI HUI");
//        TODO: update method in adapter?
        super.onResume();
    }
}