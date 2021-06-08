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
import java.util.List;

public class StockDetailsActivity extends AppCompatActivity
{
    private final List<String> MENU_BUTTONS = new ArrayList<>(Arrays.asList(
            "Stock Chart", "Recommendations", "Profile", "ESP Estimates", "Financials"
    ));
    private final int[] STAR_COLORS = new int[]{R.drawable.star_details_empty,
            R.drawable.star_details_yellow};
    private String[] stockInfo;
    private TextView tvSymbol;
    private TextView tvName;
    private WebView webView;
    private RecyclerView rvMenu;
    private ImageButton btnBack;
    private ImageButton btnFavorite;
    private List<String> favorites;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_details);

        Intent inIntent = getIntent();
        stockInfo = inIntent.getStringArrayExtra("stockInfo");
        TinyDB tinyDB = new TinyDB(this);
        favorites = tinyDB.getListString("favorites");

        setHeader();
        setHeaderImageButtons();
        setInitWebView();
        setRvMenu();

        btnFavorite.setOnClickListener(v ->
        {
            btnFavorite.setBackgroundResource(STAR_COLORS[favorites.contains(stockInfo[0]) ? 0 : 1]);
            if (favorites.contains(stockInfo[0]))
            {
                favorites.remove(stockInfo[0]);
            }
            else
            {
                favorites.add(stockInfo[0]);
            }
            tinyDB.putListString("favorites", new ArrayList<>(favorites));
            List<ListItem> itemList = tinyDB.getListObject("clickedList", ListItem.class);
            itemList.get(tinyDB.getInt("clickedPos")).changeFav();
            tinyDB.putListObject("clickedList", new ArrayList<>(itemList));
        });
    }

    public void setHeaderImageButtons()
    {
        btnBack = (ImageButton) findViewById(R.id.barBackArrow);
        btnBack.setOnClickListener(v -> onBackPressed());
        btnFavorite = (ImageButton) findViewById(R.id.barFavorite);
        if (favorites.contains(stockInfo[0]))
        {
            btnFavorite.setBackgroundResource(STAR_COLORS[1]);
        }
    }

    public void setHeader()
    {
        tvSymbol = (TextView) findViewById(R.id.tvSymbolDetail);
        tvName = (TextView) findViewById(R.id.tvNameDetail);
        tvSymbol.setText(stockInfo[0]);
        tvName.setText(stockInfo[1]);
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void setInitWebView()
    {
        webView = (WebView) findViewById(R.id.detailWebView);
        String htmlWidget = WidgetRequests.getWidget(stockInfo[0], 0);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadData(htmlWidget, "text/html; charset=utf-8", "UTF-8");
        webView.loadUrl(htmlWidget);
    }

    public void setRvMenu()
    {
        rvMenu = (RecyclerView) findViewById(R.id.rvDetailsMenu);
        LinearLayoutManager llManager = new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false
        );
        rvMenu.setLayoutManager(llManager);
        rvMenu.setAdapter(new DetailsMenuAdapter(MENU_BUTTONS, webView, stockInfo[0], this));
    }
}