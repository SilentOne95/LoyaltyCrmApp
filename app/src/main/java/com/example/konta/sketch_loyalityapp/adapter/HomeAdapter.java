package com.example.konta.sketch_loyalityapp.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.pojo.menu.MenuComponent;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static com.example.konta.sketch_loyalityapp.Constants.BITMAP_CORNER_RADIUS_SINGLE_COLUMN;
import static com.example.konta.sketch_loyalityapp.Constants.BITMAP_CORNER_RADIUS_TWO_COLUMNS;
import static com.example.konta.sketch_loyalityapp.Constants.BITMAP_HEIGHT;
import static com.example.konta.sketch_loyalityapp.Constants.BITMAP_WIDTH;
import static com.example.konta.sketch_loyalityapp.Constants.DEFAULT_STRING;
import static com.example.konta.sketch_loyalityapp.ui.main.MainActivity.PACKAGE_NAME;

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
        int cornerRadius;

        switch (numOfColumns) {
            case 1:
                cornerRadius = BITMAP_CORNER_RADIUS_SINGLE_COLUMN;
                break;
            case 2:
                cornerRadius = BITMAP_CORNER_RADIUS_TWO_COLUMNS;
                break;
            default:
                cornerRadius = BITMAP_CORNER_RADIUS_SINGLE_COLUMN;
                break;
        }

        if (currentItem.getImage() != null && !currentItem.getImage().trim().isEmpty() && !currentItem.getImage().equals("")) {
            // TODO: Upload images to server and change "else" image to no_image_available
            int imageId = holder.imageView.getContext()
                    .getResources()
                    .getIdentifier(currentItem.getImage(), "drawable", PACKAGE_NAME);

            Picasso.get()
                    .load(imageId)
                    .into(holder.imageView);

        } else {
            Picasso.get()
                    .load(R.drawable.image_offer)
                    .placeholder(R.drawable.placeholder)
                    .transform(new RoundedCornersTransformation(cornerRadius, 0))
                    .resize(BITMAP_WIDTH, BITMAP_HEIGHT)
                    .into(holder.imageView);
        }

        if (currentItem.getComponentTitle() != null && !currentItem.getComponentTitle().trim().isEmpty()) {
            holder.titleView.setText(currentItem.getComponentTitle());
        } else {
            holder.titleView.setText(DEFAULT_STRING);
        }

        holder.itemView.setOnClickListener(view ->
                homeClickListener.onItemHomeClick(listOfItems.get(position), position));
    }

    @Override
    public int getItemCount() { return listOfItems.size(); }
}
