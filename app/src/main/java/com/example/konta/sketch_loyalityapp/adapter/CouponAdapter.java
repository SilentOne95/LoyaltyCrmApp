package com.example.konta.sketch_loyalityapp.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.pojo.coupon.Coupon;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static com.example.konta.sketch_loyalityapp.Constants.BITMAP_CORNER_RADIUS_SINGLE_COLUMN;
import static com.example.konta.sketch_loyalityapp.Constants.BITMAP_CORNER_RADIUS_THREE_COLUMNS;
import static com.example.konta.sketch_loyalityapp.Constants.BITMAP_CORNER_RADIUS_TWO_COLUMNS;
import static com.example.konta.sketch_loyalityapp.Constants.BITMAP_HEIGHT_SINGLE_COLUMN;
import static com.example.konta.sketch_loyalityapp.Constants.BITMAP_HEIGHT_THREE_COLUMNS;
import static com.example.konta.sketch_loyalityapp.Constants.BITMAP_HEIGHT_TWO_COLUMNS;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.ViewHolder> {

    private List<Coupon> listOfItems;
    private RecyclerItemClickListener.CouponRetrofitClickListener couponClickListener;
    private int numOfColumns;

    public CouponAdapter(List<Coupon> items,
                         RecyclerItemClickListener.CouponRetrofitClickListener clickListener,
                         int columns) {
        listOfItems = items;
        couponClickListener = clickListener;
        numOfColumns = columns;
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
                    couponClickListener.onItemCouponDetailsClick(listOfItems
                            .get(getAdapterPosition())
                            .getId());
                    break;
                case R.id.grid_item_show_code_button:
                    couponClickListener.onItemCouponCodeCheckClick(listOfItems
                            .get(getAdapterPosition())
                            .getCouponCode());
                    break;
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final Coupon currentItem = listOfItems.get(position);
        int cornerRadius, imageHeight;

        switch (numOfColumns) {
            case 1:
                cornerRadius = BITMAP_CORNER_RADIUS_SINGLE_COLUMN;
                imageHeight = BITMAP_HEIGHT_SINGLE_COLUMN;
                break;
            case 2:
                cornerRadius = BITMAP_CORNER_RADIUS_TWO_COLUMNS;
                imageHeight = BITMAP_HEIGHT_TWO_COLUMNS;
                break;
            case 3:
                cornerRadius = BITMAP_CORNER_RADIUS_THREE_COLUMNS;
                imageHeight = BITMAP_HEIGHT_THREE_COLUMNS;
                break;
            default:
                cornerRadius = BITMAP_CORNER_RADIUS_SINGLE_COLUMN;
                imageHeight = BITMAP_HEIGHT_SINGLE_COLUMN;
                break;
        }

        if (!(currentItem.getImage() == null || currentItem.getImage().isEmpty())) {
            Picasso.get()
                    .load("")
                    .transform(new RoundedCornersTransformation(cornerRadius, 0))
                    .resize(holder.imageView.getWidth(),imageHeight)
                    .into(holder.imageView);
        }

        if (currentItem.getReductionType().equals("percent")){
            holder.discountMarker.setText("-".concat(currentItem.getReductionAmount()).concat("%"));
        } else {
            holder.discountMarker.setText("-".concat(currentItem.getReductionAmount()).concat("zł"));
        }
        holder.titleView.setText(currentItem.getTitle());
        holder.basicPrice.setText(String.valueOf(currentItem.getPrice()).concat(" ").concat("zł"));
        holder.newPrice.setText(String.valueOf(currentItem.getPriceAfter()).concat(" ").concat("zł"));
        holder.descriptionText.setText(Html.fromHtml(currentItem.getShortDescription()));
    }

    @Override
    public int getItemCount() { return listOfItems.size(); }
}
