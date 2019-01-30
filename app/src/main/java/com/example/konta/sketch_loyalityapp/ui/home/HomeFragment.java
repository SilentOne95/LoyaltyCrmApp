package com.example.konta.sketch_loyalityapp.ui.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

import com.example.konta.sketch_loyalityapp.adapter.HomeAdapter;
import com.example.konta.sketch_loyalityapp.adapter.RecyclerItemClickListener;
import com.example.konta.sketch_loyalityapp.pojo.menu.MenuComponent;
import com.example.konta.sketch_loyalityapp.ui.main.MainActivityModel;
import com.example.konta.sketch_loyalityapp.utils.CustomItemDecoration;
import com.example.konta.sketch_loyalityapp.base.BaseFragment;
import com.example.konta.sketch_loyalityapp.R;

public class HomeFragment extends BaseFragment implements HomeContract.View {

    private static final String TAG = HomeFragment.class.getSimpleName();

    HomePresenter presenter;

    private RecyclerView recyclerView;

    @Override
    protected int getLayout() { return R.layout.fragment_home; }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Home");

        // Retrofit
        presenter = new HomePresenter(this, new MainActivityModel());
        presenter.requestDataFromServer();

        // Adapter
        recyclerView = rootView.findViewById(R.id.recycler_view);
    }

    private RecyclerItemClickListener.HomeRetrofitClickListener recyclerItemClickListener = new RecyclerItemClickListener.HomeRetrofitClickListener() {
        @Override
        public void onItemHomeClick(MenuComponent item, int selectedViewId) {
            navigationPresenter.getSelectedLayoutType(item);
            presenter.passIdOfSelectedView(selectedViewId);
        }
    };

    @Override
    public void setUpAdapter(SparseArray<MenuComponent> menuComponentList, int numOfColumns) {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numOfColumns));
        CustomItemDecoration itemDecoration = new CustomItemDecoration(getContext(), R.dimen.small_value);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(new HomeAdapter(menuComponentList, recyclerItemClickListener, numOfColumns));
    }
}