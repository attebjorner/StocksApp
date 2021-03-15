package com.example.stapp.api;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.stapp.ListItem;
import com.example.stapp.R;
import com.example.stapp.TinyDB;
import com.example.stapp.adapters.MainListAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
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
                    searchResponseItems.add(newStock);
                    searchedStocksContainer.updateStocksList(newStock);
                } catch (Exception e) { e.printStackTrace(); }
            }
            tinyDB.putObject("searchedStocks", searchedStocksContainer);

            //TODO: check if new stocks are in favorites

            if (searchResponseItems.isEmpty()) Toast.makeText(context, "No results", Toast.LENGTH_SHORT).show();
            RecyclerView rvSearchResults = (RecyclerView) rootView.findViewById(R.id.rvSearchResults);
            LinearLayoutManager llManager = new LinearLayoutManager(context);
            rvSearchResults.setLayoutManager(llManager);
            MainListAdapter adapter = new MainListAdapter(searchResponseItems, context);
            rvSearchResults.setAdapter(adapter);
        }, error -> Toast.makeText(context, "Error occurred", Toast.LENGTH_SHORT).show()
        );
        SearchResultsDataSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public static void addExistingStocks(StocksDailyContainer container,
                                         ArrayList<ListItem> stocksList, String symbol)
    {
        int id = container.getStocksItemsSymbols().indexOf(symbol);
        stocksList.add(container.getStocksItems().get(id));
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
                addExistingStocks(searchedStocksContainer, responseItems, sym);
                queryArray.remove(i);
            }
        }
//        because I have 8 requests maximum per minute
        if (queryArray.size() > 8) queryArray = new ArrayList<>(queryArray.subList(0, 8));
        return String.join(",", queryArray);
    }
}
