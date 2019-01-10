package com.example.konta.sketch_loyalityapp.ui.couponsFragment;

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

import com.example.konta.sketch_loyalityapp.adapters.CouponRetrofitAdapter;
import com.example.konta.sketch_loyalityapp.adapters.RecyclerItemClickListener;
import com.example.konta.sketch_loyalityapp.base.BaseFragment;
import com.example.konta.sketch_loyalityapp.adapterModel.ItemCoupon;
import com.example.konta.sketch_loyalityapp.data.coupon.Coupon;
import com.example.konta.sketch_loyalityapp.root.MyApplication;
import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.ui.mainActivity.MainActivity;
import com.example.konta.sketch_loyalityapp.utils.CustomItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.konta.sketch_loyalityapp.Constants.BITMAP_CORNER_RADIUS;

public class CouponsFragment extends BaseFragment implements CouponsContract.View {

    CouponsPresenter mPresenter;

    private static ArrayList<ItemCoupon> itemList;
    private String json;
    private String layoutTitle;
    int columns = 0;
    private RecyclerView recyclerView;
    private View emptyStateView;

    // Temporary variables using to get json data from assets
    private static final String jsonFileData = "coupons.json";

    @Override
    protected int getLayout() { return R.layout.fragment_coupons; }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Reading JSON file from assets
        json = ((MyApplication) getActivity().getApplication()).readFromAssets(jsonFileData);

        // Extracting objects that has been built up from parsing the given JSON file,
        // preparing and displaying data using custom adapter
        extractDataFromJson();

        getActivity().setTitle(layoutTitle);

        // Retrofit
        mPresenter = new CouponsPresenter(this, new CouponsModel());
        mPresenter.requestDataFromServer();

        // Set up adapter
        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        CustomItemDecoration itemDecoration = new CustomItemDecoration(getContext(), R.dimen.mid_value);
        recyclerView.addItemDecoration(itemDecoration);
        emptyStateView = rootView.findViewById(R.id.empty_state_coupons_container);
    }

    private RecyclerItemClickListener.CouponRetrofitClickListener recyclerItemClickListener = new RecyclerItemClickListener.CouponRetrofitClickListener() {
        @Override
        public void onItemCouponDetailsClick(int couponId) {
            Toast.makeText(getContext(), "Show details" , Toast.LENGTH_LONG).show();
        }

        @Override
        public void onItemCouponCodeCheckClick(Coupon coupon) {
            Toast.makeText(getContext(), "Show coupon code" , Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void setUpAdapter(List<Coupon> couponList) {

        recyclerView.setAdapter(new CouponRetrofitAdapter(couponList, recyclerItemClickListener));

        // Check if empty state view is needed
        if (!couponList.isEmpty()) {
            recyclerView.setVisibility(View.VISIBLE);
            emptyStateView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            emptyStateView.setVisibility(View.VISIBLE);
        }
    }

    private void extractDataFromJson() {
        try {
            Resources resources = this.getResources();
            itemList = new ArrayList<>();

            JSONObject object = new JSONObject(json);
            layoutTitle = object.getString("componentTitleCurrent");

            // Get sample image and description
            String image = object.getString("contentImage");
            String description = object.getString("contentShortDescription");

            final int resourceCategoryImage = resources
                    .getIdentifier(image, "drawable", MainActivity.PACKAGE_NAME);

            Bitmap bitmap = BitmapFactory.decodeResource(resources, resourceCategoryImage);
            RoundedBitmapDrawable bitmapDrawable = RoundedBitmapDrawableFactory.create(resources, bitmap);
            bitmapDrawable.setCornerRadius(BITMAP_CORNER_RADIUS);

            JSONArray array = object.getJSONArray("coupons");
            for (int i = 0; i < array.length(); i++) {
                JSONObject insideObj = array.getJSONObject(i);
                String title = insideObj.getString("contentTitle");
                double basicPrice = insideObj.getDouble("contentBasicPrice");
                double finalPrice = insideObj.getDouble("contentFinalPrice");
                int discount = insideObj.getInt("contentDiscount");
                String date = insideObj.getString("contentValidDate");

                itemList.add(new ItemCoupon(title, bitmapDrawable, discount, basicPrice,
                        finalPrice, date, description));
            }

            columns = object.getInt("numberOfColumns");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}