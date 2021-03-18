package com.example.stapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebView;

import com.example.stapp.R;
import com.example.stapp.WidgetRequests;
import com.example.stapp.adapters.DetailsMenuAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class StockDetailsActivity extends AppCompatActivity
{
    private static final ArrayList<String> MENU_BUTTONS = new ArrayList<>(
            Arrays.asList("Stock Chart", "Profile", "Financials", "Recommendations", "ESP Estimates")
    );

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_details);

//TODO: recycler view
        RecyclerView rvMenu = (RecyclerView) findViewById(R.id.rvDetailsMenu);
        LinearLayoutManager llManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvMenu.setLayoutManager(llManager);
        rvMenu.setAdapter(new DetailsMenuAdapter(MENU_BUTTONS));
        

        WebView webView = (WebView) findViewById(R.id.detailWebView);
        String stock = "AAPL";
        String htmlWidget = WidgetRequests.getFinancials(stock);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadData(htmlWidget, "text/html; charset=utf-8", "UTF-8");

//        TODO: override back to go back to previous fragment
    }
}