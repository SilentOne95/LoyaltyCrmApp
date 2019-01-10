package com.example.konta.sketch_loyalityapp.ui.couponsFragment;

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
import com.example.konta.sketch_loyalityapp.data.coupon.Coupon;
import com.example.konta.sketch_loyalityapp.R;
import com.example.konta.sketch_loyalityapp.utils.CustomItemDecoration;

import java.util.List;

public class CouponsFragment extends BaseFragment implements CouponsContract.View {

    CouponsPresenter mPresenter;

    private RecyclerView recyclerView;
    private View emptyStateView;

    @Override
    protected int getLayout() { return R.layout.fragment_coupons; }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Coupons");

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