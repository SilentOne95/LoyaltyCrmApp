package com.sellger.konta.sketch_loyaltyapp.base.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment implements BaseFragmentContract.View{

    protected View rootView;
    protected BaseFragmentContract.Presenter navigationPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayout(), container, false);

        return rootView;
    }

    protected abstract int getLayout();

    public abstract void initViews();

    @Override
    public void attachPresenter(BaseFragmentContract.Presenter presenter) {
        navigationPresenter = presenter;
    }
}
