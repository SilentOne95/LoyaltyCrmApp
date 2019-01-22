package com.example.konta.sketch_loyalityapp.ui.coupons;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.konta.sketch_loyalityapp.adapters.CouponRetrofitAdapter;
import com.example.konta.sketch_loyalityapp.adapters.RecyclerItemClickListener;
import com.example.konta.sketch_loyalityapp.base.BaseFragment;
import com.example.konta.sketch_loyalityapp.pojo.coupon.Coupon;
import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.ui.couponDetails.CouponDetailsActivity;
import com.example.konta.sketch_loyalityapp.utils.CustomItemDecoration;

import java.util.List;

public class CouponsFragment extends BaseFragment implements CouponsContract.View {

    CouponsPresenter presenter;

    private RecyclerView recyclerView;
    private View emptyStateView;

    @Override
    protected int getLayout() { return R.layout.fragment_coupons; }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Coupons");

        // Retrofit
        presenter = new CouponsPresenter(this, new CouponsModel());
        presenter.requestDataFromServer();

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
            Intent startCouponDetailsActivity = new Intent(getContext(), CouponDetailsActivity.class);
            startCouponDetailsActivity.putExtra("EXTRA_ELEMENT_ID", couponId);
            startActivity(startCouponDetailsActivity);
        }

        @Override
        public void onItemCouponCodeCheckClick(String couponCode) {
            Toast.makeText(getContext(), "Code: " + couponCode , Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void setUpAdapter(List<Coupon> couponList) {
        recyclerView.setAdapter(new CouponRetrofitAdapter(couponList, recyclerItemClickListener));

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