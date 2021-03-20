package com.example.stapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import com.example.stapp.R;
import com.example.stapp.utils.WidgetRequests;
import com.example.stapp.adapters.DetailsMenuAdapter;

import org.w3c.dom.Text;

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

        Intent inIntent = getIntent();
        String[] stockInfo = inIntent.getStringArrayExtra("stockInfo");

        TextView tvSymbol = (TextView) findViewById(R.id.tvSymbolDetail);
        TextView tvName = (TextView) findViewById(R.id.tvNameDetail);
        tvSymbol.setText(stockInfo[0]);
        tvName.setText(stockInfo[1]);

//TODO: recycler view
        RecyclerView rvMenu = (RecyclerView) findViewById(R.id.rvDetailsMenu);
        LinearLayoutManager llManager = new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false
        );
        rvMenu.setLayoutManager(llManager);
        rvMenu.setAdapter(new DetailsMenuAdapter(MENU_BUTTONS));
        

        WebView webView = (WebView) findViewById(R.id.detailWebView);
//        String stock = "AAPL";
        String htmlWidget = WidgetRequests.getRecommendations(stockInfo[0]);
        webView.getSettings().setJavaScriptEnabled(true);
//        webView.loadData(htmlWidget, "text/html; charset=utf-8", "UTF-8");
        webView.loadUrl(htmlWidget);
    }
}