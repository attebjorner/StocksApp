package com.example.stapp.api;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.stapp.ListItem;
import com.example.stapp.R;
import com.example.stapp.TinyDB;
import com.example.stapp.adapters.MainListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;

public class InitStocksRequest
{
    public static void getMboumStocks(Context context, View rootView)
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
                                    temp.get("regularMarketPrice").toString(), temp.get("regularMarketChange").toString(),
                                    temp.get("regularMarketChangePercent").toString()
                            ));
                        }
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }

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

                    tinyDB.putObject("mainStocks", new StocksDailyContainer(LocalDate.now(), stocksResponseItems));

                    RecyclerView rvStocks = (RecyclerView) rootView.findViewById(R.id.rvStocks);
                    LinearLayoutManager llManager = new LinearLayoutManager(context);
                    rvStocks.setLayoutManager(llManager);
                    MainListAdapter adapter = new MainListAdapter(stocksResponseItems, context);
                    rvStocks.setAdapter(adapter);
                }, error -> Toast.makeText(context, "Error occurred", Toast.LENGTH_SHORT).show()
        );
        InitStocksSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
}
