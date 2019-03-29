package com.example.konta.sketch_loyalityapp.ui.products;

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

import com.example.konta.sketch_loyalityapp.adapter.ProductAdapter;
import com.example.konta.sketch_loyalityapp.adapter.RecyclerItemClickListener;
import com.example.konta.sketch_loyalityapp.base.BaseFragment;
import com.example.konta.sketch_loyalityapp.pojo.product.Product;
import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.ui.productDetails.ProductDetailsActivity;
import com.example.konta.sketch_loyalityapp.utils.CustomItemDecoration;

import java.util.List;

public class ProductsFragment extends BaseFragment implements ProductsContract.View, SearchView.OnQueryTextListener {

    private static final String TAG = ProductsFragment.class.getSimpleName();

    ProductsPresenter presenter;

    private RecyclerView recyclerView;
    private View emptyStateView;
    private ProgressBar progressBar;
    private ProductAdapter adapter;

    @Override
    protected int getLayout() { return R.layout.fragment_products; }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Produkty");

        setHasOptionsMenu(true);

        progressBar = rootView.findViewById(R.id.progress_bar);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        emptyStateView = rootView.findViewById(R.id.empty_state_products_container);
        emptyStateView.setVisibility(View.GONE);

        presenter = new ProductsPresenter(this, new ProductsModel());
        presenter.requestDataFromServer();
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
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numOfColumns));

            CustomItemDecoration itemDecoration;
            if (numOfColumns == 1) {
                itemDecoration = new CustomItemDecoration(getContext(), R.dimen.mid_value);
            } else {
                itemDecoration = new CustomItemDecoration(getContext(), R.dimen.small_value);
            }
            recyclerView.addItemDecoration(itemDecoration);
            adapter = new ProductAdapter(productList, recyclerItemClickListener, numOfColumns);
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