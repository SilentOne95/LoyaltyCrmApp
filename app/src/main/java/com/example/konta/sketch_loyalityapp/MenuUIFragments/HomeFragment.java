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
import android.widget.ImageView;

import com.example.konta.sketch_loyalityapp.Adapters.GridViewProductAdapter;
import com.example.konta.sketch_loyalityapp.Data.SampleData;
import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.ModelClasses.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private static ArrayList<Item> itemList;
    private String json;
    private String layoutTitle;
    private int resourceSpecialOffer;
    int columns = 0;

    // Temporary variables using to get json data from assets
    private SampleData sampleData = new SampleData();
    private static final String jsonFileData = "home.json";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Reading JSON file from assets
        json = sampleData.readFromAssets(jsonFileData, this.getContext());

        // Extracting objects that has been built up from parsing the given JSON file,
        // preparing and displaying data in Navigation Drawer using custom adapter
        extractDataFromJson();

        ImageView specialOfferImage = rootView.findViewById(R.id.special_offer_image);
        specialOfferImage.setImageResource(resourceSpecialOffer);

        GridViewProductAdapter adapter = new GridViewProductAdapter(getActivity(), itemList, false);
        final GridView gridView = rootView.findViewById(R.id.grid_view);
        gridView.setNumColumns(columns);
        gridView.setAdapter(adapter);
        gridView.setNestedScrollingEnabled(false);

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

            JSONArray array = object.getJSONArray("components");

            for (int i = 0; i < array.length(); i++) {
                JSONObject insideObj = array.getJSONObject(i);

                String title = insideObj.getString("componentTitle");
                String image = insideObj.getString("componentImage");

                final int resourceCategoryImage = resources
                        .getIdentifier(image, "drawable", getActivity().getPackageName());

                Bitmap bitmap = BitmapFactory.decodeResource(resources, resourceCategoryImage);
                RoundedBitmapDrawable bitmapDrawable = RoundedBitmapDrawableFactory.create(resources, bitmap);
                bitmapDrawable.setCornerRadius(20);

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