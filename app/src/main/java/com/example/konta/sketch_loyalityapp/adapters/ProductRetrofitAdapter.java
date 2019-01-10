package com.example.konta.sketch_loyalityapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.data.product.Product;

import java.util.List;

public class ProductRetrofitAdapter extends RecyclerView.Adapter<ProductRetrofitAdapter.ViewHolder> {

    private List<Product> listOfItems;
    private RecyclerItemClickListener.ProductClickListener productClickListener;

    public ProductRetrofitAdapter(List<Product> items,
                          RecyclerItemClickListener.ProductClickListener clickListener ) {
        listOfItems = items;
        productClickListener = clickListener;
    }

    @NonNull
    @Override
    public ProductRetrofitAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_list_item_product, parent, false);
        return new ProductRetrofitAdapter.ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // imageView
        private TextView titleView, price, shortDescription;
        private Button button;

        ViewHolder(@NonNull View view) {
            super(view);
            // imageView
            titleView = view.findViewById(R.id.grid_item_title_text);
            price = view.findViewById(R.id.grid_item_price_amount);
            shortDescription = view.findViewById(R.id.grid_item_content_description);
            button = view.findViewById(R.id.grid_item_view_details_button);
            button.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            productClickListener.onItemProductClick(getAdapterPosition());
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product currentItem = listOfItems.get(position);

        // holder - imageView
        holder.titleView.setText(currentItem.getTitle());
        holder.price.setText(String.valueOf(currentItem.getPrice()).concat(" ").concat("zł"));
        holder.shortDescription.setText(currentItem.getShortDescription());
    }

    @Override
    public int getItemCount() { return listOfItems.size(); }
}
