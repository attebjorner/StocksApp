package com.example.stapp.models;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ListItem
{
//    private final String symbol;
    private String symbol;
    private final String name;
    private final String price;
    private final String change;
    private boolean isFavorite;

    public ListItem(String symbol, String name, String price, String change, String percChange)
    {
        this.symbol = symbol;
        this.name = name;
        this.price = "$" + round(Double.parseDouble(price), 2);
        change = Double.toString(round(Double.parseDouble(change), 2));
        percChange = Double.toString(round(Double.parseDouble(percChange), 2));
        change = change.startsWith("-") ? change.replace("-", "-$") : "+$" + change;
        this.change = change + " (" + percChange.replace("-", "") + "%)";
        this.isFavorite = false;
    }

    public ListItem(ListItem item)
    {
        this.symbol = item.getSymbol();
        this.name = item.getName();
        this.price = item.getPrice();
        this.change = this.getChange();
        this.isFavorite = this.isFavorite();
    }

    public void setSymbol(String s)
    {
        this.symbol = s;
    }

    public String getSymbol()
    {
        return symbol;
    }

    public String getName()
    {
        return name;
    }

    public String getPrice()
    {
        return price;
    }

    public String getChange()
    {
        return change;
    }

    public boolean isFavorite()
    {
        return isFavorite;
    }

    public void setFavorite(boolean favorite)
    {
        isFavorite = favorite;
    }

//    TODO: replace setFav to chanchefav
    public void changeFav()
    {
        isFavorite = !isFavorite;
    }

    public static double round(double value, int places)
    {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
