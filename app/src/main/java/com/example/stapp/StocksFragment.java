package com.example.stapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

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
        SearchView svStocks = (SearchView) getActivity().findViewById(R.id.svStocks);
        final int[] lastFirstItem = new int[1];

        TinyDB tinyDB = new TinyDB(getActivity());

//        StocksContainer a = tinyDB.getObject("mainStocks", StocksContainer.class);
//        a.setDateMan("hui");
//        tinyDB.putObject("mainStocks", new StocksContainer(LocalDate.now(), new ArrayList<>()));

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

//        lastFirstItem[0] = llManager.findFirstVisibleItemPosition();
//        rvStocks.addOnScrollListener(new RecyclerView.OnScrollListener()
//        {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState)
//            {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy)
//            {
//                super.onScrolled(recyclerView, dx, dy);
//                int currentFirstVisible = llManager.findFirstCompletelyVisibleItemPosition();
////                if ((currentFirstVisible == 0 || currentFirstVisible < lastFirstItem[0])
////                        && (currentFirstVisible != lastFirstItem[0])) svStocks.setVisibility(View.VISIBLE);
////                else if (currentFirstVisible != lastFirstItem[0]) svStocks.setVisibility(View.GONE);
////                lastFirstItem[0] = currentFirstVisible;
//            }
//        });
    }
}