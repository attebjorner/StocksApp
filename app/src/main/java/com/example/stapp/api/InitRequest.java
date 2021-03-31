package com.example.stapp.api;

import android.content.Context;

import com.android.volley.AuthFailureError;
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
import java.util.HashMap;
import java.util.Map;

public class InitRequest
{
    public static void getInitStocks(Context context, ResponseListener responseListener)
    {
        ArrayList<ListItem> stocksResponseItems = new ArrayList<>();
        String REQUEST_URL = "https://apidojo-yahoo-finance-v1.p.rapidapi.com/market/get-trending-tickers?region=US";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, REQUEST_URL, null, response ->
        {
            try
            {
                JSONObject tempObj = (JSONObject) response.get("finance");
                JSONArray tempArr = (JSONArray) tempObj.get("result");
                tempObj = (JSONObject) tempArr.get(0);

                JSONArray quotesArray = (JSONArray) tempObj.get("quotes");
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
        }, error -> responseListener.onError("Error occurred"))
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("x-rapidapi-key", "25dd633a04msh499a426602c4beap1eb72cjsn4ae6df4e0a74");
                params.put("x-rapidapi-host", "apidojo-yahoo-finance-v1.p.rapidapi.com");
                return params;
            }
        };
        RequestsSingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
}
