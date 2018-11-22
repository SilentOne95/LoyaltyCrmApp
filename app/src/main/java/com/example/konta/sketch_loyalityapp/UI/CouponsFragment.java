package com.example.konta.sketch_loyalityapp.UI;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

public class CouponsFragment extends Fragment {

    static final ArrayList<Item> itemList = new ArrayList<>();
    private String json;
    private String layoutTitle;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_coupons, container, false);

        // Reading JSON file from assets
        try {
            InputStream inputStream = getActivity().getAssets().open("coupons.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Extracting objects that has been built up from parsing the given JSON file
        int columns = 0;
        try {
            JSONObject object = new JSONObject(json);
            JSONArray array = object.getJSONArray("coupons");

            for (int i = 0; i < array.length(); i++) {
                JSONObject insideObj = array.getJSONObject(i);
                String title = insideObj.getString("couponTitle");
                String image = insideObj.getString("couponImage");

                Resources resources = this.getResources();
                final int resourceId = resources
                        .getIdentifier(image, "drawable", getActivity().getPackageName());
                itemList.add(new Item(title, resourceId));
            }

            layoutTitle = object.getString("layoutTitle");
            columns = object.getInt("numColumns");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        GridItemAdapter adapter = new GridItemAdapter(getActivity(), itemList);
        final GridView gridView = rootView.findViewById(R.id.grid_view);
        gridView.setNumColumns(columns);
        gridView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(layoutTitle);
    }
}
