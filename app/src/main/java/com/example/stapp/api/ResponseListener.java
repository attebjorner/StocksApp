package com.example.stapp.api;

import com.example.stapp.models.ListItem;

import java.util.List;

public interface ResponseListener
{
    void onResponse(List<ListItem> stocksResponseItems);

    void onError(String message);
}
