package com.sellger.konta.sketch_loyaltyapp.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.sellger.konta.sketch_loyaltyapp.R;
import com.sellger.konta.sketch_loyaltyapp.data.entity.MenuComponent;
import com.sellger.konta.sketch_loyaltyapp.databinding.GridListItemHomeTwoColBinding;

import java.util.ArrayList;

import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_TYPE_COUPONS;
import static com.sellger.konta.sketch_loyaltyapp.Constants.LAYOUT_TYPE_SCANNER;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private ArrayList<MenuComponent> mListOfItems;
    private RecyclerItemClickListener.HomeRetrofitClickListener mHomeClickListener;
    private int mNumOfColumns;
    private boolean mIsUserAnonymous;

    public HomeAdapter(ArrayList<MenuComponent> items,
                       RecyclerItemClickListener.HomeRetrofitClickListener clickListener,
                       int columns) {
        mListOfItems = items;
        mHomeClickListener = clickListener;
        mNumOfColumns = columns;
        mIsUserAnonymous = FirebaseAuth.getInstance().getCurrentUser().isAnonymous();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private GridListItemHomeTwoColBinding mBinding;

        ViewHolder(GridListItemHomeTwoColBinding binding, RecyclerItemClickListener.HomeRetrofitClickListener listener) {
            super(binding.getRoot());
            mBinding = binding;
            mHomeClickListener = listener;
        }

        public void bind(@NonNull MenuComponent menuComponent) {
            mBinding.setItem(menuComponent);
            mBinding.executePendingBindings();
        }
    }

    @NonNull
    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        GridListItemHomeTwoColBinding binding;

        switch (mNumOfColumns) {
            case 1:
                binding = DataBindingUtil.inflate(inflater, R.layout.grid_list_item_home_one_col, parent, false);
                break;
            case 2:
                binding = DataBindingUtil.inflate(inflater, R.layout.grid_list_item_home_two_col, parent, false);
                break;
            default:
                binding = DataBindingUtil.inflate(inflater, R.layout.grid_list_item_home_two_col, parent, false);
                break;
        }

        return new ViewHolder(binding, mHomeClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mListOfItems.get(position));
        if (!shouldViewBeDisabled(mListOfItems.get(position).getType())) {
            holder.mBinding.getRoot().findViewById(R.id.grid_item_home)
                    .setOnClickListener(view ->
                            mHomeClickListener.onItemHomeClick(mListOfItems.get(position), position));
        }
    }

    @Override
    public int getItemCount() { return mListOfItems.size(); }

    private boolean shouldViewBeDisabled(String currentItemLayoutType) {
        return mIsUserAnonymous && (currentItemLayoutType.equals(LAYOUT_TYPE_COUPONS)
                || currentItemLayoutType.equals(LAYOUT_TYPE_SCANNER));
    }
}