package com.example.stapp.api;

import com.example.stapp.ListItem;

import java.time.LocalDate;
import java.util.ArrayList;

public class StocksDailyContainer
{
    private String date;
    private ArrayList<ListItem> stocksItems;
    private final ArrayList<String> stocksItemsSymbols = new ArrayList<>();

    public StocksDailyContainer(LocalDate date, ArrayList<ListItem> stocksItems)
    {
        this.date = date.toString();
        this.stocksItems = stocksItems;
        for (ListItem item : stocksItems) stocksItemsSymbols.add(item.getSymbol());
    }

    public void setDate(LocalDate date)
    {
        this.date = date.toString();
    }

    public void setDateMan(String date)
    {
        this.date = date;
    }

    public String getDate()
    {
        return date;
    }

    public void setStocksItems(ArrayList<ListItem> stocksItems)
    {
        this.stocksItems = stocksItems;
    }

    public ArrayList<ListItem> getStocksItems()
    {
        return stocksItems;
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
