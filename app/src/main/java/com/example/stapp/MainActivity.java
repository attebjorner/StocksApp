package com.example.stapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.stapp.adapters.EmptySearchAdapter;
import com.example.stapp.fragments.EmptySearchFragment;
import com.example.stapp.fragments.FavoriteStocksFragment;
import com.example.stapp.fragments.StocksFragment;

public class MainActivity extends AppCompatActivity
{
    private SearchView svStocks;
    private Button btnStocks, btnFavorite;
    private LinearLayout llMenuButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStocks = (Button) findViewById(R.id.btnStocks);
        btnFavorite = (Button) findViewById(R.id.btnFavorite);
        StocksFragment fStocks = new StocksFragment();
        doFragmentTransaction(fStocks);

        llMenuButtons = (LinearLayout) findViewById(R.id.llMenuButtons);
        svStocks = (SearchView) findViewById(R.id.svStocks);
        svStocks.clearFocus();
        int idSearchText = svStocks.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) svStocks.findViewById(idSearchText);
        textView.setTextColor(getColor(R.color.black));
        textView.setHintTextColor(getColor(R.color.black));
        textView.setTextSize(16);
        Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.montserratsemibold);
        textView.setTypeface(typeface);

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
                if (svStocks.getQuery().length() == 0)
                {
                    llMenuButtons.setVisibility(View.GONE);
                    EmptySearchFragment emptySearchFragment = new EmptySearchFragment();
                    doFragmentTransaction(emptySearchFragment);
                }
                return false;
            }
        });
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
        btnUnpressed.setTextSize(18);
        btnUnpressed.setTextColor(getColor(R.color.gray));
    }

}