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

public class RecyclerGridViewManager extends RecyclerView.Adapter<RecyclerGridViewManager.GridItemViewHolder> {

    private final String currencyCode;
    private Context context;
    private ArrayList<Item> itemArrayList;

    public RecyclerGridViewManager(Context context, ArrayList<Item> itemArrayList) {
        this.context = context;
        this.itemArrayList = itemArrayList;
        this.currencyCode = Currency.getInstance(Locale.getDefault()).getCurrencyCode();
    }

    @NonNull
    @Override
    public RecyclerGridViewManager.GridItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_list_item, parent, false);
        return new GridItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerGridViewManager.GridItemViewHolder holder, int position) {
        Item item = itemArrayList.get(position);
        holder.renderItem(item, holder.itemView.getContext());
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    class GridItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemListItemRootLinearLayout)
        LinearLayout itemRootLinearLayout;
        @BindView(R.id.itemListItemImageView)
        ImageView itemImageView;
        @BindView(R.id.itemListItemSubcategoryTextView)
        TextView itemSubcategoryTextView;
        @BindView(R.id.itemListItemNameTextView)
        TextView itemNameTextView;
        @BindView(R.id.itemListItemOriginalPriceTextView)
        TextView itemOriginalPriceTextView;
        @BindView(R.id.itemListItemFinalPriceTextView)
        TextView itemFinalPriceTextView;

        GridItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void renderItem(Item item, Context context) {
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
        }

        double roundTwoDecimals(double d) {
            DecimalFormat twoDForm = new DecimalFormat("#.##");
            return Double.valueOf(twoDForm.format(d));
        }
    }
}
