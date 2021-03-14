package com.example.stapp;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ListItem
{
    private final String symbol;
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

    public ListItem(String symbol, String name, int c, int pc)
    {
        this(
                symbol, name, String.valueOf(c),
                String.valueOf(c - pc), String.valueOf(((c - pc) * 100) / pc)
        );
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

    public static double round(double value, int places)
    {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
