package com.sellger.konta.sketch_loyaltyapp.ui.coupons;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sellger.konta.sketch_loyaltyapp.adapter.CouponAdapter;
import com.sellger.konta.sketch_loyaltyapp.adapter.RecyclerItemClickListener;
import com.sellger.konta.sketch_loyaltyapp.base.fragment.BaseFragment;
import com.sellger.konta.sketch_loyaltyapp.data.Injection;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Coupon;
import com.sellger.konta.sketch_loyaltyapp.R;
import com.sellger.konta.sketch_loyaltyapp.ui.couponDetails.CouponDetailsActivity;
import com.sellger.konta.sketch_loyaltyapp.utils.utilsMap.CustomItemDecoration;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.BlurTransformation;
import jp.wasabeef.picasso.transformations.GrayscaleTransformation;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static com.sellger.konta.sketch_loyaltyapp.Constants.BITMAP_CORNER_RADIUS;
import static com.sellger.konta.sketch_loyaltyapp.Constants.BITMAP_HEIGHT_ONE_COLUMN;
import static com.sellger.konta.sketch_loyaltyapp.Constants.BITMAP_HEIGHT_TWO_COLUMNS;
import static com.sellger.konta.sketch_loyaltyapp.Constants.BITMAP_WIDTH_ONE_COLUMN;
import static com.sellger.konta.sketch_loyaltyapp.Constants.BITMAP_WIDTH_TWO_COLUMNS;
import static com.sellger.konta.sketch_loyaltyapp.Constants.BUNDLE_TITLE_STRING;
import static com.sellger.konta.sketch_loyaltyapp.Constants.EXTRAS_ELEMENT_ID;
import static com.sellger.konta.sketch_loyaltyapp.Constants.TOAST_ERROR;

public class CouponsFragment extends BaseFragment implements CouponsContract.View, SearchView.OnQueryTextListener {

    private static final String TAG = CouponsFragment.class.getSimpleName();

    private CouponsPresenter presenter;

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

        // Extract additional data, which is fragment's title
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            getActivity().setTitle(bundle.getString(BUNDLE_TITLE_STRING));
        } else {
            getActivity().setTitle("Kupony");
        }

        setHasOptionsMenu(true);

        // Init views
        initViews();

        // Setting up views
        emptyStateView.setVisibility(View.GONE);

        presenter = new CouponsPresenter(this, Injection.provideLoyaltyRepository(getContext()));
        presenter.requestDataFromServer();
    }

    /**
     * Called from {@link #onCreate(Bundle)} to init all the views.
     */
    @Override
    public void initViews() {
        progressBar = rootView.findViewById(R.id.progress_bar);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        emptyStateView = rootView.findViewById(R.id.empty_state_coupons_container);
    }

    /**
     * Initialize the contents of the Activity's standard options menu and sets up items visibility.
     *
     * @param menu in which you place items
     * @param inflater menu inflater
     */
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

    /**
     * Implementation of callback listener that handle adapter items click events.
     * See interface {@link RecyclerItemClickListener}.
     */
    private RecyclerItemClickListener.CouponRetrofitClickListener recyclerItemClickListener = new RecyclerItemClickListener.CouponRetrofitClickListener() {
        @Override
        public void onItemCouponDetailsClick(int couponId) {
            Intent startCouponDetailsActivity = new Intent(getContext(), CouponDetailsActivity.class);
            startCouponDetailsActivity.putExtra(EXTRAS_ELEMENT_ID, couponId);
            startActivity(startCouponDetailsActivity);
        }

        @Override
        public void onItemCouponCodeCheckClick(int position, String imageUrl) {
            ImageView couponImage = recyclerView.getLayoutManager()
                    .findViewByPosition(position)
                    .findViewById(R.id.grid_item_image);
            TextView couponCodeText = recyclerView.getLayoutManager()
                    .findViewByPosition(position)
                    .findViewById(R.id.grid_item_code_text);

            // Blur, gray out and display coupon code on selected image
            switch (numOfColumns) {
                case 1:
                    Picasso.get()
                            .load(imageUrl)
                            .noPlaceholder()
                            .transform(new BlurTransformation(couponImage.getContext()))
                            .transform(new GrayscaleTransformation())
                            .transform(new RoundedCornersTransformation(BITMAP_CORNER_RADIUS, 0))
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
                            .load(imageUrl)
                            .noPlaceholder()
                            .transform(new BlurTransformation(couponImage.getContext()))
                            .transform(new GrayscaleTransformation())
                            .transform(new RoundedCornersTransformation(BITMAP_CORNER_RADIUS, 0))
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

    /**
     * Called from {@link CouponsPresenter#passDataToAdapter(List, int)} to populate adapter with data.
     *
     * @param couponList of items that are going to pass to adapter
     * @param numOfColumns in which data will be displayed
     */
    @Override
    public void setUpAdapter(List<Coupon> couponList, int numOfColumns) {
        this.numOfColumns = numOfColumns;
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

    /**
     * Called from {@link #setUpAdapter(List, int)} to change visibility of custom EmptyStateView.
     *
     * @param isNeeded boolean parameter to decide whether view should be visible or not
     */
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

    /**
     * Called from {@link CouponsPresenter#hideProgressBar()} to hide progress bar when data is fetched or not.
     */
    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    /**
     * Called from {@link CouponsPresenter#requestDataFromServer()} whenever data is
     * unavailable to get.
     *
     * @param message is a string with type of toast that should be displayed
     */
    @Override
    public void displayToastMessage(String message) {
        if (message.equals(TOAST_ERROR)) {
            message = getString(R.string.default_toast_error_message);
        }

        Toast.makeText(getContext(), message , Toast.LENGTH_LONG).show();
    }

    /**
     * Implementation of SearchView callbacks for changes to the query text.
     * @see CouponAdapter for implementation of filtering logic
     *
     * @param newText provided by the user in a SearchView
     * @return set false if the SearchView should perform the default action of showing
     * any suggestions if available
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }
}