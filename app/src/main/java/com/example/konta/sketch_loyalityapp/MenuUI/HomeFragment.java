package com.example.konta.sketch_loyalityapp.MenuUI;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.konta.sketch_loyalityapp.Adapters.GridItemAdapter;
import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.ModelClasses.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    static final ArrayList<Item> itemList = new ArrayList<>();
    private String json;
    private String layoutTitle;
    private int resourceSpecialOffer;
    int columns = 0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Reading JSON file from assets
        readFromAssets();

        // Extracting objects that has been built up from parsing the given JSON file,
        // preparing and displaying data in Navigation Drawer using custom adapter
        extractDataFromJson();

        ImageView specialOfferImage = rootView.findViewById(R.id.special_offer_image);
        specialOfferImage.setImageResource(resourceSpecialOffer);

        GridItemAdapter adapter = new GridItemAdapter(getActivity(), itemList);
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

    private void readFromAssets() {
        try {
            InputStream inputStream = getActivity().getAssets().open("home.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void extractDataFromJson() {
        try {
            JSONObject object = new JSONObject(json);
            JSONArray array = object.getJSONArray("offers");

            for (int i = 0; i < array.length(); i++) {
                JSONObject insideObj = array.getJSONObject(i);
                String title = insideObj.getString("offerTitle");
                String image = insideObj.getString("offerImage");

                Resources resources = this.getResources();
                final int resourceId = resources
                        .getIdentifier(image, "drawable", getActivity().getPackageName());
                itemList.add(new Item(title, resourceId));
            }

            layoutTitle = object.getString("layoutTitle");

            Resources resources = this.getResources();
            String specialOfferImage = object.getString("specialImage");
            resourceSpecialOffer = resources
                    .getIdentifier(specialOfferImage, "drawable", getActivity().getPackageName());
            columns = object.getInt("numColumns");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}