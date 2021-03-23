package com.example.stapp.api;

import com.example.stapp.models.ListItem;

import java.util.ArrayList;

public interface ResponseListener
{
    void onResponse(ArrayList<ListItem> stocksResponseItems);

    void onError(String message);
}
