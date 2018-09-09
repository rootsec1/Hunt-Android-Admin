package io.github.abhishekwl.huntadmin.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.abhishekwl.huntadmin.Models.Category;
import io.github.abhishekwl.huntadmin.R;

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.CategoryViewHolder> {

    private Context context;
    private ArrayList<Category> categoryArrayList;

    public CategoryRecyclerViewAdapter(Context context, ArrayList<Category> categoryArrayList) {
        this.context = context;
        this.categoryArrayList = categoryArrayList;
    }

    @NonNull
    @Override
    public CategoryRecyclerViewAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.inventory_list_item, parent, false);
        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryRecyclerViewAdapter.CategoryViewHolder holder, int position) {
        if (categoryArrayList.size()>0) {
            Category category = categoryArrayList.get(position);
            holder.render(holder.itemView.getContext(), category);
        }
    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.inventoryListItemCategoryNameTextView)
        TextView categoryNameTextView;
        @BindView(R.id.inventoryListItemCategoryRecyclerView)
        RecyclerView itemsRecyclerView;

        CategoryViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void render(Context context, Category category) {
            categoryNameTextView.setText(category.getCategoryName());
            setupRecyclerView(context, category);
        }

        private void setupRecyclerView(Context context, Category category) {
            itemsRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            itemsRecyclerView.setHasFixedSize(true);
            itemsRecyclerView.setItemAnimator(new DefaultItemAnimator());
            itemsRecyclerView.setAdapter(new ItemRecyclerViewAdapter(context, category.getItemArrayList()));
        }
    }
}
