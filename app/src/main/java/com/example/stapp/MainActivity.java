package com.example.stapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private SearchView svStocks;
    private Button btnStocks, btnFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStocks = (Button) findViewById(R.id.btnStocks);
        btnFavorite = (Button) findViewById(R.id.btnFavorite);
        StocksFragment fStocks = new StocksFragment();
        doFragmentTransaction(fStocks);

        svStocks = (SearchView) findViewById(R.id.svStocks);
        int id = svStocks.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) svStocks.findViewById(id);
        textView.setTextColor(getColor(R.color.black));
        textView.setHintTextColor(getColor(R.color.black));
        Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.stemregular);
        textView.setTypeface(typeface);
    }

    public void onClickStocks(View view)
    {
        setActiveButtonStyle(btnStocks, btnFavorite);
        StocksFragment fStocks = new StocksFragment();
        doFragmentTransaction(fStocks);
    }

    public void onClickFavorite(View view)
    {
        setActiveButtonStyle(btnFavorite, btnStocks);
        FavoriteStocksFragment fFavoriteStocks = new FavoriteStocksFragment();
        doFragmentTransaction(fFavoriteStocks);
    }

    private void doFragmentTransaction(Fragment fragment)
    {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFragment, fragment);
        ft.commit();
    }

    private void setActiveButtonStyle(Button btnPressed, Button btnUnpressed)
    {
        btnPressed.setTextSize(28);
        btnPressed.setTextColor(getColor(R.color.black));
        btnUnpressed.setTextSize(20);
        btnUnpressed.setTextColor(getColor(R.color.gray));
    }

}