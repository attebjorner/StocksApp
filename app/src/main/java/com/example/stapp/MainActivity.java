package com.example.stapp;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.stapp.fragments.EmptySearchFragment;
import com.example.stapp.fragments.FavoriteStocksFragment;
import com.example.stapp.fragments.SearchResultsFragment;
import com.example.stapp.fragments.StocksFragment;

public class MainActivity extends AppCompatActivity
{
    private SearchView svStocks;
    private Button btnStocks, btnFavorite, lastActiveMenuBtn;
    private LinearLayout llMenuButtons;
    private int backPressedCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStocks = (Button) findViewById(R.id.btnStocks);
        btnFavorite = (Button) findViewById(R.id.btnFavorite);
        lastActiveMenuBtn = btnStocks;
        try
        {
            doFragmentTransaction(StocksFragment.class);
        } catch (InstantiationException | IllegalAccessException e) { e.printStackTrace(); }

        llMenuButtons = (LinearLayout) findViewById(R.id.llMenuButtons);
        svStocks = (SearchView) findViewById(R.id.svStocks);
        svStocks.clearFocus();
        int idSearchText = svStocks.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView searchTextView = (TextView) svStocks.findViewById(idSearchText);
        searchTextView.setTextColor(getColor(R.color.black));
        searchTextView.setHintTextColor(getColor(R.color.black));
        searchTextView.setTextSize(16);
        Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.montserratsemibold);
        searchTextView.setTypeface(typeface);

        searchTextView.setOnFocusChangeListener((v, hasFocus) ->
        {
            if (hasFocus)
            {
                try
                {
                    setEmptySearchFragment();
                } catch (IllegalAccessException | InstantiationException e) { e.printStackTrace(); }
            }
        });

        svStocks.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                try
                {
                    if (svStocks.getQuery().length() == 0) setEmptySearchFragment();
                    else doFragmentTransaction(SearchResultsFragment.class);
                } catch (IllegalAccessException | InstantiationException e) { e.printStackTrace(); }
                return false;
            }
        });
    }

    public void onClickStocks(View view) throws IllegalAccessException, InstantiationException
    {
        setActiveButtonStyle(btnStocks, btnFavorite);
        doFragmentTransaction(StocksFragment.class);
    }

    public void onClickFavorite(View view) throws IllegalAccessException, InstantiationException
    {
        setActiveButtonStyle(btnFavorite, btnStocks);
        doFragmentTransaction(FavoriteStocksFragment.class);
    }

    public void doFragmentTransaction(Class<? extends Fragment> fragmentClass) throws InstantiationException, IllegalAccessException
    {
        Fragment fragment = fragmentClass.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFragment, fragment);
        ft.commit();
    }

    private void setActiveButtonStyle(Button btnPressed, Button btnUnpressed)
    {
        btnPressed.setTextSize(28);
        btnPressed.setTextColor(getColor(R.color.black));
        btnUnpressed.setTextSize(18);
        btnUnpressed.setTextColor(getColor(R.color.gray));
        lastActiveMenuBtn = btnPressed;
    }

    private void setEmptySearchFragment() throws IllegalAccessException, InstantiationException
    {
        llMenuButtons.setVisibility(View.GONE);
        doFragmentTransaction(EmptySearchFragment.class);
    }

    //    TODO: onbackpressed if 3rd fragm then switch to menu fragms
//    switch to last active menu fragment && visible menu buttons
    @Override
    public void onBackPressed()
    {
        backPressedCounter++;
        if (backPressedCounter == 2) super.onBackPressed();
        llMenuButtons.setVisibility(View.VISIBLE);
        try
        {
            if (lastActiveMenuBtn.equals(btnStocks)) onClickStocks(btnStocks);
            else onClickFavorite(btnFavorite);
            svStocks.clearFocus();
            backPressedCounter = 0;
        } catch (IllegalAccessException | InstantiationException e) { e.printStackTrace(); }
    }
}