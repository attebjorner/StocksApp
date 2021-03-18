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

public class DetailsMenuAdapter extends RecyclerView.Adapter<DetailsMenuAdapter.ViewHolder>
{
    private final ArrayList<String> menuButtons;

    public DetailsMenuAdapter(ArrayList<String> menuButtons)
    {
        this.menuButtons = menuButtons;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.details_menu_item, parent, false
        );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.tvMenuItem.setText(menuButtons.get(position));
        //TODO: manage colors etc
    }

    @Override
    public int getItemCount()
    {
        return menuButtons.size();
    }

    public final static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvMenuItem;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tvMenuItem = (TextView) itemView.findViewById(R.id.tvDetailsMenu);
        }
    }

//    TODO: listneer
//    public static class MenuListener implements View.OnClickListener
//    {
//        private final TextView tvMenu;
//
//        public MenuListener(TextView tvMenu)
//        {
//            this.tvMenu = tvMenu;
//        }
//
//        @Override
//        public void onClick(View v)
//        {
//
//        }
//    }
}
