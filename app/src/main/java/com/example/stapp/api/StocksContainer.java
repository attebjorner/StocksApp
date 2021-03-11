package com.example.stapp.api;

import com.example.stapp.ListItem;

import java.time.LocalDate;
import java.util.ArrayList;

public class StocksContainer
{
    private String date;
    private ArrayList<ListItem> mainStocks;

    public StocksContainer(LocalDate date, ArrayList<ListItem> mainStocks)
    {
        this.date = date.toString();
        this.mainStocks = mainStocks;
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

    public void setMainStocks(ArrayList<ListItem> mainStocks)
    {
        this.mainStocks = mainStocks;
    }

    public ArrayList<ListItem> getMainStocks()
    {
        return mainStocks;
    }
}
