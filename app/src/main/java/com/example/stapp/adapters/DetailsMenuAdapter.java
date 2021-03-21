package com.example.stapp.adapters;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stapp.R;
import com.example.stapp.utils.WidgetRequests;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class DetailsMenuAdapter extends RecyclerView.Adapter<DetailsMenuAdapter.ViewHolder>
{
    private final ArrayList<String> menuButtons;
    private static WeakReference<WebView> webView;
    private static String symbol;
    private static WeakReference<TextView> oldMenu;
    private static final int[] COLORS = {0xFF1A1A1A, 0xFFBABABA};

    public DetailsMenuAdapter(ArrayList<String> menuButtons, WebView webView, String symbol)
    {
        this.menuButtons = menuButtons;
        DetailsMenuAdapter.webView = new WeakReference<>(webView);
        DetailsMenuAdapter.symbol = symbol;
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
        if (position == 0)
        {
            holder.tvMenuItem.setTextSize(18);
            holder.tvMenuItem.setTextColor(COLORS[0]);
            oldMenu = new WeakReference<>(holder.tvMenuItem);
        }
        holder.tvMenuItem.setOnClickListener(new MenuListener(
                holder.tvMenuItem, position
        ));
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

    public static class MenuListener implements View.OnClickListener
    {
        private final TextView newMenu;
        private final int position;

        public MenuListener(TextView newMenu, int position)
        {
            this.newMenu = newMenu;
            this.position = position;
        }

        @SuppressLint("SetJavaScriptEnabled")
        @Override
        public void onClick(View v)
        {
            newMenu.setTextSize(18);
            newMenu.setTextColor(COLORS[0]);
            oldMenu.get().setTextSize(14);
            oldMenu.get().setTextColor(COLORS[1]);
            oldMenu = new WeakReference<>(newMenu);
            String widget = WidgetRequests.getWidget(symbol, position);
            webView.get().getSettings().setJavaScriptEnabled(true);
            if (position % 2 == 0) webView.get().loadData(widget, "text/html; charset=utf-8", "UTF-8");
            else webView.get().loadUrl(widget);
        }
    }
}
