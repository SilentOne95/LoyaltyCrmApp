package com.example.konta.sketch_loyalityapp.ui.coupons;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.konta.sketch_loyalityapp.adapter.CouponAdapter;
import com.example.konta.sketch_loyalityapp.adapter.RecyclerItemClickListener;
import com.example.konta.sketch_loyalityapp.base.BaseFragment;
import com.example.konta.sketch_loyalityapp.pojo.coupon.Coupon;
import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.ui.couponDetails.CouponDetailsActivity;
import com.example.konta.sketch_loyalityapp.utils.CustomItemDecoration;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.BlurTransformation;
import jp.wasabeef.picasso.transformations.GrayscaleTransformation;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static com.example.konta.sketch_loyalityapp.Constants.BASE_URL_IMAGES;
import static com.example.konta.sketch_loyalityapp.Constants.BITMAP_CORNER_RADIUS_ONE_COLUMN;
import static com.example.konta.sketch_loyalityapp.Constants.BITMAP_CORNER_RADIUS_TWO_COLUMNS;
import static com.example.konta.sketch_loyalityapp.Constants.BITMAP_HEIGHT_ONE_COLUMN;
import static com.example.konta.sketch_loyalityapp.Constants.BITMAP_HEIGHT_TWO_COLUMNS;
import static com.example.konta.sketch_loyalityapp.Constants.BITMAP_WIDTH_ONE_COLUMN;
import static com.example.konta.sketch_loyalityapp.Constants.BITMAP_WIDTH_TWO_COLUMNS;

public class CouponsFragment extends BaseFragment implements CouponsContract.View, SearchView.OnQueryTextListener {

    private static final String TAG = CouponsFragment.class.getSimpleName();

    CouponsPresenter presenter;

    private RecyclerView recyclerView;
    private View emptyStateView;
    private ProgressBar progressBar;
    private int numOfColumns;
    private CouponAdapter adapter;

    @Override
    protected int getLayout() { return R.layout.fragment_coupons; }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Kupony");

        setHasOptionsMenu(true);

        progressBar = rootView.findViewById(R.id.progress_bar);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        emptyStateView = rootView.findViewById(R.id.empty_state_coupons_container);
        emptyStateView.setVisibility(View.GONE);

        presenter = new CouponsPresenter(this, new CouponsModel());
        presenter.requestDataFromServer();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem searchItem = menu.findItem(R.id.main_menu_search);
        MenuItem optionsItem = menu.findItem(R.id.main_menu_options);
        optionsItem.setVisible(false);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search");

        super.onCreateOptionsMenu(menu, inflater);
    }

    private RecyclerItemClickListener.CouponRetrofitClickListener recyclerItemClickListener = new RecyclerItemClickListener.CouponRetrofitClickListener() {
        @Override
        public void onItemCouponDetailsClick(int couponId) {
            Intent startCouponDetailsActivity = new Intent(getContext(), CouponDetailsActivity.class);
            startCouponDetailsActivity.putExtra("EXTRA_ELEMENT_ID", couponId);
            startActivity(startCouponDetailsActivity);
        }

        @Override
        public void onItemCouponCodeCheckClick(int position, String imageUrl) {
            ImageView couponImage = recyclerView.getChildAt(position).findViewById(R.id.grid_item_image);
            TextView couponCodeText = recyclerView.getChildAt(position).findViewById(R.id.grid_item_code_text);

            switch (numOfColumns) {
                case 1:
                    Picasso.get()
                            .load(BASE_URL_IMAGES + imageUrl)
                            .noPlaceholder()
                            .transform(new BlurTransformation(couponImage.getContext()))
                            .transform(new GrayscaleTransformation())
                            .transform(new RoundedCornersTransformation(BITMAP_CORNER_RADIUS_ONE_COLUMN, 0))
                            .error(R.drawable.no_image_available)
                            .resize(BITMAP_WIDTH_ONE_COLUMN, BITMAP_HEIGHT_ONE_COLUMN)
                            .into(couponImage, new Callback() {
                                @Override
                                public void onSuccess() {
                                    couponCodeText.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onError(Exception e) {

                                }
                            });
                    break;
                case 2:
                    Picasso.get()
                            .load(BASE_URL_IMAGES + imageUrl)
                            .noPlaceholder()
                            .transform(new BlurTransformation(couponImage.getContext()))
                            .transform(new GrayscaleTransformation())
                            .transform(new RoundedCornersTransformation(BITMAP_CORNER_RADIUS_TWO_COLUMNS, 0))
                            .error(R.drawable.no_image_available)
                            .resize(BITMAP_WIDTH_TWO_COLUMNS, BITMAP_HEIGHT_TWO_COLUMNS)
                            .into(couponImage, new Callback() {
                                @Override
                                public void onSuccess() {
                                    couponCodeText.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onError(Exception e) {

                                }
                            });
                    break;
            }
        }
    };

    @Override
    public void setUpAdapter(List<Coupon> couponList, int numOfColumns) {
        this.numOfColumns = numOfColumns;
        // Set empty state view if needed
        if (couponList.isEmpty()) {
            setUpEmptyStateView(true);
        } else {
            setUpEmptyStateView(false);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numOfColumns));

            CustomItemDecoration itemDecoration;
            if (numOfColumns == 1) {
                itemDecoration = new CustomItemDecoration(getContext(), R.dimen.mid_value);
            } else {
                itemDecoration = new CustomItemDecoration(getContext(), R.dimen.small_value);
            }
            recyclerView.addItemDecoration(itemDecoration);
            adapter = new CouponAdapter(couponList, recyclerItemClickListener, numOfColumns);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void setUpEmptyStateView(boolean isNeeded) {
        if (isNeeded) {
            recyclerView.setVisibility(View.GONE);
            emptyStateView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyStateView.setVisibility(View.GONE);
        }
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return false;
    }
}