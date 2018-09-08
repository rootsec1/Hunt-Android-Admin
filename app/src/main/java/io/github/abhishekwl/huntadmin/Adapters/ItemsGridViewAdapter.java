package io.github.abhishekwl.huntadmin.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

import io.github.abhishekwl.huntadmin.Models.Item;
import io.github.abhishekwl.huntadmin.R;

public class ItemsGridViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Item> itemArrayList;
    private LayoutInflater layoutInflater;
    private String currencyCode;

    public ItemsGridViewAdapter(Context context, ArrayList<Item> itemArrayList) {
        this.context = context;
        this.itemArrayList = itemArrayList;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.currencyCode = Currency.getInstance(Locale.getDefault()).getCurrencyCode();
    }

    @Override
    public int getCount() {
        return itemArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridView;
        if (convertView==null) {
            gridView = layoutInflater.inflate(R.layout.grid_list_item, parent, false);

            ImageView itemImageView = gridView.findViewById(R.id.itemListItemImageView);
            TextView itemNameTextView = gridView.findViewById(R.id.itemListItemNameTextView);
            TextView itemSubcategoryTextView = gridView.findViewById(R.id.itemListItemSubcategoryTextView);
            TextView itemOriginalPriceTextView = gridView.findViewById(R.id.itemListItemOriginalPriceTextView);
            TextView itemFinalPriceTextView = gridView.findViewById(R.id.itemListItemFinalPriceTextView);
            LinearLayout itemLinearLayout = gridView.findViewById(R.id.itemListItemRootLinearLayout);

            Item item = itemArrayList.get(position);

            Glide.with(gridView.getContext()).load(item.getImage()).into(itemImageView);
            itemNameTextView.setText(item.getName());
            itemSubcategoryTextView.setText(item.getSubcategory());
            if (item.getDiscount()==0) {
                if (itemOriginalPriceTextView.getParent() != null) ((ViewGroup) itemOriginalPriceTextView.getParent()).removeView(itemOriginalPriceTextView);
                itemFinalPriceTextView.setText(currencyCode.concat(" ").concat(Double.toString(item.getPrice())));
            }
            else {
                itemOriginalPriceTextView.setText(currencyCode.concat(" ").concat(Double.toString(item.getPrice())));
                itemOriginalPriceTextView.setPaintFlags(itemOriginalPriceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                double finalPrice = item.getPrice() - (item.getDiscount()/100 * item.getPrice());
                itemFinalPriceTextView.setText(currencyCode.concat(" ").concat(Double.toString(finalPrice)));
            }
            itemLinearLayout.setOnClickListener(v -> {
                //TODO: On item Press
            });

        } else gridView = convertView;
        return gridView;
    }
}
