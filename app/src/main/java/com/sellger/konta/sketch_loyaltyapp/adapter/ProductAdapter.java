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
import com.sellger.konta.sketch_loyaltyapp.pojo.product.Product;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static com.sellger.konta.sketch_loyaltyapp.Constants.BASE_URL_IMAGES;
import static com.sellger.konta.sketch_loyaltyapp.Constants.BITMAP_CORNER_RADIUS_ONE_COLUMN;
import static com.sellger.konta.sketch_loyaltyapp.Constants.BITMAP_CORNER_RADIUS_TWO_COLUMNS;
import static com.sellger.konta.sketch_loyaltyapp.Constants.BITMAP_HEIGHT_ONE_COLUMN;
import static com.sellger.konta.sketch_loyaltyapp.Constants.BITMAP_HEIGHT_TWO_COLUMNS;
import static com.sellger.konta.sketch_loyaltyapp.Constants.BITMAP_WIDTH_ONE_COLUMN;
import static com.sellger.konta.sketch_loyaltyapp.Constants.BITMAP_WIDTH_TWO_COLUMNS;
import static com.sellger.konta.sketch_loyaltyapp.Constants.DEFAULT_STRING;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> implements Filterable {

    private List<Product> listOfItems, listOfItemsHelper;
    private RecyclerItemClickListener.ProductRetrofitClickListener productClickListener;
    private int numOfColumns;
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public ProductAdapter(List<Product> items,
                          RecyclerItemClickListener.ProductRetrofitClickListener clickListener,
                          int columns) {
        listOfItems = items;
        listOfItemsHelper = new ArrayList<>(listOfItems);
        productClickListener = clickListener;
        numOfColumns = columns;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (numOfColumns) {
            case 1:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.grid_list_item_product_one_col, parent, false);
                break;
            case 2:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.grid_list_item_product_two_col, parent, false);
                break;
            default:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.grid_list_item_product_two_col, parent, false);
                break;
        }

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
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Product currentItem = listOfItems.get(position);
        int cornerRadius, imageWidth, imageHeight;

        switch (numOfColumns) {
            case 1:
                cornerRadius = BITMAP_CORNER_RADIUS_ONE_COLUMN;
                imageWidth = BITMAP_WIDTH_ONE_COLUMN;
                imageHeight = BITMAP_HEIGHT_ONE_COLUMN;
                break;
            case 2:
                cornerRadius = BITMAP_CORNER_RADIUS_TWO_COLUMNS;
                imageWidth = BITMAP_WIDTH_TWO_COLUMNS;
                imageHeight = BITMAP_HEIGHT_TWO_COLUMNS;
                break;
            default:
                cornerRadius = BITMAP_CORNER_RADIUS_ONE_COLUMN;
                imageWidth = BITMAP_WIDTH_ONE_COLUMN;
                imageHeight = BITMAP_HEIGHT_ONE_COLUMN;
                break;
        }

        if (currentItem.getImage() != null && !currentItem.getImage().trim().isEmpty() && !currentItem.getImage().equals("")) {
            // TODO: Upload images to server and change "else" image to no_image_available
            Picasso.get()
                    .load(BASE_URL_IMAGES + currentItem.getImage())
                    .placeholder(R.drawable.placeholder)
                    .transform(new RoundedCornersTransformation(cornerRadius, 0))
                    .error(R.drawable.no_image_available)
                    .resize(imageWidth, imageHeight)
                    .into(holder.imageView);
        } else {
            Picasso.get()
                    .load(R.drawable.no_image_available)
                    .placeholder(R.drawable.placeholder)
                    .transform(new RoundedCornersTransformation(cornerRadius, 0))
                    .resize(imageWidth, imageHeight)
                    .into(holder.imageView);
        }

        if (!TextUtils.isEmpty(currentItem.getTitle())) {
            holder.titleView.setText(currentItem.getTitle());
        } else {
            holder.titleView.setText(DEFAULT_STRING);
        }

        if (currentItem.getPrice() != null && !currentItem.getPrice().toString().trim().isEmpty()) {
            holder.price.setText(String.valueOf(decimalFormat.format(currentItem.getPrice())).concat("zł"));
        } else {
            holder.price.setText(DEFAULT_STRING);
        }
        if (!TextUtils.isEmpty(currentItem.getShortDescription())) {
            holder.shortDescription.setText(Html.fromHtml(currentItem.getShortDescription()));
        } else {
            holder.shortDescription.setText(DEFAULT_STRING);
        }
    }

    @Override
    public int getItemCount() { return listOfItems.size(); }

    @Override
    public Filter getFilter() {
        return productFilter;
    }

    private Filter productFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Product> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(listOfItemsHelper);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Product product : listOfItemsHelper) {
                    if (product.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(product);
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
