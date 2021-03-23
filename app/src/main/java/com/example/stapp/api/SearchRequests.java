package com.example.stapp.api;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.stapp.models.ListItem;
import com.example.stapp.models.StocksDaily;
import com.example.stapp.utils.TinyDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class SearchRequests
{
    public static void getSearchResults(Context context, String searchQuery,
                                        ResponseListener responseListener)
    {
        final StringBuffer[] queryResults = {new StringBuffer()};
        String LOOKUP_REQUEST = "https://finnhub.io/api/v1/search?q=" + searchQuery
                + "&token=c18e4dn48v6oak5h46ug";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, LOOKUP_REQUEST, null, response ->
        {
            try
            {
                JSONArray quotesArray = (JSONArray) response.get("result");
                for (int i = 0; i < quotesArray.length(); i++)
                {
                    JSONObject temp = quotesArray.getJSONObject(i);
                    queryResults[0].append(temp.get("symbol").toString());
                    if (i != quotesArray.length() - 1) queryResults[0].append(",");
                }
            } catch (JSONException e) { e.printStackTrace(); }

            //get comma-separated string query results and then search for them
            getSearchResultsData(context, queryResults[0].toString(), responseListener);

        }, error -> responseListener.onError("Error occurred")
        );
//        because finnhub works very slow sometimes
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        RequestsSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public static void getSearchResultsData(Context context, String queryResults,
                                            ResponseListener responseListener)
    {
        TinyDB tinyDB = new TinyDB(context);
        ArrayList<ListItem> searchResponseItems = new ArrayList<>();
        //fun to return query without shit which i have
        queryResults = manageExistingStocks(searchResponseItems, queryResults, tinyDB);
        String LOOKUP_DATA_REQUEST = "https://api.twelvedata.com/quote?symbol=" + queryResults
                + "&apikey=ba5fd509861a483ebba2d3cebda53e84";
        StocksDaily searchedStocksContainer = tinyDB.getObject("searchedStocks", StocksDaily.class);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, LOOKUP_DATA_REQUEST, null, response ->
        {
            ArrayList<String> favorites = tinyDB.getListString("favorites");
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
//            TODO:
            responseListener.onResponse(searchResponseItems);
        }, error -> responseListener.onError("Error occurred")
        );

        if (queryResults.length() != 0)
        {
            RequestsSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
        } else responseListener.onResponse(searchResponseItems);
    }

    public static void addExistingStocks(StocksDaily container, ArrayList<ListItem> stocksList,
                                         String symbol, TinyDB tinyDB)
    {
        ArrayList<String> favorites = tinyDB.getListString("favorites");
        int id = container.getStocksItemsSymbols().indexOf(symbol);
        stocksList.add(container.getStocksItems().get(id));
        if (favorites.contains(symbol)) stocksList.get(stocksList.size()-1).setFavorite(true);
    }

    public static String manageExistingStocks(ArrayList<ListItem> responseItems,
                                              String query, TinyDB tinyDB)
    {
        StocksDaily searchedStocksContainer = tinyDB.getObject("searchedStocks", StocksDaily.class);
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

        StringBuilder queries = new StringBuilder();
        for (String s : queryArray) queries.append(s).append(",");
        queries.deleteCharAt(queries.length()-1);
        return queries.toString();
    }
}
