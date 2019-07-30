package com.sellger.konta.sketch_loyaltyapp.adapter;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.sellger.konta.sketch_loyaltyapp.R;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Coupon;
import com.sellger.konta.sketch_loyaltyapp.databinding.GridListItemCouponOneColBinding;
import com.sellger.konta.sketch_loyaltyapp.databinding.GridListItemCouponTwoColBinding;

import java.util.ArrayList;
import java.util.List;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.ViewHolder> implements Filterable {

    private List<Coupon> mListOfItems, mListOfItemsHelper;
    private RecyclerItemClickListener.CouponRetrofitClickListener mCouponClickListener;
    private int mNumOfColumns;

    public CouponAdapter(List<Coupon> items,
                          RecyclerItemClickListener.CouponRetrofitClickListener clickListener,
                          int columns) {
        mListOfItems = items;
        mCouponClickListener = clickListener;
        mNumOfColumns = columns;
        mListOfItemsHelper = new ArrayList<>(mListOfItems);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private Object mBinding;

        ViewHolder(GridListItemCouponOneColBinding binding, RecyclerItemClickListener.CouponRetrofitClickListener listener) {
            super(binding.getRoot());
            mBinding = binding;
            mCouponClickListener = listener;
        }

        ViewHolder(GridListItemCouponTwoColBinding binding, RecyclerItemClickListener.CouponRetrofitClickListener listener) {
            super(binding.getRoot());
            mBinding = binding;
            mCouponClickListener = listener;
        }

        public void bind(@NonNull Coupon coupon) {
            if (mNumOfColumns == 2) {
                GridListItemCouponTwoColBinding binding = (GridListItemCouponTwoColBinding) mBinding;
                binding.setItem(coupon);
                binding.setNumOfColumns(mNumOfColumns);
                binding.executePendingBindings();
            } else {
                GridListItemCouponOneColBinding binding = (GridListItemCouponOneColBinding) mBinding;
                binding.setItem(coupon);
                binding.setNumOfColumns(mNumOfColumns);
                binding.executePendingBindings();
            }
        }
    }

    @NonNull
    @Override
    public CouponAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (mNumOfColumns == 1) {
            GridListItemCouponOneColBinding binding;
            binding = DataBindingUtil.inflate(inflater, R.layout.grid_list_item_coupon_one_col, parent, false);
            return new CouponAdapter.ViewHolder(binding, mCouponClickListener);
        } else {
            GridListItemCouponTwoColBinding binding;
            binding = DataBindingUtil.inflate(inflater, R.layout.grid_list_item_coupon_two_col, parent, false);
            return new CouponAdapter.ViewHolder(binding, mCouponClickListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CouponAdapter.ViewHolder holder, int position) {
        holder.bind(mListOfItems.get(position));

        if (mNumOfColumns == 2) {
            GridListItemCouponTwoColBinding binding = (GridListItemCouponTwoColBinding) holder.mBinding;
            binding.getRoot().findViewById(R.id.grid_item_show_details_button)
                    .setOnClickListener(view ->
                            mCouponClickListener.onItemCouponDetailsClick(mListOfItems.get(position).getId()));

            binding.getRoot().findViewById(R.id.grid_item_show_code_button)
                    .setOnClickListener(view ->
                            mCouponClickListener.onItemCouponCodeCheckClick(position,
                                    mListOfItems.get(position).getImage()));
        } else {
            GridListItemCouponOneColBinding binding = (GridListItemCouponOneColBinding) holder.mBinding;
            binding.getRoot().findViewById(R.id.grid_item_show_details_button)
                    .setOnClickListener(view ->
                            mCouponClickListener.onItemCouponDetailsClick(mListOfItems.get(position).getId()));

            binding.getRoot().findViewById(R.id.grid_item_show_code_button)
                    .setOnClickListener(view ->
                            mCouponClickListener.onItemCouponCodeCheckClick(position,
                                    mListOfItems.get(position).getImage()));
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
            List<Coupon> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mListOfItemsHelper);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Coupon product : mListOfItemsHelper) {
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
