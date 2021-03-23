package com.example.stapp.view.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.stapp.R;
import com.example.stapp.models.ListItem;
import com.example.stapp.utils.TinyDB;
import com.example.stapp.utils.WidgetRequests;
import com.example.stapp.adapters.DetailsMenuAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

public class StockDetailsActivity extends AppCompatActivity
{
    private final ArrayList<String> MENU_BUTTONS = new ArrayList<>(Arrays.asList(
            "Stock Chart", "Recommendations", "Profile", "ESP Estimates", "Financials"
    ));
    private final int[] STAR_COLORS = new int[]{R.drawable.star_details_empty,
                                                R.drawable.star_details_yellow};

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_details);

        Intent inIntent = getIntent();
        String[] stockInfo = inIntent.getStringArrayExtra("stockInfo");

        TinyDB tinyDB = new TinyDB(this);
        ArrayList<String> favorites = tinyDB.getListString("favorites");

        ImageButton btnBack = (ImageButton) findViewById(R.id.barBackArrow);
        btnBack.setOnClickListener(v -> onBackPressed());
        ImageButton btnFavorite = (ImageButton) findViewById(R.id.barFavorite);
        if (favorites.contains(stockInfo[0])) btnFavorite.setBackgroundResource(STAR_COLORS[1]);

        btnFavorite.setOnClickListener(v ->
        {
            btnFavorite.setBackgroundResource(STAR_COLORS[favorites.contains(stockInfo[0]) ? 0 : 1]);
            if (favorites.contains(stockInfo[0])) favorites.remove(stockInfo[0]);
            else favorites.add(stockInfo[0]);
            tinyDB.putListString("favorites", favorites);
            ArrayList<ListItem> itemList = tinyDB.getListObject("clickedList", ListItem.class);
            itemList.get(tinyDB.getInt("clickedPos")).changeFav();
            tinyDB.putListObject("clickedList", itemList);
//            ListItem item = tinyDB.getObject("clicked", ListItem.class);
//            item.setFavorite(!item.isFavorite());
//            tinyDB.putObject("clicked", item);
        });

        TextView tvSymbol = (TextView) findViewById(R.id.tvSymbolDetail);
        TextView tvName = (TextView) findViewById(R.id.tvNameDetail);
        tvSymbol.setText(stockInfo[0]);
        tvName.setText(stockInfo[1]);

        WebView webView = (WebView) findViewById(R.id.detailWebView);
        String htmlWidget = WidgetRequests.getWidget(stockInfo[0], 0);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadData(htmlWidget, "text/html; charset=utf-8", "UTF-8");
        webView.loadUrl(htmlWidget);

        RecyclerView rvMenu = (RecyclerView) findViewById(R.id.rvDetailsMenu);
        LinearLayoutManager llManager = new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false
        );
        rvMenu.setLayoutManager(llManager);
        rvMenu.setAdapter(new DetailsMenuAdapter(MENU_BUTTONS, webView, stockInfo[0]));
    }
}