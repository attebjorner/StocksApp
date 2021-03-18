package com.example.stapp.mainActivityApi;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.lang.ref.WeakReference;

public class SearchResultsDataSingleton
{
    private static SearchResultsDataSingleton instance;
    private RequestQueue requestQueue;
    private static WeakReference<Context> ctx;

    private SearchResultsDataSingleton(Context context)
    {
        ctx = new WeakReference<>(context);
        requestQueue = getRequestQueue();
    }

    public static synchronized SearchResultsDataSingleton getInstance(Context context)
    {
        if (instance == null) instance = new SearchResultsDataSingleton(context);
        return instance;
    }

    public RequestQueue getRequestQueue()
    {
        if (requestQueue == null)
        {
            requestQueue = Volley.newRequestQueue(ctx.get().getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req)
    {
        getRequestQueue().add(req);
    }
}
