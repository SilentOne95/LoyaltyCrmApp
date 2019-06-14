package com.sellger.konta.sketch_loyaltyapp.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.sellger.konta.sketch_loyaltyapp.R;

public class CustomSnackbar extends BaseTransientBottomBar {

    private CustomSnackbar(@NonNull ViewGroup parent, @NonNull View content, @NonNull com.google.android.material.snackbar.ContentViewCallback contentViewCallback) {
        super(parent, content, contentViewCallback);
    }

    public static CustomSnackbar make(ViewGroup parent, int duration) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_snackbar, parent, false);

        com.google.android.material.snackbar.ContentViewCallback callback = new CustomSnackbar.ContentViewCallback(view);
        CustomSnackbar customSnackbar = new CustomSnackbar(parent, view, callback);

        customSnackbar.setDuration(duration);

        return customSnackbar;
    }

    public static class ContentViewCallback implements com.google.android.material.snackbar.ContentViewCallback {

        private View view;

        ContentViewCallback(View view) {
            this.view = view;
        }

        @Override
        public void animateContentIn(int i, int i1) {

        }

        @Override
        public void animateContentOut(int i, int i1) {

        }
    }
}
