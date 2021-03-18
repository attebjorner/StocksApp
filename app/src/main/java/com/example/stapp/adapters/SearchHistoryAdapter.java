package com.example.stapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stapp.R;

import java.util.ArrayList;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder>
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.searched_list_item, parent, false
        );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
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

    public final static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvSearched;

        public ViewHolder(@NonNull View itemView)
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
