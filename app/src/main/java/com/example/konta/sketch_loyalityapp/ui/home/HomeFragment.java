package com.example.konta.sketch_loyalityapp.ui.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.konta.sketch_loyalityapp.adapters.HomeRetrofitAdapter;
import com.example.konta.sketch_loyalityapp.adapters.RecyclerItemClickListener;
import com.example.konta.sketch_loyalityapp.data.menu.MenuComponent;
import com.example.konta.sketch_loyalityapp.ui.main.MainActivityModel;
import com.example.konta.sketch_loyalityapp.utils.CustomItemDecoration;
import com.example.konta.sketch_loyalityapp.base.BaseFragment;
import com.example.konta.sketch_loyalityapp.R;

import java.util.List;

public class HomeFragment extends BaseFragment implements HomeContract.View {

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
        presenter.fetchDataFromServer();

        // Set up adapter
        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        CustomItemDecoration itemDecoration = new CustomItemDecoration(getContext(), R.dimen.small_value);
        recyclerView.addItemDecoration(itemDecoration);
    }

    private RecyclerItemClickListener.HomeRetrofitClickListener recyclerItemClickListener = new RecyclerItemClickListener.HomeRetrofitClickListener() {
        @Override
        public void onItemHomeClick(MenuComponent item) {
            navigationPresenter.getSelectedLayoutType(item);
        }
    };

    @Override
    public void setUpAdapter(List<MenuComponent> menuComponentList) {
        recyclerView.setAdapter(new HomeRetrofitAdapter(menuComponentList, recyclerItemClickListener));
    }

}