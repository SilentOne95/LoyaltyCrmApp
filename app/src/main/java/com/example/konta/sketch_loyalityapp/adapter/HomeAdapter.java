package com.example.konta.sketch_loyalityapp.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.pojo.menu.MenuComponent;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static com.example.konta.sketch_loyalityapp.Constants.BITMAP_CORNER_RADIUS_SINGLE_COLUMN;
import static com.example.konta.sketch_loyalityapp.Constants.BITMAP_CORNER_RADIUS_THREE_COLUMNS;
import static com.example.konta.sketch_loyalityapp.Constants.BITMAP_CORNER_RADIUS_TWO_COLUMNS;
import static com.example.konta.sketch_loyalityapp.Constants.BITMAP_HEIGHT_SINGLE_COLUMN;
import static com.example.konta.sketch_loyalityapp.Constants.BITMAP_HEIGHT_THREE_COLUMNS;
import static com.example.konta.sketch_loyalityapp.Constants.BITMAP_HEIGHT_TWO_COLUMNS;
import static com.example.konta.sketch_loyalityapp.Constants.DEFAULT_STRING;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private SparseArray<MenuComponent> listOfItems;
    private RecyclerItemClickListener.HomeRetrofitClickListener homeClickListener;
    private int numOfColumns;

    public HomeAdapter(SparseArray<MenuComponent> items,
                       RecyclerItemClickListener.HomeRetrofitClickListener clickListener,
                       int columns) {
        listOfItems = items;
        homeClickListener = clickListener;
        numOfColumns = columns;
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
        private ImageView imageView;
        private TextView titleView, descriptionText;
        private Button button;

        ViewHolder(@NonNull View view) {
            super(view);
            imageView = view.findViewById(R.id.grid_item_image);
            titleView = view.findViewById(R.id.grid_item_title_text);
            descriptionText = view.findViewById(R.id.grid_item_content_description);
            button = view.findViewById(R.id.grid_item_view_details_button);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final MenuComponent currentItem = listOfItems.get(position);
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

        if (currentItem.getImage() != null && !currentItem.getImage().isEmpty()) {
            // TODO: Upload images to server
            Picasso.get()
                    .load("")
                    .transform(new RoundedCornersTransformation(cornerRadius, 0))
                    .resize(holder.imageView.getWidth(),imageHeight)
                    .into(holder.imageView);
        } else {
            Picasso.get()
                    .load(R.drawable.image_not_available)
                    .transform(new RoundedCornersTransformation(cornerRadius, 0))
                    .into(holder.imageView);
        }

        if (currentItem.getComponentTitle() != null && !currentItem.getComponentTitle().trim().isEmpty()) {
            holder.titleView.setText(currentItem.getComponentTitle());
        } else {
            holder.titleView.setText(DEFAULT_STRING);
        }

        holder.descriptionText.setVisibility(View.GONE);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeClickListener.onItemHomeClick(listOfItems.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() { return listOfItems.size(); }
}
