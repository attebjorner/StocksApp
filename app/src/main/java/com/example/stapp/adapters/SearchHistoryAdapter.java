package com.example.stapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stapp.R;

import java.lang.ref.WeakReference;
import java.util.List;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder>
{
    private final List<String> searchedList;
    private static WeakReference<SearchView> svStocks;

    public SearchHistoryAdapter(List<String> searchedList, SearchView svStocks)
    {
        this.searchedList = searchedList;
        SearchHistoryAdapter.svStocks = new WeakReference<>(svStocks);
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
                new SearchedListener(searchedList.get(position))
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

        public SearchedListener(String searched)
        {
            this.searched = searched;
        }

        @Override
        public void onClick(View v)
        {
            svStocks.get().setQuery(searched, true);
            svStocks.get().clearFocus();
        }
    }
}
