package com.example.konta.sketch_loyalityapp.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

    @Override
    public void attachPresenter(BaseFragmentContract.Presenter presenter) {
        navigationPresenter = presenter;
    }
}
