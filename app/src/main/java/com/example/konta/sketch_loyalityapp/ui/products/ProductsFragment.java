package com.example.konta.sketch_loyalityapp.ui.products;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.konta.sketch_loyalityapp.adapters.ProductRetrofitAdapter;
import com.example.konta.sketch_loyalityapp.adapters.RecyclerItemClickListener;
import com.example.konta.sketch_loyalityapp.base.BaseFragment;
import com.example.konta.sketch_loyalityapp.pojo.product.Product;
import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.ui.productDetails.ProductDetailsActivity;
import com.example.konta.sketch_loyalityapp.utils.CustomItemDecoration;

import java.util.List;

public class ProductsFragment extends BaseFragment implements ProductsContract.View {

    ProductsPresenter presenter;

    private RecyclerView recyclerView;
    private View emptyStateView;

    @Override
    protected int getLayout() { return R.layout.fragment_products; }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Products");

        // Set up adapter
        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        CustomItemDecoration itemDecoration = new CustomItemDecoration(getContext(), R.dimen.mid_value);
        recyclerView.addItemDecoration(itemDecoration);
        emptyStateView = rootView.findViewById(R.id.empty_state_products_container);

        presenter = new ProductsPresenter(this, new ProductsModel());
        presenter.requestDataFromServer();
    }

    private RecyclerItemClickListener.ProductRetrofitClickListener recyclerItemClickListener = new RecyclerItemClickListener.ProductRetrofitClickListener() {
        @Override
        public void onItemProductClick(int productId) {
            Intent startProductDetailsActivity = new Intent(getContext(), ProductDetailsActivity.class);
            startProductDetailsActivity.putExtra("EXTRA_ELEMENT_ID", productId);
            startActivity(startProductDetailsActivity);
        }
    };

    @Override
    public void setUpAdapter(List<Product> couponList) {
        recyclerView.setAdapter(new ProductRetrofitAdapter(couponList, recyclerItemClickListener));

        // Set empty state view if needed
        if (!couponList.isEmpty()) {
            recyclerView.setVisibility(View.VISIBLE);
            emptyStateView.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            emptyStateView.setVisibility(View.VISIBLE);
        }
    }
}