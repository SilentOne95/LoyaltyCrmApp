package com.example.konta.sketch_loyalityapp.ui.productsFragment;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.konta.sketch_loyalityapp.adapters.ProductAdapter;
import com.example.konta.sketch_loyalityapp.adapters.RecyclerItemClickListener;
import com.example.konta.sketch_loyalityapp.base.BaseFragment;
import com.example.konta.sketch_loyalityapp.root.MyApplication;
import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.modelClasses.ItemProduct;
import com.example.konta.sketch_loyalityapp.ui.mainActivity.MainActivity;
import com.example.konta.sketch_loyalityapp.utils.CustomItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.konta.sketch_loyalityapp.Constants.BITMAP_CORNER_RADIUS;

public class ProductsFragment extends BaseFragment {

    private static ArrayList<ItemProduct> itemList;
    private String json;
    private String layoutTitle;
    int columns = 0;

    // Temporary variables using to get json data from assets
    private static final String jsonFileData = "products.json";

    @Override
    protected int getLayout() { return R.layout.fragment_products; }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Reading JSON file from assets
        json = ((MyApplication) getActivity().getApplication()).readFromAssets(jsonFileData);

        // Extracting objects that has been built up from parsing the given JSON file,
        // preparing and displaying data using custom adapter
        extractDataFromJson();

        getActivity().setTitle(layoutTitle);

        // Set up adapter
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        CustomItemDecoration itemDecoration = new CustomItemDecoration(getContext(), R.dimen.mid_value);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(new ProductAdapter(itemList, recyclerItemClickListener));

        // Empty state view
        View emptyStateView = rootView.findViewById(R.id.empty_state_products_container);
        if (!itemList.isEmpty()) {
            recyclerView.setVisibility(View.VISIBLE);
            emptyStateView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            emptyStateView.setVisibility(View.VISIBLE);
        }
    }

    private RecyclerItemClickListener.ProductClickListener recyclerItemClickListener = new RecyclerItemClickListener.ProductClickListener() {
        @Override
        public void onItemProductClick(ItemProduct item) {
            Toast.makeText(getContext(), item.getItemTitle(), Toast.LENGTH_LONG).show();
        }
    };

    private void extractDataFromJson() {
        try {
            Resources resources = this.getResources();
            itemList = new ArrayList<>();

            JSONObject object = new JSONObject(json);
            layoutTitle = object.getString("componentTitleCurrent");

            // Get sample image and description
            String image = object.getString("contentImage");
            String description = object.getString("contentShortDescription");

            JSONArray array = object.getJSONArray("products");
            final int resourceCategoryImage = resources
                    .getIdentifier(image, "drawable", MainActivity.PACKAGE_NAME);

            Bitmap bitmap = BitmapFactory.decodeResource(resources, resourceCategoryImage);
            RoundedBitmapDrawable bitmapDrawable = RoundedBitmapDrawableFactory.create(resources, bitmap);
            bitmapDrawable.setCornerRadius(BITMAP_CORNER_RADIUS);

            for (int i = 0; i < array.length(); i++) {
                JSONObject insideObj = array.getJSONObject(i);
                String title = insideObj.getString("contentTitle");
                double price = insideObj.getDouble("contentPrice");
                itemList.add(new ItemProduct(title, bitmapDrawable, price, description));
            }

            columns = object.getInt("numberOfColumns");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}