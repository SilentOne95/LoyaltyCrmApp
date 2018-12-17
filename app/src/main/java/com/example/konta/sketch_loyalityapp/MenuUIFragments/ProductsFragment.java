package com.example.konta.sketch_loyalityapp.MenuUIFragments;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.konta.sketch_loyalityapp.Adapters.GridViewProductAdapter;
import com.example.konta.sketch_loyalityapp.MyApplication;
import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.ModelClasses.ItemProduct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProductsFragment extends Fragment {

    private static ArrayList<ItemProduct> itemList;
    private String json;
    private String layoutTitle;
    int columns = 0;

    // Temporary variables using to get json data from assets
    private static final String jsonFileData = "products.json";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_products, container, false);

        // Reading JSON file from assets
        json = ((MyApplication) getActivity().getApplication()).readFromAssets(jsonFileData);

        // Extracting objects that has been built up from parsing the given JSON file,
        // preparing and displaying data in Navigation Drawer using custom adapter
        extractDataFromJson();

        GridViewProductAdapter adapter = new GridViewProductAdapter(getActivity(), itemList, true);
        final GridView gridView = rootView.findViewById(R.id.grid_view);
        gridView.setEmptyView(rootView.findViewById(R.id.empty_state_products_container));
        gridView.setNumColumns(columns);
        gridView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(layoutTitle);
    }

    private void extractDataFromJson() {
        try {
            Resources resources = this.getResources();
            itemList = new ArrayList<>();

            JSONObject object = new JSONObject(json);
            layoutTitle = object.getString("componentTitleCurrent");

            // Get sample image and description
            String image = object.getString("contentImage");
            String description = object.getString("contentDescription");

            JSONArray array = object.getJSONArray("products");
            final int resourceCategoryImage = resources
                    .getIdentifier(image, "drawable", getActivity().getPackageName());

            Bitmap bitmap = BitmapFactory.decodeResource(resources, resourceCategoryImage);
            RoundedBitmapDrawable bitmapDrawable = RoundedBitmapDrawableFactory.create(resources, bitmap);
            bitmapDrawable.setCornerRadius(20);

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