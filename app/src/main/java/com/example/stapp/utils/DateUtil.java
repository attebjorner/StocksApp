package com.example.stapp.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil
{
    private static final java.util.Date date = new java.util.Date();
    @SuppressLint("SimpleDateFormat")
    private static final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    public static String now()
    {
        return format.format(date);
    }
}
