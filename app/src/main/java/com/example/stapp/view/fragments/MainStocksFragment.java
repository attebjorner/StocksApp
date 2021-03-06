package com.example.stapp.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stapp.R;
import com.example.stapp.adapters.StocksListAdapter;
import com.example.stapp.api.ResponseListener;
import com.example.stapp.models.ListItem;
import com.example.stapp.models.StocksDaily;
import com.example.stapp.utils.DateUtil;
import com.example.stapp.utils.TinyDB;

import java.util.ArrayList;
import java.util.List;

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
            if (mainStocks.getStocksItems() == null)
            {
                throw new NullPointerException();
            }
        }
        catch (NullPointerException e)
        {
            mainStocks = new StocksDaily(DateUtil.now(), new ArrayList<>());
        }
        if (!mainStocks.getDate().equals(DateUtil.now()) || mainStocks.getStocksItems().isEmpty())
        {
            StocksDaily finalMainStocks = mainStocks;
            getInitStocks(getActivity(), new ResponseListener()
            {
                @Override
                public void onResponse(List<ListItem> stocksResponseItems)
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
        super.onResume();
        try
        {
            List<ListItem> itemList = tinyDB.getListObject("clickedList", ListItem.class);
            if (itemList.size() != 0)
            {
                setRecyclerViewAdapter(itemList);
                tinyDB.putListObject("clickedList", new ArrayList<>());
            }
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
    }

    public List<ListItem> getFavorites(StocksDaily stocks)
    {
        List<ListItem> stocksList = stocks.getStocksItems();
        ArrayList<String> favorites;
        favorites = tinyDB.getListString("favorites");
        for (int i = 0; i < stocksList.size(); i++)
        {
            stocksList.get(i).setFavorite(favorites.contains(stocksList.get(i).getSymbol()));
        }
        tinyDB.putObject("mainStocks", new StocksDaily(DateUtil.now(), stocksList));
        return stocksList;
    }

    public void initRecyclerView(StocksDaily stocks)
    {
        LinearLayoutManager llManager = new LinearLayoutManager(getActivity());
        rvStocks.setLayoutManager(llManager);
        setRecyclerViewAdapter(getFavorites(stocks));
    }

    public void setRecyclerViewAdapter(List<ListItem> itemList)
    {
        adapter = new StocksListAdapter(itemList, getActivity());
        rvStocks.setAdapter(adapter);
    }
}