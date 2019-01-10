package com.example.konta.sketch_loyalityapp.adapters;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.data.coupon.Coupon;

import java.util.List;

public class CouponRetrofitAdapter extends RecyclerView.Adapter<CouponRetrofitAdapter.ViewHolder> {

    private List<Coupon> listOfItems;
    private RecyclerItemClickListener.CouponRetrofitClickListener couponClickListener;

    public CouponRetrofitAdapter(List<Coupon> items,
                                 RecyclerItemClickListener.CouponRetrofitClickListener clickListener) {
        listOfItems = items;
        couponClickListener = clickListener;
    }

    @NonNull
    @Override
    public CouponRetrofitAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_list_item_coupon, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // imageView
        private TextView titleView, descriptionText, discountMarker, basicPrice, newPrice;
        private Button checkCodeButton, showDetailsButton;

        ViewHolder(@NonNull final View view) {
            super(view);
            // imageView
            discountMarker = view.findViewById(R.id.grid_item_discount_marker);
            titleView = view.findViewById(R.id.grid_item_coupon_title);
            basicPrice = view.findViewById(R.id.grid_item_old_price_amount);
            newPrice = view.findViewById(R.id.grid_item_price_amount);
            descriptionText = view.findViewById(R.id.grid_item_coupon_description_text);
            showDetailsButton = view.findViewById(R.id.grid_item_show_details_button);
            showDetailsButton.setOnClickListener(this);
            checkCodeButton = view.findViewById(R.id.grid_item_show_code_button);
            checkCodeButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.grid_item_show_details_button:
                    couponClickListener.onItemCouponDetailsClick(getAdapterPosition());
                    break;
                case R.id.grid_item_show_code_button:
                    couponClickListener.onItemCouponCodeCheckClick(listOfItems.get(getAdapterPosition()));
                    break;
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final Coupon currentItem = listOfItems.get(position);

        // holder - imageView
        holder.discountMarker.setText("-".concat(currentItem.getReductionAmount()).concat("%"));
        holder.titleView.setText(currentItem.getTitle());
        holder.basicPrice.setText(String.valueOf(currentItem.getPrice()).concat(" ").concat("zł"));
        holder.newPrice.setText(String.valueOf(currentItem.getPriceAfter()).concat(" ").concat("zł"));
        holder.descriptionText.setText(currentItem.getShortDescription());
    }

    @Override
    public int getItemCount() { return listOfItems.size(); }
}
