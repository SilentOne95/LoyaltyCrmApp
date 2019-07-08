package com.sellger.konta.sketch_loyaltyapp.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sellger.konta.sketch_loyaltyapp.adapter.HomeAdapter;
import com.sellger.konta.sketch_loyaltyapp.adapter.RecyclerItemClickListener;
import com.sellger.konta.sketch_loyaltyapp.base.activity.BaseActivity;
import com.sellger.konta.sketch_loyaltyapp.data.Injection;
import com.sellger.konta.sketch_loyaltyapp.data.entity.MenuComponent;
import com.sellger.konta.sketch_loyaltyapp.utils.utilsMap.CustomItemDecoration;
import com.sellger.konta.sketch_loyaltyapp.base.fragment.BaseFragment;
import com.sellger.konta.sketch_loyaltyapp.R;

import java.util.ArrayList;

import static com.sellger.konta.sketch_loyaltyapp.Constants.TOAST_ERROR;

public class HomeFragment extends BaseFragment implements HomeContract.View {

    private static final String TAG = HomeFragment.class.getSimpleName();

    private HomePresenter presenter;

    private ImageView mSpecialOfferImage;
    private TextView mSpecialOfferText;
    private RecyclerView mRecyclerView;
    private View mEmptyStateView;
    private ProgressBar mProgressBar;

    @Override
    protected int getLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Start");

        // Show action bar if view is shown after log in procedure
        ((BaseActivity) getActivity()).getSupportActionBar().show();

        setHasOptionsMenu(true);

        // Init views
        initViews();

        // Setting up presenter
        presenter = new HomePresenter(this, Injection.provideLoyaltyRepository(getContext()));
        presenter.requestDataFromServer();
    }

    /**
     * Called from {@link #onCreate(Bundle)} to init all the views.
     */
    @Override
    public void initViews() {
        mSpecialOfferImage = rootView.findViewById(R.id.special_offer_image);
        mSpecialOfferText = rootView.findViewById(R.id.info_text_view);

        mProgressBar = rootView.findViewById(R.id.progress_bar_home);
        mRecyclerView = rootView.findViewById(R.id.recycler_view);
        mEmptyStateView = rootView.findViewById(R.id.empty_state_home_container);

        // Setting up views visibility
        mSpecialOfferImage.setVisibility(View.GONE);
        mSpecialOfferText.setVisibility(View.GONE);
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
        searchItem.setVisible(false);
        optionsItem.setVisible(false);

        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Implementation of callback listener that handle adapter items click events.
     * See interface {@link RecyclerItemClickListener}.
     */
    private RecyclerItemClickListener.HomeRetrofitClickListener recyclerItemClickListener = new RecyclerItemClickListener.HomeRetrofitClickListener() {
        @Override
        public void onItemHomeClick(MenuComponent item, int selectedViewId) {
            navigationPresenter.getSelectedLayoutType(item.getType(), "");
            // Need to add 1 position as views count from 0, but menu items from 1
            presenter.passIdOfSelectedView(selectedViewId + 1);
        }
    };

    /**
     * Called from {@link HomePresenter#passDataToAdapter(ArrayList, int)} to set up adapter
     * {@link HomeAdapter} with data.
     *
     * @param menuComponentList of items that are going to pass to adapter
     * @param numOfColumns      in which data will be displayed
     */
    @Override
    public void setUpAdapter(ArrayList<MenuComponent> menuComponentList, int numOfColumns) {
        if (menuComponentList.size() == 0) {
            setUpEmptyStateView(true);
        } else {
            setUpEmptyStateView(false);

            mSpecialOfferText.setVisibility(View.VISIBLE);
            mSpecialOfferImage.setVisibility(View.VISIBLE);

            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), numOfColumns));

            // Set up custom
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

    /**
     * Called from {@link #setUpAdapter(ArrayList, int)} to change visibility of custom EmptyStateView.
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
     * Called from {@link HomePresenter#hideProgressBar()} to hide progress bar when data is fetched or not.
     */
    @Override
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    /**
     * Called from {@link HomePresenter#requestDataFromServer()} whenever data is
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
}