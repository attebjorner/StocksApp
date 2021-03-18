package com.example.stapp.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CurrentDate
{
    private static final Date date = new Date();
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    public static String now()
    {
        return format.format(date);
    }
}
