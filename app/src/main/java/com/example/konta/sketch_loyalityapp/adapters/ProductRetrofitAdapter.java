package com.example.konta.sketch_loyalityapp.adapters;

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
import com.example.konta.sketch_loyalityapp.data.product.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.konta.sketch_loyalityapp.Constants.BASE_URL_IMAGES;

public class ProductRetrofitAdapter extends RecyclerView.Adapter<ProductRetrofitAdapter.ViewHolder> {

    private List<Product> listOfItems;
    private RecyclerItemClickListener.ProductRetrofitClickListener productClickListener;

    public ProductRetrofitAdapter(List<Product> items,
                          RecyclerItemClickListener.ProductRetrofitClickListener clickListener ) {
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
        private ImageView imageView;
        private TextView titleView, price, shortDescription;
        private Button button;

        ViewHolder(@NonNull View view) {
            super(view);
            imageView = view.findViewById(R.id.grid_item_image);
            titleView = view.findViewById(R.id.grid_item_title_text);
            price = view.findViewById(R.id.grid_item_price_amount);
            shortDescription = view.findViewById(R.id.grid_item_content_description);
            button = view.findViewById(R.id.grid_item_view_details_button);
            button.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            productClickListener.onItemProductClick(listOfItems.get(getAdapterPosition()).getId());
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product currentItem = listOfItems.get(position);

        // Temporary solution - testing library
        if (!(currentItem.getImage() == null || currentItem.getImage().isEmpty())) {
            Picasso.get().load(BASE_URL_IMAGES + currentItem.getImage()).into(holder.imageView);
        }

        holder.titleView.setText(currentItem.getTitle());
        holder.price.setText(String.valueOf(currentItem.getPrice()).concat(" ").concat("zł"));
        holder.shortDescription.setText(Html.fromHtml(currentItem.getShortDescription()));
    }

    @Override
    public int getItemCount() { return listOfItems.size(); }
}
