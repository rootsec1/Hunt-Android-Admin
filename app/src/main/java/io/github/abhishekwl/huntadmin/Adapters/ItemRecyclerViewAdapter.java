package io.github.abhishekwl.huntadmin.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.abhishekwl.huntadmin.Models.Item;
import io.github.abhishekwl.huntadmin.R;

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ItemViewHolder> {

    private Context context;
    private ArrayList<Item> itemArrayList;

    public ItemRecyclerViewAdapter(Context context, ArrayList<Item> itemArrayList) {
        this.context = context;
        this.itemArrayList = itemArrayList;
    }

    @NonNull
    @Override
    public ItemRecyclerViewAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_item, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemRecyclerViewAdapter.ItemViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemListItemImageView)
        ImageView itemImageView;
        @BindView(R.id.itemListItemNameTextView)
        TextView itemNameTextView;
        @BindView(R.id.itemListItemOriginalPriceTextView)
        TextView itemOriginalPriceTextView;
        @BindView(R.id.itemListItemFinalPriceTextView)
        TextView itemFinalPriceTextView;
        @BindView(R.id.itemListItemRootLinearLayout)
        LinearLayout itemLinearLayout;

        ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
