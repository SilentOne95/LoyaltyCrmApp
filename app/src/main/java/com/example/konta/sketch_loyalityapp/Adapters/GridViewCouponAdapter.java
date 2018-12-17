package com.example.konta.sketch_loyalityapp.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.konta.sketch_loyalityapp.AdditionalUI.CouponDetailsActivity;
import com.example.konta.sketch_loyalityapp.ModelClasses.ItemCoupon;
import com.example.konta.sketch_loyalityapp.R;

import java.util.ArrayList;

public class GridViewCouponAdapter extends ArrayAdapter<ItemCoupon> {

    private boolean mShowDescription;

    /**
     * This is a custom constructor.
     * The context is used to inflate the layout file.
     * The list is the data we want to populate into the list.
     * @param context of the app
     * @param gridItem A list of objects to display in a list
     */
    public GridViewCouponAdapter(@NonNull Activity context, @NonNull ArrayList<ItemCoupon> gridItem,
                                 boolean showDescription) {
        // Initialize the ArrayAdapter's internal storage for the context
        super(context, 0, gridItem);
        mShowDescription = showDescription;
    }

    /**
     * Provides a view for an AdapterView (GridView)
     */
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {

        View listItemView = convertView;

        class ViewHolder {
            private ImageView imageView;
            private TextView titleView, descriptionText, discountMarker, basicPrice, newPrice;
            private Button showDetailsButton;
        }

        ViewHolder holder;

        // Check if there is any existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.grid_list_item_coupon, parent, false);

            holder = new ViewHolder();
            holder.imageView = listItemView.findViewById(R.id.grid_item_image);
            holder.discountMarker = listItemView.findViewById(R.id.grid_item_discount_marker);
            holder.titleView = listItemView.findViewById(R.id.grid_item_coupon_title);
            holder.basicPrice = listItemView.findViewById(R.id.grid_item_old_price_amount);
            holder.newPrice = listItemView.findViewById(R.id.grid_item_price_amount);
            holder.descriptionText = listItemView.findViewById(R.id.grid_item_coupon_description_text);
            holder.showDetailsButton = listItemView.findViewById(R.id.grid_item_show_details_button);

            // Depending on view, decide if those views should be shown or not
            if (!mShowDescription) {
                holder.descriptionText.setVisibility(View.GONE);
            }

            listItemView.setTag(holder);
        } else {
            holder = (ViewHolder) listItemView.getTag();
        }

        // Get the {@link GridViewItem} object located at this position in the list
        ItemCoupon currentItem = getItem(position);

        holder.imageView.setImageDrawable(currentItem.getItemBitmapDrawable());
        holder.discountMarker.setText("-".concat(Integer.toString(currentItem.getItemDiscount())).concat("%"));
        holder.titleView.setText(currentItem.getItemTitle());
        holder.basicPrice.setText(String.valueOf(currentItem.getItemBasicPrice()).concat(" ").concat("zł"));
        holder.newPrice.setText(String.valueOf(currentItem.getItemFinalPrice()).concat(" ").concat("zł"));
        holder.descriptionText.setText(currentItem.getItemDescription());
        holder.showDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CouponDetailsActivity.class);
                intent.putExtra("EXTRA_ELEMENT_ID", position);
                getContext().startActivity(intent);
            }
        });

        return listItemView;
    }
}