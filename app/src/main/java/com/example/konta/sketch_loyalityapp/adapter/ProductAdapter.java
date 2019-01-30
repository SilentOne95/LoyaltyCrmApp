package com.example.konta.sketch_loyalityapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.pojo.product.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static com.example.konta.sketch_loyalityapp.Constants.BITMAP_CORNER_RADIUS_SINGLE_COLUMN;
import static com.example.konta.sketch_loyalityapp.Constants.BITMAP_CORNER_RADIUS_THREE_COLUMNS;
import static com.example.konta.sketch_loyalityapp.Constants.BITMAP_CORNER_RADIUS_TWO_COLUMNS;
import static com.example.konta.sketch_loyalityapp.Constants.BITMAP_HEIGHT_SINGLE_COLUMN;
import static com.example.konta.sketch_loyalityapp.Constants.BITMAP_HEIGHT_THREE_COLUMNS;
import static com.example.konta.sketch_loyalityapp.Constants.BITMAP_HEIGHT_TWO_COLUMNS;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<Product> listOfItems;
    private RecyclerItemClickListener.ProductRetrofitClickListener productClickListener;
    private int numOfColumns;

    public ProductAdapter(List<Product> items,
                          RecyclerItemClickListener.ProductRetrofitClickListener clickListener,
                          int columns) {
        listOfItems = items;
        productClickListener = clickListener;
        numOfColumns = columns;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_list_item_product, parent, false);
        return new ProductAdapter.ViewHolder(view);
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
        int cornerRadius, imageHeight;

        Log.d("test", String.valueOf(numOfColumns));
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

        Log.d("test", String.valueOf(cornerRadius) + String.valueOf(imageHeight));


        if (!(currentItem.getImage() == null || currentItem.getImage().isEmpty())) {
            Picasso.get()
                    .load("")
                    .transform(new RoundedCornersTransformation(cornerRadius, 0))
                    .resize(holder.imageView.getWidth(),imageHeight)
                    .into(holder.imageView);
        }

        holder.titleView.setText(currentItem.getTitle());
        holder.price.setText(String.valueOf(currentItem.getPrice()).concat(" ").concat("zł"));
        holder.shortDescription.setText(Html.fromHtml(currentItem.getShortDescription()));
    }

    @Override
    public int getItemCount() { return listOfItems.size(); }
}
