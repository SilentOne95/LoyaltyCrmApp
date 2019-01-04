package com.example.konta.sketch_loyalityapp.adapters;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.modelClasses.ItemCoupon;

import java.util.ArrayList;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.ViewHolder> {

    private ArrayList<ItemCoupon> listOfItems;
    private RecyclerItemClickListener.CouponClickListener couponClickListener;

    public CouponAdapter(ArrayList<ItemCoupon> items,
                         RecyclerItemClickListener.CouponClickListener clickListener) {
        listOfItems = items;
        couponClickListener = clickListener;
    }

    @NonNull
    @Override
    public CouponAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_list_item_coupon, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;
        private TextView titleView, descriptionText, discountMarker, basicPrice, newPrice;
        private Button checkCodeButton, showDetailsButton;

        ViewHolder(@NonNull final View view) {
            super(view);
            imageView = view.findViewById(R.id.grid_item_image);
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
                    couponClickListener.onItemCouponDetailsClick(listOfItems.get(getAdapterPosition()));
                    break;
                case R.id.grid_item_show_code_button:
                    couponClickListener.onItemCouponCodeCheckClick(listOfItems.get(getAdapterPosition()));
                    break;
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final ItemCoupon currentItem = listOfItems.get(position);

        holder.imageView.setImageDrawable(currentItem.getItemBitmapDrawable());
        holder.discountMarker.setText("-".concat(Integer.toString(currentItem.getItemDiscount())).concat("%"));
        holder.titleView.setText(currentItem.getItemTitle());
        holder.basicPrice.setText(String.valueOf(currentItem.getItemBasicPrice()).concat(" ").concat("zł"));
        holder.newPrice.setText(String.valueOf(currentItem.getItemFinalPrice()).concat(" ").concat("zł"));
        holder.descriptionText.setText(currentItem.getItemDescription());
    }

    @Override
    public int getItemCount() { return listOfItems.size(); }
}
