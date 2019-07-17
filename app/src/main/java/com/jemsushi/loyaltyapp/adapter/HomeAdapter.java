package com.jemsushi.loyaltyapp.adapter;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.jemsushi.loyaltyapp.R;
import com.jemsushi.loyaltyapp.data.entity.MenuComponent;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static com.jemsushi.loyaltyapp.Constants.BITMAP_CORNER_RADIUS;
import static com.jemsushi.loyaltyapp.Constants.BITMAP_HEIGHT_ONE_COLUMN;
import static com.jemsushi.loyaltyapp.Constants.BITMAP_HEIGHT_TWO_COLUMNS;
import static com.jemsushi.loyaltyapp.Constants.BITMAP_WIDTH_ONE_COLUMN;
import static com.jemsushi.loyaltyapp.Constants.BITMAP_WIDTH_TWO_COLUMNS;
import static com.jemsushi.loyaltyapp.Constants.DEFAULT_STRING;
import static com.jemsushi.loyaltyapp.Constants.LAYOUT_TYPE_COUPONS;
import static com.jemsushi.loyaltyapp.Constants.LAYOUT_TYPE_SCANNER;
import static com.jemsushi.loyaltyapp.ui.main.MainActivity.PACKAGE_NAME;
import static com.facebook.FacebookSdk.getApplicationContext;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private ArrayList<MenuComponent> listOfItems;
    private RecyclerItemClickListener.HomeRetrofitClickListener homeClickListener;
    private int numOfColumns;
    private boolean isUserAnonymous;

    public HomeAdapter(ArrayList<MenuComponent> items,
                       RecyclerItemClickListener.HomeRetrofitClickListener clickListener,
                       int columns) {
        listOfItems = items;
        homeClickListener = clickListener;
        numOfColumns = columns;
        isUserAnonymous = FirebaseAuth.getInstance().getCurrentUser().isAnonymous();
    }

    @NonNull
    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        switch (numOfColumns) {
            case 1:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.grid_list_item_home_one_col, parent, false);
                break;
            case 2:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.grid_list_item_home_two_col, parent, false);
                break;
            default:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.grid_list_item_home_two_col, parent, false);
                break;
        }

        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private ImageView imageView;
        private TextView titleView;

        ViewHolder(@NonNull View view) {
            super(view);
            itemView = view.findViewById(R.id.grid_item_home);
            imageView = view.findViewById(R.id.grid_item_image);
            titleView = view.findViewById(R.id.grid_item_title_text);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final MenuComponent currentItem = listOfItems.get(position);
        int imageWidth, imageHeight;

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
            int imageId = holder.imageView.getContext()
                    .getResources()
                    .getIdentifier(currentItem.getImage(), "drawable", PACKAGE_NAME);

            Picasso.get()
                    .load(imageId)
                    .into(holder.imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            if (shouldViewBeDisabled(currentItem.getType())) {
                                DrawableCompat.setTint(holder.imageView.getDrawable(),
                                        getApplicationContext().getResources().getColor(R.color.colorNavViewStateEnableFalse));
                            } else {
                                DrawableCompat.setTint(holder.imageView.getDrawable(),
                                        getApplicationContext().getResources().getColor(R.color.colorAccent));
                            }
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
        } else {
            Picasso.get()
                    .load(R.drawable.no_image_available)
                    .transform(new RoundedCornersTransformation(BITMAP_CORNER_RADIUS, 0))
                    .resize(imageWidth, imageHeight)
                    .into(holder.imageView);
        }

        if (!TextUtils.isEmpty(currentItem.getComponentTitle())) {
            holder.titleView.setText(currentItem.getComponentTitle());
            if (shouldViewBeDisabled(currentItem.getType())) {
                holder.titleView.setTextColor(getApplicationContext().getResources().getColor(R.color.colorNavViewStateEnableFalse));
            }
        } else {
            holder.titleView.setText(DEFAULT_STRING);
        }

        if (!shouldViewBeDisabled(currentItem.getType())) {
            holder.itemView.setOnClickListener(view ->
                    homeClickListener.onItemHomeClick(listOfItems.get(position), position));
        }
    }

    @Override
    public int getItemCount() { return listOfItems.size(); }

    private boolean shouldViewBeDisabled(String currentItemLayoutType) {
        return isUserAnonymous && (currentItemLayoutType.equals(LAYOUT_TYPE_COUPONS)
                || currentItemLayoutType.equals(LAYOUT_TYPE_SCANNER));
    }
}
