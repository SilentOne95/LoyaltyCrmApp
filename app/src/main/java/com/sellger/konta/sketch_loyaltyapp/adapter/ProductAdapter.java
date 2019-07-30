package com.sellger.konta.sketch_loyaltyapp.adapter;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.sellger.konta.sketch_loyaltyapp.R;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Product;
import com.sellger.konta.sketch_loyaltyapp.databinding.GridListItemProductOneColBinding;
import com.sellger.konta.sketch_loyaltyapp.databinding.GridListItemProductTwoColBinding;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> implements Filterable {

    private List<Product> mListOfItems, mListOfItemsHelper;
    private RecyclerItemClickListener.ProductRetrofitClickListener mProductClickListener;
    private int mNumOfColumns;

    public ProductAdapter(List<Product> items,
                              RecyclerItemClickListener.ProductRetrofitClickListener clickListener,
                              int columns) {
        mListOfItems = items;
        mProductClickListener = clickListener;
        mNumOfColumns = columns;
        mListOfItemsHelper = new ArrayList<>(mListOfItems);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private Object mBinding;

        ViewHolder(GridListItemProductTwoColBinding binding, RecyclerItemClickListener.ProductRetrofitClickListener listener) {
            super(binding.getRoot());
            mBinding = binding;
            mProductClickListener = listener;
        }

        ViewHolder(GridListItemProductOneColBinding binding, RecyclerItemClickListener.ProductRetrofitClickListener listener) {
            super(binding.getRoot());
            mBinding = binding;
            mProductClickListener = listener;
        }

        public void bind(@NonNull Product product) {
            if (mNumOfColumns == 1) {
                GridListItemProductOneColBinding binding = (GridListItemProductOneColBinding) mBinding;
                binding.setItem(product);
                binding.setNumOfColumns(mNumOfColumns);
                binding.executePendingBindings();
            } else {
                GridListItemProductTwoColBinding binding = (GridListItemProductTwoColBinding) mBinding;
                binding.setItem(product);
                binding.setNumOfColumns(mNumOfColumns);
                binding.executePendingBindings();
            }
        }
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (mNumOfColumns == 1) {
            GridListItemProductOneColBinding binding;
            binding = DataBindingUtil.inflate(inflater, R.layout.grid_list_item_product_one_col, parent, false);
            return new ProductAdapter.ViewHolder(binding, mProductClickListener);
        } else {
            GridListItemProductTwoColBinding binding;
            binding = DataBindingUtil.inflate(inflater, R.layout.grid_list_item_product_two_col, parent, false);
            return new ProductAdapter.ViewHolder(binding, mProductClickListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        holder.bind(mListOfItems.get(position));

        if (mNumOfColumns == 1) {
            GridListItemProductOneColBinding binding = (GridListItemProductOneColBinding) holder.mBinding;
            binding.getRoot().findViewById(R.id.grid_item_view_details_button)
                    .setOnClickListener(view ->
                            mProductClickListener.onItemProductClick(mListOfItems.get(position).getId()));
        } else {
            GridListItemProductTwoColBinding binding = (GridListItemProductTwoColBinding) holder.mBinding;
            binding.getRoot().findViewById(R.id.grid_item_view_details_button)
                    .setOnClickListener(view ->
                            mProductClickListener.onItemProductClick(mListOfItems.get(position).getId()));
        }
    }

    @Override
    public int getItemCount() {
        return mListOfItems.size();
    }

    @Override
    public Filter getFilter() {
        return productFilter;
    }

    private Filter productFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Product> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mListOfItemsHelper);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Product product : mListOfItemsHelper) {
                    if (product.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(product);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mListOfItems.clear();
            mListOfItems.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
