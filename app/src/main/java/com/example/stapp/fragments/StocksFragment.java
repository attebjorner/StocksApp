package com.example.stapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stapp.ListItem;
import com.example.stapp.adapters.MainListAdapter;
import com.example.stapp.R;
import com.example.stapp.TinyDB;
import com.example.stapp.api.StocksContainer;

import java.time.LocalDate;
import java.util.ArrayList;

import static com.example.stapp.api.MboumApi.*;

public class StocksFragment extends Fragment
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
        StocksContainer mainStocks;
        try
        {
            mainStocks = tinyDB.getObject("mainStocks", StocksContainer.class);
        } catch (NullPointerException e)
        {
            mainStocks = new StocksContainer(LocalDate.now(), new ArrayList<>());
        }
        if (!mainStocks.getDate().equals(LocalDate.now().toString()) || mainStocks.getMainStocks().isEmpty())
        {
            getMboumStocks(getActivity(), rootView);
        }
        ArrayList<ListItem> stocksList = mainStocks.getMainStocks();

        ArrayList<String> favorites;
        favorites = tinyDB.getListString("favorites");

        for (int i = 0; i < stocksList.size(); i++)
        {
            if (favorites.contains(stocksList.get(i).getSymbol())) stocksList.get(i).setFavorite(true);
        }

        LinearLayoutManager llManager = new LinearLayoutManager(getActivity());
        rvStocks.setLayoutManager(llManager);
        MainListAdapter adapter = new MainListAdapter(stocksList, getActivity());
        rvStocks.setAdapter(adapter);

        return rootView;
    }
}