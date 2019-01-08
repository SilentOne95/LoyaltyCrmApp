package com.example.konta.sketch_loyalityapp.ui.homeFragment;

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
import android.widget.ImageView;

import com.example.konta.sketch_loyalityapp.adapters.RecyclerItemClickListener;
import com.example.konta.sketch_loyalityapp.utils.CustomItemDecoration;
import com.example.konta.sketch_loyalityapp.adapters.HomeAdapter;
import com.example.konta.sketch_loyalityapp.base.BaseFragment;
import com.example.konta.sketch_loyalityapp.model.adapterItem.ItemHome;
import com.example.konta.sketch_loyalityapp.root.MyApplication;
import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.ui.mainActivity.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.konta.sketch_loyalityapp.Constants.BITMAP_CORNER_RADIUS;
import static com.example.konta.sketch_loyalityapp.Constants.INITIAL_CAPACITY_ARRAY;

public class HomeFragment extends BaseFragment implements HomeContract.View {

    private static ArrayList<ItemHome> itemList;
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

        // Set up adapter
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        CustomItemDecoration itemDecoration = new CustomItemDecoration(getContext(), R.dimen.small_value);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(new HomeAdapter(itemList, recyclerItemClickListener));
    }

    private RecyclerItemClickListener.HomeClickListener recyclerItemClickListener = new RecyclerItemClickListener.HomeClickListener() {
        @Override
        public void onItemHomeClick(ItemHome item) {
            navigationPresenter.getSelectedLayoutType(item);
        }
    };

    private void extractDataFromJson() {
        try {
            Resources resources = this.getResources();
            itemList = new ArrayList<>(INITIAL_CAPACITY_ARRAY);

            JSONObject object = new JSONObject(json);
            layoutTitle = object.getString("componentTitleCurrent");

            String specialOfferImage = object.getString("specialOfferImage");
            resourceSpecialOffer = resources.getIdentifier(specialOfferImage, "drawable", MainActivity.PACKAGE_NAME);

            JSONArray array = object.getJSONArray("components");

            for (int i = 0; i < array.length(); i++) {
                JSONObject insideObj = array.getJSONObject(i);

                String title = insideObj.getString("componentTitle");
                String image = insideObj.getString("componentImage");
                String type = insideObj.getString("componentType");

                final int resourceCategoryImage = resources
                        .getIdentifier(image, "drawable", MainActivity.PACKAGE_NAME);

                Bitmap bitmap = BitmapFactory.decodeResource(resources, resourceCategoryImage);
                RoundedBitmapDrawable bitmapDrawable = RoundedBitmapDrawableFactory.create(resources, bitmap);
                bitmapDrawable.setCornerRadius(BITMAP_CORNER_RADIUS);

                itemList.add(new ItemHome(title, bitmapDrawable, type));
            }

            columns = object.getInt("numberOfColumns");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String getSelectedLayoutType(ItemHome item) {
        return item.getLayoutType();
    }
}