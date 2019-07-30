package com.sellger.konta.sketch_loyaltyapp.ui.coupons;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.os.Handler;
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

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import jp.wasabeef.picasso.transformations.BlurTransformation;
import jp.wasabeef.picasso.transformations.GrayscaleTransformation;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static com.sellger.konta.sketch_loyaltyapp.Constants.BITMAP_CORNER_RADIUS;
import static com.sellger.konta.sketch_loyaltyapp.Constants.BITMAP_HEIGHT_ONE_COLUMN;
import static com.sellger.konta.sketch_loyaltyapp.Constants.BITMAP_HEIGHT_TWO_COLUMNS;
import static com.sellger.konta.sketch_loyaltyapp.Constants.BITMAP_WIDTH_ONE_COLUMN;
import static com.sellger.konta.sketch_loyaltyapp.Constants.BITMAP_WIDTH_TWO_COLUMNS;
import static com.sellger.konta.sketch_loyaltyapp.Constants.BUNDLE_TITLE_STRING;
import static com.sellger.konta.sketch_loyaltyapp.Constants.DELAY_REFRESH_NETWORK_CONNECTION;
import static com.sellger.konta.sketch_loyaltyapp.Constants.EXTRAS_ELEMENT_ID;
import static com.sellger.konta.sketch_loyaltyapp.Constants.TOAST_ERROR;

public class CouponsFragment extends BaseFragment implements CouponsContract.View, View.OnClickListener,
        SearchView.OnQueryTextListener {

    private static final String TAG = CouponsFragment.class.getSimpleName();

    private CouponsPresenter presenter;

    private SearchView mSearchView;

    private RecyclerView mRecyclerView;
    private View mEmptyStateView;
    private ProgressBar mProgressBar;
    private int mNumOfColumns;
    private CouponAdapter mAdapter;

    private View mNoNetworkView;
    private CircularProgressButton mRefreshNetworkButton;

    @Override
    protected int getLayout() {
        return R.layout.fragment_coupons;
    }

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

        presenter = new CouponsPresenter(this, Injection.provideLoyaltyRepository(getContext()));
        presenter.requestDataFromServer(getContext());
    }

    /**
     * Called from {@link #onCreate(Bundle)} to init all the views.
     */
    @Override
    public void initViews() {
        mProgressBar = rootView.findViewById(R.id.progress_bar);
        mRecyclerView = rootView.findViewById(R.id.recycler_view);
        mEmptyStateView = rootView.findViewById(R.id.empty_state_coupons_container);
        mNoNetworkView = getActivity().findViewById(R.id.no_network_connection_container);
        mRefreshNetworkButton = getActivity().findViewById(R.id.no_network_connection_button);

        // Setting up views
        mEmptyStateView.setVisibility(View.GONE);
    }

    /**
     * Initialize the contents of the Activity's standard options menu and sets up items visibility.
     *
     * @param menu     in which you place items
     * @param inflater menu inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem searchItem = menu.findItem(R.id.main_menu_search);
        MenuItem optionsItem = menu.findItem(R.id.main_menu_options);
        optionsItem.setVisible(false);
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mSearchView.setQueryHint(getString(R.string.main_menu_search_hint));
        mSearchView.setOnQueryTextListener(this);

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
            ImageView couponImage = mRecyclerView.getLayoutManager()
                    .findViewByPosition(position)
                    .findViewById(R.id.grid_item_image);
            TextView couponCodeText = mRecyclerView.getLayoutManager()
                    .findViewByPosition(position)
                    .findViewById(R.id.grid_item_code_text);

            // Blur, gray out and display coupon code on selected image
            switch (mNumOfColumns) {
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
     * Called from {@link #checkIfNetworkIsAvailableAndGetData()}, {@link #onDestroyView()} and
     * {@link CouponsPresenter#requestDataFromServer()} to display / hide view to user about potential
     * network problem.
     *
     * @param shouldBeVisible boolean value to determine whether view should be visible or not
     */
    @Override
    public void changeVisibilityNoNetworkConnectionView(boolean shouldBeVisible) {
        if (shouldBeVisible) {
            mNoNetworkView.setVisibility(View.VISIBLE);
            mRefreshNetworkButton.setOnClickListener(this);
        } else {
            mNoNetworkView.setVisibility(View.GONE);
        }
    }

    /**
     * Called from {@link CouponsPresenter#passDataToAdapter(List, int)} to set up adapter
     * {@link CouponAdapter} with data.
     *
     * @param couponList   of items that are going to pass to adapter
     * @param numOfColumns in which data will be displayed
     */
    @Override
    public void setUpAdapter(List<Coupon> couponList, int numOfColumns) {
        mNumOfColumns = numOfColumns;
        if (couponList.isEmpty()) {
            setUpEmptyStateView(true);
        } else {
            setUpEmptyStateView(false);
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), numOfColumns));

            CustomItemDecoration itemDecoration;
            if (numOfColumns == 1) {
                itemDecoration = new CustomItemDecoration(getContext(), R.dimen.mid_value);
            } else {
                itemDecoration = new CustomItemDecoration(getContext(), R.dimen.small_value);
            }
            mRecyclerView.addItemDecoration(itemDecoration);
            mRecyclerView.setHasFixedSize(true);
            mAdapter = new CouponAdapter(couponList, recyclerItemClickListener, numOfColumns);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    /**
     * Called from {@link CouponsPresenter#hideProgressBar()} and {@link #checkIfNetworkIsAvailableAndGetData()}
     * to inform user whether data is loading or not.
     */
    @Override
    public void changeVisibilityProgressBar(boolean shouldBeVisible) {
        if (shouldBeVisible) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    /**
     * Called from {@link #setUpAdapter(List, int)} to change visibility of custom EmptyStateView.
     *
     * @param isNeeded boolean parameter to decide whether view should be visible or not
     */
    private void setUpEmptyStateView(boolean isNeeded) {
        if (isNeeded) {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyStateView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyStateView.setVisibility(View.GONE);
        }
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

        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    /**
     * Callbacks for changes to the query text.
     * Called when the query text is changed by the user and populate data with {@link CouponAdapter}.
     *
     * @param newText provided by user
     * @return true if the action was handled by the listener
     * @see <a href="https://developer.android.com/reference/android/widget/SearchView.OnQueryTextListener">Android Dev Doc</a>
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        if (mAdapter != null) {
            mAdapter.getFilter().filter(newText);
        }
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param view which was clicked
     * @see <a href="https://developer.android.com/reference/android/view/View.OnClickListener">Android Dev Doc</a>
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.no_network_connection_button) {
            checkIfNetworkIsAvailableAndGetData();
        }
    }

    /**
     * Called from {@link #onClick(View)} when user what to get data and reload view.
     */
    private void checkIfNetworkIsAvailableAndGetData() {
        mRefreshNetworkButton.startMorphAnimation();
        new Handler().postDelayed(() -> {
            if (presenter.isNetworkAvailable(getContext())) {
                mRefreshNetworkButton.doneLoadingAnimation(Color.rgb(255, 152, 0),
                        BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_check));
                presenter.requestDataFromServer(getContext());

                changeVisibilityNoNetworkConnectionView(false);
                changeVisibilityProgressBar(true);
            } else {
                mRefreshNetworkButton.revertAnimation(() -> null);
            }
        }, DELAY_REFRESH_NETWORK_CONNECTION);
    }

    @Override
    public void onDestroyView() {
        changeVisibilityNoNetworkConnectionView(false);
        super.onDestroyView();
    }
}