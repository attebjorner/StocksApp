package com.example.stapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stapp.R;

import java.util.LinkedList;

public class EmptySearchAdapter extends RecyclerView.Adapter<EmptySearchAdapter.EmptySearchViewHolder>
{
    private final LinkedList<String> searchedList;

    public EmptySearchAdapter(LinkedList<String> searchedList)
    {
        this.searchedList = searchedList;
    }

    @NonNull
    @Override
    public EmptySearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.searched_list_item, parent, false
                );
        return new EmptySearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmptySearchViewHolder holder, int position)
    {
        holder.tvSearched.setText(searchedList.get(position));
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
}
