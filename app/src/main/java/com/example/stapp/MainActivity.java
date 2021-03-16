package com.example.stapp;

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

import com.example.stapp.fragments.FavoriteStocksFragment;
import com.example.stapp.fragments.SearchHistoryFragment;
import com.example.stapp.fragments.SearchResultsFragment;
import com.example.stapp.fragments.StocksFragment;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private SearchView svStocks;
    private Button btnStocks, btnFavorite, lastActiveMenuBtn;
    private LinearLayout llMenuButtons;
    private View mainFragment;
    private TinyDB tinyDB;
    private int backPressedCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tinyDB = new TinyDB(this);

        ArrayDeque<String> test = new ArrayDeque<>();
        test.push("hui1");
        test.push("hui2");
        test.push("hui3");
        test.push("hui4");
        test.push("hui5");
        test.push("hui6");
        test.push("hui7");
        test.push("hui8");

//        tinyDB.putObject("testArrayDeque", test);
//        ArrayDeque<String> test2 = tinyDB.getObject("testArrayDeque", ArrayDeque.class);
//        for (String s : test2) System.out.println(s);

        mainFragment = (View) findViewById(R.id.mainFragment);
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
        int idSearchText = svStocks.getContext().getResources().getIdentifier(
                "android:id/search_src_text", null, null
        );
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
                    setSearchHistoryFragment();
                } catch (IllegalAccessException | InstantiationException e) { e.printStackTrace(); }
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
                    } catch (IllegalAccessException | InstantiationException e) { e.printStackTrace(); }
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
        if (backPressedCounter == 2) super.onBackPressed();
        llMenuButtons.animate().translationY(0).alpha(1.0f).setDuration(500);
        mainFragment.animate()
                .translationY(0)
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter()
                {
                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        super.onAnimationEnd(animation);
                        llMenuButtons.setVisibility(View.VISIBLE);
                    }
                });
        try
        {
            if (lastActiveMenuBtn.equals(btnStocks)) onClickStocks(btnStocks);
            else onClickFavorite(btnFavorite);
            svStocks.clearFocus();
            backPressedCounter = 0;
        } catch (IllegalAccessException | InstantiationException e) { e.printStackTrace(); }
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

    private void setSearchHistoryFragment() throws IllegalAccessException, InstantiationException
    {
        llMenuButtons.animate().alpha(0.0f).setDuration(500);
        mainFragment.animate()
                .translationY(-(llMenuButtons.getHeight()))
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter()
                {
                    @Override
                    public void onAnimationEnd(Animator animation)
                    {
                        super.onAnimationEnd(animation);
                        llMenuButtons.setVisibility(View.INVISIBLE);
                    }
                });
        doFragmentTransaction(SearchHistoryFragment.class);
    }

    private void addSearchedHistoryQuery(String query)
    {
        ArrayList<String> searchedHistory = tinyDB.getListString("searchedHistory");
        if (!searchedHistory.contains(query)) searchedHistory.add(query);
        if (searchedHistory.size() == 101) searchedHistory.remove(0);
        tinyDB.putListString("searchedHistory", searchedHistory);
    }
}