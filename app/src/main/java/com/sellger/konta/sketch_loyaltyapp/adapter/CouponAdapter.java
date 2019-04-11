package com.sellger.konta.sketch_loyaltyapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.sellger.konta.sketch_loyaltyapp.R;
import com.sellger.konta.sketch_loyaltyapp.pojo.coupon.Coupon;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static com.sellger.konta.sketch_loyaltyapp.Constants.BASE_URL_IMAGES;
import static com.sellger.konta.sketch_loyaltyapp.Constants.BITMAP_CORNER_RADIUS;
import static com.sellger.konta.sketch_loyaltyapp.Constants.BITMAP_HEIGHT_ONE_COLUMN;
import static com.sellger.konta.sketch_loyaltyapp.Constants.BITMAP_HEIGHT_TWO_COLUMNS;
import static com.sellger.konta.sketch_loyaltyapp.Constants.BITMAP_WIDTH_ONE_COLUMN;
import static com.sellger.konta.sketch_loyaltyapp.Constants.BITMAP_WIDTH_TWO_COLUMNS;
import static com.sellger.konta.sketch_loyaltyapp.Constants.DEFAULT_STRING;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.ViewHolder> implements Filterable {

    private List<Coupon> listOfItems, listOfItemsHelper;
    private RecyclerItemClickListener.CouponRetrofitClickListener couponClickListener;
    private int numOfColumns;
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public CouponAdapter(List<Coupon> items,
                         RecyclerItemClickListener.CouponRetrofitClickListener clickListener,
                         int columns) {
        listOfItems = items;
        listOfItemsHelper = new ArrayList<>(listOfItems);
        couponClickListener = clickListener;
        numOfColumns = columns;
    }

    @NonNull
    @Override
    public CouponAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        switch (numOfColumns) {
            case 1:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.grid_list_item_coupon_one_col, parent, false);
                break;
            case 2:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.grid_list_item_coupon_two_col, parent, false);
                break;
            default:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.grid_list_item_coupon_one_col, parent, false);
                break;
        }

        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;
        private TextView titleView, codeText, descriptionText, discountMarker, basicPrice, newPrice;
        private Button checkCodeButton, showDetailsButton;

        ViewHolder(@NonNull final View view) {
            super(view);
            imageView = view.findViewById(R.id.grid_item_image);
            discountMarker = view.findViewById(R.id.grid_item_discount_marker);
            titleView = view.findViewById(R.id.grid_item_coupon_title);
            codeText = view.findViewById(R.id.grid_item_code_text);
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
            switch (v.getId()) {
                case R.id.grid_item_show_details_button:
                    couponClickListener.onItemCouponDetailsClick(listOfItems
                            .get(getAdapterPosition()).getId());
                    break;
                case R.id.grid_item_show_code_button:
                    couponClickListener.onItemCouponCodeCheckClick(getAdapterPosition(),
                            listOfItems.get(getAdapterPosition()).getImage());
                    break;
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Coupon currentItem = listOfItems.get(position);
        int cornerRadius = BITMAP_CORNER_RADIUS, imageWidth, imageHeight;

        switch (numOfColumns) {
            case 1:
                imageWidth = BITMAP_WIDTH_ONE_COLUMN;
                imageHeight = BITMAP_HEIGHT_ONE_COLUMN;
                break;
            case 2:
                imageWidth = BITMAP_WIDTH_TWO_COLUMNS;
                imageHeight = BITMAP_HEIGHT_TWO_COLUMNS;
                break;
            default:
                imageWidth = BITMAP_WIDTH_ONE_COLUMN;
                imageHeight = BITMAP_HEIGHT_ONE_COLUMN;
                break;
        }

        if (!TextUtils.isEmpty(currentItem.getImage())) {
            // TODO: Upload images to server and change "else" image to no_image_available
            Picasso.get()
                    .load(BASE_URL_IMAGES + currentItem.getImage())
                    .transform(new RoundedCornersTransformation(cornerRadius, 0))
                    .error(R.drawable.no_image_available)
                    .resize(imageWidth, imageHeight)
                    .into(holder.imageView);
        } else {
            Picasso.get()
                    .load(R.drawable.no_image_available)
                    .transform(new RoundedCornersTransformation(cornerRadius, 0))
                    .resize(imageWidth, imageHeight)
                    .into(holder.imageView);
        }

        if (currentItem.getReductionAmount() != null && !currentItem.getReductionAmount().trim().isEmpty()) {
            if (currentItem.getReductionType().equals("percent")) {
                holder.discountMarker.setText("-".concat(currentItem.getReductionAmount()).concat("%"));
            } else {
                holder.discountMarker.setText("-".concat(currentItem.getReductionAmount()).concat("zł"));
            }
        } else {
            holder.discountMarker.setText(DEFAULT_STRING);
        }

        if (!TextUtils.isEmpty(currentItem.getTitle())) {
            holder.titleView.setText(currentItem.getTitle());
        } else {
            holder.titleView.setText(DEFAULT_STRING);
        }

        holder.codeText.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(currentItem.getCouponCode())) {
            holder.codeText.setText(currentItem.getCouponCode());
        } else {
            holder.codeText.setText(DEFAULT_STRING);
        }

        if (currentItem.getPrice() != null && !currentItem.getPrice().toString().trim().isEmpty()) {
            holder.basicPrice.setText(String.valueOf(decimalFormat.format(currentItem.getPrice())).concat("zł"));
        } else {
            holder.basicPrice.setText(DEFAULT_STRING);
        }

        if (currentItem.getPriceAfter() != null && !currentItem.getPriceAfter().toString().trim().isEmpty()) {
            holder.newPrice.setText(String.valueOf(decimalFormat.format(currentItem.getPriceAfter())).concat("zł"));
        } else {
            holder.newPrice.setText(DEFAULT_STRING);
        }

        if (!TextUtils.isEmpty(currentItem.getShortDescription())) {
            holder.descriptionText.setText(Html.fromHtml(currentItem.getShortDescription()));
        } else {
            holder.descriptionText.setText(DEFAULT_STRING);
        }
    }

    @Override
    public int getItemCount() {
        return listOfItems.size();
    }

    @Override
    public Filter getFilter() {
        return couponFilter;
    }

    private Filter couponFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Coupon> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(listOfItemsHelper);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Coupon coupon : listOfItemsHelper) {
                    if (coupon.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(coupon);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listOfItems.clear();
            listOfItems.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
