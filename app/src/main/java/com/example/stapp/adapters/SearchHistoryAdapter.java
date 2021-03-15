package com.example.stapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stapp.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.EmptySearchViewHolder>
{
    private final ArrayList<String> searchedList;
    private final SearchView svStocks;

    public SearchHistoryAdapter(ArrayList<String> searchedList, SearchView svStocks)
    {
        this.searchedList = searchedList;
        this.svStocks = svStocks;
    }

    @NonNull
    @Override
    public EmptySearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.searched_list_item, parent, false);
        return new EmptySearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmptySearchViewHolder holder, int position)
    {
        holder.tvSearched.setText(searchedList.get(position));
        holder.tvSearched.setOnClickListener(
                new SearchedListener(searchedList.get(position), svStocks)
        );
    }

    @Override
    public int getItemCount()
    {
        return searchedList.size();
    }

    public final static class EmptySearchViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvSearched;

        public EmptySearchViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tvSearched = (TextView) itemView.findViewById(R.id.tvSearched);
        }
    }

    public static class SearchedListener implements View.OnClickListener
    {
        private final String searched;
        private final SearchView svStocks;

        public SearchedListener(String searched, SearchView svStocks)
        {
            this.searched = searched;
            this.svStocks = svStocks;
        }

        @Override
        public void onClick(View v)
        {
            svStocks.setQuery(searched, true);
            svStocks.clearFocus();
        }
    }
}
