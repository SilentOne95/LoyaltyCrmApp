package com.example.konta.sketch_loyalityapp.MenuUIFragments;

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

import com.example.konta.sketch_loyalityapp.Adapters.GridItemAdapter;
import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.ModelClasses.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ProductsFragment extends Fragment {

    static final ArrayList<Item> itemList = new ArrayList<>();
    private String json;
    private String layoutTitle;
    int columns = 0;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_products, container, false);

        // Reading JSON file from assets
        readFromAssets();

        // Extracting objects that has been built up from parsing the given JSON file,
        // preparing and displaying data in Navigation Drawer using custom adapter
        extractDataFromJson();

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
            InputStream inputStream = getActivity().getAssets().open("products.json");
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
            layoutTitle = object.getString("layoutTitle");

            JSONArray array = object.getJSONArray("products");
            JSONObject insideObj = array.getJSONObject(0);

            String title = insideObj.getString("productTitle");
            String image = insideObj.getString("productImage");

            Resources resources = this.getResources();
            final int resourceId = resources
                    .getIdentifier(image, "drawable", getActivity().getPackageName());

            for (int i = 1; i < 11; i++) {
                itemList.add(new Item(title.concat(" ").concat(Integer.toString(i)), resourceId));
            }

            columns = object.getInt("numColumns");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
