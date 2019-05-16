package com.sellger.konta.sketch_loyaltyapp.ui.products;

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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sellger.konta.sketch_loyaltyapp.adapter.ProductAdapter;
import com.sellger.konta.sketch_loyaltyapp.adapter.RecyclerItemClickListener;
import com.sellger.konta.sketch_loyaltyapp.base.BaseFragment;
import com.sellger.konta.sketch_loyaltyapp.data.Injection;
import com.sellger.konta.sketch_loyaltyapp.data.entity.Product;
import com.sellger.konta.sketch_loyaltyapp.R;
import com.sellger.konta.sketch_loyaltyapp.ui.productDetails.ProductDetailsActivity;
import com.sellger.konta.sketch_loyaltyapp.utils.utilsMap.CustomItemDecoration;

import java.util.List;

public class ProductsFragment extends BaseFragment implements ProductsContract.View, SearchView.OnQueryTextListener {

    private static final String TAG = ProductsFragment.class.getSimpleName();

    ProductsPresenter presenter;

    private RecyclerView mRecyclerView;
    private View mEmptyStateView;
    private ProgressBar mProgressBar;
    private ProductAdapter mAdapter;

    @Override
    protected int getLayout() { return R.layout.fragment_products; }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Produkty");

        setHasOptionsMenu(true);

        // Init views
        initViews();

        // Setting up views
        mEmptyStateView.setVisibility(View.GONE);

        // Setting up presenter
        presenter = new ProductsPresenter(this, Injection.provideLoyaltyRepository(getContext()));
        presenter.requestDataFromServer();
    }

    @Override
    public void initViews() {
        mProgressBar = rootView.findViewById(R.id.progress_bar);
        mRecyclerView = rootView.findViewById(R.id.recycler_view);
        mEmptyStateView = rootView.findViewById(R.id.empty_state_products_container);
    }

    private RecyclerItemClickListener.ProductRetrofitClickListener recyclerItemClickListener = productId -> {
        Intent startProductDetailsActivity = new Intent(getContext(), ProductDetailsActivity.class);
        startProductDetailsActivity.putExtra("EXTRA_ELEMENT_ID", productId);
        startActivity(startProductDetailsActivity);
    };

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

    @Override
    public void setUpAdapter(List<Product> productList, int numOfColumns) {
        // Set empty state view if needed
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

    @Override
    public void setUpEmptyStateView(boolean isNeeded) {
        if (isNeeded) {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyStateView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyStateView.setVisibility(View.GONE);
        }
    }

    @Override
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void displayToastMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mAdapter.getFilter().filter(newText);
        return false;
    }
}