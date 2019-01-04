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
import com.example.konta.sketch_loyalityapp.modelClasses.ItemProduct;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private ArrayList<ItemProduct> listOfItems;
    private RecyclerItemClickListener.ProductClickListener productClickListener;

    public ProductAdapter(ArrayList<ItemProduct> items,
                          RecyclerItemClickListener.ProductClickListener clickListener ) {
        listOfItems = items;
        productClickListener = clickListener;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_list_item_product, parent, false);
        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView titleView, price, descriptionText;
        private Button button;

        ViewHolder(@NonNull View view) {
            super(view);
            imageView = view.findViewById(R.id.grid_item_image);
            titleView = view.findViewById(R.id.grid_item_title_text);
            price = view.findViewById(R.id.grid_item_price_amount);
            descriptionText = view.findViewById(R.id.grid_item_content_description);
            button = view.findViewById(R.id.grid_item_view_details_button);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        ItemProduct currentItem = listOfItems.get(position);

        holder.imageView.setImageDrawable(currentItem.getItemBitmapDrawable());
        holder.titleView.setText(currentItem.getItemTitle());
        holder.price.setText(String.valueOf(currentItem.getItemPrice()).concat(" ").concat("zł"));
        holder.descriptionText.setText(currentItem.getItemDescription());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productClickListener.onItemProductClick(listOfItems.get(position));
            }
        });
    }

    @Override
    public int getItemCount() { return listOfItems.size(); }
}
