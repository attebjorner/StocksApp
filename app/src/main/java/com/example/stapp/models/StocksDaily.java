package com.example.stapp.models;

import java.util.ArrayList;

public class StocksDaily
{
    private String date;
    private ArrayList<ListItem> stocksItems;
    private final ArrayList<String> stocksItemsSymbols = new ArrayList<>();

    public StocksDaily(String date, ArrayList<ListItem> stocksItems)
    {
        this.date = date;
        this.stocksItems = stocksItems;
        for (ListItem item : stocksItems) stocksItemsSymbols.add(item.getSymbol());
    }

    public String getDate()
    {
        return date;
    }

    public ArrayList<ListItem> getStocksItems()
    {
        return stocksItems;
    }

    public void setStocksItems(ArrayList<ListItem> stocksItems)
    {
        this.stocksItems = stocksItems;
        for (ListItem item : stocksItems) stocksItemsSymbols.add(item.getSymbol());
    }

    public ArrayList<String> getStocksItemsSymbols()
    {
        return stocksItemsSymbols;
    }

    public void updateStocksList(ListItem item)
    {
        stocksItems.add(item);
        stocksItemsSymbols.add(item.getSymbol());
    }
}
