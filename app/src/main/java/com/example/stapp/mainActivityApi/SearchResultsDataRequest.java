package com.example.stapp.mainActivityApi;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.stapp.models.ListItem;
import com.example.stapp.R;
import com.example.stapp.TinyDB;
import com.example.stapp.adapters.MainListAdapter;
import com.example.stapp.models.StocksDailyContainer;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class SearchResultsDataRequest
{
    public static void getSearchResultsData(Context context, View rootView, String queryResults)
    {
        TinyDB tinyDB = new TinyDB(context);
        ArrayList<ListItem> searchResponseItems = new ArrayList<>();
        //fun to return query without shit which i have
        queryResults = manageExistingStocks(searchResponseItems, queryResults, tinyDB);
        String LOOKUP_DATA_REQUEST = "https://api.twelvedata.com/quote?symbol=" + queryResults
                + "&apikey=ba5fd509861a483ebba2d3cebda53e84";
        StocksDailyContainer searchedStocksContainer = tinyDB.getObject("searchedStocks", StocksDailyContainer.class);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, LOOKUP_DATA_REQUEST, null, response ->
        {
            ArrayList<String> favorites = tinyDB.getListString("favorites");
            System.out.println("SEARCH RESULTS DATA API REQUEST");
            Iterator<String> keys = response.keys();
            while(keys.hasNext())
            {
                String key = keys.next();
                try
                {
                    JSONObject temp = (JSONObject) response.get(key);
                    String tempSymbol = temp.getString("symbol");
                    ListItem newStock = new ListItem(
                            tempSymbol, temp.getString("name"),
                            temp.getString("close"), temp.getString("change"),
                            temp.getString("percent_change")
                    );
                    if (favorites.contains(tempSymbol)) newStock.setFavorite(true);
                    searchResponseItems.add(newStock);
                    searchedStocksContainer.updateStocksList(newStock);
                } catch (Exception e) { e.printStackTrace(); }
            }
            tinyDB.putObject("searchedStocks", searchedStocksContainer);
            displaySearchResults(searchResponseItems, context, rootView);
        }, error -> Toast.makeText(context, "Error occurred", Toast.LENGTH_SHORT).show()
        );
        if (queryResults.length() != 0)
        {
            SearchResultsDataSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
        } else displaySearchResults(searchResponseItems, context, rootView);
    }

    public static void displaySearchResults(ArrayList<ListItem> searchResponseItems,
                                            Context context, View rootView)
    {
        if (searchResponseItems.isEmpty()) Toast.makeText(context, "No results", Toast.LENGTH_SHORT).show();
        RecyclerView rvSearchResults = (RecyclerView) rootView.findViewById(R.id.rvSearchResults);
        LinearLayoutManager llManager = new LinearLayoutManager(context);
        rvSearchResults.setLayoutManager(llManager);
        MainListAdapter adapter = new MainListAdapter(searchResponseItems, context);
        rvSearchResults.setAdapter(adapter);
    }

    public static void addExistingStocks(StocksDailyContainer container,
                                         ArrayList<ListItem> stocksList, String symbol, TinyDB tinyDB)
    {
        ArrayList<String> favorites = tinyDB.getListString("favorites");
        int id = container.getStocksItemsSymbols().indexOf(symbol);
        stocksList.add(container.getStocksItems().get(id));
        if (favorites.contains(symbol)) stocksList.get(stocksList.size()-1).setFavorite(true);
    }

    public static String manageExistingStocks(ArrayList<ListItem> responseItems, String query, TinyDB tinyDB)
    {
        StocksDailyContainer searchedStocksContainer = tinyDB.getObject("searchedStocks", StocksDailyContainer.class);
        ArrayList<String> queryArray = new ArrayList<>(Arrays.asList(query.split(",")));
        for (int i = queryArray.size()-1; i>-1; i--)
        {
            String sym = queryArray.get(i);
            if (searchedStocksContainer.getStocksItemsSymbols().contains(sym))
            {
                addExistingStocks(searchedStocksContainer, responseItems, sym, tinyDB);
                queryArray.remove(i);
            }
        }
//        because I have 8 requests maximum per minute
        if (queryArray.size() > 8) queryArray = new ArrayList<>(queryArray.subList(0, 8));
        return String.join(",", queryArray);
    }
}
