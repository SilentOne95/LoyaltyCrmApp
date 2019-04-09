package com.sellger.konta.sketch_loyaltyapp.ui.login.phoneAuthNumber;

public interface LogInPhoneContract {

    interface View {

        void initViews();

        boolean isInputEditTextValid();
        void displayErrorInputMessage(String type);
        void dismissError(android.view.View v);
    }
}
