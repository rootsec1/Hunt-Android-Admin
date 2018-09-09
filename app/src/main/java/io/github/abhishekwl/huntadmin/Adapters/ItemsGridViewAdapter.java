package io.github.abhishekwl.huntadmin.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;

import io.github.abhishekwl.huntadmin.Helpers.ApiClient;
import io.github.abhishekwl.huntadmin.Helpers.ApiInterface;
import io.github.abhishekwl.huntadmin.Models.Item;
import io.github.abhishekwl.huntadmin.R;

public class ItemsGridViewAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Item> itemArrayList;
    private LayoutInflater layoutInflater;
    private String currencyCode;
    private MaterialDialog materialDialog;
    private Activity activity;
    private ApiInterface apiInterface;

    public ItemsGridViewAdapter(Context context, ArrayList<Item> itemArrayList, Activity activity) {
        this.context = context;
        this.itemArrayList = itemArrayList;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.currencyCode = Currency.getInstance(Locale.getDefault()).getCurrencyCode();
        this.activity = activity;
        this.apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
    }

    @Override
    public int getCount() {
        return itemArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
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
                finalPrice = roundTwoDecimals(finalPrice);
                itemFinalPriceTextView.setText(currencyCode.concat(" ").concat(Double.toString(finalPrice)));
            }
        } else gridView = convertView;
        return gridView;
    }

    double roundTwoDecimals(double d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }

}
