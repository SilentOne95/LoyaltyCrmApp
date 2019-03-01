package com.example.konta.sketch_loyalityapp.ui.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.konta.sketch_loyalityapp.adapter.HomeAdapter;
import com.example.konta.sketch_loyalityapp.adapter.RecyclerItemClickListener;
import com.example.konta.sketch_loyalityapp.base.BaseActivity;
import com.example.konta.sketch_loyalityapp.pojo.menu.MenuComponent;
import com.example.konta.sketch_loyalityapp.ui.main.MainActivityModel;
import com.example.konta.sketch_loyalityapp.utils.CustomItemDecoration;
import com.example.konta.sketch_loyalityapp.base.BaseFragment;
import com.example.konta.sketch_loyalityapp.R;

public class HomeFragment extends BaseFragment implements HomeContract.View {

    private static final String TAG = HomeFragment.class.getSimpleName();

    HomePresenter presenter;

    private ImageView specialOfferImage;
    private TextView specialOfferText;
    private RecyclerView recyclerView;
    private View emptyStateView;
    private ProgressBar progressBar;

    @Override
    protected int getLayout() { return R.layout.fragment_home; }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Home");

        // Show action bar if view is shown after log in procedure
        ((BaseActivity) getActivity()).getSupportActionBar().show();

        specialOfferImage = rootView.findViewById(R.id.special_offer_image);
        specialOfferImage.setVisibility(View.GONE);
        specialOfferText = rootView.findViewById(R.id.info_text_view);
        specialOfferText.setVisibility(View.GONE);

        progressBar = rootView.findViewById(R.id.progress_bar_home);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        emptyStateView = rootView.findViewById(R.id.empty_state_home_container);
        emptyStateView.setVisibility(View.GONE);

        presenter = new HomePresenter(this, new MainActivityModel());
        presenter.requestDataFromServer();
    }

    private RecyclerItemClickListener.HomeRetrofitClickListener recyclerItemClickListener = new RecyclerItemClickListener.HomeRetrofitClickListener() {
        @Override
        public void onItemHomeClick(MenuComponent item, int selectedViewId) {
            navigationPresenter.getSelectedLayoutType(item.getType(), "");
            presenter.passIdOfSelectedView(selectedViewId);
        }
    };

    @Override
    public void setUpAdapter(SparseArray<MenuComponent> menuComponentList, int numOfColumns) {
        if (menuComponentList.size() == 0) {
            setUpEmptyStateView(true);
        } else {
            setUpEmptyStateView(false);

            specialOfferText.setVisibility(View.VISIBLE);
            specialOfferImage.setVisibility(View.VISIBLE);

            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numOfColumns));

            CustomItemDecoration itemDecoration;
            if (numOfColumns == 1) {
                itemDecoration = new CustomItemDecoration(getContext(), R.dimen.mid_value);
            } else {
                itemDecoration = new CustomItemDecoration(getContext(), R.dimen.small_value);
            }
            recyclerView.addItemDecoration(itemDecoration);
            recyclerView.setAdapter(new HomeAdapter(menuComponentList, recyclerItemClickListener, numOfColumns));
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
}