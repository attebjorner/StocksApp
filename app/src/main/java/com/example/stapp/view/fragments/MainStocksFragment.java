package com.example.stapp.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.stapp.api.ResponseListener;
import com.example.stapp.utils.DateUtil;
import com.example.stapp.models.ListItem;
import com.example.stapp.adapters.StocksListAdapter;
import com.example.stapp.R;
import com.example.stapp.utils.TinyDB;
import com.example.stapp.models.StocksDaily;

import java.util.ArrayList;

import static com.example.stapp.api.InitRequest.getInitStocks;

public class MainStocksFragment extends Fragment
{
    private RecyclerView rvStocks;
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
        View rootView = inflater.inflate(R.layout.fragment_stocks, container, false);
        rvStocks = (RecyclerView) rootView.findViewById(R.id.rvStocks);

        tinyDB = new TinyDB(getActivity());
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
            StocksDaily finalMainStocks = mainStocks;
            getInitStocks(getActivity(), new ResponseListener()
            {
                @Override
                public void onResponse(ArrayList<ListItem> stocksResponseItems)
                {
                    finalMainStocks.setStocksItems(stocksResponseItems);
                    initRecyclerView(finalMainStocks);
                }

                @Override
                public void onError(String message)
                {
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }
            });
        }

        initRecyclerView(mainStocks);
        return rootView;
    }

    @Override
    public void onResume()
    {
        System.out.println("HUI HUI HUI HUI HUI");
//        TODO: update method in adapter?
        try
        {
            ListItem item = new ListItem(tinyDB.getObject("clicked", ListItem.class));
            int position = tinyDB.getInt("clickedPos");
            item.setSymbol("hui");
//            TODO:
            adapter.notifyItemChanged(position, item);
        } catch (NullPointerException e) { e.printStackTrace(); }

        super.onResume();
    }

    public ArrayList<ListItem> getFavorites(StocksDaily stocks)
    {
        ArrayList<ListItem> stocksList = stocks.getStocksItems();
        ArrayList<String> favorites;
        favorites = tinyDB.getListString("favorites");
        for (int i = 0; i < stocksList.size(); i++)
        {
            if (favorites.contains(stocksList.get(i).getSymbol())) stocksList.get(i).setFavorite(true);
        }
        return stocksList;
    }

    public void initRecyclerView(StocksDaily stocks)
    {
        LinearLayoutManager llManager = new LinearLayoutManager(getActivity());
        rvStocks.setLayoutManager(llManager);
        adapter = new StocksListAdapter(getFavorites(stocks), getActivity());
        rvStocks.setAdapter(adapter);
    }
}