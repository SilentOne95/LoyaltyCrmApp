package com.example.konta.sketch_loyalityapp.ui.map.bottomSheet.contactInfo;

import android.support.annotation.Nullable;

import com.example.konta.sketch_loyalityapp.ui.map.bottomSheet.BottomSheetContract;

public class ContactInfoPresenter implements BottomSheetContract.ContactInfoPresenter {

    @Nullable
    private BottomSheetContract.ContactInfoView view;

    ContactInfoPresenter(@Nullable BottomSheetContract.ContactInfoView view) {
        this.view = view;
    }
}
