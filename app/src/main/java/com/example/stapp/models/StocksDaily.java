package com.example.stapp.models;

import java.util.ArrayList;
import java.util.List;

public class StocksDaily
{
    private String date;
    private List<ListItem> stocksItems;
    private final List<String> stocksItemsSymbols = new ArrayList<>();

    public StocksDaily(String date, List<ListItem> stocksItems)
    {
        this.date = date;
        this.stocksItems = stocksItems;
        for (ListItem item : stocksItems) stocksItemsSymbols.add(item.getSymbol());
    }

    public String getDate()
    {
        return date;
    }

    public List<ListItem> getStocksItems()
    {
        return stocksItems;
    }

    public void setStocksItems(List<ListItem> stocksItems)
    {
        this.stocksItems = stocksItems;
        for (ListItem item : stocksItems) stocksItemsSymbols.add(item.getSymbol());
    }

    public List<String> getStocksItemsSymbols()
    {
        return stocksItemsSymbols;
    }

    public void updateStocksList(ListItem item)
    {
        stocksItems.add(item);
        stocksItemsSymbols.add(item.getSymbol());
    }
}
