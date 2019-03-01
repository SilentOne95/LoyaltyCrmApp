package com.example.konta.sketch_loyalityapp.ui.login.phoneAuthNumber;

public interface LogInPhoneContract {

    interface View {

        boolean isInputEditTextValid();
        void displayErrorInputMessage(String type);
        void dismissError(android.view.View v);
    }
}
