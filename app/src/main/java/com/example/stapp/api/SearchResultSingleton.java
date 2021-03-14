package com.example.stapp.api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.lang.ref.WeakReference;

public class SearchResultSingleton
{
    private static SearchResultSingleton instance;
    private RequestQueue requestQueue;
    private static WeakReference<Context> ctx;

    private SearchResultSingleton(Context context)
    {
        ctx = new WeakReference<>(context);
        requestQueue = getRequestQueue();
    }

    public static synchronized SearchResultSingleton getInstance(Context context)
    {
        if (instance == null) instance = new SearchResultSingleton(context);
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
