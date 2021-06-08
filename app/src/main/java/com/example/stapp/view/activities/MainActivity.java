package com.example.stapp.view.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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

import com.example.stapp.R;
import com.example.stapp.utils.TinyDB;
import com.example.stapp.view.fragments.FavoriteFragment;
import com.example.stapp.view.fragments.SearchHistoryFragment;
import com.example.stapp.view.fragments.SearchResultsFragment;
import com.example.stapp.view.fragments.MainStocksFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private SearchView svStocks;
    private Button btnStocks, btnFavorite, lastActiveMenuBtn;
    private LinearLayout llMenuButtons;
    private int idSearchText;
    private TextView searchTextView;
    private View mainFragment;
    private TinyDB tinyDB;
    private int backPressedCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tinyDB = new TinyDB(this);
        mainFragment = (View) findViewById(R.id.mainFragment);
        btnStocks = (Button) findViewById(R.id.btnStocks);
        btnFavorite = (Button) findViewById(R.id.btnFavorite);
        lastActiveMenuBtn = btnStocks;
        try
        {
            doFragmentTransaction(MainStocksFragment.class);
        }
        catch (InstantiationException | IllegalAccessException e)
        {
            e.printStackTrace();
        }

        llMenuButtons = (LinearLayout) findViewById(R.id.llMenuButtons);
        svStocks = (SearchView) findViewById(R.id.svStocks);
        idSearchText = svStocks.getContext().getResources().getIdentifier(
                "android:id/search_src_text", null, null
        );
        searchTextView = (TextView) svStocks.findViewById(idSearchText);
        setSearchViewStyle();
        searchTextView.setOnFocusChangeListener((v, hasFocus) ->
        {
            if (hasFocus)
            {
                try
                {
                    setSearchHistoryFragment();
                }
                catch (IllegalAccessException | InstantiationException e)
                {
                    e.printStackTrace();
                }
            }
        });
        svStocks.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                if (query.length() != 0)
                {
                    addSearchedHistoryQuery(query);
                    try
                    {
                        doFragmentTransaction(SearchResultsFragment.class);
                    }
                    catch (IllegalAccessException | InstantiationException e)
                    {
                        e.printStackTrace();
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                return false;
            }
        });
    }

    //    switch to last active menu fragment && visible menu buttons
    @Override
    public void onBackPressed()
    {
        backPressedCounter++;
        if (backPressedCounter == 2)
        {
            super.onBackPressed();
        }
        mainFragment.animate()
                .translationY(0)
                .setDuration(400)
                .setListener(new AnimatorListenerAdapter()
                {
                    @Override
                    public void onAnimationStart(Animator animation)
                    {
                        super.onAnimationStart(animation);
                        llMenuButtons.animate().alpha(1.0f).setDuration(300);
                        setMenuClickable(true);
                    }
                });
        try
        {
            if (lastActiveMenuBtn.equals(btnStocks))
            {
                onClickStocks(btnStocks);
            }
            else
            {
                onClickFavorite(btnFavorite);
            }
            svStocks.setQuery("", false);
            svStocks.setBackgroundResource(R.drawable.search_rounded);
            svStocks.clearFocus();
            backPressedCounter = 0;
        }
        catch (IllegalAccessException | InstantiationException e)
        {
            e.printStackTrace();
        }
    }

    public void onClickStocks(View view) throws IllegalAccessException, InstantiationException
    {
        setActiveButtonStyle(btnStocks, btnFavorite);
        doFragmentTransaction(MainStocksFragment.class);
    }

    public void onClickFavorite(View view) throws IllegalAccessException, InstantiationException
    {
        setActiveButtonStyle(btnFavorite, btnStocks);
        doFragmentTransaction(FavoriteFragment.class);
    }

    public void setMenuClickable(boolean clickable)
    {
        btnStocks.setClickable(clickable);
        btnFavorite.setClickable(clickable);
    }

    public void doFragmentTransaction(Class<? extends Fragment> fragmentClass)
            throws InstantiationException, IllegalAccessException
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

    private void setSearchViewStyle()
    {
        svStocks.clearFocus();
        searchTextView = (TextView) svStocks.findViewById(idSearchText);
        searchTextView.setTextColor(getColor(R.color.black));
        searchTextView.setHintTextColor(getColor(R.color.black));
        searchTextView.setTextSize(16);
        Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.montserratsemibold);
        searchTextView.setTypeface(typeface);
    }

    private void setSearchHistoryFragment() throws IllegalAccessException, InstantiationException
    {
        llMenuButtons.animate().alpha(0.0f).setDuration(400);
        mainFragment.animate()
                .translationY(-(llMenuButtons.getHeight()))
                .setDuration(400)
                .setListener(new AnimatorListenerAdapter()
                {
                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        super.onAnimationEnd(animation);
                        setMenuClickable(false);
                    }
                });
        doFragmentTransaction(SearchHistoryFragment.class);
    }

    private void addSearchedHistoryQuery(String query)
    {
        List<String> searchedHistory = tinyDB.getListString("searchedHistory");
        if (!searchedHistory.contains(query))
        {
            searchedHistory.add(query);
        }
        if (searchedHistory.size() == 101)
        {
            searchedHistory.remove(0);
        }
        tinyDB.putListString("searchedHistory", new ArrayList<>(searchedHistory));
    }
}
