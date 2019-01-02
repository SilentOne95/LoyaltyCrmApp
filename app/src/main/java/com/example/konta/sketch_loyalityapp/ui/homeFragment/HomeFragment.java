package com.example.konta.sketch_loyalityapp.ui.homeFragment;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.konta.sketch_loyalityapp.adapters.HomeAdapter;
import com.example.konta.sketch_loyalityapp.base.BaseFragment;
import com.example.konta.sketch_loyalityapp.modelClasses.Item;
import com.example.konta.sketch_loyalityapp.root.MyApplication;
import com.example.konta.sketch_loyalityapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.konta.sketch_loyalityapp.Constants.BITMAP_CORNER_RADIUS;
import static com.example.konta.sketch_loyalityapp.Constants.INITIAL_CAPACITY_ARRAY;

public class HomeFragment extends BaseFragment {

    private static ArrayList<Item> itemList;
    private String json;
    private String layoutTitle;
    private int resourceSpecialOffer;
    int columns = 0;

    // Temporary variables using to get json data from assets
    private static final String jsonFileData = "home.json";

    @Override
    protected int getLayout() { return R.layout.fragment_home; }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Reading JSON file from assets
        json = ((MyApplication) getActivity().getApplication()).readFromAssets(jsonFileData);

        // Extracting objects that has been built up from parsing the given JSON file,
        // preparing and displaying data using custom adapter
        extractDataFromJson();

        getActivity().setTitle(layoutTitle);

        ImageView specialOfferImage = rootView.findViewById(R.id.special_offer_image);
        specialOfferImage.setImageResource(resourceSpecialOffer);

        HomeAdapter adapter = new HomeAdapter(getActivity(), itemList, false);
        final GridView gridView = rootView.findViewById(R.id.grid_view);
        gridView.setNumColumns(columns);
        gridView.setAdapter(adapter);
    }

    private void extractDataFromJson() {
        try {
            Resources resources = this.getResources();
            itemList = new ArrayList<>(INITIAL_CAPACITY_ARRAY);

            JSONObject object = new JSONObject(json);
            layoutTitle = object.getString("componentTitleCurrent");

            JSONArray array = object.getJSONArray("components");

            for (int i = 0; i < array.length(); i++) {
                JSONObject insideObj = array.getJSONObject(i);

                String title = insideObj.getString("componentTitle");
                String image = insideObj.getString("componentImage");

                final int resourceCategoryImage = resources
                        .getIdentifier(image, "drawable", getActivity().getPackageName());

                Bitmap bitmap = BitmapFactory.decodeResource(resources, resourceCategoryImage);
                RoundedBitmapDrawable bitmapDrawable = RoundedBitmapDrawableFactory.create(resources, bitmap);
                bitmapDrawable.setCornerRadius(BITMAP_CORNER_RADIUS);

                itemList.add(new Item(title, bitmapDrawable));
            }

            String specialOfferImage = object.getString("specialOfferImage");

            resourceSpecialOffer = resources.getIdentifier(specialOfferImage, "drawable", getActivity().getPackageName());
            columns = object.getInt("numberOfColumns");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}