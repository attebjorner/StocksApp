package com.example.stapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stapp.view.activities.StockDetailsActivity;
import com.example.stapp.models.ListItem;
import com.example.stapp.R;
import com.example.stapp.utils.TinyDB;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class StocksListAdapter extends RecyclerView.Adapter<StocksListAdapter.ViewHolder>
{
    private final ArrayList<ListItem> stocksList;
    private final TinyDB tinyDB;
    private final Context context;
    private static final int[] ITEM_COLORS = new int[]{0xFFF0F4F7, 0xFFFFFFFF};
    private static final int[] CHANGE_COLORS = new int[]{0xFF24B25D, 0xFFB22424}; //green, red
    private static final int[] STAR_COLORS = new int[]{0xFFBABABA, 0xFFFFCA1C}; //gray, yellow
    private static final String IMAGE_URL = "https://finnhub.io/api/logo?symbol=";

    public StocksListAdapter(ArrayList<ListItem> stocksList, Context context)
    {
        this.stocksList = stocksList;
        this.context = context;
        tinyDB = new TinyDB(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item, parent, false
        );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        holder.itemView.setBackgroundResource(R.drawable.stocks_list_item_shape);
        GradientDrawable drawable = (GradientDrawable) holder.itemView.getBackground();
        drawable.setColor(ITEM_COLORS[position % 2]);
        holder.itemView.setOnClickListener(new ListItemListener(
                context, stocksList, position, tinyDB
        ));

        holder.tvSymbol.setText(stocksList.get(position).getSymbol());
        holder.tvName.setText(stocksList.get(position).getName());
        holder.tvPrice.setText(stocksList.get(position).getPrice());
        holder.tvChange.setText(stocksList.get(position).getChange());
        holder.tvChange.setTextColor(
                stocksList.get(position).getChange().startsWith("-")
                        ? CHANGE_COLORS[1] : CHANGE_COLORS[0]
        );
        Picasso.get()
                .load(IMAGE_URL + stocksList.get(position).getSymbol())
                .transform(new RoundedCornersTransformation(20, 0))
                .into(holder.imvLogo);
        holder.imbFavorite.setColorFilter(
                stocksList.get(position).isFavorite() ? STAR_COLORS[1] : STAR_COLORS[0]
        );
        holder.imbFavorite.setOnClickListener(
                new IsFavoriteListener(stocksList.get(position), holder.imbFavorite, tinyDB)
        );
    }

    @Override
    public int getItemCount()
    {
        return stocksList.size();
    }

    public final static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvSymbol, tvName, tvPrice, tvChange;
        ImageButton imbFavorite;
        ImageView imvLogo;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            tvSymbol = (TextView) itemView.findViewById(R.id.symbol);
            tvName = (TextView) itemView.findViewById(R.id.name);
            tvPrice = (TextView) itemView.findViewById(R.id.price);
            tvChange = (TextView) itemView.findViewById(R.id.change);
            imbFavorite = (ImageButton) itemView.findViewById(R.id.isFavorite);
            imvLogo = (ImageView) itemView.findViewById(R.id.stockLogo);
        }
    }

    public static class IsFavoriteListener implements View.OnClickListener
    {
        private final ListItem item;
        private final ImageButton imbFavorite;
        private final TinyDB tinyDB;

        public IsFavoriteListener(ListItem item, ImageButton imbFavorite, TinyDB tinyDB)
        {
            this.item = item;
            this.imbFavorite = imbFavorite;
            this.tinyDB = tinyDB;
        }

        @Override
        public void onClick(View v)
        {
            item.setFavorite(!item.isFavorite());
            imbFavorite.setColorFilter(item.isFavorite() ? STAR_COLORS[1] : STAR_COLORS[0]);
            ArrayList<String> temp = tinyDB.getListString("favorites");
            if (item.isFavorite()) temp.add(item.getSymbol());
            else temp.remove(item.getSymbol());
            tinyDB.putListString("favorites", temp);
        }
    }

    public static class ListItemListener implements View.OnClickListener
    {
        private final Context context;
        private final String[] stockInfo = new String[2];
        private final TinyDB tinyDB;
        private final ArrayList<ListItem> itemList;
        private final int position;

        ListItemListener(Context context, ArrayList<ListItem> itemList, int position, TinyDB tinyDB)
        {
            this.context = context;
            stockInfo[0] = itemList.get(position).getSymbol();
            stockInfo[1] = itemList.get(position).getName();
            this.tinyDB = tinyDB;
            this.itemList = itemList;
            this.position = position;
        }

        @Override
        public void onClick(View v)
        {
//            tinyDB.putObject("clicked", itemList.get(position));
            tinyDB.putListObject("clickedList", itemList);
            tinyDB.putInt("clickedPos", position);
            Intent intent = new Intent(context, StockDetailsActivity.class);
            intent.putExtra("stockInfo", stockInfo);
            context.startActivity(intent);
        }
    }
}
