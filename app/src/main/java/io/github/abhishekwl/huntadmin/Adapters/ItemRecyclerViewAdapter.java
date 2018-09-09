package io.github.abhishekwl.huntadmin.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.abhishekwl.huntadmin.Models.Item;
import io.github.abhishekwl.huntadmin.R;

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ItemViewHolder> {

    private Context context;
    private ArrayList<Item> itemArrayList;
    private String currencyCode;

    ItemRecyclerViewAdapter(Context context, ArrayList<Item> itemArrayList) {
        this.context = context;
        this.itemArrayList = itemArrayList;
        this.currencyCode = Currency.getInstance(Locale.getDefault()).getCurrencyCode();
    }

    @NonNull
    @Override
    public ItemRecyclerViewAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_item, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemRecyclerViewAdapter.ItemViewHolder holder, int position) {
        Item item = itemArrayList.get(position);
        holder.render(holder.itemView.getContext(), item);
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
        @BindView(R.id.itemListItemSubcategoryTextView)
        TextView itemSubcategoryTextView;

        ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void render(Context context, Item item) {
            Glide.with(context).load(item.getImage()).into(itemImageView);
            itemNameTextView.setText(item.getName());
            itemSubcategoryTextView.setText(item.getSubcategory());
            if (item.getDiscount()==0) {
                if (itemOriginalPriceTextView.getParent()!=null && itemOriginalPriceTextView!=null) ((ViewGroup) itemOriginalPriceTextView.getParent()).removeView(itemOriginalPriceTextView);
                itemFinalPriceTextView.setText(currencyCode.concat(" ").concat(Double.toString(item.getPrice())));
            }
            else {
                itemOriginalPriceTextView.setText(currencyCode.concat(" ").concat(Double.toString(item.getPrice())));
                itemOriginalPriceTextView.setPaintFlags(itemOriginalPriceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                double finalPrice = item.getPrice() - (item.getDiscount()/100 * item.getPrice());
                finalPrice = roundTwoDecimals(finalPrice);
                itemFinalPriceTextView.setText(currencyCode.concat(" ").concat(Double.toString(finalPrice)));
            }
            itemLinearLayout.setOnClickListener(v -> {
                //TODO: On item Press
            });
        }


        double roundTwoDecimals(double d)
        {
            DecimalFormat twoDForm = new DecimalFormat("#.##");
            return Double.valueOf(twoDForm.format(d));
        }
    }
}
