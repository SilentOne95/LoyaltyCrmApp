package com.sellger.konta.sketch_loyaltyapp.ui.products;

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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sellger.konta.sketch_loyaltyapp.adapter.ProductAdapter;
import com.sellger.konta.sketch_loyaltyapp.adapter.RecyclerItemClickListener;
import com.sellger.konta.sketch_loyaltyapp.base.fragment.BaseFragment;
import com.sellger.konta.sketch_loyaltyapp.data.Injection;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Product;
import com.sellger.konta.sketch_loyaltyapp.R;
import com.sellger.konta.sketch_loyaltyapp.ui.productDetails.ProductDetailsActivity;
import com.sellger.konta.sketch_loyaltyapp.utils.utilsMap.CustomItemDecoration;

import java.util.List;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

import static com.sellger.konta.sketch_loyaltyapp.Constants.BUNDLE_TITLE_STRING;
import static com.sellger.konta.sketch_loyaltyapp.Constants.DELAY_REFRESH_NETWORK_CONNECTION;
import static com.sellger.konta.sketch_loyaltyapp.Constants.EXTRAS_ELEMENT_ID;
import static com.sellger.konta.sketch_loyaltyapp.Constants.TOAST_ERROR;

public class ProductsFragment extends BaseFragment implements ProductsContract.View, View.OnClickListener,
        SearchView.OnQueryTextListener {

    private static final String TAG = ProductsFragment.class.getSimpleName();

    private ProductsPresenter presenter;

    private RecyclerView mRecyclerView;
    private View mEmptyStateView;
    private ProgressBar mProgressBar;
    private ProductAdapter mAdapter;

    private View mNoNetworkView;
    private CircularProgressButton mRefreshNetworkButton;

    @Override
    protected int getLayout() {
        return R.layout.fragment_products;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Extract additional data, which is fragment's title
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            getActivity().setTitle(bundle.getString(BUNDLE_TITLE_STRING));
        } else {
            getActivity().setTitle("Produkty");
        }

        setHasOptionsMenu(true);

        // Init views
        initViews();

        // Setting up views
        mEmptyStateView.setVisibility(View.GONE);

        // Setting up presenter
        presenter = new ProductsPresenter(this, Injection.provideLoyaltyRepository(getContext()));
        presenter.requestDataFromServer(getContext());
    }

    /**
     * Called from {@link #onCreate(Bundle)} to init all the views.
     */
    @Override
    public void initViews() {
        mProgressBar = rootView.findViewById(R.id.progress_bar);
        mRecyclerView = rootView.findViewById(R.id.recycler_view);
        mEmptyStateView = rootView.findViewById(R.id.empty_state_products_container);

        mNoNetworkView = getActivity().findViewById(R.id.no_network_connection_container);
        mRefreshNetworkButton = getActivity().findViewById(R.id.no_network_connection_button);
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

    /**
     * Called from {@link #checkIfNetworkIsAvailableAndGetData()}, {@link #onDestroyView()} and
     * {@link ProductsPresenter#requestDataFromServer()} to display / hide view to user about potential
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
     * Implementation of callback listener that handle adapter items click events.
     * See interface {@link RecyclerItemClickListener}.
     */
    private RecyclerItemClickListener.ProductRetrofitClickListener recyclerItemClickListener = productId -> {
        Intent startProductDetailsActivity = new Intent(getContext(), ProductDetailsActivity.class);
        startProductDetailsActivity.putExtra(EXTRAS_ELEMENT_ID, productId);
        startActivity(startProductDetailsActivity);
    };

    /**
     * Called from {@link ProductsPresenter#passDataToAdapter(List, int)} to set up adapter
     * {@link ProductAdapter} with data.
     *
     * @param productList  of items that are going to pass to adapter
     * @param numOfColumns in which data will be displayed
     */
    @Override
    public void setUpAdapter(List<Product> productList, int numOfColumns) {
        if (productList.isEmpty()) {
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
            mAdapter = new ProductAdapter(productList, recyclerItemClickListener, numOfColumns);
            mRecyclerView.setAdapter(mAdapter);
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
     * Called from {@link ProductsPresenter#hideProgressBar()} and {@link #checkIfNetworkIsAvailableAndGetData()}
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
     * Called from {@link ProductsPresenter#requestDataFromServer()} whenever data is
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
     * Called when the query text is changed by the user and populate data with {@link ProductAdapter}.
     *
     * @param newText provided by user
     * @return true if the action was handled by the listener
     * @see <a href="https://developer.android.com/reference/android/widget/SearchView.OnQueryTextListener">Android Dev Doc</a>
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        mAdapter.getFilter().filter(newText);
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