package com.example.stapp.api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.stapp.models.ListItem;
import com.example.stapp.models.StocksDaily;
import com.example.stapp.utils.DateUtil;
import com.example.stapp.utils.TinyDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class InitRequest
{
    public static void getInitStocks(Context context, ResponseListener responseListener)
    {
        ArrayList<ListItem> stocksResponseItems = new ArrayList<>();
        String REQUEST_URL = "https://mboum.com/api/v1/co/collections/?list=most_actives&start=1&" +
                "apikey=xThUx6KqmURgMfT1xXXjcT8GIpB1zeJ6ghcWqT3eHeOTlJTtw9DnDq9qWP2g";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, REQUEST_URL, null, response ->
        {
            try
            {
                JSONArray quotesArray = (JSONArray) response.get("quotes");
                for (int i = 0; i < quotesArray.length(); i++)
                {
                    JSONObject temp = quotesArray.getJSONObject(i);
                    stocksResponseItems.add(new ListItem(
                            temp.get("symbol").toString(), temp.get("shortName").toString(),
                            temp.get("regularMarketPrice").toString(),
                            temp.get("regularMarketChange").toString(),
                            temp.get("regularMarketChangePercent").toString()
                    ));
                }
            } catch (JSONException e) { e.printStackTrace(); }

            TinyDB tinyDB = new TinyDB(context);
            ArrayList<String> favorites;
            favorites = tinyDB.getListString("favorites");

            for (int i = 0; i < stocksResponseItems.size(); i++)
            {
                if (favorites.contains(stocksResponseItems.get(i).getSymbol()))
                {
                    stocksResponseItems.get(i).setFavorite(true);
                }
            }
            tinyDB.putObject("mainStocks", new StocksDaily(DateUtil.now(), stocksResponseItems));

            responseListener.onResponse(stocksResponseItems);
        }, error -> responseListener.onError("Error occurred"));
        RequestsSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
}
