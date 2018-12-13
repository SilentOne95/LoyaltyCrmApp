package com.example.konta.sketch_loyalityapp.MenuUIFragments;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.example.konta.sketch_loyalityapp.Adapters.GridViewCouponAdapter;
import com.example.konta.sketch_loyalityapp.Data.SampleData;
import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.ModelClasses.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CouponsFragment extends Fragment {

    private static ArrayList<Item> itemList;
    private String json;
    private String layoutTitle;
    int columns = 0;

    // Temporary variables using to get json data from assets
    private SampleData sampleData = new SampleData();
    private static final String jsonFileData = "coupons.json";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_coupons, container, false);

        // Reading JSON file from assets
        json = sampleData.readFromAssets(jsonFileData, this.getContext());

        // Extracting objects that has been built up from parsing the given JSON file,
        // preparing and displaying data in Navigation Drawer using custom adapter
        extractDataFromJson();

        GridViewCouponAdapter adapter = new GridViewCouponAdapter(getActivity(), itemList, true);
        final GridView gridView = rootView.findViewById(R.id.grid_view);
        gridView.setEmptyView(rootView.findViewById(R.id.empty_state_coupons_container));
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

            JSONArray array = object.getJSONArray("coupons");

            JSONObject insideObj = array.getJSONObject(0);
            String title = insideObj.getString("contentTitle");
            String image = insideObj.getString("contentImage");

            final int resourceCategoryImage = resources
                    .getIdentifier(image, "drawable", getActivity().getPackageName());

            Bitmap bitmap = BitmapFactory.decodeResource(resources, resourceCategoryImage);
            RoundedBitmapDrawable bitmapDrawable = RoundedBitmapDrawableFactory.create(resources, bitmap);
            bitmapDrawable.setCornerRadius(15);

            for (int i = 0; i < 10; i++) {
                itemList.add(new Item(title.concat(" ").concat(Integer.toString(i + 1)), bitmapDrawable));
            }

            columns = object.getInt("numberOfColumns");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}