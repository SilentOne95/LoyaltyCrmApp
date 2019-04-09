package com.sellger.konta.sketch_loyaltyapp.ui.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sellger.konta.sketch_loyaltyapp.adapter.HomeAdapter;
import com.sellger.konta.sketch_loyaltyapp.adapter.RecyclerItemClickListener;
import com.sellger.konta.sketch_loyaltyapp.base.BaseActivity;
import com.sellger.konta.sketch_loyaltyapp.pojo.menu.MenuComponent;
import com.sellger.konta.sketch_loyaltyapp.ui.main.MainActivityModel;
import com.sellger.konta.sketch_loyaltyapp.utils.CustomItemDecoration;
import com.sellger.konta.sketch_loyaltyapp.base.BaseFragment;
import com.sellger.konta.sketch_loyaltyapp.R;

import java.util.ArrayList;

public class HomeFragment extends BaseFragment implements HomeContract.View {

    private static final String TAG = HomeFragment.class.getSimpleName();

    HomePresenter presenter;

    private ImageView mSpecialOfferImage;
    private TextView mSpecialOfferText;
    private RecyclerView mRecyclerView;
    private View mEmptyStateView;
    private ProgressBar mProgressBar;

    @Override
    protected int getLayout() { return R.layout.fragment_home; }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Start");

        // Show action bar if view is shown after log in procedure
        ((BaseActivity) getActivity()).getSupportActionBar().show();

        setHasOptionsMenu(true);

        // Init views
        initViews();

        // Setting up views
        mSpecialOfferImage.setVisibility(View.GONE);
        mSpecialOfferText.setVisibility(View.GONE);
        mEmptyStateView.setVisibility(View.GONE);

        // Setting up presenter
        presenter = new HomePresenter(this, new MainActivityModel());
        presenter.requestDataFromServer();
    }

    @Override
    public void initViews() {
        mSpecialOfferImage = rootView.findViewById(R.id.special_offer_image);
        mSpecialOfferText = rootView.findViewById(R.id.info_text_view);

        mProgressBar = rootView.findViewById(R.id.progress_bar_home);
        mRecyclerView = rootView.findViewById(R.id.recycler_view);
        mEmptyStateView = rootView.findViewById(R.id.empty_state_home_container);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem searchItem = menu.findItem(R.id.main_menu_search);
        MenuItem optionsItem = menu.findItem(R.id.main_menu_options);
        searchItem.setVisible(false);
        optionsItem.setVisible(false);

        super.onCreateOptionsMenu(menu, inflater);
    }

    private RecyclerItemClickListener.HomeRetrofitClickListener recyclerItemClickListener = new RecyclerItemClickListener.HomeRetrofitClickListener() {
        @Override
        public void onItemHomeClick(MenuComponent item, int selectedViewId) {
            navigationPresenter.getSelectedLayoutType(item.getType(), "");
            // Need to add 1 position as views count from 0, but menu items from 1
            presenter.passIdOfSelectedView(selectedViewId + 1);
        }
    };

    @Override
    public void setUpAdapter(ArrayList<MenuComponent> menuComponentList, int numOfColumns) {
        if (menuComponentList.size() == 0) {
            setUpEmptyStateView(true);
        } else {
            setUpEmptyStateView(false);

            mSpecialOfferText.setVisibility(View.VISIBLE);
            mSpecialOfferImage.setVisibility(View.VISIBLE);

            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), numOfColumns));

            CustomItemDecoration itemDecoration;
            if (numOfColumns == 1) {
                itemDecoration = new CustomItemDecoration(getContext(), R.dimen.mid_value);
            } else {
                itemDecoration = new CustomItemDecoration(getContext(), R.dimen.small_value);
            }
            mRecyclerView.addItemDecoration(itemDecoration);
            mRecyclerView.setAdapter(new HomeAdapter(menuComponentList, recyclerItemClickListener, numOfColumns));
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
}